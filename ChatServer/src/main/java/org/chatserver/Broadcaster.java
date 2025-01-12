package org.chatserver;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Broadcaster {
    private final ArrayList<PrintWriter> writers = new ArrayList<>();

    public synchronized void add(PrintWriter writer) {
        writers.add(writer);
    }

    public synchronized void delete(PrintWriter writer) {
        writers.remove(writer);
    }

    public synchronized void broadcast(String message) {
        for (PrintWriter writer : writers) {
            writer.println(message);
        }
    }
}
