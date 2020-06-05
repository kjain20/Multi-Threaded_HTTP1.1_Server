package edu.upenn.cis.cis455.m1.server.implementations;

import edu.upenn.cis.cis455.m1.server.interfaces.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ResponseImpl extends Response {

    private Map<String, String> headers;
    final static Logger logger = LogManager.getLogger(Response.class);


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public ResponseImpl() {
        headers = new HashMap<>();
        logger.info("Initializing Response object");
    }


    @Override
    public String getHeaders() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setConnectionFalse()
    {
        this.headers.put("Connection","close");
    }

    public void setDate()
    {
        final Date currentTime = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.headers.put("Date",sdf.format(currentTime));
    }

}
