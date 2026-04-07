package org.example.autentificare;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.models.Doctor;
import org.example.models.Pacient;


import java.io.PrintWriter;

public class LoginService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("conector-bd-alex");


    public void procesareLogin(String date, PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        String []campuri=date.substring("login".length()).trim().split(",");
        String email="";
        String parola="";

        for (String camp :campuri){
            String []pereche = camp.trim().split("=");
            if(pereche.length==2){
                String cheie=pereche[0].trim();
                String valoare=pereche[1].trim();

                if(cheie.equals("email")){
                    email=valoare;
                }
                else if(cheie.equals("parola")){
                    parola=valoare;
                }
            }
            }
            System.out.println("email:"+email);
            System.out.println("parola:"+parola);
            try {
                Pacient pacient = em.createQuery("SELECT p FROM Pacient p WHERE p.email = :email AND p.parola = :parola", Pacient.class)
                        .setParameter("email", email)
                        .setParameter("parola", parola)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if(pacient!=null&&pacient.getNume()!=null){
                    out.println("Autenticare reusita pentru pacientul "+pacient.getNume()+pacient.getPrenume());
                    return;
                }
                Doctor doctor=em.createQuery("SELECT d FROM Doctor d WHERE d.email= :email AND d.parola= :parola",Doctor.class)
                        .setParameter("email",email)
                        .setParameter("parola",parola)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                if(doctor != null && doctor.getNume()!=null){
                    out.println("Autentificarea reusita pentru doctorul "+doctor.getNume()+doctor.getPrenume());
                }
                else{
                    out.println("Eroare: Email sau parola introduse gresit!");
                }
            }catch(Exception e) {
                out.println("Eroare la autentificare");
                e.printStackTrace();
            }finally{
                if(em.isOpen()){
                    em.close();
            }

        }
    }

}
