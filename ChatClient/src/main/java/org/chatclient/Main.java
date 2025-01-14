package org.chatclient;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        String serverIP = "192.168.100.8";

        try (Client client = new Client(port, serverIP)) {
            client.start();
        }
    }
}
