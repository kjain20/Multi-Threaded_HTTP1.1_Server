package edu.upenn.cis.cis455.m1.server.implementations;

/**
 * Cookie Implementation class for incoming requests
 */
public class CookieImpl {

    private String path;
    private String name;
    private String value;
    private int maxAge = 0;
    private boolean secured;
    private boolean httpOnly;

    /**
     * Cookie Implementation constructor with value and maxAge
     */
    public CookieImpl(String value, int maxAge) {
        this.value = value;
        this.maxAge = maxAge;
    }

    /**
     * Cookie Implementation constructor with name and value
     */
    public CookieImpl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Cookie Implementation constructor with name ,value and maxAge
     */
    public CookieImpl(String name, String value, int maxAge) {
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
    }

    /**
     * Cookie Implementation constructor with name ,value ,maxAge and secured
     */
    public CookieImpl(String name, String value, int maxAge, boolean secured) {
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
        this.secured = secured;
    }

    /**
     * Cookie Implementation constructor with name ,value ,maxAge , secured , http only requests
     */
    public CookieImpl(String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
        this.secured = secured;
        this.httpOnly = httpOnly;
    }

    /**
     * Cookie Implementation constructor with path ,name, value
     */
    public CookieImpl(String path, String name, String value) {
        this.path = path;
        this.name = name;
        this.value = value;
    }


    /**
     * Cookie Implementation constructor with path ,name, value, maxAge
     */
    public CookieImpl(String path, String name, String value, int maxAge) {
        this.path = path;
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
    }

    /**
     * Cookie Implementation constructor with path ,name, value, maxAge, secured
     */
    public CookieImpl(String path, String name, String value, int maxAge, boolean secured) {
        this.path = path;
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
        this.secured = secured;
    }

    /**
     * Cookie Implementation constructor with path ,name, value, maxAge, secured, httponly
     */
    public CookieImpl(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        this.path = path;
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
        this.secured = secured;
        this.httpOnly = httpOnly;
    }

    /**
     * method for getting cookiestring
     * return cookiestring
     */

    public String getSetCookieString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Set-Cookie: ");
        stringBuilder.append(name);
        stringBuilder.append("=");
        stringBuilder.append(value);


        if (path != null) {
            stringBuilder.append(",");
            stringBuilder.append("Path=");
            stringBuilder.append(path);

        }

        if (maxAge != 0) {
            stringBuilder.append(",");
            stringBuilder.append("Max-Age=");
            stringBuilder.append(String.valueOf(maxAge));

        }

        if(secured) {
            stringBuilder.append(",");
            stringBuilder.append("Secure");
        }
        if(httpOnly) {
            stringBuilder.append(",");
            stringBuilder.append("HttpOnly");
        }

        return stringBuilder.toString();

    }
}
