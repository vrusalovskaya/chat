# Client-Server Chat Application

This repository contains two Java applications:
- **ChatClient**: A simple client application that connects to the server.
- **ChatServer**: A server application that accepts multiple client connections and facilitates broadcasting messages between them.

## Features
### ChatClient
- Connects to the server via a specified IP address and port.
- Allows sending messages to the server.
- Displays broadcast messages from other clients.
- Gracefully disconnects by sending the message `bye`.

### ChatServer
- Accepts multiple client connections using threading.
- Broadcasts messages received from one client to all connected clients.
- Removes disconnected clients from the broadcast list.
- Provides server logs for client activity.

---

## How It Works
1. The server listens for incoming client connections on a specified IP address and port.
2. Each client connects to the server using the same IP and port.
3. The server assigns a `ClientHandler` thread to each connected client.
4. When a client sends a message:
   - The server receives it.
   - The `Broadcaster` sends the message to all other connected clients.
5. Clients can disconnect by sending the message `bye`.

---
