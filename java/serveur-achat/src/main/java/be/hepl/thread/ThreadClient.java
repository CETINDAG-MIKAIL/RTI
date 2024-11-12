package be.hepl.thread;

import be.hepl.myException.EndConnexionException;
import be.hepl.protocol.Protocol;
import be.hepl.trace.Logger;
import be.hepl.request.Request;
import be.hepl.response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ThreadClient extends Thread {
    protected Protocol protocol;
    protected Socket csocket;
    protected Logger logger;

    private int numero;

    private static int numCourant = 1;

    public ThreadClient(Protocol protocol, Socket csocket, Logger logger) throws IOException
    {
        super("TH Client " + numCourant + " (protocol=" + protocol.getNom() + ")");
        this.protocol = protocol;
        this.csocket = csocket;
        this.logger = logger;
        this.numero = numCourant++;
    }

    @Override
    public void run()
    {
        try
        {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            ois = new ObjectInputStream(csocket.getInputStream());
            oos = new ObjectOutputStream(csocket.getOutputStream());

            while (true) {
                Request request = (Request) ois.readObject();
                Response response = protocol.processingRequest(request, csocket);
                oos.writeObject(response);
            }
        }
        catch (IOException ex) {
            logger.trace("Erreur I/O");
        } catch (ClassNotFoundException ex) {
            logger.trace("Erreur requete invalide");
        } catch (EndConnexionException e) {
            throw new RuntimeException(e);
        } finally
        {
            try {
                csocket.close();
            } catch (IOException ex) {
                logger.trace("Erreur fermeture socket");
            }
        }
    }
}
