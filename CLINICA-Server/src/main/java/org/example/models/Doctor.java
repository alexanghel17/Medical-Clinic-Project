package org.example.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tabel_doctori")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="email",nullable = false, unique = true)
    private String email;
    @Column(name="parola",nullable = false)
    private String parola;
    @Column(name="nume",nullable = false)
    private String nume;
    @Column(name="prenume",nullable = false)
    private String prenume;
//    @Column(name="specializare",nullable = true)
//    private String specializare;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Programare> programari = new ArrayList<>();

    public Doctor(String email, String parola, String nume, String prenume) {
        this.email = email;
        this.parola = parola;
        this.nume = nume;
        this.prenume = prenume;
//        this.specializare = specializare;
    }

    public Doctor() {}

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

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Programare> getProgramari() {
        return programari;
    }

    public void setProgramari(List<Programare> programari) {
        this.programari = programari;
    }

//    public String getSpecializare() {
//        return specializare;
//    }
//
//    public void setSpecializare(String specializare) {
//        this.specializare = specializare;
//    }
}

