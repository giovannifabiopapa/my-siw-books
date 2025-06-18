package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long> {
    Optional<Libro> findByTitolo(String titolo);
}