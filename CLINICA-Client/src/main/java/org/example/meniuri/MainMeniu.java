package org.example.meniuri;

import org.example.autentificare.MeniuLogin;
import org.example.autentificare.MeniuRegister;
import org.example.socket.SocketClient;

import java.io.IOException;
import java.util.Scanner;

public class MainMeniu {

    public void showMeniu(SocketClient client) throws IOException {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("Bine ai venit la Clinica Medicala!");
            System.out.println("Alege o optiune:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Iesire");

            MeniuLogin meniuLogin = new MeniuLogin(client);
            MeniuRegister meniuRegister = new MeniuRegister(client);

            int optiune = sc.nextInt();
            sc.nextLine();

            switch (optiune) {
                case 1:
                    meniuLogin.login(client);
                    break;
                case 2:
                    meniuRegister.register(client);
                    break;
                case 3:
                    System.out.println("La revedere!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Optiune invalida , mai introduce ti inca odata!");

            }
        }
    }
}

