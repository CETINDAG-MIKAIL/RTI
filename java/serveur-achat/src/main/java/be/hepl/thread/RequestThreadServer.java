package be.hepl.thread;

import be.hepl.protocol.Protocol;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RequestThreadServer extends ThreadServer {
    public RequestThreadServer(int port, Protocol protocol, Logger logger) throws IOException
    {
        super(port, protocol, logger);
    }

    @Override
    public void run()
    {
        logger.Trace("Démarrage du TH Serveur (Demande)...");
        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {
                ssocket.setSoTimeout(2000);
                csocket = ssocket.accept();
                logger.Trace("Connexion acceptée, création TH Client");
                Thread th = new RequestThreadClient(protocol, csocket, logger);
                th.start();
            }
            catch (SocketTimeoutException ex)
            {
                // Pour vérifier si le thread a été interrompu
            }
            catch (IOException ex)
            {
                logger.Trace("Erreur I/O");
            }
        }
        logger.Trace("TH Serveur (Demande) interrompu.");
        try { ssocket.close(); }
        catch (IOException ex) { logger.Trace("Erreur I/O"); }
    }
}
