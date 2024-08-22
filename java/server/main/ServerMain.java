package server.main;

import server.ComputationServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        try {
            int portNumber = Integer.parseInt(args[0]);
            new ComputationServer(portNumber).run();
        } catch (Exception e) {
            logger.error("An error occurred while running the server.", e);
        }
    }
}
