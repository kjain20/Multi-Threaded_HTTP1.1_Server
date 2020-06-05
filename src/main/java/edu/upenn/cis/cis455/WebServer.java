package edu.upenn.cis.cis455;


import edu.upenn.cis.cis455.handlers.PostHandler;
import edu.upenn.cis.cis455.handlers.TestHandler;
import edu.upenn.cis.cis455.m2.server.interfaces.WebService;
import org.apache.logging.log4j.Level;
import java.lang.*;




public class WebServer {
    public static void main(String[] args) throws Exception {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
//        // TODO: make sure you parse *BOTH* command line arguments properly
//
//        // TODO: launch your server daemon
          String SERVER_END_POINT = "8080";
          String STATIC_FILE_LOCATION = "www";
          String ipaddress = "127.0.0.1";
          System.out.println(System.getProperty("user.dir"));

         if(args.length != 0)
         {
                STATIC_FILE_LOCATION = args[1];
                SERVER_END_POINT = args[0];
         }



         WebService WEB_SERVICE = WebServiceController.getInstance();
         WebServiceController.ipAddress(ipaddress);
         WebServiceController.port(Integer.parseInt(SERVER_END_POINT));
         WebServiceController.staticFileLocation(STATIC_FILE_LOCATION);
         WebServiceController.threadPool(1);
         WEB_SERVICE.awaitInitialization();
         WebServiceController.post("/test",new PostHandler());
//         WebServiceController.get("/hello/:name",new HelloWorldHandler());
//         WebServiceController.get("/hello/:name1/world/:name2",new HelloWorldHandler());
//         WebServiceController.get("/hello/*", new HelloWorldHandler());
//         WebServiceController.before("/hello/:name","GET",new Filter1());
//         WebServiceController.before("/hello/:name","HEAD",new Filter2());
//         WebServiceController.after("/hello/:name","GET",new Filter3());
    }

}
