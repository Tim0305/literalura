package com.aluracursos.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private Integer anioNacimiento;
    private Integer anioMuerte;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosAutor autor) {
        this.nombre = autor.nombre();
        this.anioNacimiento = autor.anioNacimiento();
        this.anioMuerte = autor.anioMuerte();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioMuerte() {
        return anioMuerte;
    }

    public void setAnioMuerte(Integer anioMuerte) {
        this.anioMuerte = anioMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }

    public void addLibro(Libro libro) {
        libro.setAutor(this);
        libros.add(libro);
    }

    @Override
    public String toString() {
        String librosString = libros.stream()
                .map(l -> l.getTitulo())
                .collect(Collectors.joining(" - ")); // junta todos los elementos en un string separado, y divide cada elemento por comas
        return nombre + " [" + anioNacimiento + " - " + anioMuerte + "]" +
                "\nLibros: " + librosString;
    }
}