package com.aluracursos.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias(value = "title") String titulo,
        @JsonAlias(value = "authors") List<Autor> autores,
        @JsonAlias(value = "languages") List<String> idiomas,
        @JsonAlias(value = "download_count") Integer descargas
) {
}
