package edu.upenn.cis.cis455.m1.server.implementations;


import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.upenn.cis.cis455.m1.server.interfaces.Request;

/**
 * Request Impl class which extends the Request class
 */
public class RequestImpl extends Request {

    private Map<String, String> headers;
    private Map<String, List<String>> params;

    private String pathInfo;
    private String requestMethod;
    private String host;
    private String userAgent;
    private int port;
    private String url;
    private String uri;
    private String protocol;
    private String contentType;
    private String ip;
    private String body;
    private int contentLength;

    /**
     * Sets Request path
     * @param pathInfo
     */
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    /**
     * gets request method
     * @return
     */
    @Override
    public String requestMethod() {
        return headers.getOrDefault("Method", null);
    }

    /**
     * gets host
     * @return
     */

    @Override
    public String host() {
        return headers.getOrDefault("host", null);
    }


    @Override
    public String userAgent() {
        return headers.getOrDefault("user-agent", null);
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public String pathInfo() {
        return pathInfo;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String uri() {
        return uri;
    }

    @Override
    public String protocol() {
        return headers.getOrDefault("protocolVersion", null);
    }

    @Override
    public String contentType() {
        return headers.getOrDefault("content-type", null);
    }

    @Override
    public String ip() {
        return ip;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public int contentLength() {
        String length = headers.getOrDefault("content-length", null);
        if (length == null)
            return 0;
        return Integer.valueOf(length);
    }

    @Override
    public String headers(String name) {
        return headers.getOrDefault(name, null);
    }

    public Map<String, String> getHeadersList() {
        return this.headers;
    }

    @Override
    public Set<String> headers() {
        return headers.keySet();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }

    public void setParms(Map<String, List<String>> parms) {
        this.params = parms;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * Method to check last modified date
     * @return
     */
    public String lastModifiedDate()
    {
        if(this.headers.containsKey("if-modified-since")){

            return this.headers.get("if-modified-since");
        }
        return null;
    }


}


