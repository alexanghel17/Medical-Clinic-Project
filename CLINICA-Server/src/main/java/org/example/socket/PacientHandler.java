package org.example.socket;

import org.example.autentificare.LoginService;
import org.example.autentificare.RegisterService;
import org.example.services.DoctorService;
import org.example.services.PacientService;

import java.io.*;
import java.net.Socket;

public class PacientHandler implements Runnable {
    private final Socket clientSocket;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final PacientService pacientService;
    private final DoctorService doctorService;

    public PacientHandler(Socket socket, RegisterService regService, LoginService logService, DoctorService doctorService) {
        this.clientSocket = socket;
        this.registerService = regService;
        this.loginService = logService;
        this.doctorService = new DoctorService();
        this.pacientService = new PacientService();
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("register")) {
                    System.out.println("Registrare client...");
                    registerService.procesareRegister(inputLine, out);
                } else if (inputLine.startsWith("login")) {
                    System.out.println("Login client...");
                    loginService.procesareLogin(inputLine, out);
                } else if (inputLine.startsWith("programare_pacient")
                        || inputLine.startsWith("vezi_doctor")
                        || inputLine.startsWith("vezi_istoric")||inputLine.startsWith("vezi_pacient,")||inputLine.startsWith("editare_pacient,")) {
                    System.out.println("Comanda pacient...");
                    pacientService.procesareComandaPacient(inputLine, out);
                }else if(inputLine.startsWith("vezi_programarile")||inputLine.startsWith("vezi_pacienti")||inputLine.startsWith("service,")||inputLine.startsWith("editare,")) {
                    System.out.println("Comanda doctor...");
                    doctorService.procesareComandaDoctor(inputLine,out);
                }
                else
                {
                    out.println("Comanda necunoscuta!");
                }
            }
        } catch (IOException e) {
            System.out.println("Eroare comunicare cu clientul: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Eroare la inchiderea socket-ului: " + e.getMessage());
            }
        }
    }
}
