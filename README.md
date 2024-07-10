<h1 align="center"><strong>Literalura</strong></h1>

Librería digital en Java con Spring Framework

<p align="center">
  <img src = "imagen/imagen.jpg" alt = "Imagen representativa de la funcionalidad de la aplicacion">
</p>

## Descripción del proyecto
Literalura es un proyecto realizado en Java con ayuda de Spring Framework y PostgresSQL. Es una librería digital que se basa en los datos de libros de la API de <a href="https://gutendex.com/">Gutendex</a>.

## :hammer: Funcionalidades del proyecto
- `Agregar Libros`: El usuario puede agregar nuevos libros a la base de datos ingresando el nombre del libro a buscar. Litaralura es flexible en este aspecto porque permite buscar todos los libros que contengan en el nombre del titulo el nombre que el usuario ingresó.
- `Mostrar Libros registrados en la BD`: El usuario puede visualizar todos los datos de los libros registrados en la base de datos.
- `Mostrar Autores registrados en la BD`: El usuario puede visualizar todos los datos de los autores registrados en la base de datos.
- `Buscar Libros por nombre`: El usuario puede buscar todos los libros en la base de datos que contengan en su nombre el nombre que el usuario ingrese.
- `Buscar Libros por idioma`: El usuario puede buscar todos los libros en la base de datos que fueron escritos en un idioma en específico.
- `Buscar Libros por autor`: El usuario puede buscar todos los libros en la base de datos que fueron escritos por un autor en específico.
- `Buscar Libros por año de autor`: El usuario puede buscar todos los libros en la base de datos que fueron escritos por un autor en un año en específico.
- `Mostrar Top 10 Libros en Literalura`: El usuario puede visualizar los Top 10 Libros registrados en Literalura basados en la cantidad de descargas de cada libro.
- `Mostrar Top 10 Libros en la API`: El usuario puede visualizar los Top 10 Libros registrados en la API de <a href="https://gutendex.com/">Gutendex</a> basados en la cantidad de descargas de cada libro.
- `Mostrar estadísticas de los libros por idioma`: El usuario puede visualizar las estadísticas de los libros escritos en un idioma en específico.
  - `Cantidad Máxima de Descargas`
  - `Cantidad Mínima de Descargas`
  - `Promedio de Descargas`
  - `Cantidad Total de Descargas`
  - `Cantidad de Libros en ese idioma`


## 📂 Acceso al proyecto
Para acceder al proyecto solo es descargar la carpeta Moneycon y ejecutarla.

<p align="center">
  <img style="height: 100" src = "https://github.com/Tim0305/literalura/assets/83103008/f8424290-ff88-4ef9-b366-c06768f7419d" alt = "Spring Framework">
  <img src = "https://github.com/Tim0305/literalura/assets/83103008/7069072b-af69-4720-bad3-101b55dc3678" alt = "PostgreSQL">
</p>


## ✔️ Tecnologias usadas:
- Java 17
- IntelliJ IDEA
- Jackson (para mapear los datos del archivo Json)
- PostgreSQL (para almacenar todos los datos en una base de datos relacional)
- Spring Framework v3.3.1
- Spring JPA (para mapear los objetos Java a entidades de una BD)
- Driver Spring PostgreSQL (para establecer una comunicación entre Spring Framework y PostgreSQL)
- <a href="https://gutendex.com/">Gutendex</a> (para obtener los datos de los libros y autores)

## 🔴 PATH Variables
En el archivo "application.properties" existen algunas variables:
- `$DB_HOST`: Es la dirección de la BD. Si el proyecto es local debe ser "localhost".
- `$DB_NAME`: Es el nombre de la BD.
- `$DB_USER`: Es el nombre de usuario dentro de la BD.
- `$DB_PASSWORD`: Es la contraseña del usuario dentro de la BD.

