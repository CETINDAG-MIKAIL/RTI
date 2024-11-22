package be.hepl.thread;

import java.net.Socket;
import java.util.LinkedList;

public class WaitingQueue {

    private LinkedList<Socket> waitingQueue;

    public WaitingQueue()
    {
        waitingQueue = new LinkedList<>();
    }

    public synchronized void addConnexion(Socket socket)
    {
        waitingQueue.addLast(socket);
        notify();
    }

    public synchronized Socket getConnexion() throws InterruptedException
    {
        while(waitingQueue.isEmpty())
            wait();

        return waitingQueue.remove();
    }
}
