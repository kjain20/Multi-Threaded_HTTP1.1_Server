package edu.upenn.cis.cis455.m2.server.interfaces;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m1.server.implementations.CookieImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class ResponseImpl extends Response {

    private Map<String, String> headers;
    private Map<String, Map<String, CookieImpl>> cookieMap = new HashMap<>();
    final static Logger logger = LogManager.getLogger(edu.upenn.cis.cis455.m1.server.interfaces.Response.class);
    public ResponseImpl() {
        headers = new HashMap<>();
        logger.info("Initializing Response object");
    }
    @Override
    public void header(String header, String value) {
        this.headers.put(header,value);
    }

    @Override
    public void redirect(String location) {
        redirect(location, 302);
    }

    @Override
    public void redirect(String location, int httpStatusCode) {
        this.header("Location", location);
        throw new HaltException(httpStatusCode, "redirect");
    }

    @Override
    public void cookie(String name, String value) {
        addCookie("/", name, new CookieImpl(name, value));
    }

    @Override
    public void cookie(String name, String value, int maxAge) {
        addCookie("/", name, new CookieImpl(name, value, maxAge));

    }

    @Override
    public void cookie(String name, String value, int maxAge, boolean secured) {
        addCookie("/", name, new CookieImpl(name, value, maxAge, secured));

    }

    @Override
    public void cookie(String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        addCookie("/", name, new CookieImpl(name, value, maxAge, secured, httpOnly));
    }

    @Override
    public void cookie(String path, String name, String value) {
        addCookie(path, name, new CookieImpl(path, name, value));
    }

    @Override
    public void cookie(String path, String name, String value, int maxAge) {
        addCookie(path, name, new CookieImpl(path, name, value, maxAge));

    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured) {
        addCookie(path, name, new CookieImpl(path, name, value, maxAge, secured));

    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        addCookie(path, name, new CookieImpl(path, name, value, maxAge, secured, httpOnly));
    }

    @Override
    public void removeCookie(String name) {
        this.cookieMap.remove(name);
    }

    @Override
    public void removeCookie(String path, String name) {
        if(!this.cookieMap.containsKey(path)) {
            return;
        }
        this.cookieMap.get(path).remove(name);
    }

    @Override
    public String getHeaders() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    public void setConnectionFalse()
    {
        this.headers.put("Connection","close");
    }

    @Override
    public void setDate() {
        {
            final Date currentTime = new Date();
            final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            this.headers.put("Date",sdf.format(currentTime));
        }

    }

    public List<CookieImpl> getCookieMap() {
        List<CookieImpl> cookieList= new ArrayList<>();
        for(Map.Entry<String, Map<String, CookieImpl>> entry: cookieMap.entrySet()) {
            cookieList.addAll(new ArrayList<>(entry.getValue().values()));
        }
        return cookieList;
    }

    public void addCookie(String path, String name, CookieImpl cookie) {

        if(!cookieMap.containsKey(path)) {
            cookieMap.put(path, new HashMap<>());
        }
        cookieMap.get(path).put(name, cookie);

    }
}
