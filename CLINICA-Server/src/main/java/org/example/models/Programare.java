package org.example.models;

import jakarta.persistence.*;
import java.time.*;
@Entity
@Table(name="tabel_programari")

public class Programare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProgramare;

    @ManyToOne
    @JoinColumn(name = "pacient_id", nullable = false)
    private Pacient pacient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "ora", nullable = false)
    private LocalTime ora;

    @Column( name="recomandare")
    private String recomandare;

    @Column(name="diagnostic")
    private String diagnostic;

    @Column(name="tratament")
    private String tratament;


    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getTratament() {
        return tratament;
    }

    public void setTratament(String tratament) {
        this.tratament = tratament;
    }

    public Programare(int idProgramare, Pacient pacient, Doctor doctor, LocalDate data, LocalTime ora, String recomandare) {
        this.idProgramare = idProgramare;
        this.pacient = pacient;
        this.doctor = doctor;
        this.data = data;
        this.ora = ora;
        this.recomandare = recomandare;
    }
    public Programare() {}

    public String getRecomandare() {
        return recomandare;
    }

    public void setRecomandare(String recomandare) {
        this.recomandare = recomandare;
    }

    public int getIdProgramare() {
        return idProgramare;
    }

    public Pacient getPacient() {
        return pacient;
    }

    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }
    public String afisareProgramare() {
        return ("La doctorul " + getDoctor().getNume() + " " + getDoctor().getPrenume() +
                ", data: " + getData() + ", ora: " + getOra() +
                ", recomandare: " + (getRecomandare() != null ? getRecomandare() : "N/A") +
                ", diagnostic: " + (getDiagnostic() != null ? getDiagnostic() : "N/A") +
                ", tratament: " + (getTratament() != null ? getTratament() : "N/A"));
    }


    public String afisareOreProgramarePtDoc() {
        return "Ora: "+getOra();
    }
}



