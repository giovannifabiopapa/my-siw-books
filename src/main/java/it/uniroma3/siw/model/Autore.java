package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Autore {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String nome;
        private String cognome;
        private LocalDate dataDiNascita;
        private LocalDate dataDiMorte;
        private String nazionalita;
        private String fotografia;

        @ManyToMany(mappedBy = "autori")
        private List<Libro> libri = new ArrayList<>();

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getCognome() {
                return cognome;
        }

        public void setCognome(String cognome) {
                this.cognome = cognome;
        }

        public LocalDate getDataDiNascita() {
                return dataDiNascita;
        }

        public void setDataDiNascita(LocalDate dataDiNascita) {
                this.dataDiNascita = dataDiNascita;
        }

        public LocalDate getDataDiMorte() {
                return dataDiMorte;
        }

        public void setDataDiMorte(LocalDate dataDiMorte) {
                this.dataDiMorte = dataDiMorte;
        }

        public String getNazionalita() {
                return nazionalita;
        }

        public void setNazionalita(String nazionalita) {
                this.nazionalita = nazionalita;
        }

        public String getFotografia() {
                return fotografia;
        }

        public void setFotografia(String fotografia) {
                this.fotografia = fotografia;
        }

        public List<Libro> getLibri() {
                return libri;
        }

        public void setLibri(List<Libro> libri) {
                this.libri = libri;
        }
}