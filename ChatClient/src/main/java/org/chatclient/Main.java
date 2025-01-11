package org.chatclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        String serverIP = "192.168.100.8";

        try (Socket socket = new Socket(serverIP, port);
             Scanner in = new Scanner(System.in);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server: " + serverIP + ":" + port);

            while (true) {
                String message = in.nextLine();
                writer.println(message);
//                        System.out.println(reader.readLine());

                if (message.equalsIgnoreCase("bye")) {
                    break;
                }
            }
        }
    }
}
