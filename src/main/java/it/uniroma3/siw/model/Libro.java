package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Libro {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String titolo;
        private Integer annoPubblicazione;

        @ElementCollection
        private List<String> immagini = new ArrayList<>();

        @ManyToMany
        private List<Autore> autori = new ArrayList<>();

        @OneToMany(mappedBy = "libro")
        private List<Recensione> recensioni = new ArrayList<>();

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getTitolo() {
                return titolo;
        }

        public void setTitolo(String titolo) {
                this.titolo = titolo;
        }

        public Integer getAnnoPubblicazione() {
                return annoPubblicazione;
        }

        public void setAnnoPubblicazione(Integer annoPubblicazione) {
                this.annoPubblicazione = annoPubblicazione;
        }

        public List<String> getImmagini() {
                return immagini;
        }

        public void setImmagini(List<String> immagini) {
                this.immagini = immagini;
        }

        public List<Autore> getAutori() {
                return autori;
        }

        public void setAutori(List<Autore> autori) {
                this.autori = autori;
        }

        public List<Recensione> getRecensioni() {
                return recensioni;
        }

        public void setRecensioni(List<Recensione> recensioni) {
                this.recensioni = recensioni;
        }
}