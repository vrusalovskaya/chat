package org.chatclient;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

@AllArgsConstructor
public class ReadingHandler extends Thread {
    private static final Logger logger = LogManager.getLogger(Client.class);

    private final BufferedReader reader;

    @Override
    public void run() {
        try {
            String message;
            while (!isInterrupted() && (message = reader.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            logger.error("Connection closed or error occurred", e);
        }
    }
}
