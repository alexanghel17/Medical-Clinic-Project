package org.example.services;

import jakarta.persistence.*;
import org.example.models.Doctor;
import org.example.models.Pacient;
import org.example.models.Programare;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PacientService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("conector-bd-alex");

    public void procesareComandaPacient(String comanda, PrintWriter out) {
        if (comanda.startsWith("vezi_doctor")) {
            vizualizareDoctori(out);
        } else if (comanda.startsWith("programare_pacient")) {
            programare(comanda, out);
        } else if (comanda.startsWith("vezi_istoric")) {
            veziIstoric(comanda, out);
        }else if(comanda.startsWith("editare_pacient,")) {
            editarePacient(comanda, out);
        } else {
            out.println("Comanda necunoscuta pentru pacient.");
        }
    }
    public void persistPacient(Pacient pacient) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pacient);
            em.getTransaction().commit();
            System.out.println("Înregistrarea pacientului a fost realizată cu succes.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Eroare la salvarea pacientului: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public void vizualizareDoctori(PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Doctor> doctori = em.createQuery("SELECT d FROM Doctor d", Doctor.class).getResultList();
            if (doctori.isEmpty()) {
                out.println("Nu exista doctori inregistrati.");
            } else {
                for (Doctor d : doctori) {
                    out.println("Doctor: " + d.getNume() + " " + d.getPrenume() + " (ID: " + d.getId() + ")");
                }
            }
        } catch (Exception e) {
            out.println("Eroare la extragerea doctorilor: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void programare(String comanda, PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            String[] campuri = comanda.substring("programare_pacient".length()).split(",");
            int doctorId = -1;
            String pacientEmail = "";
            String recomandare = "";
            LocalDate data = null;
            LocalTime ora = null;

            for (String camp : campuri) {
                String[] parts = camp.trim().split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "pacient_email":
                            pacientEmail = value;
                            break;
                        case "doctor_id":
                            doctorId = Integer.parseInt(value);
                            break;
                        case "data":
                            data = LocalDate.parse(value);
                            break;
                        case "ora":
                            ora = LocalTime.parse(value);
                            break;
                        case "recomandare":
                            recomandare = value;
                            break;
                    }
                }
            }

            tx.begin();

            Pacient pacient = em.createQuery("SELECT p FROM Pacient p WHERE p.email = :email", Pacient.class)
                    .setParameter("email", pacientEmail)
                    .getSingleResult();

            Doctor doctor = em.find(Doctor.class, doctorId);

            if (pacient == null || doctor == null) {
                out.println("Pacientul sau doctorul nu exista.");
                tx.rollback();
                return;
            }

            Programare programare = new Programare();
            programare.setPacient(pacient);
            programare.setDoctor(doctor);
            programare.setData(data);
            programare.setOra(ora);
            programare.setRecomandare(recomandare);

            em.persist(programare);
            tx.commit();

            out.println("Programarea a fost realizată cu succes.");

        } catch (NoResultException e) {
            if (tx.isActive()) tx.rollback();
            out.println("Pacientul cu emailul specificat nu a fost găsit.");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            out.println("Eroare la programare: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void veziIstoric(String comanda, PrintWriter out) {
        EntityManager em = emf.createEntityManager();

        try {

            String[] parts = comanda.substring("vezi_istoric,".length()).trim().split("=");
            String email =parts[1].trim();
            System.out.println("email"+email);

            Pacient pacient = em.createQuery("SELECT p FROM Pacient p WHERE p.email = :email", Pacient.class)
                    .setParameter("email", email)
                    .getSingleResult();
            System.out.println("pacient.pacint.getId"+pacient.getId());
            List<Programare> programari = em.createQuery(
                            "SELECT pr FROM Programare pr WHERE pr.pacient.id = :id ORDER BY pr.data DESC, pr.ora DESC",
                            Programare.class)
                    .setParameter("id", pacient.getId())
                    .getResultList();

            if (programari.isEmpty()) {
                System.out.println("programari is empty");
                out.println("Nu exista programari in istoricul pacientului.");
            } else {

                for (Programare pr : programari) {

                    out.println(pr.afisareProgramare());
                }
            }
        } catch (NoResultException e) {
            out.println("Pacientul nu a fost gasit.");
        } catch (Exception e) {
            out.println("Eroare la extragerea istoricului: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    public void editarePacient(String comanda, PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            String[] campuri = comanda.substring("editare_pacient,".length()).split(",");
            String emailInitial = null;
            String emailNou = null;
            String parola = null;
            String nume = null;
            String prenume = null;
            String alergii = null;

            for (String camp : campuri) {
                String[] parts = camp.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "email_initial":
                            emailInitial = value;
                            break;
                        case "email_nou":
                            emailNou = value;
                            break;
                        case "parola":
                            parola = value;
                            break;
                        case "nume":
                            nume = value;
                            break;
                        case "prenume":
                            prenume = value;
                            break;
                        case "alergii":
                            alergii = value;
                            break;
                    }
                }
            }

            if (emailInitial == null) {
                out.println("Eroare: emailul pacientului este necesar.");
                return;
            }

            tx.begin();

            TypedQuery<Pacient> query = em.createQuery(
                    "SELECT p FROM Pacient p WHERE p.email = :email", Pacient.class);
            query.setParameter("email", emailInitial);
            List<Pacient> rezultate = query.getResultList();

            if (rezultate.isEmpty()) {
                out.println("Pacientul nu a fost găsit.");
                tx.rollback();
                return;
            }

            Pacient pacient = rezultate.get(0);

            if (emailNou != null) pacient.setEmail(emailNou);
            if (parola != null) pacient.setParola(parola);
            if (nume != null) pacient.setNume(nume);
            if (prenume != null) pacient.setPrenume(prenume);
            if (alergii != null) pacient.setAlergii(alergii);

            em.merge(pacient);
            tx.commit();
            out.println("Datele pacientului au fost actualizate cu succes.");

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            out.println("Eroare la editarea pacientului: " + e.getMessage());
        } finally {
            em.close();
        }
    }

}
