package com.aluracursos.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer descargas;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro libro) {
        this.titulo = libro.titulo();
        this.idioma = Idioma.fromString(libro.idiomas().get(0).toLowerCase());
        this.descargas = libro.descargas();
        // Comunicacion bidireccional
        this.autor = new Autor(libro.autores().get(0)); // Obtener solo el primer autor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return  "\nTitulo= '" + titulo + '\'' +
                "\nIdioma= " + idioma.name() +
                "\nDescargas= " + descargas +
                "\nAutor= " + autor.getNombre() + " [" + autor.getAnioNacimiento() + " - " + autor.getAnioMuerte() + "]";
    }
}
