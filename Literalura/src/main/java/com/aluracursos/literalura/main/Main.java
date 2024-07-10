package com.aluracursos.literalura.main;

import com.aluracursos.literalura.models.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsultaAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class Main {

    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;
    private ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner teclado = new Scanner(System.in);
    private String URL_BASE = "https://gutendex.com/books/";

    // Constructor que recibe el repositorio de libro
    public Main(LibroRepository repositoryLibro, AutorRepository repositoryAutor) {
        this.repositoryLibro = repositoryLibro;
        this.repositoryAutor = repositoryAutor;
    }

    public void menu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Agregar Libro
                    2 - Buscar Libro Registrado
                    3 - Mostrar Libros Registrados
                    4 - Mostrar Autores Registrados
                    5 - Estadisticas
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            System.out.print("Opcion -> ");
            try {
                opcion = teclado.nextInt();
                // Limpiar el buffer de entrada
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        agregarLibro();
                        break;
                    case 2:
                        System.out.println("    1 - Por titulo");
                        System.out.println("    2 - Por idioma");
                        System.out.println("    3 - Por nombre del autor");
                        System.out.println("    4 - Por año del autor");
                        System.out.print("    Opcion -> ");
                        var opcionBusqueda = teclado.nextInt();
                        teclado.nextLine();
                        switch (opcionBusqueda) {
                            case 1:
                                buscarLibrosRegistradosPorTitulo();
                                break;
                            case 2:
                                buscarLibrosRegistradosPorIdioma();
                                break;
                            case 3:
                                buscarAutorRegistradoPorNombre();
                                break;
                            case 4:
                                buscarLibrosRegistradosPorAnioDeAutor();
                                break;
                            default:
                                System.out.println("\nOpcion incorrecta\n");
                                break;
                        }
                        break;
                    case 3:
                        mostrarLibrosRegistrados();
                        break;
                    case 4:
                        mostrarAutoresRegistrados();
                        break;
                    case 5:
                        System.out.println("    1 - Top 10 libros");
                        System.out.println("    2 - Libros en un idioma");
                        System.out.print("    Opcion -> ");
                        var opcionEstadisticas = teclado.nextInt();
                        teclado.nextLine();

                        switch (opcionEstadisticas) {
                            case 1:
                                System.out.println("        1 - Internamente en Literalura");
                                System.out.println("        2 - Globalmente");
                                System.out.print("        Opcion -> ");
                                var opcionLibros = teclado.nextInt();
                                teclado.nextLine();

                                switch (opcionLibros) {
                                    case 1:
                                        buscarTop10LibrosInternamente();
                                        break;
                                    case 2:
                                        buscarTop10LibrosGlobalmente();
                                        break;
                                    default:
                                        System.out.println("\nOpcion incorrecta\n");
                                        break;
                                }
                                break;
                            case 2:
                                mostrarEstadisticasLibrosPorIdiomas();
                                break;
                            default:
                                System.out.println("\nOpcion incorrecta\n");
                                break;
                        }
                        break;
                    default:
                        System.out.println("\nOpcion incorrecta\n");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nIngrese un dato valido\n");
                // Limpiar el buffer de entrada
                teclado.nextLine();
            }
        }
        teclado.close();
    }

    // ---------------------------------------------------------------------------------
    // ------------------------------------Funciones------------------------------------
    // ---------------------------------------------------------------------------------

    // Obtener libros de la API
    private List<Libro> getLibrosByTitulo(String titulo) {
        String json = ConsultaAPI.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "+"));
        Resultados resultados = conversor.convierteDatos(json, Resultados.class);
        return resultados.libros().stream()
                .map(l -> new Libro(l))
                .collect(Collectors.toList());
    }

    // --------------------------------------------------Insercion de libros--------------------------------------------------

    // Agregar el libro a la base de datos
    private void agregarLibro() {
        System.out.print("\nTitulo del libro: ");
        String titulo = teclado.nextLine();
        List<Libro> libros = getLibrosByTitulo(titulo);

        if (libros.isEmpty())
            System.out.println("\nLibros no encontrados\n");
        else {
            System.out.println();
            // Imprimir los libros encontrados
            for (int i = 0; i < libros.size(); i++) {
                System.out.println("Opcion: " + (i + 1));
                System.out.println(libros.get(i));
                System.out.println();
            }

            String opcionString = "";
            while (true) {
                System.out.print("Seleccione el libro que quiere agregar (ENTER Salir): ");
                opcionString = teclado.nextLine();
                if (!opcionString.isEmpty()) {
                    int indice = Integer.parseInt(opcionString) - 1;
                    if (indice < 0 || indice >= libros.size())
                        System.out.println("\nIngrese una opcion valida\n");
                    else
                        try {
                            Libro libro = libros.get(indice);
                            Optional<Autor> autorOptional = repositoryAutor.findByNombre(libro.getAutor().getNombre());
                            // Si el autor existe
                            if (autorOptional.isPresent()) {
                                Autor autor = autorOptional.get();
                                autor.addLibro(libro);
                                // Guardar el autor con la lista de libros actualizada
                                repositoryAutor.save(autor);
                            } else {
                                // Guardar libro y automaticamente se crea el autor
                                repositoryLibro.save(libro);
                            }
                        } catch (DataIntegrityViolationException e) {
                            System.out.println("\nEl libro ya esta registrado en la base de datos\n");
                        }
                } else
                    break;
            }
        }
    }

    // --------------------------------------------------Busqueda de libros--------------------------------------------------

    private void buscarLibrosRegistradosPorTitulo() {
        System.out.print("\nNombre del libro: ");
        String titulo = teclado.nextLine();

        List<Libro> librosEncontrados = repositoryLibro.findByTituloContainingIgnoreCase(titulo);

        if (librosEncontrados.isEmpty()) {
            System.out.println("\nLibros no encontrados\n");
        }
        else {
            System.out.println();
            librosEncontrados.forEach(l -> System.out.println(l + "\n"));
            String texto = " libros";
            if (librosEncontrados.size() == 1)
                texto = " libro";

            System.out.println("\nLibros registrados: " + librosEncontrados.size() + texto + "\n");
        }
    }

    private void buscarLibrosRegistradosPorIdioma() {
        Idioma.mostrarIdiomas();
        System.out.print("\nIdioma del libro: ");
        String idiomaInput = teclado.nextLine();

        Idioma idioma = Idioma.fromString(idiomaInput);
        List<Libro> librosEncontrados = repositoryLibro.findByIdioma(idioma);
        if (librosEncontrados.isEmpty()) {
            System.out.println("\nLibros no encontrados\n");
        }
        else {
            System.out.println();
            String texto = " libros";
            if (librosEncontrados.size() == 1)
                texto = " libro";

            System.out.println("\nLibros registrados: " + librosEncontrados.size() + texto + "\n");
            librosEncontrados.forEach(l -> System.out.println(l + "\n"));
        }
    }

    private void buscarAutorRegistradoPorNombre() {
        System.out.print("\nNombre del autor: ");
        String nombreAutor = teclado.nextLine();

        List<Autor> autores = repositoryAutor.buscarAutorPorNombre(nombreAutor);
        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados\n");
        } else {
            System.out.println();
            autores.forEach(a -> System.out.println(a + "\n"));
            String texto = " autores";
            if (autores.size() == 1)
                texto = " autor";

            System.out.println("\nAutores registrados: " + autores.size() + texto + "\n");
        }
    }

    private void buscarLibrosRegistradosPorAnioDeAutor() {
        System.out.print("\nAño: ");
        int anio = teclado.nextInt();
        teclado.nextLine();

//        List<Autor> autoresVivos = repositoryAutor.obtenerAutoresVivosEnUnDeterminadoAnio(anio);
        // Para hacer esto con una derived query solo debo mandar por parametro el mismo año ingresado por el usuario, ya que la comparacion se hace con el mismo año.
        List<Autor> autoresVivos = repositoryAutor.findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqualOrAnioMuerteIsNull(anio, anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("\nNo hay autores registrados\n");
        } else {
            System.out.println();
            autoresVivos.forEach(a -> System.out.println(a + "\n"));
            String texto = " autores";
            if (autoresVivos.size() == 1)
                texto = " autor";

            System.out.println("\nAutores registrados: " + autoresVivos.size() + texto + "\n");
        }
    }

    // --------------------------------------------------Lectura de datos--------------------------------------------------

    // Mostrar todos los libros registrados en la base de datos
    private void mostrarLibrosRegistrados() {
        List<Libro> librosRegistrados = repositoryLibro.findAll();
        if (librosRegistrados.isEmpty()) {
            System.out.println("\nNo hay libros registrados\n");
        } else {
            System.out.println();
            librosRegistrados.forEach(l -> System.out.println(l + "\n"));
            String texto = " libros";
            if (librosRegistrados.size() == 1)
                texto = " libro";

            System.out.println("\nLibros registrados: " + librosRegistrados.size() + texto + "\n");
        }
    }

    // Mostrar todos los autores registrados en la base de datos
    private void mostrarAutoresRegistrados() {
        List<Autor> autoresRegistrados = repositoryAutor.findAll();

        if (autoresRegistrados.isEmpty()) {
            System.out.println("\nNo hay autores registrados\n");
        }
        else {
            System.out.println();
            autoresRegistrados.forEach(a -> System.out.println(a + "\n"));
            String texto = " autores";
            if (autoresRegistrados.size() == 1)
                texto = " autor";

            System.out.println("\nAutores registrados: " + autoresRegistrados.size() + texto + "\n");
        }
    }

    // --------------------------------------------------Estadisticas--------------------------------------------------

    // Top 10 libros internamente
    private void buscarTop10LibrosInternamente() {
        List<Libro> libros = repositoryLibro.findTop10ByOrderByDescargasDesc();
        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados\n");
        }
        else {
            System.out.println();
            System.out.println("\nTop 10 Libros en Literalura\n");
            // Establecer un contador para que muestre Top1 ..... Top10
            AtomicInteger index = new AtomicInteger(1);
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getDescargas).reversed())
                    .limit(10)
                    .forEach(l -> {
                        int i = index.getAndIncrement();
                        System.out.println("Top: " + i);
                        System.out.println(l + "\n");
                    });
        }
    }

    // Top 10 libros de la API
    private void buscarTop10LibrosGlobalmente() {
        var json = ConsultaAPI.obtenerDatos(URL_BASE);
        Resultados resultados = conversor.convierteDatos(json, Resultados.class);

        // Establecer un contador para que muestre Top1 ..... Top10
        AtomicInteger index = new AtomicInteger(1);
        resultados.libros().stream()
                .sorted(Comparator.comparing(DatosLibro::descargas).reversed())
                .limit(10)
                .map(l -> new Libro(l))
                .forEach(l -> {
                    int i = index.getAndIncrement();
                    System.out.println("Top: " + i);
                    System.out.println(l + "\n");
                });
    }

    // Estadisticas de los libros por idioma
    private void mostrarEstadisticasLibrosPorIdiomas() {

        Idioma.mostrarIdiomas();
        System.out.print("\nIdioma del libro: ");
        String idiomaInput = teclado.nextLine();
        Idioma idioma = Idioma.fromString(idiomaInput);
        List<Libro> librosEncontrados = repositoryLibro.findByIdioma(idioma);
        if (librosEncontrados.isEmpty()) {
            System.out.println("\nLibros no encontrados\n");
        }
        else {
            System.out.println();
            IntSummaryStatistics ist = librosEncontrados.stream().mapToInt(Libro::getDescargas).summaryStatistics();
            System.out.println("Total de libros: " + ist.getCount());
            System.out.println("Total de descargas: " + ist.getSum());
            System.out.println("Promedio de descargas: " + ist.getAverage());
            System.out.println("Cantidad maxima de descargas: " + ist.getMax());
            System.out.println("Cantidad minima de descargas: " + ist.getMin());
            System.out.println();
        }
    }
}