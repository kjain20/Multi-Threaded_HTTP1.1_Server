package edu.upenn.cis.cis455.m1.server;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.util.*;

import edu.upenn.cis.cis455.ServiceFactory;
import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.handlers.Route;
import edu.upenn.cis.cis455.m1.server.implementations.FileRequestHandler;
import edu.upenn.cis.cis455.m1.server.interfaces.HttpRequestHandler;
import edu.upenn.cis.cis455.m2.server.interfaces.Routable;
import edu.upenn.cis.cis455.util.HttpParsing;
import edu.upenn.cis.cis455.util.RoutesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Stub class for a thread worker for
 * handling Web requests
 */
public class HttpWorker extends Thread{

    private HttpTaskQueue taskQueueObject;
    private HttpTask workerTask;
    private Socket socket;
    private String STATIC_FOLDER_LOCATION;
    private HttpRequestHandler httpRequestHandler;
    private HttpServer httpServer;
    private Map<String, LinkedHashMap<String,Route>> routesMap;
    private String handlerKey;
    private String pathInfo;
    private Map<String,String> workerHeaders;
    private Map<String, List<String>> workerParams;
    private Route routeHandler;
    private String route_path;


    final static Logger logger = LogManager.getLogger(HttpWorker.class);
    
    public HttpWorker(HttpServer serverObject)
    {
        httpServer = serverObject;
        taskQueueObject = serverObject.getRequestQueue();
        logger.info("Got the Http Task Queue Object Inside HttpWorker class"+taskQueueObject.toString());
        STATIC_FOLDER_LOCATION = serverObject.getStaticFolderLocation();
        logger.info("Initializing Worker Thread");
        workerHeaders = new HashMap<>();
        workerParams = new HashMap<>();
    }
    
    public void run()
    {
        while(true)
        {
            if(!httpServer.isWorking && taskQueueObject.isEmpty()) {
                logger.warn("Fresh Worker thread terminating....");
                //remove from server list
                return;
            }

            workerTask = taskQueueObject.dequeue();
            try {
				if(workerTask != null)
				{
                    logger.info("Executing Http Task");
                    socket = workerTask.getSocket();
                    Routable route_object = getApiRouteHandler(socket,workerHeaders,workerParams);
                    if(route_object != null)
                    {
                        routeHandler = route_object.getRoute();
                        route_path = route_object.getRoute_path();
                        HttpIoHandler.processRequestForApiHandler(socket,routeHandler,route_path,pathInfo,workerHeaders,workerParams);
                    }
                    else {
                        httpRequestHandler = ServiceFactory.createRequestHandlerInstance(Paths.get(STATIC_FOLDER_LOCATION));
                        HttpIoHandler.processRequest(socket,this.pathInfo,this.workerHeaders,this.workerParams,httpRequestHandler);
                    }

				    socket.close();
				}
				else
                {
                    logger.warn("In deQueue wait Worker thread terminating....");
                    //remove from server list
                    return;
                }
			} catch (Exception e) {
				// TODO Auto-generated catch block
                logger.error("Task execution failed - http request lost..");
                e = new HaltException(404);
				e.printStackTrace();
			}
        }
    }

    private Routable getApiRouteHandler(Socket socket, Map<String,String> workerHeaders, Map<String, List<String>> workerParams) throws IOException{
        String uriReceived =  HttpParsing.parseRequest(socket.getRemoteSocketAddress().toString(), socket.getInputStream(), workerHeaders, workerParams);
        this.pathInfo = uriReceived.split("\\?")[0];
        this.routesMap = httpServer.getRoutesMap();
        this.handlerKey = workerHeaders.get("Method");
        if(routesMap.containsKey(handlerKey))
        {
            Iterator it = routesMap.get(handlerKey).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String registeredAPI = pair.getKey().toString();
                if(isAPIPresent(registeredAPI,pathInfo))
                {
                    Routable routeObject = new Routable((Route)pair.getValue(),registeredAPI);
                    return routeObject;
                }
            }
        }
        return null;
    }

//    private boolean isAPIPresent(String registeredAPI, String pathInfo) {
//        String separator_symbol = "/";
//        String[] registeredApiTokens = registeredAPI.split(separator_symbol);
//        String[] pathInfoTokens = pathInfo.split(separator_symbol);
//        if(registeredApiTokens.length == pathInfoTokens.length)
//        {
//             int token_length = registeredApiTokens.length;
//             for(int i = 0;i < token_length;i++)
//             {
//                 if(registeredApiTokens[i].equals(pathInfoTokens[i]) || registeredApiTokens[i].equals("\\*")||registeredApiTokens[i].contains(":"))
//                 {
//                     continue;
//                 }
//                 else
//                 {
//                     return false;
//                 }
//             }
//             return true;
//        }
//        return false;
//    }

    public boolean isAPIPresent(String registeredAPI, String pathInfo)
    {
        //registered api is the route path
        //pathInfo is the uri
        List<String> thisPathList = RoutesUtil.convertRouteToList(registeredAPI);
        List<String> pathList = RoutesUtil.convertRouteToList(pathInfo);

        int thisPathSize = thisPathList.size();
        int pathSize = pathList.size();

        if (thisPathSize == pathSize) {
            for (int i = 0; i < thisPathSize; i++) {
                String thisPathPart = thisPathList.get(i);
                String pathPart = pathList.get(i);

                if ((i == thisPathSize - 1) && (thisPathPart.equals("*") && registeredAPI.endsWith("*"))) {
                    // wildcard match
                    return true;
                }

                if ((!thisPathPart.startsWith(":"))
                        && !thisPathPart.equals(pathPart)
                        && !thisPathPart.equals("*")) {
                    return false;
                }
            }
            // All parts matched
            return true;
        } else {
            // Number of "path parts" not the same
            // check wild card:
            if (registeredAPI.endsWith("*")) {
                if (pathSize == (thisPathSize - 1) && (pathInfo.endsWith("/"))) {
                    // Hack for making wildcards work with trailing slash
                    pathList.add("");
                    pathList.add("");
                    pathSize += 2;
                }

                if (thisPathSize < pathSize) {
                    for (int i = 0; i < thisPathSize; i++) {
                        String thisPathPart = thisPathList.get(i);
                        String pathPart = pathList.get(i);
                        if (thisPathPart.equals("*") && (i == thisPathSize - 1) && registeredAPI.endsWith("*")) {
                            // wildcard match
                            return true;
                        }
                        if (!thisPathPart.startsWith(":")
                                && !thisPathPart.equals(pathPart)
                                && !thisPathPart.equals("*")) {
                            return false;
                        }
                    }
                    // All parts matched
                    return true;
                }
                // End check wild card
            }
            return false;
        }

    }



}
