package org.example.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

//    public String sendMessage(String msg) throws IOException {
//        out.println(msg);
//        String resp = in.readLine();
//        return resp;
//    }
    public String sendMessage(String msg) throws IOException {
    out.println(msg);
    StringBuilder response = new StringBuilder();
    String line;

    // Așteptăm puțin să se umple bufferul (opțional, depinde de protocol)
    while ((line = in.readLine()) != null) {
        response.append(line).append("\n");

        // Sau ieșim dacă nu mai e nimic disponibil imediat
        if (!in.ready()) {
            break;
        }
    }
    return response.toString();
}

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
