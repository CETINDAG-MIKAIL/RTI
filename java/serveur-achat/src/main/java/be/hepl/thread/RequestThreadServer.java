package be.hepl.thread;

import be.hepl.protocol.Protocol;
import be.hepl.trace.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RequestThreadServer extends ThreadServer {
    public RequestThreadServer(int port, Protocol protocol, Logger logger)
    {
        super(port, protocol, logger);
    }

    @Override
    public void run()
    {
        logger.trace("Starting TH Server (RequestThreadServer)...");

        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {
                ssocket.setSoTimeout(2000);
                csocket = ssocket.accept();
                logger.trace("Connection accepted, creation TH Client");
                Thread th = new RequestThreadClient(protocol, csocket, logger);
                th.start();
            }
            catch (SocketTimeoutException ex)
            {
                if(this.isInterrupted()) {
                    break;
                } else {
                    logger.trace("Connection timed out, time out reset on " + "2000" + " ms.");
                }
            }
            catch (IOException ex)
            {
                logger.trace("Erreur I/O");
            }
        }

        logger.trace("TH  (RequestThreadServer) interrupted.");

        try {
            ssocket.close();
        } catch (IOException ex) {
            logger.trace("Erreur I/O");
        }
    }
}
