package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.LibroRepository;
import jakarta.transaction.Transactional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Transactional
    public Libro getLibro(Long id) {
        Optional<Libro> result = libroRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public List<Libro> getAllLibri() {
        List<Libro> result = new ArrayList<>();
        libroRepository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public Libro saveLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Transactional
    public void deleteLibro(Long id) {
        libroRepository.deleteById(id);
    }

    public boolean alreadyExists(Libro libro) {
        return libroRepository.findByTitolo(libro.getTitolo()).isPresent();
    }
}