package org.chatclient;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;

@AllArgsConstructor
public class ReadingHandler extends Thread {
    private final BufferedReader reader;

    @Override
    public void run() {
        try {
            String message;
            while (!isInterrupted() && (message = reader.readLine()) != null) {
                System.out.println("Server: " + message);
            }
        } catch (IOException e) {
            if (!isInterrupted()) {
                System.err.println("Connection closed or error occurred: " + e.getMessage());
            }
        }
    }
}
