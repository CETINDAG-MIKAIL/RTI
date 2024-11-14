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
        return null;
    }

    private synchronized Response ProcessingEmployee(RequestEmployee request) throws EndConnexionException {
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
            dao.save(client);

            connectedClients.put(socket, client.getId());

            System.out.println("1---- id du client que je vais mettre dans la HashMap");

            return new ResponseClient(client.getId());
        }

        ClientSearchVM vm = new ClientSearchVM();
        vm.setLastName(request.getLastName());
        vm.setFirstName(request.getFirstName());
        ArrayList<Client> clients = dao.read(vm);

        if(clients.isEmpty()) {
            return new ResponseClient(null);
        }

        System.out.println("2--- id du client que je vais mettre dans la HashMap, idclient = " + clients.getFirst().getId());

        connectedClients.put(socket, clients.getFirst().getId());

        return new ResponseClient(clients.getFirst().getId());
    }

    private synchronized ResponseSelectBook ProcessingSelectBook(RequestSelectBook request) throws EndConnexionException {
        logger.trace("RequestSelectBook received");

        BookDAO dao = new BookDAO();
        ArrayList<Book> books = dao.read(request.getSearchFilters());

        return new ResponseSelectBook(books);
    }

    private synchronized ResponseAddCaddyItem ProcessingAddCaddyItem(RequestAddCaddyItem request, Integer clientId) throws EndConnexionException {
        logger.trace("RequestAddCaddyItem received");

        System.out.println("----- ClientId recu en parametre = " + clientId);

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


            CaddyDAO daoC = new CaddyDAO();
            Caddy caddy = daoC.getById(clientId);

            if(caddy == null) {     // check if the caddy exists, if not, juste create it
                System.out.println("Le client n'a pas encore de caddy, je vais en créer un");
                caddy = new Caddy();

                ClientDAO daoCli = new ClientDAO();
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

    private synchronized ResponseDeleteCaddyItem ProcessingDeleteCaddyItem(RequestDeleteCaddyItem request) throws EndConnexionException {
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

        CaddySearchVM cvm = new CaddySearchVM();
        cvm.setClientId(request.getIdClient());
        CaddyDAO dao = new CaddyDAO();
        ArrayList<Caddy> caddies = dao.read(cvm);

        if(caddies.isEmpty()) {
            return new ResponseCancelCaddy(false);
        }
        dao.delete(caddies.getFirst().getId());
        return new ResponseCancelCaddy(true);
    }

    private synchronized ResponsePayCaddy ProcessingPayCaddy(RequestPayCaddy request) throws EndConnexionException {
        logger.trace("RequestPayCaddy received");

        CaddyDAO dao = new CaddyDAO();
        Caddy caddy = dao.getById(request.getIdClient());
        caddy.setPayed(true);

        dao.update(caddy);

        return new ResponsePayCaddy(true);
    }
}
