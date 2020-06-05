package edu.upenn.cis.cis455.m2.server.interfaces;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class SessionImpl extends Session {

    private long creationTime;
    private long lastAccessedTime;
    private int maxInactiveInterval;
    private String jsessionId;

    private boolean valid = true;

    Map<String,Object> attributes = null;

    public SessionImpl(String jsessionId)
    {
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
        this.jsessionId = jsessionId;
    }

    @Override
    public String id() {
        return this.jsessionId;
    }

    @Override
    public long creationTime() {
        return this.creationTime;
    }

    @Override
    public long lastAccessedTime() {
        return this.lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime)
    {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public void invalidate() {
        this.valid = false;
    }

    @Override
    public int maxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @Override
    public void maxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;

    }

    @Override
    public void access() {
        setLastAccessedTime(new Date().getTime());
    }

    @Override
    public void attribute(String name, Object value) {
        if(attributes!=null && name!=null) {
            attributes.put(name, value);
        }
    }

    @Override
    public Object attribute(String name) {
        // TODO Auto-generated method stub
        if(attributes!=null && name!=null) {
            return attributes.get(name);
        }
        return null;
    }

    @Override
    public Set<String> attributes() {
        if(attributes!=null) {
            return attributes.keySet();
        }
        return null;
    }

    @Override
    public void removeAttribute(String name) {
        if(attributes!=null && name!=null) {
            attributes.remove(name);
        }
    }

    public boolean isValid() {
        return valid;
    }
}
