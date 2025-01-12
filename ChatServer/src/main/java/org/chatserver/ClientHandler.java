package org.chatserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
@Getter
public class ClientHandler extends Thread {
    private final Socket socket;
    private final Broadcaster broadcaster;

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
        ) {
            String clientMessage;
            broadcaster.add(writer);

            while ((clientMessage = reader.readLine()) != null) {
                broadcaster.broadcast(socket.getInetAddress().getHostAddress() + ": " + clientMessage);
                System.out.println(socket.getInetAddress().getHostAddress() + ": " + clientMessage);

                if (clientMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Client disconnected");
                    broadcaster.delete(writer);
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Server error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                broadcaster.delete(new PrintWriter(socket.getOutputStream(), true));
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
}

