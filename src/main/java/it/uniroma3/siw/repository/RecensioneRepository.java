package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {
    boolean existsByLibroAndAutore(Libro libro, User autore);
    List<Recensione> findByLibro(Libro libro);
}