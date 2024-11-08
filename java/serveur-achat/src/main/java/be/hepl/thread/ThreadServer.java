package be.hepl.thread;

import be.hepl.protocol.Protocol;
import be.hepl.trace.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ThreadServer extends Thread {
    protected int port;
    protected Protocol protocol;
    protected Logger logger;

    protected ServerSocket ssocket;

    public ThreadServer(int port, Protocol protocol, Logger logger)
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocol.getNom() + ")");
        this.port = port;
        this.protocol = protocol;
        this.logger = logger;

        try {
            ssocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
