package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import jakarta.transaction.Transactional;

@Service
public class AutoreService {

    @Autowired
    private AutoreRepository autoreRepository;

    @Transactional
    public Autore getAutore(Long id) {
        Optional<Autore> result = autoreRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public List<Autore> getAllAutori() {
        List<Autore> result = new ArrayList<>();
        autoreRepository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public Autore saveAutore(Autore autore) {
        return autoreRepository.save(autore);
    }

    @Transactional
    public void deleteAutore(Long id) {
        Autore autore = autoreRepository.findById(id).orElse(null);
        if (autore != null) {
            // Rimuovi l'autore da tutti i libri a cui è associato
            for (Libro libro : autore.getLibri()) {
                libro.getAutori().remove(autore);
            }
            autore.getLibri().clear(); // aggiorna anche lato autore
            autoreRepository.delete(autore);
        }
    }


    public boolean alreadyExists(Autore autore) {
        return autoreRepository.findByNomeAndCognome(autore.getNome(), autore.getCognome()).isPresent();
    }
}