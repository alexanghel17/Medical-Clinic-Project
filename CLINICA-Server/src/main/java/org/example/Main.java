package org.example;

import org.example.database.DataBaseManager;
import org.example.socket.SocketServer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        SocketServer server = new SocketServer();
        server.run(5555);


    }
}


