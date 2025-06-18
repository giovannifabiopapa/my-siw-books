package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Recensione {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String titolo;
        private Integer voto;
        private String testo;

        @ManyToOne
        private Libro libro;

        @ManyToOne
        private User autore;

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

        public Integer getVoto() {
                return voto;
        }

        public void setVoto(Integer voto) {
                this.voto = voto;
        }

        public String getTesto() {
                return testo;
        }

        public void setTesto(String testo) {
                this.testo = testo;
        }

        public Libro getLibro() {
                return libro;
        }

        public void setLibro(Libro libro) {
                this.libro = libro;
        }

        public User getAutore() {
                return autore;
        }

        public void setAutore(User autore) {
                this.autore = autore;
        }
}