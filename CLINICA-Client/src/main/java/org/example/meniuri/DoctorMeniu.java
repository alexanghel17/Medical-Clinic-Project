package org.example.meniuri;

import org.example.socket.SocketClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class DoctorMeniu    {
    MainMeniu main = new MainMeniu();
    Scanner sc = new Scanner(System.in);

    private SocketClient client;
    public DoctorMeniu(SocketClient client){
        this.client = client;
    }

    public void meniuDoctor(String email) throws IOException {
        System.out.println();
        System.out.println("--- Meniu Doctor ---");
        System.out.println("1. Vezi programările pentru o zi");
        System.out.println("2. Vizualiazare programari proprii.");
        System.out.println("3. Adaugă diagnostic și tratament");
        System.out.println("4. Editare date pacient.");
        System.out.println("5. Logout");
        System.out.print("Alege opțiunea: ");

        String optiune = sc.nextLine();
        switch (optiune) {
            case "1":
                programariPeZi(email);
                break;
            case "2":
                vizualizeazaPacienti(email);
                break;
            case "3":
                tratamentSiDiagnostic();
                break;
            case "4":
                editarePacienti(email);
                break;
            case "5":
                System.out.println("Te-ai delogat cu succes.");
                main.showMeniu(client);
            default:
                System.out.println("Optiune invalida. Incearca din nou.");
        }
    }
    public void programariPeZi(String email) throws IOException {
        System.out.println("Introduceți data (yyyy-MM-dd) in care vre-ti sa consulta-ti programarile.");
        String dataString = sc.nextLine();
        LocalDate data = LocalDate.parse(dataString);
        String mesaj="vezi_programarile,email_doctor="+email+",data="+data;
        String raspuns=client.sendMessage(mesaj);
        if(raspuns.contains("Nu exista")) {
            System.out.println(raspuns);
            return;
        }
        System.out.println("Acestea sunt programarile din data de "+data);
        System.out.println(raspuns);

    }
    public void vizualizeazaPacienti(String email) throws IOException {
        String mesaj = "vezi_pacienti,email_doctor=" + email;
        String raspuns = client.sendMessage(mesaj);
        System.out.println("Acesta sunt programarile dumneavoastră:");
        System.out.println(raspuns);
        System.out.println("Ține minte ID-ul pacientului si al programarii pentru a putea pune un diagnostic și tratament!");
    }
    public void tratamentSiDiagnostic() throws IOException {
        System.out.println("Introduce ti id ul pacientului.");
        String idPacient= sc.nextLine();
        System.out.println("Introduce ti id ul programarii.");
        String idProgramare = sc.nextLine();
        System.out.println("Introduce ti diagnosticul pacientului:");
        String diagnostic = sc.nextLine();
        System.out.println("Introduce ti tratamentul pacientului:");
        String tratament = sc.nextLine();
        String mesaj ="service,diagnostic="+diagnostic+",tratamentul="+tratament+",idPacient="+idPacient+",idProgramare="+idProgramare;
        String raspuns=client.sendMessage(mesaj);
        System.out.println(raspuns);
    }
    public void editarePacienti(String email) throws IOException {
        System.out.println("Introduceți ID-ul programării pacientului:");
        String idProgramare = sc.nextLine().trim();

        if (idProgramare.isEmpty()) {
            System.out.println("ID-ul programării este obligatoriu.");
            return;
        }

        System.out.println("Doriți să ștergeți această programare? (da/nu):");
        String optiuneStergere = sc.nextLine().trim().toLowerCase();

        if (optiuneStergere.equals("da")) {
            String mesaj = "editare,idProgramare=" + idProgramare + ",stergere=true";
            String raspuns = client.sendMessage(mesaj);
            System.out.println(raspuns);
            return;
        }

        System.out.println("Introduceți noul diagnostic (sau apăsați Enter pentru a nu modifica):");
        String diagnostic = sc.nextLine().trim();

        System.out.println("Introduceți noul tratament (sau apăsați Enter pentru a nu modifica):");
        String tratament = sc.nextLine().trim();

        System.out.println("Introduceți noua dată (format yyyy-MM-dd) sau Enter pentru a păstra:");
        String data = sc.nextLine().trim();

        System.out.println("Introduceți noua oră (format HH:mm) sau Enter pentru a păstra:");
        String ora = sc.nextLine().trim();

        StringBuilder mesaj = new StringBuilder("editare,idProgramare=" + idProgramare);

        if (!diagnostic.isEmpty()) {
            mesaj.append(",diagnostic=").append(diagnostic);
        }
        if (!tratament.isEmpty()) {
            mesaj.append(",tratamentul=").append(tratament);
        }
        if (!data.isEmpty()) {
            mesaj.append(",data=").append(data);
        }
        if (!ora.isEmpty()) {
            mesaj.append(",ora=").append(ora);
        }
        String raspuns = client.sendMessage(mesaj.toString());
        System.out.println(raspuns);
    }





}
