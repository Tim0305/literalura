package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Autor;
import com.aluracursos.literalura.models.Idioma;
import com.aluracursos.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByIdioma(Idioma idioma);

    List<Libro> findTop10ByOrderByDescargasDesc();
}
