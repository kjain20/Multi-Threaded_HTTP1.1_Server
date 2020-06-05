
package edu.upenn.cis.cis455.m1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Stub class for implementing the queue of HttpTasks
 */
public class HttpTaskQueue {
    protected Queue<HttpTask> taskQueue; // HttpTask has socket member

    public static final Logger logger = LogManager.getLogger(HttpTask.class);
    private boolean blocking = true;


    public synchronized void unblock() {
        logger.info("making the Task queue not blocking for workers to end");
        blocking = false;
        notifyAll();
    }

    public HttpTaskQueue() {
        this.taskQueue = new LinkedList<>();
    }

    public synchronized void enqueue(HttpTask task) {
        taskQueue.add(task);
        logger.info("Added Task to Queue");
        notifyAll();

    }

    /**
     * deque method
     * @return
     */
    public synchronized HttpTask dequeue() {
        while (true) {
            if (!taskQueue.isEmpty()) {
                return taskQueue.remove();
            } else {
                if (!blocking) {
                    return null;
                }

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }


}
