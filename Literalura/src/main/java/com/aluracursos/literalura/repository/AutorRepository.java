package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Autor;
import com.aluracursos.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    List<Libro> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT a from Autor a WHERE a.nombre ILIKE %:nombre%")
    List<Autor> buscarAutorPorNombre(String nombre);

    List<Autor> findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqualOrAnioMuerteIsNull(int anioNacimiento, int anioMuerte);

    @Query("SELECT a from Autor a WHERE a.anioNacimiento <= :anio AND a.anioMuerte >= :anio")
    List<Autor> obtenerAutoresVivosEnUnDeterminadoAnio(int anio);
}
