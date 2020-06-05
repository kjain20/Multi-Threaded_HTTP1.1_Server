package edu.upenn.cis.cis455;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.*;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.handlers.Filter;
import edu.upenn.cis.cis455.m1.server.HttpServer;
import edu.upenn.cis.cis455.m1.server.implementations.FileRequestHandler;
import edu.upenn.cis.cis455.m1.server.implementations.WebServiceImpl;
import edu.upenn.cis.cis455.m1.server.interfaces.HttpRequestHandler;
import edu.upenn.cis.cis455.m2.server.interfaces.*;
import edu.upenn.cis.cis455.util.HttpParsing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ServiceFactory {


    /**
     * Get the HTTP server associated with port 8080
     *
     */

    private static Map<String, SessionImpl> sessionMap;
    private static WebServiceImpl  webService = null;
    public final static Logger logger = LogManager.getLogger(ServiceFactory.class);

    public static WebService getServerInstance() {
        if(webService == null) {
            webService = WebServiceImpl.ignite();
            return webService;
        }
        else {
            return webService;
        }
    }
    
    /**
     * Create an HTTP request given an incoming socket
     * @throws IOException
     * @throws HaltException
     */
    public static Request createRequest(Socket SOCKET,
                                        String uri,
                                        boolean keepAlive,
                                        Map<String, String> headers,
                                        Map<String, List<String>> params, Response response) throws HaltException, IOException, Exception {
        if(uri == null) {
            uri = HttpParsing.parseRequest(SOCKET.getRemoteSocketAddress().toString(), SOCKET.getInputStream(), headers, params);
        }

        RequestImpl request = new RequestImpl();
        request.setHeaders(headers);
        request.setParams(params);
        request.setPathInfo(uri.split("\\?")[0]);
        request.setUri(uri.split("\\?")[0]);
        request.setUrl("http://" + request.host() + request.getUri());
        //For request cookies
        if(headers.containsKey("cookie"))
        {
            Map<String,String> cookies = ServiceFactory.enrich(headers.get("cookie"));
            request.cookies(cookies);
            String sessionId = request.cookie("JSESSIONID");
            if(sessionId != null)
            {
                request.setSession(ServiceFactory.getSession(sessionId));
            }

        }
        logger.error(request.body());
        logger.info("Creating Request");
        if(request.host().split(":").length > 1) {
            request.setPort(Integer.valueOf(request.host().split(":")[1]));
        } else {
            request.setPort(80);
        }

        logger.info("Returning Request Object");
        ArrayList<Filter> filters = getBeforeFilters(uri,request.contentType());
        for(int i = 0;i<filters.size();i++)
        {
            Filter filter = filters.get(i);
            filter.handle(request,response);
        }
        return request;
    }

    private static Map<String, String> enrich(String cookie) {
        Map<String,String> cookies = new HashMap<>();
        String key_vals[] = cookie.split(";");
        for(int i=0;i<key_vals.length;i++)
        {
            if(key_vals[i].split("=").length == 2) {
                String key = key_vals[i].split("=")[0];
                String value = key_vals[i].split("=")[1];
                cookies.put(key,value);
            }

        }
        return cookies;
    }

    public static ArrayList<Filter> getBeforeFilters(String path, String accept_type)
    {
        ArrayList<Filter> filters = new ArrayList<>();
        HttpServer server = webService.getBasicServer();
        Map<String, LinkedHashMap<String, ArrayList<Filter>>> hashmapEntries = server.getBeforeFiltersMap();
        if(hashmapEntries.containsKey(accept_type))
        {
         if(hashmapEntries.get(accept_type).containsKey(path))
         {
             filters = hashmapEntries.get(accept_type).get(path);
         }
        }

        return filters;
    }

    public static ArrayList<Filter> getAfterFilters(String path, String accept_type)
    {
        ArrayList<Filter> filters = new ArrayList<>();
        HttpServer server = webService.getBasicServer();
        Map<String, LinkedHashMap<String, ArrayList<Filter>>> hashmapEntries = server.getAfterFiltersMap();
        if(hashmapEntries.containsKey(accept_type))
        {
            if(hashmapEntries.get(accept_type).containsKey(path))
            {
                filters = hashmapEntries.get(accept_type).get(path);
            }
        }

        return filters;
    }

    public static void applyAfterFilters(String path, String accept_type, Request request, Response response) throws Exception
    {
        ArrayList<Filter> filters = getAfterFilters(path,request.contentType());
        for(int i = 0;i<filters.size();i++)
        {
            Filter filter = filters.get(i);
            filter.handle(request,response);
        }

    }
    
    /**
     * Gets a request handler for files (i.e., static content) or dynamic content
     */
    public static HttpRequestHandler createRequestHandlerInstance(Path serverRoot) {
        logger.info("Returning HttpRequestHandler Object");
        return new FileRequestHandler(webService.getBasicServer(), serverRoot.toAbsolutePath().toString());
    }

    /**
     * Gets a new HTTP Response object
     */
    public static Response createResponse() {
        logger.info("Returning Response Object");
        return new ResponseImpl();
    }

    /**
     * Creates a blank session ID and registers a Session object for the request
     */
    public static String createSession() {
        String jsession_id = UUID.randomUUID().toString();
        sessionMap.put(jsession_id,new SessionImpl(jsession_id));
        return jsession_id;
    }
    
    /**
     * Looks up a session by ID and updates / returns it
     */
    public static Session getSession(String id) {
        if(sessionMap.containsKey(id) && sessionMap.get(id).isValid())
        {
            Session session_object = sessionMap.get(id);
            if(session_object.lastAccessedTime()-session_object.creationTime()<session_object.maxInactiveInterval())
            {
                session_object.setLastAccessedTime(System.currentTimeMillis());
                return session_object;
            }
            else
            {
                session_object.invalidate();
                sessionMap.remove(id);
                return null;
            }
        }
        return null;
    }
}