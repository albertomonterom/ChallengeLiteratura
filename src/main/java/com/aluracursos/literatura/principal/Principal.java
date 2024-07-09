package com.aluracursos.literatura.principal;

import com.aluracursos.literatura.model.DatosLibro;
import com.aluracursos.literatura.model.JSON;
import com.aluracursos.literatura.model.Libro;
import com.aluracursos.literatura.repository.LibroRepository;
import com.aluracursos.literatura.service.ConsumoAPI;
import com.aluracursos.literatura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Optional;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> libros;
    private LibroRepository repositorio;
    private String nombreLibro;
    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while(opcion != 0){
            var menu = """
                    ------------------------------------
                    Elije la opción a través de su número
                        1 - Buscar libro por título
                        2 - Listar libros registrados
                        3 - Listar autores registrados
                        4 - Listar autores vivos en un determinado año
                        5 - Listar libros por idioma
                        0 - salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año vivo del autor(es) que desea buscar");
        var annio = teclado.nextInt();
        teclado.nextLine();

        List<Libro> libros = repositorio.listarAutoresVivos(annio);
        mostrarMapOrdenadosPorAutor(libros);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                    es - español
                    en - ingles
                    fr - frances
                    pt - portugues
                """);
        var idioma = teclado.nextLine();
        List<Libro> libros = repositorio.listarLibrosPorIdioma(idioma);
        libros.stream()
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Libro> libros = repositorio.findAll();
        mostrarMapOrdenadosPorAutor(libros);
    }

    private void mostrarMapOrdenadosPorAutor(List<Libro> libros) {
        Map<String, List<Libro>> autoresLibros = libros.stream()
                .collect(Collectors.groupingBy(Libro::getAutor));

        System.out.println();
        autoresLibros.forEach((autor, listaLibros) -> {
            System.out.println("Autor: " + autor);

            if(!listaLibros.isEmpty()) {
                Libro primerLibro = listaLibros.get(0);
                System.out.println("Fecha de nacimiento: " + primerLibro.getNacimientoAutor());
                System.out.println("Fecha de fallecimiento: " + primerLibro.getMuerteAutor());

                System.out.print("Libros: ");
                List<String> items = new ArrayList<>();
                listaLibros.forEach(item -> {
                    items.add(item.getTitulo());
                });
                System.out.println(items);
            }
            System.out.println();
        });
    }

    private void listarLibrosRegistrados() {
        libros = repositorio.findAll();

        System.out.println();
        libros.stream()
                .forEach(System.out::println);
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        nombreLibro = teclado.nextLine();

        Optional<Libro> libroExistente = repositorio.findByTitulo(nombreLibro);
        if(libroExistente.isPresent()){
            System.out.println("No se puede registrar el libro más de una vez");
            return;
        }

        try {
            DatosLibro datos = getDatosLibro();
            Libro libro = new Libro(datos);
            repositorio.save(libro);
            System.out.println(libro);
        }catch(Exception e){
            System.out.println("Libro no encontrado");
        }
    }
    private DatosLibro getDatosLibro() throws Exception {
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        if(json == null) {
            throw new Exception("Datos no encontrados");
        }
        DatosLibro datos = conversor.obtenerDatos(json, JSON.class).libros().get(0);
        return datos;
    }
}
