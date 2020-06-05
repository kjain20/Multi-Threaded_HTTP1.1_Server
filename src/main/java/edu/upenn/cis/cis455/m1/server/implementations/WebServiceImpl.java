package edu.upenn.cis.cis455.m1.server.implementations;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.handlers.Filter;
import edu.upenn.cis.cis455.handlers.Route;
import edu.upenn.cis.cis455.m1.server.HttpServer;
import edu.upenn.cis.cis455.m2.server.interfaces.WebService;

public class WebServiceImpl extends WebService {

    protected HttpServer basicServer;
    protected int maxThreads;

    final static Logger logger = LogManager.getLogger(WebServiceImpl.class);

    private boolean initialized = false;
    protected static final String DEFAULT_ACCEPT_TYPE = "*/*";
    protected int port;
    protected String ipAddress;
    protected String staticFolderLocation;
    
    
    
    public static WebServiceImpl ignite(){
        logger.log(Level.DEBUG,"About to return WebService object");
        return new WebServiceImpl();
    }

    
    public void awaitInitialization() throws Exception {
        logger.info("Initializing server");
        start();
    }
    
	@Override
	public void start() throws IOException {
        logger.info("Calling Http Server from WebService");
	    this.initialized = true;
	    basicServer = new HttpServer(maxThreads,ipAddress,port,staticFolderLocation);
	}

    /**
     * Handle an HTTP POST request to the path
     */
    public void post(String path, Route route)
    {
        basicServer.registerPOSTRoute(path,route);
    }

    /**
     * Handle an HTTP PUT request to the path
     */
    public  void put(String path, Route route)
    {
        basicServer.registerPUTRoute(path,route);
        
    }

    /**
     * Handle an HTTP DELETE request to the path
     */
    public  void delete(String path, Route route)
    {
        basicServer.registerDELETERoute(path,route);
    }

    /**
     * Handle an HTTP HEAD request to the path
     */
    public  void head(String path, Route route)
    {
        basicServer.registerHEADRoute(path,route);
    }

    /**
     * Handle an HTTP OPTIONS request to the path
     */
    public  void options(String path, Route route)
    {
        basicServer.registerOPTIONSRoute(path,route);
    }

    @Override
    public void get(String path, Route route) {
        // TODO Auto-generated method stub
        basicServer.registerGETRoute(path,route);
    }
    
    ///////////////////////////////////////////////////
    // HTTP request filtering
    ///////////////////////////////////////////////////
    
    /**
     * Add filters that get called before a request
     */
    public  void before(Filter filter)
    {
            this.basicServer.registerGETBeforeFilter("/",filter);
            this.basicServer.registerHEADBeforeFilter("/",filter);
            this.basicServer.registerPOSTBeforeFilter("/",filter);
            this.basicServer.registerDELETEBeforeFilter("/",filter);
            this.basicServer.registerOPTIONSBeforeFilter("/",filter);
    }

    /**
     * Add filters that get called after a request
     */
    public  void after(Filter filter)
    {
        this.basicServer.registerGETAfterFilter("/",filter);
        this.basicServer.registerHEADAfterFilter("/",filter);
        this.basicServer.registerPOSTAfterFilter("/",filter);
        this.basicServer.registerDELETEAfterFilter("/",filter);
        this.basicServer.registerOPTIONSAfterFilter("/",filter);
    }
    /**
     * Add filters that get called before a request
     */
    public  void before(String path, String acceptType, Filter filter)
    {
        if(acceptType.equals("GET"))
        {
            this.basicServer.registerGETBeforeFilter(path,filter);
        }
        else if(acceptType.equals("HEAD"))
        {
            this.basicServer.registerHEADBeforeFilter(path,filter);
        }
        else if(acceptType.equals("POST"))
        {
            this.basicServer.registerPOSTBeforeFilter(path,filter);
        }
        else if(acceptType.equals("DELETE"))
        {
            this.basicServer.registerDELETEBeforeFilter(path,filter);
        }
        else if(acceptType.equals("OPTIONS"))
        {
            this.basicServer.registerOPTIONSBeforeFilter(path,filter);
        }
    }
    /**
     * Add filters that get called after a request
     */
    public  void after(String path, String acceptType, Filter filter)
    {
        if(acceptType.equals("GET"))
        {
            this.basicServer.registerGETAfterFilter(path,filter);
        }
        else if(acceptType.equals("HEAD"))
        {
            this.basicServer.registerHEADAfterFilter(path,filter);
        }
        else if(acceptType.equals("POST"))
        {
            this.basicServer.registerPOSTAfterFilter(path,filter);
        }
        else if(acceptType.equals("DELETE"))
        {
            this.basicServer.registerDELETEAfterFilter(path,filter);
        }
        else if(acceptType.equals("OPTIONS"))
        {
            this.basicServer.registerOPTIONSAfterFilter(path,filter);
        }
    }


	@Override
	public void stop() {
		// TODO Auto-generated method stub

		
	}


	@Override
	public void staticFileLocation(String directory) {
		// TODO Auto-generated method stub
		this.staticFolderLocation = directory;
	}




	@Override
	public void ipAddress(String ipAddress) {
		// TODO Auto-generated method stub
        this.ipAddress = ipAddress;
		
	}


	@Override
	public void port(int port) {
		// TODO Auto-generated method stub
        this.port = port;
		
	}


	@Override
	public void threadPool(int threads) {
		// TODO Auto-generated method stub
        this.maxThreads = threads;
		
	}

    public HttpServer getBasicServer() {
        return basicServer;
    }
}
