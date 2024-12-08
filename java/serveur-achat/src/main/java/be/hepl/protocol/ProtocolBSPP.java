package be.hepl.protocol;

import be.hepl.model.dao.*;
import be.hepl.model.entity.*;
import be.hepl.model.viewmodel.*;
import be.hepl.myException.EndConnexionException;
import be.hepl.trace.Logger;
import be.hepl.request.*;
import be.hepl.response.*;
import be.hepl.trace.MyLogger;

import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ProtocolBSPP implements Protocol {
    private final HashMap<Socket, Integer> connectedClients = new HashMap<>();
    private Logger logger;

    @Override
    public String getNom() {
        return "ProtocolBSPP";
    }

    @Override
    public HashMap<Socket, Integer> getConnectedClients() {
        return connectedClients;
    }

    @Override
    public synchronized Response processingRequest(Request request, Socket socket) throws EndConnexionException {
        logger = MyLogger.getInstance();

        if(request instanceof RequestEmployee) {
            return ProcessingEmployee((RequestEmployee) request);
        }
        if(request instanceof RequestClient) {
            return ProcessingClient((RequestClient) request, socket);
        }
        if(request instanceof RequestSelectBook) {
            return ProcessingSelectBook((RequestSelectBook) request);
        }

        Integer idClient = connectedClients.get(socket);
        System.out.println("idClient = " + idClient);

        if(request instanceof RequestAddCaddyItem) {
            return ProcessingAddCaddyItem((RequestAddCaddyItem) request, idClient);
        }
        if(request instanceof RequestDeleteCaddyItem) {
            return ProcessingDeleteCaddyItem((RequestDeleteCaddyItem) request);
        }
        if(request instanceof RequestCancelCaddy) {
            return ProcessingCancelCaddy((RequestCancelCaddy) request);
        }
        if(request instanceof RequestPayCaddy) {
            return ProcessingPayCaddy((RequestPayCaddy) request);
        }
        if(request instanceof RequestPayCaddyWithVisa) {
            return ProcessingPayCaddyWithVisa((RequestPayCaddyWithVisa) request);
        }
        if(request instanceof RequestEmptyCaddy) {
            return ProcessingEmptyCaddy((RequestEmptyCaddy) request);
        }
        return null;
    }

    private synchronized ResponseEmployee ProcessingEmployee(RequestEmployee request) {
        logger.trace("RequestEmployee received, login = " + request.getLogin());

        EmployeeSearchVM vm = new EmployeeSearchVM();
        vm.setLogin(request.getLogin());
        vm.setPassword(request.getPassword());

        EmployeeDAO dao = new EmployeeDAO();
        ArrayList<Employee> employees = dao.read(vm);

        if(employees.isEmpty()) {
            return new ResponseEmployee(false);
        }
        return new ResponseEmployee(true);
    }

    private synchronized ResponseClient ProcessingClient(RequestClient request, Socket socket) throws EndConnexionException {
        logger.trace("RequestClient received from " + request.getFirstName() + " " + request.getLastName());
        ClientDAO dao = new ClientDAO();

        if(request.isNewClient()) {
            Client client = new Client();
            client.setId(null);
            client.setLastName(request.getLastName());
            client.setFirstName(request.getFirstName());
            client.setNumClient(-1);
            dao.save(client);

            connectedClients.put(socket, client.getId());

            return new ResponseClient(client.getId());
        }

        ClientSearchVM vm = new ClientSearchVM();
        vm.setLastName(request.getLastName());
        vm.setFirstName(request.getFirstName());
        ArrayList<Client> clients = dao.read(vm);

        if(clients.isEmpty()) {
            throw new EndConnexionException(new ResponseClient(null));
        }

        connectedClients.put(socket, clients.getFirst().getId());

        return new ResponseClient(clients.getFirst().getId());
    }

    private synchronized ResponseSelectBook ProcessingSelectBook(RequestSelectBook request) {
        logger.trace("RequestSelectBook received");

        BookDAO dao = new BookDAO();
        ArrayList<Book> books = dao.read(request.getSearchFilters());

        return new ResponseSelectBook(books);
    }

    private synchronized ResponseAddCaddyItem ProcessingAddCaddyItem(RequestAddCaddyItem request, Integer clientId) {
        logger.trace("RequestAddCaddyItem received");

        BookSearchVM bvm = new BookSearchVM();
        bvm.setId(request.getIdBook());
        bvm.setMinStockQuantity(request.getQuantity());
        BookDAO daoB = new BookDAO();
        ArrayList<Book> books = daoB.read(bvm);
        if(!books.isEmpty()) {
            // modify the quantity, if the quantity requered and the stock are the same, juste delete the book
            Book book = books.getFirst();
            if(book.getStockQuantity() == request.getQuantity()) {
                //daoB.delete(book);
                book.setStockQuantity(book.getStockQuantity() - request.getQuantity());
                daoB.update(book);
            } else {
                book.setStockQuantity(book.getStockQuantity() - request.getQuantity());
                daoB.update(books.getFirst());
            }


            CaddySearchVM cvm = new CaddySearchVM();
            cvm.setClientId(clientId);
            cvm.setPayed(false);
            CaddyDAO daoC = new CaddyDAO();
            ArrayList<Caddy> caddies = daoC.read(cvm);

            Caddy caddy = null;
            if(!caddies.isEmpty()) {
                caddy = caddies.getFirst();
            }

            if(caddy == null) {     // check if the caddy exists, if not, juste create it
                caddy = new Caddy();

                ClientDAO daoCli = new ClientDAO();
                daoCli.read();
                Client client = daoCli.getById(clientId);

                caddy.setClient(client);
                caddy.setAmount(0.0);
                caddy.setDate(LocalDate.now());
                caddy.setPayed(false);

                daoC.create(caddy);
            } else {
                System.out.println("Le client a déjà un caddy");
            }

            CaddyItem caddyItem = new CaddyItem();
            caddyItem.setCaddy(caddy);
            caddyItem.setBook(book);
            caddyItem.setQuantity(request.getQuantity());

            CaddyItemDAO daoCI = new CaddyItemDAO();
            daoCI.create(caddyItem);

            return new ResponseAddCaddyItem(true);
        }

        return new ResponseAddCaddyItem(false);
    }

    private synchronized ResponseDeleteCaddyItem ProcessingDeleteCaddyItem(RequestDeleteCaddyItem request) {
        logger.trace("RequestDeleteCaddyItem received");


        CaddyItemSearchVM cvm = new CaddyItemSearchVM();
        cvm.setBookId(request.getIdBook());
        CaddyItemDAO dao = new CaddyItemDAO();
        ArrayList<CaddyItem> caddyItems = dao.read(cvm);

        if(caddyItems.isEmpty()) {
            return new ResponseDeleteCaddyItem(false);
        }

        for(CaddyItem caddyItem : caddyItems) {
            dao.delete(caddyItem.getId());
        }
        
        return new ResponseDeleteCaddyItem(true);
    }

    private synchronized ResponseCancelCaddy ProcessingCancelCaddy(RequestCancelCaddy request) throws EndConnexionException {
        logger.trace("RequestCancelCaddy received");
        System.out.println("ProcessingCancelCaddy 0");
        CaddySearchVM cvm = new CaddySearchVM();
        cvm.setClientId(request.getIdClient());
        CaddyDAO dao = new CaddyDAO();
        ArrayList<Caddy> caddies = dao.read(cvm);

        System.out.println("ProcessingCancelCaddy 1");
        if(caddies.isEmpty()) {
            System.out.println("ProcessingCancelCaddy 2");
            throw new EndConnexionException(new ResponseCancelCaddy(true));
        }
        System.out.println("ProcessingCancelCaddy 3");
        dao.delete(caddies.getFirst().getId());
        System.out.println("ProcessingCancelCaddy 4");
        throw new EndConnexionException(new ResponseCancelCaddy(true));
    }

    private synchronized ResponsePayCaddy ProcessingPayCaddy(RequestPayCaddy request) {
        logger.trace("RequestPayCaddy received");

        CaddySearchVM cvm = new CaddySearchVM();
        cvm.setClientId(request.getIdClient());
        CaddyDAO dao = new CaddyDAO();
        ArrayList<Caddy> caddies = dao.read(cvm);

        if(caddies.isEmpty()) {
            return new ResponsePayCaddy(false);
        }

        Caddy caddy = caddies.getFirst();
        caddy.setPayed(true);

        dao.update(caddy);

        return new ResponsePayCaddy(true);
    }

    private synchronized ResponsePayCaddy ProcessingPayCaddyWithVisa(RequestPayCaddyWithVisa request) {
        logger.trace("RequestPayCaddyWithVisa received");

        CaddySearchVM cvm = new CaddySearchVM();
        cvm.setClientId(request.getIdClient());
        CaddyDAO dao = new CaddyDAO();
        ArrayList<Caddy> caddies = dao.read(cvm);

        if(caddies.isEmpty()) {
            return new ResponsePayCaddy(false);
        }

        Caddy caddy = caddies.getFirst();
        caddy.setPayed(true);

        dao.update(caddy);

        return new ResponsePayCaddy(true);
    }

    private synchronized ResponseEmptyCaddy ProcessingEmptyCaddy(RequestEmptyCaddy request) {
        logger.trace("RequestEmptyCaddy received");

        CaddySearchVM cvm = new CaddySearchVM();
        cvm.setClientId(request.getIdClient());
        CaddyDAO dao = new CaddyDAO();
        ArrayList<Caddy> caddies = dao.read(cvm);

        if(caddies.isEmpty()) {
            return new ResponseEmptyCaddy(false);
        }

        Caddy caddy = caddies.getFirst();
        dao.delete(caddy.getId());

        return new ResponseEmptyCaddy(true);
    }
}
