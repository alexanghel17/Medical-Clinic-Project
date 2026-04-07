package org.example.database;
import jakarta.persistence.*;


import java.util.List;

public class DataBaseManager {

    private EntityManagerFactory emf;
    private EntityManager em;

    public DataBaseManager() {
        emf = Persistence.createEntityManagerFactory("conector-bd-alex");
        em = emf.createEntityManager();
    }

//    public void insertClient(Client client) {
//        em.getTransaction().begin();
//        em.persist(client);
//        em.getTransaction().commit();
//    }
//
//    public void deleteClient(int id ) {
//        em.getTransaction().begin();
//        Client client = em.find(Client.class,id);
//        if(client != null) {
//            em.remove(client);
//            System.out.println("Clientul cu ID-ul "+id+" nu a fost sters.");
//        }
//        else{
//            System.out.println("Clientul cu ID-ul "+id+" nu a fost gasit.");
//        }
//        em.getTransaction().commit();
//    }
//
//    public void updateClient(int id ,String numeNou, String prenumeNou) {
//        em.getTransaction().begin();
//        Client client = em.find(Client.class,id);
//        if(client != null) {
//            client.setNume(numeNou);
//            client.setPrenume(prenumeNou);
//        }
//        else{
//            System.out.println("Clientul cu ID-ul "+id+" nu a fost gasit.");
//        }
//        em.getTransaction().commit();
//    }
    public void close() {
        if (em != null) em.close();
        if (emf != null) emf.close();
        System.out.println("EntityManager și EntityManagerFactory au fost închise.");
    }

//    public List<Client> getAllClients() {
//        return em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
//    }

}
