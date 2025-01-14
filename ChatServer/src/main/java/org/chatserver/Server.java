package org.chatserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private final Integer port;
    private final InetAddress address;
    private final Broadcaster broadcaster;
    private final ClientHandlersManager clientHandlersManager;

    private Boolean isActive;

    public Server(
            Integer port,
            InetAddress address,
            Broadcaster broadcaster,
            ClientHandlersManager clientHandlersManager) {
        this.port = port;
        this.address = address;
        this.broadcaster = broadcaster;
        this.clientHandlersManager = clientHandlersManager;
        isActive = false;
    }

    public void start() {
        if (isActive) {
            throw new RuntimeException("The server is already started");
        }

        try (ServerSocket serverSocket = new ServerSocket(port, 50, address);) {
            isActive = true;
            logger.info("Server is listening on host {} and port {}", address.getHostAddress(), port);

            while (isActive) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler newHandler = new ClientHandler(socket, broadcaster, clientHandlersManager);
                    logger.info("New client connected: {}", newHandler.getClientName());

                    clientHandlersManager.add(newHandler);
                    newHandler.start();
                } catch (IOException e) {
                    logger.error("Error accepting connection", e);
                }
            }
        } catch (IOException ex) {
            logger.error("Server exception", ex);
        }
    }

    @Override
    public void close() {
        if (!isActive) {
            return;
        }

        isActive = false;
        clientHandlersManager.close();
        logger.info("Server stopped.");
    }
}

