package be.hepl.thread;

import be.hepl.protocol.Protocol;
import be.hepl.trace.Logger;

import java.io.IOException;
import java.net.Socket;

public class RequestThreadClient extends ThreadClient {

    public RequestThreadClient(Protocol protocol, Socket csocket, Logger logger) throws IOException
    {
        super(protocol, csocket, logger);
    }

    @Override
    public void run()
    {
        logger.trace("TH Client (Request) starts...");
        super.run();
        logger.trace("TH Client (Request) ends.");
    }
}
