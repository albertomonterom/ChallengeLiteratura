package com.aluracursos.literatura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
    /*
        DatosLibro datos obtenerDatos(String json, Class<DatosLibro> clase);
    */
}
