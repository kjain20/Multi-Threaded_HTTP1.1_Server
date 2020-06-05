package edu.upenn.cis.cis455.m1.server.implementations;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m1.server.HttpServer;
import edu.upenn.cis.cis455.m1.server.HttpWorker;
import edu.upenn.cis.cis455.m1.server.interfaces.HttpRequestHandler;
import edu.upenn.cis.cis455.m1.server.interfaces.Request;
import edu.upenn.cis.cis455.m1.server.interfaces.Response;
import edu.upenn.cis.cis455.util.ComputeUtil;
import edu.upenn.cis.cis455.util.HttpParsing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class FileRequestHandler implements HttpRequestHandler {

    private String staticFolderLocation;
    private HttpServer httpServer;
    private List<HttpWorker> worker_list;

    final static Logger logger = LogManager.getLogger(HttpRequestHandler.class);

    /**
     * Constructor for RequestHandler
     * @param httpServer
     * @param staticFolderLocation
     */
    public FileRequestHandler(HttpServer httpServer, String staticFolderLocation) {
        this.staticFolderLocation = staticFolderLocation;
        this.httpServer = httpServer;
        logger.info("Created the Http Request Object");
    }

    /**
     * Handle Method
     * @param request
     * @param response
     * @throws HaltException
     */
    public void handle(Request request, Response response) throws HaltException {

        if ("/shutdown".equals(request.uri())) {
            logger.debug("Shutting down the HttpServer");
            httpServer.shutdown();
        } else if ("/control".equals(request.uri())) {
            String responseString = "";
            worker_list = httpServer.getWorkers_List();
            String htmlString = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Control panel</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Status of Threads</h1>\n" +
                    "\n" +
                    "<ul>\n" +
                    "<li><a href=\"/shutdown\">Shut down</a></li>\n" +
                    "</ul>\n";
            for (ListIterator<HttpWorker> iter = worker_list.listIterator(); iter.hasNext(); ) {
                HttpWorker worker = iter.next();
                responseString = responseString + "ThreadName:" + worker.getId() + "     Thread Status:" + worker.getState() + "<br>";
            }
            responseString = responseString + "<br><br><br><br><br><br>";
            try {
                responseString = responseString + getErrorLogs("app.log");

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            responseString = htmlString + responseString + "</body>\n" +
                    "</html>";
            response.body(responseString);
            response.setDate();
            logger.info("response for Images");

        }

        else if("/compute".equals(request.uri()))
        {
            int i = 900000;
            long startTime =  System.currentTimeMillis();
            ComputeUtil.factorial(i);
            logger.info("Value computed:");
            long endTime = System.currentTimeMillis();

            try {
                ///Users/venkat/Desktop/GitWorkspace/IWS/555-hw1/src/main/java/edu/upenn/cis/cis455/m1/server/implementations/stats.txt
                PrintWriter writer = new PrintWriter(new FileWriter("stats.txt", true));
                writer.println("Worker Threads\t"+httpServer.getWorker_thread_count()+"\tComputation Time:\t"+String.valueOf((endTime - startTime)/1000)+"\n");
                writer.close();
                }
            catch (Exception e)
            {
                //writer.close();
                e.printStackTrace();
            }

        }

        else {

            final String resourcePathFileLocation = staticFolderLocation + request.pathInfo();
            try {
                String method = request.requestMethod();
                String mime_type = HttpParsing.getMimeType(resourcePathFileLocation);
                if (mime_type.equals("image/gif") || mime_type.equals("image/jpeg")) {
                    byte[] responseString = getResponseForImageString(resourcePathFileLocation);
                    if(method.equalsIgnoreCase("GET"))
                    {
                        response.bodyRaw(responseString);
                    }
                    else
                    {
                        response.body("");

                    }
                } else {
                    String responseString = getResponseString(resourcePathFileLocation);
                    if(method.equalsIgnoreCase("GET"))
                    {

                        response.body(responseString);
                    }
                    else
                    {
                        response.body("");

                    }
                }

                response.status(200);
                String lastModifiedTime = request.lastModifiedDate();
                if(lastModifiedTime != null)
                {
                    if(isLastModifiedFalse(lastModifiedTime,resourcePathFileLocation))
                    {
                        response.body("");
                        response.status(304);
                    }
                }
                response.setDate();
                response.type(mime_type);
                response.setConnectionFalse();
                logger.info("Response for valid file requests");

            } catch (Exception e) {
                throw new HaltException(404, e.getMessage());
            }
        }


    }

    /**
     * Gives the response String
     * @param resourcePathFileLocation
     * @return
     * @throws IOException
     */

    public String getResponseString(String resourcePathFileLocation) throws IOException {
        // TODO Auto-generated method stub


        BufferedReader br = new BufferedReader(new FileReader(resourcePathFileLocation));

        try {

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }

    }

    /**
     * This method is for /control route
     * @param resourcePathFileLocation
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String getErrorLogs(String resourcePathFileLocation) throws IOException, URISyntaxException {
        URL u = getClass().getProtectionDomain().getCodeSource().getLocation();
        File f = new File(u.toURI());
        String filename = f.getPath().split("555-hw1/")[0];
        filename = filename + "555-hw1/app.log";
        BufferedReader br = new BufferedReader(new FileReader((filename)));
        logger.info("Reached Reader");
        try
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null)
            {
                if(line.contains("ERROR")) {
                    sb.append(line);
                    sb.append("<br>");
                }
                line = br.readLine();
            }
            return sb.toString();
        }
        finally {
            br.close();
        }
    }

    /**
     * This will check if the file got modified at last
     * @param request_date
     * @param resourceFile
     * @return
     */
    public boolean isLastModifiedFalse(String request_date, String resourceFile)
    {
        File f = new File(resourceFile);
        Date file_modified_Date = new Date(f.lastModified());
        Date date_in_request = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        try {
            date_in_request = dateFormat1.parse(request_date);
        }
        catch (ParseException e1)
        {
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            try
            {
                date_in_request = dateFormat2.parse(request_date);

            }
            catch (ParseException e2)
            {
                SimpleDateFormat dateFormat3 = new SimpleDateFormat("EEEE dd-MMM-yy HH:mm:ss z", Locale.US);
                try {
                    date_in_request = dateFormat3.parse(request_date);
                }
                catch (ParseException e3)
                {
                        e3.printStackTrace();
                }
            }
        }

        finally {

            if(date_in_request.compareTo(file_modified_Date) < 0 )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * This is to send images to the client
     * @param resourcePathFileLocation
     * @return
     * @throws IOException
     */
    private byte[] getResponseForImageString(String resourcePathFileLocation) throws IOException {
        // TODO Auto-generated method stub

        File file = new File(resourcePathFileLocation);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file.toPath());
            return bytes;
        } catch (FileNotFoundException e) {
            throw new HaltException(404, "File Not Found");
        }
    }

}
