package org.chatclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(Client.class);

    private final Integer port;
    private final String serverIp;

    private ReadingHandler readingHandler;
    private Boolean isActive;

    public Client(Integer port, String serverIp) {
        this.port = port;
        this.serverIp = serverIp;
        isActive = false;
    }

    public void start() {
        if (isActive) {
            throw new RuntimeException("The client is already started");
        }

        try (
                Socket socket = new Socket(serverIp, port);
                Scanner in = new Scanner(System.in);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            isActive = true;
            logger.info("Connected to server: {}:{}", serverIp, port);
            readingHandler = new ReadingHandler(reader);
            readingHandler.start();

            while (isActive) {
                String message = in.nextLine();

                if(message.startsWith("/register")){
                    System.out.println("Enter your name: ");
                    String name = in.nextLine();
                    writer.println("/registration:" + name);
                    continue;
                }

                if(message.startsWith("/list-users")){
                    writer.println("/getusers");
                    continue;
                }

                writer.println(message);

                if (message.equalsIgnoreCase("bye")) {
                    readingHandler.interrupt();
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("Client exception", e);
        }
    }

    @Override
    public void close() {
        if (!isActive) {
            return;
        }
        isActive = false;
        readingHandler.interrupt();
    }
}
