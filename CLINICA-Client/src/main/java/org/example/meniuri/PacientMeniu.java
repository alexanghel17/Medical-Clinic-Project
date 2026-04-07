package org.example.meniuri;
import org.example.socket.SocketClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class PacientMeniu {
    MainMeniu main=new MainMeniu();
    Scanner sc = new Scanner(System.in);

    private SocketClient client;

    public PacientMeniu(SocketClient client) {
        this.client = client;
    }

    public void meniuPacient(String email) throws IOException {
        System.out.println();
        System.out.println("Meniu pacient...");
        System.out.println("1. Vizualizeaza doctori disponibili");
        System.out.println("2. Programeaza o vizita");
        System.out.println("3. Vezi istoricul programarilor");
        System.out.println("4. Editare date personale");
        System.out.println("5. Logout");
        System.out.print("Alege o optiune: ");

        String optiune = sc.nextLine();
        switch (optiune) {
            case "1":
                vizualizeazaDoctori();
                break;
            case "2":
                programeazaVizita(sc , email);
                break;
            case "3":
                veziIstoric(email);
                break;
            case "4":
                editareProfilPacient(email);
            case "5":
                System.out.println("Te-ai delogat cu succes.");
                main.showMeniu(client);
            default:
                System.out.println("Optiune invalida. Incearca din nou.");
        }
    }
    public void vizualizeazaDoctori() throws IOException {
        String raspuns=client.sendMessage("vezi_doctor");
        System.out.println("Acestia sunt doctorii disponibili:");
        System.out.println(raspuns);
        System.out.println("Tine minte id-ul doctorului pt a putea face ulterior o programare!");
    }

    public void programeazaVizita(Scanner sc, String emailPacient) throws IOException {
        System.out.println("Scrieți ID-ul doctorului pentru care doriți o programare:");
        int idDoc = sc.nextInt();
        sc.nextLine();

        System.out.println("Introduceți data programării (yyyy-MM-dd):");
        String dataString = sc.nextLine();
        LocalDate data = LocalDate.parse(dataString);

        System.out.println("Introduceți ora programării (HH:mm):");
        String oraString = sc.nextLine();
        LocalTime ora = LocalTime.parse(oraString);

        System.out.println("Aveți recomandare medicală? (da/nu)");
        String recomandare = sc.nextLine();

        String mesaj = "programare_pacient,pacient_email=" + emailPacient +
                ",doctor_id=" + idDoc +
                ",data=" + data +
                ",ora=" + ora +
                ",recomandare=" + recomandare;

        String raspuns = client.sendMessage(mesaj);
        System.out.println("Server: " + raspuns);
    }

    public void veziIstoric(String email) throws IOException {
        String raspuns=client.sendMessage("vezi_istoric,pacient_email="+email);
        System.out.println(raspuns);
    }
    public void editareProfilPacient(String emailPacient) throws IOException {
        System.out.println("Editare profil pacient (doar câmpurile completate vor fi modificate).");

        System.out.println("Introduceți noul email (sau Enter pentru a păstra):");
        String emailNou = sc.nextLine().trim();

        System.out.println("Introduceți noua parolă (sau Enter pentru a păstra):");
        String parolaNoua = sc.nextLine().trim();

        System.out.println("Introduceți noul nume (sau Enter pentru a păstra):");
        String numeNou = sc.nextLine().trim();

        System.out.println("Introduceți noul prenume (sau Enter pentru a păstra):");
        String prenumeNou = sc.nextLine().trim();

        System.out.println("Introduceți noile alergii (sau Enter pentru a păstra):");
        String alergiiNoi = sc.nextLine().trim();

        StringBuilder mesaj = new StringBuilder("editare_pacient,email_initial=" + emailPacient);

        if (!emailNou.isEmpty()) {
            mesaj.append(",email_nou=").append(emailNou);
        }
        if (!parolaNoua.isEmpty()) {
            mesaj.append(",parola=").append(parolaNoua);
        }
        if (!numeNou.isEmpty()) {
            mesaj.append(",nume=").append(numeNou);
        }
        if (!prenumeNou.isEmpty()) {
            mesaj.append(",prenume=").append(prenumeNou);
        }
        if (!alergiiNoi.isEmpty()) {
            mesaj.append(",alergii=").append(alergiiNoi);
        }

        String raspuns = client.sendMessage(mesaj.toString());
        System.out.println(raspuns);
    }

}

