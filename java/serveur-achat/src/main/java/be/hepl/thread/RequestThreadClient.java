package be.hepl.thread;

import be.hepl.protocol.Protocol;

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
        logger.Trace("TH Client (Demande) démarre...");
        super.run();
        logger.Trace("TH Client (Demande) se termine.");
    }
}
