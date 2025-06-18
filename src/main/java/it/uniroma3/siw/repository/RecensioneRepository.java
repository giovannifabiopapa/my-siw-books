package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {
    boolean existsByLibroAndAutore(Libro libro, User autore);
    List<Recensione> findByLibro(Libro libro);
    Optional<Recensione> findByLibroAndAutore(Libro libro, User autore);
}