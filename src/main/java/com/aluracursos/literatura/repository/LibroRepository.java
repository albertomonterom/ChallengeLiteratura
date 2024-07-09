package com.aluracursos.literatura.repository;

import com.aluracursos.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query(value = "SELECT * FROM libros l WHERE l.idioma LIKE :idioma", nativeQuery = true)
    List<Libro> listarLibrosPorIdioma(String idioma);
    @Query(value = "SELECT * FROM libros l WHERE :annio < l.muerte_autor", nativeQuery = true)
    List<Libro> listarAutoresVivos(Integer annio);
    Optional<Libro> findByTitulo(String nombreLibro);
}
