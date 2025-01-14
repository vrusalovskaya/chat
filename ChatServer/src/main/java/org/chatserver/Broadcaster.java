package org.chatserver;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class Broadcaster {
    private final ArrayList<PrintWriter> writers = new ArrayList<>();

    public synchronized void add(PrintWriter writer) {
        writers.add(writer);
    }

    public synchronized void delete(PrintWriter writer) {
        writers.remove(writer);
    }

    public synchronized void broadcast(String message) {
        Iterator<PrintWriter> iterator = writers.iterator();

        while (iterator.hasNext()) {
            PrintWriter writer = iterator.next();
            if (writer.checkError()) {
                iterator.remove();
            } else {
                writer.println(message);
            }
        }
    }
}
