package com.aluracursos.literatura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autor;

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getNacimientoAutor() {
        return nacimientoAutor;
    }

    public Integer getMuerteAutor() {
        return muerteAutor;
    }

    public String getIdioma() {
        return idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    private Integer nacimientoAutor;
    private Integer muerteAutor;
    private String idioma;
    private Integer descargas;

    public Libro(DatosLibro libro) {
        Autor autor = libro.autores().get(0);
        this.titulo = libro.titulo();
        this.autor = autor.name();
        this.nacimientoAutor = autor.birth_year();
        this.muerteAutor = autor.death_year();
        this.idioma = libro.idiomas().get(0);
        this.descargas = libro.descargas();
    }
    public Libro(){}
    @Override
    public String toString(){
        return "--------- LIBRO --------- " + '\n' +
                    "Título: " + this.titulo + "\n" +
                    "Autor: " + this.autor + "\n" +
                    "Idioma: " + this.idioma + "\n" +
                    "Número de descargas: " + this.descargas + "\n" +
                    "-------------------------" + "\n";
    }
}