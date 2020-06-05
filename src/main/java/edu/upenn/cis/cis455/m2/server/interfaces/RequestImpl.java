package edu.upenn.cis.cis455.m2.server.interfaces;

import edu.upenn.cis.cis455.ServiceFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestImpl extends Request
{

    private Map<String, String> headers;
    private Map<String, List<String>> params;
    private Map<String, String> uri_params;

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
    private String lastModifiedDate;
    private Session session;
    private Map<String,String> cookies;
    private Map<String,Object> attributesMap;

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getHost() {
        return host;
    }
    public String getUrl() {
        return url;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getUri() {
        return uri;
    }
    public Map<String, List<String>>queryParams;

    @Override
    public Session session() {
        return this.session;
    }

    @Override
    public Session session(boolean create) {

        if(create && this.session == null) {
            String id = ServiceFactory.createSession();
            this.session = ServiceFactory.getSession(id);
        }
        return this.session;

    }

    @Override
    public Map<String, String> params() {
        return this.uri_params;
    }

    @Override
    public void set_uri_params(Map<String,String> params)
    {
            this.uri_params = params;
    }

    @Override
    public String queryParams(String param) {
        return param;
    }

    @Override
    public List<String> queryParamsValues(String param) {
        return this.queryParams.get(param);
    }

    @Override
    public Set<String> queryParams() {
        return this.queryParams.keySet();
    }

    @Override
    public String queryString() {
        if(queryParams!=null) {
            StringBuilder queryString = new StringBuilder("?");
            for(String key : queryParams.keySet()) {
                queryString.append(key).append("=").append(queryParams.get(key)).append("&");
            }
            queryString.substring(0, queryString.length()-1);
            return queryString.toString();
        }
        return null;
    }

    @Override
    public void attribute(String attrib, Object val) {
        this.attributesMap.put(attrib,val);
    }

    @Override
    public Object attribute(String attrib) {
        return this.attributesMap.get(attrib);
    }

    @Override
    public Set<String> attributes() {
        return this.attributesMap.keySet();
    }

    @Override
    public Map<String, String> cookies() {
        return this.cookies;
    }

    @Override
    public String requestMethod() {
        return headers.getOrDefault("Method", null);
    }

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
        return this.port;
    }

    @Override
    public String pathInfo() {
        return this.pathInfo;
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
        return this.ip;
    }

    @Override
    public String body() {
        return this.body;
    }

    @Override
    public int contentLength() {
        String length = headers.getOrDefault("content-length", null);
        if (length == null)
            return 0;
        return Integer.valueOf(length);
    }

    public void cookies(Map<String, String> cookies) {
        if(cookies!=null) {
            this.cookies = cookies;
        }
    }

    @Override
    public String headers(String name) {
        return headers.getOrDefault(name, null);
    }

    @Override
    public Set<String> headers() {
        return headers.keySet();
    }

    @Override
    public String lastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
