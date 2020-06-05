package edu.upenn.cis.cis455.m1.server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.upenn.cis.cis455.exceptions.BadPacketException;
import edu.upenn.cis.cis455.handlers.Route;
import edu.upenn.cis.cis455.m1.server.implementations.CookieImpl;
import edu.upenn.cis.cis455.m1.server.interfaces.HttpRequestHandler;
import edu.upenn.cis.cis455.m2.server.interfaces.ResponseImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.upenn.cis.cis455.ServiceFactory;
import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m2.server.interfaces.Request;
import edu.upenn.cis.cis455.m2.server.interfaces.Response;

/**
 * Handles marshalling between HTTP Requests and Responses
 */
public class HttpIoHandler {
    final static Logger logger = LogManager.getLogger(HttpIoHandler.class);

    /**
     * Sends an exception back, in the form of an HTTP response code and message.  Returns true
     * if we are supposed to keep the connection open (for persistent connections).
     */
    public static boolean sendException(Socket socket, Request request, HaltException except) {
        try {
            logger.error("Sending Exception out to the socket");
            boolean keepConnectionAlive = false;
            if (socket != null) {
                OutputStream resultOutputStream = socket.getOutputStream();
                String protocol = "HTTP/1.1 ";
                int statusCode = except.statusCode();
                String body = protocol + statusCode;
                except.setBody(body);
                resultOutputStream.write(body.getBytes());
                resultOutputStream.write("\r\n".getBytes());
                resultOutputStream.write(except.body().getBytes());
                resultOutputStream.flush();
                return keepConnectionAlive;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Sends data back.   Returns true if we are supposed to keep the connection open (for
     * persistent connections).
     */
    public static boolean sendResponse(Socket socket, Request request, Response response) throws Exception {
        logger.error("Sending Response out to the socket");
        ServiceFactory.applyAfterFilters(request.pathInfo(),request.contentType(),request,response);
        String headers = stringHeaders(request, response);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.write(headers.getBytes());
        dataOutputStream.write("\r\n".getBytes());
        if (response.bodyRaw() != null) {
            dataOutputStream.write(response.bodyRaw());
        }
        dataOutputStream.flush();
        return false;
    }


    /**
     * Gets the headers for response
     * @param request
     * @param response
     * @return
     */
    private static String stringHeaders(Request request, Response response) {
        String status = request.protocol() + " " + String.valueOf(response.status()) + "\r\n";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Length", "0");
        if (response.bodyRaw() != null) {
            headers.put("Content-Length", String.valueOf(response.bodyRaw().length));
        }

        ResponseImpl response1 = (ResponseImpl) response;
        headers.put("Content-Type", response.type());
        headers.put("Connection", "closed");

        if(request.session()!=null) {
            response.cookie("JSESSIONID", request.session().id());
        }
        //response.cookie("name","value");
        return status + getCookieString(response1.getCookieMap()) + getHeadersString(headers) ;
    }

    /**
     * gets headers
     * @param headers
     * @return
     */
    public static String getHeadersString(Map<String, String> headers) {

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * gets cookies
     * @param values
     * @return
     */
    public static String getCookieString(List<CookieImpl> values) {
        StringBuilder stringBuilder = new StringBuilder();
        for(CookieImpl cookie : values) {
            stringBuilder.append(cookie.getSetCookieString());
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }


    /**
     * processes requests
     * @param socket
     * @param uri
     * @param headers
     * @param params
     * @param requestHandler
     * @throws Exception
     */
    public static void processRequest(Socket socket,String uri,Map<String, String> headers,Map<String, List<String>> params,HttpRequestHandler requestHandler) throws Exception{

        Request request = null;
        Response response = null;
        try {
            request = ServiceFactory.createRequest(socket, uri,false, headers, params, response);
            request.setBody(headers.get("body"));
            // set body of request

            response = ServiceFactory.createResponse();
            requestHandler.handle(request, response);
            HttpIoHandler.sendResponse(socket, request, response);
        } catch (HaltException e) {
            e = new HaltException(404);
            HttpIoHandler.sendException(socket, request, e);
        } catch (IOException i) {
            throw new BadPacketException(i);
        }

    }

    /**
     * processes requests for corresponding match of api handlers
     * @param socket
     * @param routeHandler
     * @param route_path
     * @param uri
     * @param headers
     * @param params
     */

    public static void processRequestForApiHandler(Socket socket, Route routeHandler, String route_path,String uri, Map<String, String> headers, Map<String, List<String>> params)
    {
        Request request = null;
        Response response = null;
        try {
            request = ServiceFactory.createRequest(socket, uri, false, headers, params, response);
            Map<String,String> uriParams = HttpIoHandler.getURIParams(route_path,uri);
            request.set_uri_params(uriParams);
            response = ServiceFactory.createResponse();
            routeHandler.handle(request,response);
            HttpIoHandler.sendResponse(socket, request, response);
        } catch (HaltException e) {
            e = new HaltException(404);
            HttpIoHandler.sendException(socket, request, e);
        } catch (IOException i) {
            throw new BadPacketException(i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * gets URI parameters
     * @param route_path
     * @param uri
     * @return
     */
    public static Map<String, String> getURIParams(String route_path, String uri)
    {
        Map<String, String> URIParams = new HashMap<>();
        String routes[] = route_path.split("/");
        String uris[] = uri.split("/");
        for(int i = 0;i<routes.length;i++)
        {
            if(routes[i].contains(":"))
            {
                URIParams.put(routes[i],uris[i]);
            }
        }
        return URIParams;

    }
}
