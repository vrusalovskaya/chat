package org.chatserver;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    private final Socket socket;
    private final Broadcaster broadcaster;
    private final ClientNamesProvider namesProvider;

    @Getter
    private volatile Boolean isRunning;

    @Getter
    private String clientName;

    public ClientHandler(Socket socket, Broadcaster broadcaster, ClientNamesProvider namesProvider) {
        this.socket = socket;
        this.broadcaster = broadcaster;
        clientName = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        this.namesProvider = namesProvider;
        isRunning = false;
    }

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
        ) {
            isRunning = true;
            broadcaster.add(writer);
            String clientMessage;

            while (isRunning && !isInterrupted() && ((clientMessage = reader.readLine()) != null)) {

                if (clientMessage.startsWith("/registration:")) {
                    clientName = clientMessage.substring(14);
                    broadcaster.broadcast("New client registered: " + clientName);
                    continue;
                }

                if (clientMessage.startsWith("/getusers")) {
                    writer.println("The list of the users: " + namesProvider.getNames());
                    continue;
                }

                String message = clientName + ": " + clientMessage;
                System.out.println(message);
                broadcaster.broadcast(message);

                if (clientMessage.equalsIgnoreCase("bye")) {
                    broadcaster.delete(writer);
                    break;
                }
            }
        } catch (IOException ex) {
            logger.error("Error in ClientHandler", ex);
        } finally {
            try {
                close();
            } catch (Exception e) {
                logger.error("Error while closing ClientHandler", e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        socket.close();
        logger.info("ClientHandler for {} was closed.", socket.getInetAddress().getHostAddress());
    }
}

