package be.hepl.myException;

import be.hepl.response.Response;

public class EndConnexionException extends Exception {
    private Response response;

    public EndConnexionException(Response response)
    {
        super("Fin de Connexion décidée par protocole");
        this.response = response;
    }

    public Response getReponse()
    {
        return response;
    }
}
