package be.hepl.myException;

import src.main.java.be.hepl.response.Response;

public class EndConnexionException extends Exception {
    private Response response;

    public EndConnexionException(Response response)
    {
        super("End of connection decided by protocol");
        this.response = response;
    }

    public Response getResponse()
    {
        return response;
    }
}
