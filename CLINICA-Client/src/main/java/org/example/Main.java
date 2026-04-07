package org.example;

import org.example.meniuri.MainMeniu;
import org.example.socket.SocketClient;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        SocketClient client = new SocketClient();
        MainMeniu meniuPrincipal = new MainMeniu();

        client.startConnection("127.0.0.1", 5555);
        System.out.println("Connected to server.");

            meniuPrincipal.showMeniu(client);


    }
}