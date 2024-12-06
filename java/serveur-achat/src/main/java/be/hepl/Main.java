package be.hepl;

import be.hepl.protocol.Protocol;
import be.hepl.protocol.ProtocolBSPP;
import be.hepl.protocol.ProtocolBSPPS;
import be.hepl.thread.RequestThreadServer;
import be.hepl.thread.ThreadServer;
import be.hepl.trace.Logger;
import be.hepl.trace.MyLogger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {
    public static void main(String[] args) {
        Logger logger = MyLogger.getInstance();

        Security.addProvider(new BouncyCastleProvider());

        Protocol protocolBSPP = new ProtocolBSPP();
       int port = FileHelper.getSettingInt("./config.properties", "PORT_PURCHASE");
        System.out.println("port: " + port);
        Protocol protocolBSPPS = new ProtocolBSPPS();
        int portS = FileHelper.getSettingInt("./config.properties", "PORT_PURCHASE_SECURE");
        System.out.println("portS: " + portS);
        ThreadServer threadServeur = new RequestThreadServer(port, protocolBSPP, logger);
        ThreadServer threadServeurSecure = new RequestThreadServer(portS, protocolBSPPS, logger);

        logger.clearTrace();
        threadServeur.start();
        threadServeurSecure.start();
    }
}