package org.example.socket;

import org.example.autentificare.LoginService;
import org.example.autentificare.RegisterService;
import org.example.services.DoctorService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void run(int port) throws IOException {
        System.out.println("Serverul a pornit. Asteapta conexiuni...");
        serverSocket = new ServerSocket(port);

        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();
        DoctorService doctorService = new DoctorService();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client conectat: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            // Creezi si pornesti un thread nou pentru fiecare client
            PacientHandler clientHandler = new PacientHandler(clientSocket, registerService, loginService,doctorService);
            Thread thread = new Thread(clientHandler);
            thread.start();

        }
    }

    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

}
