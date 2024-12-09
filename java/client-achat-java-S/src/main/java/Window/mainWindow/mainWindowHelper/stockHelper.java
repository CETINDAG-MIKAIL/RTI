package Window.mainWindow.mainWindowHelper;

import Window.MySocket;
import be.hepl.CryptoHelper;
import be.hepl.model.entity.Book;
import be.hepl.request.RequestAddCaddyItem;
import be.hepl.request.RequestDeleteCaddyItem;
import be.hepl.request.RequestSecure;
import be.hepl.response.ResponseAddCaddyItem;
import be.hepl.response.ResponseDeleteCaddyItem;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class stockHelper {
    private final SecretKey sessionKey;
    private final MySocket mySocket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public stockHelper(SecretKey sessionKey, MySocket mySocket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.sessionKey = sessionKey;
        this.mySocket = mySocket;
        this.oos = oos;
        this.ois = ois;
    }

    public boolean updateBookStock(Book book, int quantity) {
        try {
            RequestAddCaddyItem request = new RequestAddCaddyItem(book.getId(), quantity);
            byte[] requestBytes = CryptoHelper.convertToBytes(request, null);
            byte[] Iv = CryptoHelper.generateIV();
            byte[] cryptedRequest = CryptoHelper.CryptSymDESede(sessionKey, requestBytes, null, Iv);

            RequestSecure initialSecureRequest = new RequestSecure(request, cryptedRequest, null, null, null, Iv);
            mySocket.getOutputStream().writeObject(initialSecureRequest);
            ResponseAddCaddyItem response = (ResponseAddCaddyItem) mySocket.getInputStream().readObject();

            return response.isSuccess();

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du stock : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }
    public boolean restoreBookStock(Book book, int quantity) {
        try {
            RequestDeleteCaddyItem request = new RequestDeleteCaddyItem(book.getId(), quantity);
            byte[] requestBytes = CryptoHelper.convertToBytes(request, null);
            if (requestBytes == null || requestBytes.length == 0)
            {
                System.out.println("requestBytes is null or empty");
            }

            byte[] Iv = CryptoHelper.generateIV();
            byte [] cryptedRequest = CryptoHelper.CryptSymDESede(sessionKey, requestBytes, null, Iv);
            RequestSecure initialSecureRequest  = new RequestSecure(request, cryptedRequest, null, null, null, Iv);
            oos.writeObject(initialSecureRequest);

            ResponseDeleteCaddyItem response = (ResponseDeleteCaddyItem) ois.readObject();

            return response.isSuccess();

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la restauration du stock : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
