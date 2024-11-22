package be.hepl.thread;

import be.hepl.protocol.Protocol;
import be.hepl.trace.Logger;

import java.io.IOException;

public class PoolThreadClient extends ThreadClient {
    private WaitingQueue connectionsWaiting;

    public PoolThreadClient(Protocol protocol, WaitingQueue file, ThreadGroup group, Logger logger) throws IOException
    {
        super(protocol, group, logger);
        connectionsWaiting = file;
    }

    @Override
    public void run()
    {
        logger.trace("TH Client (Pool) démarre...");
        boolean interrompu = false;
        while(!interrompu)
        {
            try
            {
                logger.trace("Attente d'une connexion...");
                csocket = connectionsWaiting.getConnexion();
                logger.trace("Connexion prise en charge.");
                super.run();
            }
            catch (InterruptedException ex)
            {
                logger.trace("Demande d'interruption...");
                interrompu = true;
            }
        }
        logger.trace("TH Client (Pool) se termine.");
    }
}
