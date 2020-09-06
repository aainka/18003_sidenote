package com.elg.uis;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
* <pre>
* <p> Title: QueueWorker.java </p>
* <p> Description: QUEUE Å¬·¡½º</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public abstract class QueueWorker extends Thread
{
    private LinkedList eventLinkedList = new LinkedList();
    private List messageRepository = Collections.synchronizedList(eventLinkedList);
    private Object waitObject = new Object();

    public abstract void processObject(Object o);

    private boolean flag = true;
    public static boolean DEBUG;

    public void push(Object o) {
        messageRepository.add(o);
        synchronized (waitObject) {
            waitObject.notify();
        }
    }

    public int getQueueSize() {
        return messageRepository.size();
    }

    @Override
    public void run() {
        Object o = null;
        while (flag) {
            synchronized (waitObject) {
                if (messageRepository.size() > 0) {
                    o = messageRepository.remove(0);
                }
                else {
                    o = null;
                    try {
                        waitObject.wait();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            try {
                if (o != null)
                {
                    processObject(o);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() {
        flag = false;
        synchronized (waitObject) {
            waitObject.notify();
        }
    }
}
