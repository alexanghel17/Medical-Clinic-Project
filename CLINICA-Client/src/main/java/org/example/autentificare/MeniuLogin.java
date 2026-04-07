package org.example.autentificare;

import org.example.meniuri.DoctorMeniu;
import org.example.meniuri.MainMeniu;
import org.example.meniuri.PacientMeniu;
import org.example.socket.SocketClient;

import java.io.IOException;
import java.util.Scanner;

public class MeniuLogin {

    private final SocketClient client;
    private final DoctorMeniu doctor;
    private final PacientMeniu pacient;
    MainMeniu main=new MainMeniu();

    public MeniuLogin(SocketClient client) {
        this.client = client;
        this.doctor = new DoctorMeniu(client);
        this.pacient = new PacientMeniu(client);
    }

    public  void login(SocketClient client) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introdu emailul:");
        String email=sc.nextLine();
        System.out.println("Introdu parola:");
        String parola=sc.nextLine();

        String mesajDeTransmis="login"+",email="+email+",parola="+parola;
        String raspuns =client.sendMessage(mesajDeTransmis);
        System.out.println(raspuns);

        if(raspuns.startsWith("Eroare")){
            System.out.println("Mai incerca ti odata!");
            main.showMeniu(client);
        }
        else{
            boolean isDoctor=raspuns.contains("doctor");
            if(isDoctor){
                while(true) {
                    doctor.meniuDoctor(email);
                }
            }
            else {
                while (true) {
                    pacient.meniuPacient(email);
                }
            }
        }

    }
}
