package it.uniroma3.siw.service;

import java.util.ArrayList;
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
        return recensioneRepository.save(recensione);
    }

    @Transactional
    public void deleteRecensione(Long id) {
        recensioneRepository.deleteById(id);
    }

    public boolean alreadyReviewed(Libro libro, User autore) {
        return recensioneRepository.existsByLibroAndAutore(libro, autore);
    }
}