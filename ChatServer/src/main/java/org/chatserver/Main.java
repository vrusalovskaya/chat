package org.chatserver;

import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String serverIP = "192.168.100.8";
        InetAddress serverAddress = InetAddress.getByName(serverIP);
        Broadcaster broadcaster = new Broadcaster();

        try (Server server = new Server(port, serverAddress, broadcaster, new ClientHandlersManager())) {
            server.start();
        }
    }
}