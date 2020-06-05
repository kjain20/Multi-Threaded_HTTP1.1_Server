package edu.upenn.cis.cis455.m1.server;

import edu.upenn.cis.cis455.handlers.Filter;
import edu.upenn.cis.cis455.handlers.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.*;

/**
 * Stub for your HTTP server, which
 * listens on a ServerSocket and handles
 * requests
 */
public class HttpServer implements ThreadManager {

    private ListenerHandler serverThread;
    private HttpTaskQueue httpTaskQueue;
    private Map<String, LinkedHashMap<String,Route>> routesMap;
    private LinkedHashMap<String, Route> getMapEntry; //this is for GET request
    private LinkedHashMap<String, Route> postMapEntry; //this is for GET request
    private LinkedHashMap<String, Route> deleteMapEntry; //this is for GET request
    private LinkedHashMap<String, Route> optionsMapEntry; //this is for GET request
    private LinkedHashMap<String,Route> headMapEntry; //this is for HEAD request
    private LinkedHashMap<String, Route> putMapEntry; //this is for GET request

    private Map<String, LinkedHashMap<String, ArrayList<Filter>>> beforeFiltersMap;
    private Map<String, LinkedHashMap<String, ArrayList<Filter>>> afterFiltersMap;

    private LinkedHashMap<String, ArrayList<Filter>> getAfterFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> headAfterFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> postAfterFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> deleteAfterFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> optionsAfterFilterMapEntry;

    private LinkedHashMap<String, ArrayList<Filter>> headBeforeFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> postBeforeFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> deleteBeforeFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> optionsBeforeFilterMapEntry;
    private LinkedHashMap<String, ArrayList<Filter>> getBeforeFilterMapEntry;

    public List<HttpWorker> getWorkers_List() {
        return workers_List;
    }

    private List<HttpWorker> workers_List;
    private int worker_thread_count = 0;
    private String staticFolderLocation;
    boolean isWorking = true;

    final static Logger logger = LogManager.getLogger(HttpServer.class);

    public int getWorker_thread_count() {
        return worker_thread_count;
    }

    public HttpServer(int WORKER_THREADS, String IP_ADDRESS, int PORT, String STATIC_FOLDER_LOCATION) throws IOException {
        httpTaskQueue = new HttpTaskQueue();
        serverThread = new ListenerHandler(this, IP_ADDRESS, PORT); //ipaddress is hardcoded
        logger.info("Starting HttpServerThread");
        this.worker_thread_count = WORKER_THREADS;
        serverThread.start();

        this.routesMap = new HashMap<>();
        this.staticFolderLocation = STATIC_FOLDER_LOCATION;
        this.workers_List = new ArrayList<>();

        this.getMapEntry = new LinkedHashMap<>();
        this.headMapEntry = new LinkedHashMap<>();
        this.deleteMapEntry = new LinkedHashMap<>();
        this.putMapEntry = new LinkedHashMap<>();
        this.optionsMapEntry = new LinkedHashMap<>();
        this.postMapEntry = new LinkedHashMap<>();

        this.getBeforeFilterMapEntry = new LinkedHashMap<>();
        this.headBeforeFilterMapEntry = new LinkedHashMap<>();
        this.postBeforeFilterMapEntry = new LinkedHashMap<>();
        this.deleteBeforeFilterMapEntry = new LinkedHashMap<>();
        this.optionsBeforeFilterMapEntry = new LinkedHashMap<>();

        this.getAfterFilterMapEntry = new LinkedHashMap<>();
        this.headAfterFilterMapEntry = new LinkedHashMap<>();
        this.postAfterFilterMapEntry = new LinkedHashMap<>();
        this.deleteAfterFilterMapEntry = new LinkedHashMap<>();
        this.optionsAfterFilterMapEntry = new LinkedHashMap<>();

        this.beforeFiltersMap = new HashMap<>();
        this.afterFiltersMap = new HashMap<>();

        logger.info("About to spawn Worker Threads");
        logger.info("Adding Worker Threads");

        for (int i = 0; i < WORKER_THREADS; i++) {
            logger.debug("Added Worker Thread:" + i);
            logger.info("****Adding Worker threads***");
            HttpWorker workerThread = new HttpWorker(this);
            logger.info("Adding new Worker Thread:" + workerThread.getId());
            workers_List.add(workerThread);
            workerThread.start();
        }
    }

    @Override
    public HttpTaskQueue getRequestQueue() {
        // TODO Auto-generated method stub
        logger.info("Sending HttpTask Queue to the Listener for Enqueuing");
        return httpTaskQueue;
    }

    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return this.isWorking;
    }

    @Override
    public void start(HttpWorker worker) {
        // TODO Auto-generated method stub

    }

    @Override
    public void done(HttpWorker worker) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(HttpWorker worker) {
        // TODO Auto-generated method stub

    }

    public String getStaticFolderLocation() {
        return this.staticFolderLocation;
    }

    public void shutdown() {
        synchronized (this) {
            isWorking = false;
        }
        httpTaskQueue.unblock();
    }


    public Map<String, LinkedHashMap<String, Route>> getRoutesMap()
    {
        return this.routesMap;
    }

    public Map<String, LinkedHashMap<String, ArrayList<Filter>>> getBeforeFiltersMap() {
        return beforeFiltersMap;
    }

    public Map<String, LinkedHashMap<String, ArrayList<Filter>>> getAfterFiltersMap() {
        return afterFiltersMap;
    }

    public LinkedHashMap<String, ArrayList<Filter>> retrieveGetAfterFilterMapEntry() {
        return getAfterFilterMapEntry;
    }

    public LinkedHashMap<String, ArrayList<Filter>> retrieveGetBeforeFilterMapEntry() {
        return getBeforeFilterMapEntry;
    }


    public void registerGETAfterFilter(String path, Filter filter)
    {
        if(getAfterFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = getAfterFilterMapEntry.get(path);
            filters.add(filter);
            getAfterFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            getAfterFilterMapEntry.put(path,newfilters);
        }

        this.afterFiltersMap.put("GET",getAfterFilterMapEntry);

    }

    public void registerHEADAfterFilter(String path, Filter filter) {
        if(headAfterFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = headAfterFilterMapEntry.get(path);
            filters.add(filter);
            headAfterFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            headAfterFilterMapEntry.put(path,newfilters);
        }

        this.afterFiltersMap.put("HEAD",headAfterFilterMapEntry);
    }

    public void registerPOSTAfterFilter(String path, Filter filter) {

        if(postAfterFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = postAfterFilterMapEntry.get(path);
            filters.add(filter);
            postAfterFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            postAfterFilterMapEntry.put(path,newfilters);
        }

        this.afterFiltersMap.put("POST",postAfterFilterMapEntry);

    }


    public void registerDELETEAfterFilter(String path, Filter filter) {

        if(deleteAfterFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = deleteAfterFilterMapEntry.get(path);
            filters.add(filter);
            deleteAfterFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            deleteAfterFilterMapEntry.put(path,newfilters);
        }

        this.afterFiltersMap.put("DELETE",deleteAfterFilterMapEntry);

    }

    public void registerOPTIONSAfterFilter(String path, Filter filter) {

        if(optionsAfterFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = optionsAfterFilterMapEntry.get(path);
            filters.add(filter);
            optionsAfterFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            optionsAfterFilterMapEntry.put(path,newfilters);
        }

        this.afterFiltersMap.put("OPTIONS",optionsAfterFilterMapEntry);

    }


    public void registerGETBeforeFilter(String path, Filter filter)
    {
        if(getBeforeFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = getBeforeFilterMapEntry.get(path);
            filters.add(filter);
            getBeforeFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            getBeforeFilterMapEntry.put(path,newfilters);
        }

        this.beforeFiltersMap.put("GET",getBeforeFilterMapEntry);

    }

    public void registerHEADBeforeFilter(String path, Filter filter) {
        if(headBeforeFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = headBeforeFilterMapEntry.get(path);
            filters.add(filter);
            headBeforeFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            headBeforeFilterMapEntry.put(path,newfilters);
        }

        this.beforeFiltersMap.put("HEAD",headBeforeFilterMapEntry);
    }

    public void registerPOSTBeforeFilter(String path, Filter filter) {
        if(postBeforeFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = postBeforeFilterMapEntry.get(path);
            filters.add(filter);
            postBeforeFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            postBeforeFilterMapEntry.put(path,newfilters);
        }

        this.beforeFiltersMap.put("POST",postBeforeFilterMapEntry);

    }

    public void registerDELETEBeforeFilter(String path, Filter filter) {
        if(deleteBeforeFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = deleteBeforeFilterMapEntry.get(path);
            filters.add(filter);
            deleteBeforeFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            deleteBeforeFilterMapEntry.put(path,newfilters);
        }

        this.beforeFiltersMap.put("DELETE",deleteBeforeFilterMapEntry);

    }


    public void registerOPTIONSBeforeFilter(String path, Filter filter) {

        if(optionsBeforeFilterMapEntry.containsKey(path))
        {
            ArrayList<Filter> filters = optionsBeforeFilterMapEntry.get(path);
            filters.add(filter);
            optionsBeforeFilterMapEntry.put(path,filters);
        }
        else
        {
            ArrayList<Filter> newfilters = new ArrayList<>();
            newfilters.add(filter);
            optionsBeforeFilterMapEntry.put(path,newfilters);
        }

        this.beforeFiltersMap.put("OPTIONS",optionsBeforeFilterMapEntry);

    }

    public void registerPOSTRoute(String path, Route route) {
        this.postMapEntry.put(path,route);
        this.routesMap.put("POST",postMapEntry);

    }

    public void registerGETRoute(String path, Route route) {
        this.getMapEntry.put(path,route);
        this.routesMap.put("GET",getMapEntry);
    }

    public void registerHEADRoute(String path, Route route) {
        this.headMapEntry.put(path,route);
        this.routesMap.put("HEAD",headMapEntry);
    }

    public void registerDELETERoute(String path, Route route) {
        this.deleteMapEntry.put(path,route);
        this.routesMap.put("DELETE",deleteMapEntry);
    }

    public void registerPUTRoute(String path, Route route) {
        this.putMapEntry.put(path,route);
        this.routesMap.put("PUT",putMapEntry);
    }

    public void registerOPTIONSRoute(String path, Route route) {
        this.optionsMapEntry.put(path,route);
        this.routesMap.put("OPTIONS",optionsMapEntry);
    }

}
