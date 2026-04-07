package org.example.autentificare;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.models.Doctor;
import org.example.models.Pacient;
import org.example.services.DoctorService;
import org.example.services.PacientService;

import java.io.PrintWriter;

public class RegisterService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("conector-bd-alex");
    PacientService pacientService=new PacientService();
    DoctorService doctorService=new DoctorService();

    public void procesareRegister(String date, PrintWriter out) {

        EntityManager em = emf.createEntityManager();

        try {
            if (date.startsWith("register_pacient")) {
                String[] campuri = date.substring("register_pacient".length()).trim().split(",");
                String email ="";
                String parola = "";
                String nume = "";
                String prenume = "";
                String alergii = "";
                for (String camp : campuri) {
                    String[] pereche = camp.trim().split("=");
                    if (pereche.length == 2) {
                        String cheie = pereche[0].trim();
                        String valoare = pereche[1].trim();
                        switch (cheie) {
                            case "email":
                                email = valoare;
                                break;
                            case "parola":
                                parola = valoare;
                                break;
                            case "nume":
                                nume = valoare;
                                break;
                            case "prenume":
                                prenume = valoare;
                                break;
                            case "alergii":
                                alergii = valoare;
                                break;
                        }
                    }
                }

                Pacient pacient=new Pacient(email,parola,nume,prenume,alergii);

                try{
                    pacientService.persistPacient(pacient);
                    System.out.println("ok");
                    out.println("Inregistrare pacientului a fost realizata cu succes .");
                }catch(Exception e){
                    System.out.println(e);
                    out.println("Eroare");
                }


                } else if (date.startsWith("register_doctor")) {
                String[] campuri = date.substring("register_doctor".length()).trim().split(",");
                String email = "";
                String parola = "";
                String nume = "";
                String prenume = "";
                String specializare ="";
                for (String camp : campuri) {
                    String[] pereche = camp.trim().split("=");
                    if (pereche.length == 2) {
                        String cheie = pereche[0].trim();
                        String valoare = pereche[1].trim();
                        switch (cheie) {
                            case "email":
                                email = valoare;
                                break;
                            case "parola":
                                parola = valoare;
                                break;
                            case "nume":
                                nume = valoare;
                                break;
                            case "prenume":
                                prenume = valoare;
                                break;
                            case"specializare":
                                specializare=valoare;
                                break;
                        }
                    }
                }

                Doctor doctor = new Doctor(email, parola, nume, prenume);
                doctorService.persistDoctor(doctor);
                out.println("Inregistrare doctorului a fost realizata cu succes .");

            } else {
                out.println("Mesaj necunoscut pentru inregistrarea.");
            }
        }catch(Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            out.println("Eroare la inregistrare");
            e.printStackTrace();
        }finally {
            em.close();

        }
    }

}
