package be.hepl.thread;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class WaitingQueue {

    private final Queue<Socket> waitingQueue;

    public WaitingQueue()
    {
        waitingQueue = new LinkedList<>();
    }

    public synchronized void addConnexion(Socket socket)
    {
        waitingQueue.add(socket);
        notify();
    }

    public synchronized Socket getConnexion() throws InterruptedException
    {
        while(waitingQueue.isEmpty())
            wait();

        return waitingQueue.remove();
    }
}
