package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.RecensioneRepository;
import jakarta.transaction.Transactional;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Transactional
    public Recensione getRecensione(Long id) {
        Optional<Recensione> result = recensioneRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public List<Recensione> getRecensioniByLibro(Libro libro) {
        return recensioneRepository.findByLibro(libro);
    }

    @Transactional
    public Recensione saveRecensione(Recensione recensione) {
        if (recensione.getId() != null) {
            // Cerchiamo la recensione già esistente
            Recensione managed = recensioneRepository.findById(recensione.getId()).orElse(null);
            if (managed != null) {
                // Aggiorniamo solo i campi modificabili
                managed.setTitolo(recensione.getTitolo());
                managed.setTesto(recensione.getTesto());
                managed.setVoto(recensione.getVoto());
                return recensioneRepository.save(managed);
            } else {
                recensione.setId(null); // per forzare l'inserimento se l'ID non esiste
            }
        }
        return recensioneRepository.save(recensione);
    }



    @Transactional
    public void deleteRecensione(Long id) {
        recensioneRepository.deleteById(id);
    }
    
    @Transactional
    public Recensione getByLibroAndAutore(Libro libro, User autore) {
        return recensioneRepository.findByLibroAndAutore(libro, autore).orElse(null);
    }

    public boolean alreadyReviewed(Libro libro, User autore) {
        return recensioneRepository.existsByLibroAndAutore(libro, autore);
    }
}