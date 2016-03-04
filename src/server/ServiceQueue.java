package server;

import logging.CustomLogger;

import java.util.LinkedList;
import java.util.logging.Level;

/**
 * Created by Baljit Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */
public class ServiceQueue {


    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;
    private CustomLogger logger = CustomLogger.getInstance();

    public ServiceQueue (int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];

        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;

            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }

                    r = (Runnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException,
                // the pool could leak threads
                try {
                    System.out.println(r.toString());
                    r.run();
                }
                catch (RuntimeException e) {
                    logger.log("Was unable to do the task:"+r.toString(), Level.WARNING);
                    e.printStackTrace();
                    // You might want to log something here
                }
            }
        }
    }
}
