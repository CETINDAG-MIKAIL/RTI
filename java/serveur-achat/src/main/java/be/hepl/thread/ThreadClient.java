package be.hepl.thread;

import be.hepl.myException.EndConnexionException;
import be.hepl.protocol.Protocol;

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
        super("TH Client " + numCourant + " (protocole=" + protocol.getNom() + ")");
        this.protocol = protocol;
        this.csocket = csocket;
        this.logger = logger;
        this.numero = numCourant++;
    }

    public ThreadClient(Protocol protocol, ThreadGroup groupe, Logger logger) throws IOException
    {
        super(groupe,"TH Client " + numCourant + " (protocole=" + protocol.getNom() + ")");
        this.protocol = protocol;
        this.csocket = null;
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

            while (true)
            {
                RequestThreadClient requete = (RequestThreadClient) ois.readObject();
                //RequestThreadServer reponse = protocol.TraiteRequete(requete,csocket);
                //oos.writeObject(reponse);
            }
        }
        catch (IOException ex) { logger.Trace("Erreur I/O"); }
        catch (ClassNotFoundException ex) { logger.Trace("Erreur requete invalide");
        }
        finally
        {
            try { csocket.close(); }
            catch (IOException ex) { logger.Trace("Erreur fermeture socket"); }
        }
    }
}
