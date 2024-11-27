package be.hepl.thread;

import be.hepl.protocol.Protocol;
import be.hepl.trace.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class PoolThreadServer extends ThreadServer {
    private WaitingQueue connectionsWaiting;
    private ThreadGroup pool;
    private int sizePool;

    public PoolThreadServer(int port, Protocol protocole, int sizePool, Logger logger) throws IOException {
        super(port, protocole, logger);

        connectionsWaiting = new WaitingQueue();
        pool = new ThreadGroup("POOL");
        this.sizePool = sizePool;
    }

    @Override
    public void run() {
        logger.trace("Démarrage du TH Serveur (Pool)...");
        // Création du pool de threads
        try {
            for (int i = 0; i < sizePool; i++)
                new PoolThreadClient(protocol, connectionsWaiting, pool, logger).start();
        } catch (IOException ex) {
            logger.trace("Erreur I/O lors de la création du pool de threads");
            return;
        }

        // Attente des connexions
        while (!this.isInterrupted()) {
            Socket csocket;
            try {
                ssocket.setSoTimeout(2000);
                csocket = ssocket.accept();
                logger.trace("Connexion acceptée, mise en file d'attente.");
                connectionsWaiting.addConnexion(csocket);
            } catch (SocketTimeoutException ex) {
                if (this.isInterrupted()) {
                    logger.trace("TH (RequestThreadServer) : Connection is interrupted");
                    break;
                } else {
                    //logger.trace("Connection timed out, time out reset on " + "2000" + " ms.");
                }
            } catch (IOException ex) {
                logger.trace("Erreur I/O");
            }
        }
        logger.trace("TH Serveur (Pool) interrompu.");
        pool.interrupt();
    }
}
