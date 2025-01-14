package org.chatserver;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ClientHandlersManager implements ClientNamesProvider {
    private static final Logger logger = LogManager.getLogger(ClientHandlersManager.class);

    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public synchronized void add(ClientHandler handler) {
        clientHandlers.add(handler);
    }

    public synchronized List<String> getNames() {
        var deadHandlers = clientHandlers.stream().filter(x -> !x.getIsRunning()).toList();
        clientHandlers.removeAll(deadHandlers);
        return clientHandlers.stream().map(ClientHandler::getClientName).toList();
    }

    public synchronized void close() {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.close();
            } catch (Exception e) {
                logger.error("Error while closing ClientHandler", e);
            }
            clientHandlers.clear();
        }
    }
}
