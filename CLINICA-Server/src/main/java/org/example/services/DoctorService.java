package org.example.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.example.models.Doctor;
import org.example.models.Pacient;
import org.example.models.Programare;
import jakarta.persistence.*;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("conector-bd-alex");

    public void persistDoctor(Doctor doctor) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(doctor);
            em.getTransaction().commit();
            System.out.println("Înregistrarea doctorului a fost realizată cu succes.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Eroare la salvarea doctorului: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void procesareComandaDoctor(String comanda, PrintWriter out) {
        if (comanda.startsWith("vezi_pacienti")) {
            vizualizeazaPacienti(comanda, out);
        } else if (comanda.startsWith("vezi_programarile")) {
            programariPeZi(comanda, out);
        } else if (comanda.startsWith("service,")) {
            diagnostic_tratament(comanda, out);
        } else if (comanda.startsWith("editare,")) {
            editareSauStergereProgramare(comanda, out);
        } else {
            out.println("Comanda necunoscuta pentru pacient.");
        }
    }

    public void programariPeZi(String comanda, PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("Comanda primită: [" + comanda + "]");
            System.out.println("Parametri extrași: [" + comanda.substring("vezi_programarile".length()).trim() + "]");

            String[] campuri = comanda.substring("vezi_programarile".length()).trim().split(",");
            String emailDoc = null;
            LocalDate data = null;

            for (String camp : campuri) {
                String[] parts = camp.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "email_doctor":
                            emailDoc = value;
                            break;
                        case "data":
                            data = LocalDate.parse(value);
                            break;
                    }
                }
            }

            if (emailDoc != null && data != null) {
                em.getTransaction().begin();

                TypedQuery<Programare> query = em.createQuery(
                        "SELECT pr FROM Programare pr " +
                                "JOIN pr.doctor d " +
                                "WHERE d.email = :email AND pr.data = :data " +
                                "ORDER BY pr.ora ASC", Programare.class
                );
                query.setParameter("email", emailDoc);
                query.setParameter("data", data);
                List<Programare> programari = query.getResultList();

                System.out.println(" programari"+programari.size());
                if(programari.size()==0) {
                    out.println("Nu exista pg in ziua data.");
                }

                for (Programare p : programari) {
                    out.println(p.afisareOreProgramarePtDoc());
                }

                em.getTransaction().commit();
            } else {
                out.println("Parametrii incompleți: este necesar 'email_doctor' și 'data'");
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            out.println("Eroare la vizualizarea programărilor: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void vizualizeazaPacienti(String comanda, PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        try {
            String[] campuri = comanda.substring("vezi_pacienti".length()).trim().split(",");
            String emailDoc = null;

            for (String camp : campuri) {
                String[] parts = camp.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (key.equals("email_doctor")) {
                        emailDoc = value;
                    }
                }
            }

            if (emailDoc == null) {
                out.println("Parametru lipsă: email_doctor");
                return;
            }

            TypedQuery<Programare> query = em.createQuery(
                    "SELECT pr FROM Programare pr " +
                            "JOIN FETCH pr.pacient p " +
                            "JOIN FETCH pr.doctor d " +
                            "WHERE d.email = :email", Programare.class);
            query.setParameter("email", emailDoc);

            List<Programare> programari = query.getResultList();

            if (programari.isEmpty()) {
                out.println("Nu aveți pacienți programați.");
            } else {
                for (Programare pr : programari) {
                    Pacient p = pr.getPacient();
                    out.println("Programare ID: " + pr.getIdProgramare() +
                            " | Pacient: " + p.getNume() + " " + p.getPrenume() +
                            " (Pacient ID: " + p.getId() + ")");
                }
            }
        } catch (Exception e) {
            out.println("Eroare la extragerea pacienților: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void diagnostic_tratament(String comanda, PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            if (comanda.startsWith("service,")) {
                comanda = comanda.substring("service,".length()).trim();
            }

            String diagnostic = "";
            String tratament = "";
            int idPacient = -1;
            int idProgramare = -1;

            String[] campuri = comanda.split(",");
            for (String camp : campuri) {
                String[] parts = camp.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "diagnostic":
                            diagnostic = value;
                            break;
                        case "tratamentul":
                            tratament = value;
                            break;
                        case "idPacient":
                            idPacient = Integer.parseInt(value);
                            break;
                        case "idProgramare":
                            idProgramare = Integer.parseInt(value);
                            break;
                    }
                }
            }

            if (idPacient == 0 || idProgramare == 0) {
                out.println("Eroare: idPacient și idProgramare sunt obligatorii.");
                return;
            }

            tx.begin();

            Programare programare = em.find(Programare.class, idProgramare);
            if (programare == null) {
                out.println("Programarea cu id " + idProgramare + " nu a fost găsită.");
                tx.rollback();
                return;
            }

            if (programare.getPacient() == null || programare.getPacient().getId() != idPacient) {
                out.println("Eroare: Programarea nu aparține pacientului specificat.");
                tx.rollback();
                return;
            }

            programare.setDiagnostic(diagnostic);
            programare.setTratament(tratament);
            em.merge(programare);

            tx.commit();
            out.println("Diagnostic și tratament setate pentru programarea #" + idProgramare);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            out.println("Eroare la diagnostic_tratament: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    public void editareSauStergereProgramare (String comanda, PrintWriter out){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            String[] campuri = comanda.substring("editare,".length()).trim().split(",");
            int idProgramare = -1;
            String noulDiag = "";
            String nouTratament = "";
            String dataNoua = null;
            String oraNoua = null;
            boolean stergere = false;

            for (String camp : campuri) {
                String[] parts = camp.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "diagnostic":
                            noulDiag = value;
                            break;
                        case "tratamentul":
                            nouTratament = value;
                            break;
                        case "idProgramare":
                            idProgramare = Integer.parseInt(value);
                            break;
                        case "data":
                            dataNoua = value;
                            break;
                        case "ora":
                            oraNoua = value;
                            break;
                        case "stergere":
                            stergere = value.equalsIgnoreCase("true");
                            break;
                    }
                }
            }

            if (idProgramare <= 0) {
                out.println("Eroare: idProgramare este obligatoriu și trebuie să fie pozitiv.");
                return;
            }

            tx.begin();

            Programare programare = em.find(Programare.class, idProgramare);
            if (programare == null) {
                out.println("Eroare: Programarea cu id " + idProgramare + " nu a fost găsită.");
                tx.rollback();
                return;
            }

            if (stergere) {
                em.remove(programare);
                tx.commit();
                out.println("Programarea cu id " + idProgramare + " a fost ștearsă cu succes.");
                return;
            }

            if (!noulDiag.isEmpty()) {
                programare.setDiagnostic(noulDiag);
            }

            if (!nouTratament.isEmpty()) {
                programare.setTratament(nouTratament);
            }

            if (dataNoua != null) {
                programare.setData(LocalDate.parse(dataNoua));
            }

            if (oraNoua != null) {
                programare.setOra(LocalTime.parse(oraNoua));
            }

            em.merge(programare);
            tx.commit();
            out.println("Programarea cu id " + idProgramare + " a fost actualizată.");

        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            out.println("Eroare la actualizarea/ștergerea programării: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
