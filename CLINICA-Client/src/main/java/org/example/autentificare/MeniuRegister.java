package org.example.autentificare;

import org.example.meniuri.MainMeniu;
import org.example.socket.SocketClient;

import java.io.IOException;
import java.util.Scanner;

public class MeniuRegister {

    MainMeniu main=new MainMeniu();

    public MeniuRegister(SocketClient client) {}

    public void register(SocketClient client) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Buna ziua, ce suneteti doctor sau pacient?(scrieti \"doctor\" sau \"pacient\")");
        String rol= sc.nextLine();
        System.out.println("Introdu emailul:");
        String email = sc.nextLine();
        System.out.println("Introdu parola:");
        String parola = sc.nextLine();
        System.out.println("Introdu numele de familie.");
        String nume = sc.nextLine();
        System.out.println("Introdu prenumele.");
        String prenume = sc.nextLine();

        String mesajDeTransmis="";
        String alergii ="";
        if(rol.equals("pacient")) {
            System.out.println("Ai alergii?(da/nu)");
            String optiune = sc.nextLine().toLowerCase();
            boolean flag = true;

            while (flag) {
            if (optiune.equals("da")) {
                System.out.println("Scrieti alergia");
                alergii = sc.nextLine();
                flag = false;
            } else if(optiune.equals("nu")) {
                alergii = " nu sufera de alergii";
                flag = false;
            }
            else{
                System.out.println("Introdu da/nu!");
            }
        }
            mesajDeTransmis = "register_pacienti"+",email="+email+",parola="+parola+",nume="+nume+",prenume="+prenume+",alergii="+alergii ;
        }
        else if(rol.equals("doctor")){
            System.out.println("Ce specializare aveti?");
            String specializare = sc.nextLine();
            mesajDeTransmis="register_doctor"+ ",email="+email+",parola="+parola+",nume="+nume+",prenume="+prenume+",specializare="+specializare;

        }
        else{
            System.out.println("Rol invalid");
        }

        String raspuns = client.sendMessage(mesajDeTransmis);
        System.out.println(raspuns);

        main.showMeniu(client);

    }
}


