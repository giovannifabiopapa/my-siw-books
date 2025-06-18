package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Autore;
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
        autoreRepository.deleteById(id);
    }

    public boolean alreadyExists(Autore autore) {
        return autoreRepository.findByNomeAndCognome(autore.getNome(), autore.getCognome()).isPresent();
    }
}