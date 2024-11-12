package be.hepl;

import be.hepl.protocol.Protocol;
import be.hepl.protocol.ProtocolBSPP;
import be.hepl.thread.RequestThreadServer;
import be.hepl.thread.ThreadServer;
import be.hepl.trace.Logger;
import be.hepl.trace.MyLogger;

public class Main {
    public static void main(String[] args) {
        Protocol protocol = new ProtocolBSPP();
        int port = FileHelper.getSettingInt("./config.txt", "PORT_PURCHASE");
        Logger logger = MyLogger.getInstance();

        ThreadServer threadServeur = new RequestThreadServer(port, protocol, logger);

        logger.clearTrace();
        threadServeur.start();
    }
}