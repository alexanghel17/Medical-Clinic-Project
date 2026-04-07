package org.example.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.models.Pacient;
import org.example.models.Programare;

public class ProgramareService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("conector-bd-alex");

    public void persistProgramare(Programare programare) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(programare);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Eroare la salvarea pacientului: " + e.getMessage());
        } finally {
            em.close();
        }
    }

}
