package edu.upenn.cis.cis455.m1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;


/**
 * Http Task class which will have the socket
 */
public class HttpTask {
    Socket requestSocket;

    final static Logger logger = LogManager.getLogger(HttpTask.class);
    
    public HttpTask(Socket socket) {
        requestSocket = socket;
        logger.info("Task listening on current socket"+requestSocket.getClass());
    }
    
    public Socket getSocket() {
        return requestSocket;
    }
}
