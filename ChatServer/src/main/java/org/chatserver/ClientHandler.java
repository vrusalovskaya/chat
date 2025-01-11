package org.chatserver;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class ClientHandler extends Thread {
    private final Socket socket;

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
        ) {
            String clientMessage;

            while ((clientMessage = reader.readLine()) != null) {
                System.out.println(socket.getInetAddress().getHostAddress() + ": " + clientMessage);

                if (clientMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Client disconnected");
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Server error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

