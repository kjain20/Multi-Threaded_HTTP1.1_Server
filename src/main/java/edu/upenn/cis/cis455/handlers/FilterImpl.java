package edu.upenn.cis.cis455.handlers;

import edu.upenn.cis.cis455.m1.server.interfaces.Request;
import edu.upenn.cis.cis455.m1.server.interfaces.Response;


/**
 * Implementation of Filer Interface
 */
public class FilterImpl implements Filter

{

    /**
     * No constructor
     */

    public FilterImpl() {

    }

    /**
     * Handle Method
     */
    @Override
    public void handle(Request request, Response response) throws Exception {

    }

    static final String DEFAULT_ACCEPT_TYPE = "*/*";

    private String path;
    private String acceptType;
    private Filter delegate;


    /**
     * Check for filter on a path match
     */
    public FilterImpl withPrefix(String prefix) {
        this.path = prefix + this.path;
        return this;
    }

    /**
     * Wraps the filter in FilterImpl
     *
     * @param path   the path
     * @param filter the filter
     * @return the wrapped route
     */
    static FilterImpl create(final String path, final Filter filter) {
        return create(path, DEFAULT_ACCEPT_TYPE, filter);
    }

    /**
     * Wraps the filter in FilterImpl
     *
     * @param path       the path
     * @param acceptType the accept type
     * @param filter     the filter
     * @return the wrapped route
     */
    static FilterImpl create(final String path, String acceptType, final Filter filter) {
        if (acceptType == null) {
            acceptType = DEFAULT_ACCEPT_TYPE;
        }
        return new FilterImpl(path, acceptType, filter) {
            @Override
            public void handle(Request request, Response response) throws Exception {
                filter.handle(request, response);
            }
        };
    }

    /**
     * Set Filter with path and Method of request
     */
    protected FilterImpl(String path, String acceptType) {
        this.path = path;
        this.acceptType = acceptType;
    }

    /**
     * delegating Filter
     */
    protected FilterImpl(String path, String acceptType, Filter filter) {
        this(path, acceptType);
        this.delegate = filter;
    }

    /**
     * Get accept type of Filter
     */
    public String getAcceptType() {
        return acceptType;
    }

    /**
     * Returns this route's path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Delegate Method
     */
    public Object delegate() {
        return this.delegate;
    }
}
