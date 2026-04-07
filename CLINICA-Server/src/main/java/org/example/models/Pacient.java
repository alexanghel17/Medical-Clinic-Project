package org.example.models;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tabel_pacienti")
public class Pacient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="email",nullable=false,unique=true)
    private String email;
    @Column(name="parola",nullable=false)
    private String parola;
    @Column(name="nume",nullable = false)
    private String nume;
    @Column(name="prenume",nullable = false)
    private String prenume;
    @Column(name="alergii")
    private String alergii;
    @OneToMany(mappedBy = "pacient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Programare> programari = new ArrayList<>();

    public Pacient() {}

    public Pacient(String email, String parola, String nume, String prenume, String alergii) {

        this.email = email;
        this.parola = parola;
        this.nume = nume;
        this.prenume = prenume;
        this.alergii = alergii;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAlergii() {
        return alergii;
    }

    public void setAlergii(String alergii) {
        this.alergii = alergii;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    public int getId() {
        return id;
    }


}