package edu.upenn.cis.cis455.m1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


/**
 * Server Thread
 */
public class ListenerHandler extends Thread {

    private ServerSocketChannel serverSocketChannel;
    private HttpServer httpServerObject;
    private HttpTaskQueue taskQueue;
    private String ipaddress;
    private int port;
    private HttpTask task;
    final static Logger logger = LogManager.getLogger(ListenerHandler.class);

    public ListenerHandler(HttpServer serverInstance, String ipaddress, int port) throws IOException {
        httpServerObject = serverInstance;
        this.ipaddress = ipaddress;
        this.port = port;
    }


    /**
     * Start of server
     */
    public void run() {
        logger.info("Inside Run Method of Listener Thread");
        try {
            logger.info("Inside Listener ---Setting sockets--");
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(ipaddress, port));
            logger.info("This is the IP addess of Server:" + ipaddress);
            logger.info("This is the port:" + port);
            serverSocketChannel.configureBlocking(false);
            logger.info("Finished setting Sockets---");
            logger.debug("Server ready to listen on the port:" + port);
        } catch (IOException e) {
            logger.error("Server failed to start on the port:" + port);
            logger.error("Couldnt' open new Socket for request");
        }
        while (true) {
            synchronized (httpServerObject) {
                if (!httpServerObject.isWorking) {
                    logger.info("Shutting down server on port:" + port);
                    return;
                }
            }

            try {

                SocketChannel SOCKET = serverSocketChannel.accept();
                if (SOCKET != null) {
                    logger.info("got request");
                    logger.info("Task received on the socket...");
                    task = new HttpTask(SOCKET.socket());
                    taskQueue = httpServerObject.getRequestQueue();
                    taskQueue.enqueue(task);
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("Exception caught...");
                e.printStackTrace();
            }

        }

    }
}
