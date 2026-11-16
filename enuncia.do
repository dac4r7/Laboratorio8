
Laboratorio 8
Requisitos de finalización

Laboratorio JDBC: CRUD de una Biblioteca
Objetivo
Desarrollar una aplicación de consola en Java que permita administrar información de una biblioteca utilizando JDBC (Java Database Connectivity).
El objetivo principal es que puedan conectarse desde Java a una base de datos MariaDB/MySQL, ejecutar sentencias SQL mediante JDBC y realizar las operaciones básicas de un CRUD:
•	Create → Insertar registros.
•	Read → Consultar registros.
•	Update → Modificar registros.
•	Delete → Eliminar registros.
________________________________________
Base de datos
Se utilizará la base de datos Biblioteca_DB, adjunta abajo.
Tendrán que importarla en MariaDB/MySQL ya que cuenta con toda la estructura y datos.
________________________________________
Consigna
Desarrollar una aplicación Java de consola denominada:
Sistema de Gestión de Biblioteca
La aplicación deberá conectarse a la base de datos Biblioteca_DB mediante JDBC y permitir al usuario realizar operaciones CRUD sobre las tablas indicadas.
1. Conexión a la base de datos
Implementar la conexión utilizando JDBC.
La aplicación deberá:
1.	Cargar el driver JDBC correspondiente.
2.	Establecer una conexión con MariaDB/MySQL.
3.	Utilizar un objeto Connection.
4.	Informar al usuario si la conexión fue exitosa.
5.	Manejar correctamente las posibles excepciones.
La información de conexión deberá estar centralizada en el programa, evitando repetir la URL, usuario y contraseña en distintos lugares.
________________________________________
2. Menú principal
La aplicación deberá presentar un menú similar al siguiente:
====================================
       SISTEMA DE BIBLIOTECA
====================================

1. Gestionar libros
2. Gestionar socios
3. Gestionar ejemplares
4. Gestionar préstamos
0. Salir

Seleccione una opción:
Cada opción deberá presentar su propio submenú.
________________________________________
3. CRUD de LIBROS
Implementar las siguientes operaciones:
3.1. Agregar libro
Solicitar al usuario:
•	ISBN
•	Título
•	Autor
•	Editorial
•	Año de publicación
Insertar el nuevo libro en la tabla LIBRO.
Utilizar PreparedStatement.
________________________________________
3.2. Listar libros
Mostrar todos los libros almacenados.
Ejemplo:
ISBN              TÍTULO                  AUTOR
----------------------------------------------------------------
9789500728002     Cien Años de Soledad    Gabriel García Márquez
9789500728019     Rayuela                 Julio Cortázar
...
La información deberá obtenerse mediante un SELECT y recorrer el resultado utilizando un ResultSet.
________________________________________
3.3. Buscar libro
Permitir ingresar un ISBN y mostrar la información del libro correspondiente.
Si el libro no existe, informar:
No se encontró un libro con ese ISBN.
________________________________________
3.4. Modificar libro
Permitir modificar:
•	Título
•	Autor
•	Editorial
•	Año de publicación
El ISBN no deberá modificarse.
Antes de realizar la actualización, verificar que el libro exista.
________________________________________
3.5. Eliminar libro
Solicitar el ISBN del libro que se desea eliminar.
Antes de eliminarlo, informar al usuario qué libro será eliminado y solicitar confirmación.
Por ejemplo:
¿Está seguro de eliminar el libro "Rayuela"? (S/N):
Tener en cuenta que la base de datos posee una relación entre LIBRO y EJEMPLAR.
________________________________________
4. CRUD de SOCIOS
Implementar las operaciones:
4.1. Agregar socio
Solicitar:
•	Nombre
•	DNI
•	Teléfono
•	Fecha de alta
El id_socio deberá ser generado automáticamente por la base de datos.
________________________________________
4.2. Listar socios
Mostrar todos los socios registrados.
Ejemplo:
ID    NOMBRE              DNI        TELÉFONO
---------------------------------------------------------
1     Ana García          30111222   11-4455-1001
2     Martín Pérez        29876543   11-4455-1002
...
________________________________________
4.3. Buscar socio
Permitir buscar un socio mediante su DNI.
________________________________________
4.4. Modificar socio
Permitir modificar:
•	Nombre
•	Teléfono
El DNI y el id_socio no deberán modificarse.
________________________________________
4.5. Eliminar socio
Permitir eliminar un socio a partir de su id_socio.
Antes de eliminarlo deberá verificarse si existen préstamos asociados a dicho socio.
Si existen préstamos, no deberá eliminarse directamente el socio.
Mostrar un mensaje adecuado al usuario.
________________________________________
5. Gestión de EJEMPLARES
En esta parte no es necesario implementar un CRUD completo.
La aplicación deberá permitir:
5.1. Agregar ejemplar
Solicitar:
•	ISBN del libro
•	Número de ejemplar
El estado inicial deberá ser:
Disponible
Antes de insertar el ejemplar, verificar que el ISBN corresponda a un libro existente.
________________________________________
5.2. Listar ejemplares de un libro
Solicitar un ISBN y mostrar todos sus ejemplares.
Ejemplo:
Libro: Cien Años de Soledad

Ejemplar     Estado
-------------------------
1            Disponible
2            Disponible
3            Disponible
________________________________________
5.3. Modificar estado
Permitir cambiar el estado de un ejemplar entre:
Disponible
Prestado
________________________________________
6. Gestión de PRÉSTAMOS
Implementar las siguientes operaciones básicas:
6.1. Registrar préstamo
Solicitar:
•	ID del socio
•	ISBN
•	Número de ejemplar
•	Fecha del préstamo
•	Fecha prevista de devolución
Antes de registrar el préstamo verificar:
1.	Que el socio exista.
2.	Que el libro exista.
3.	Que el ejemplar exista.
4.	Que el ejemplar se encuentre Disponible.
Si todas las condiciones se cumplen:
•	Insertar el préstamo.
•	Cambiar el estado del ejemplar a Prestado.
________________________________________
6.2. Registrar devolución
Solicitar el id_prestamo.
El programa deberá:
1.	Buscar el préstamo.
2.	Verificar que todavía no tenga fecha de devolución real.
3.	Registrar la fecha de devolución actual.
4.	Cambiar el estado del ejemplar a Disponible.
________________________________________
6.3. Listar préstamos
Mostrar los préstamos registrados.
La consulta deberá relacionar las tablas correspondientes para mostrar información comprensible para el usuario.
Por ejemplo:
ID   SOCIO          LIBRO                    F. PRÉSTAMO
----------------------------------------------------------------
1    Ana García     Cien Años de Soledad     01/05/2026
2    Martín Pérez   Rayuela                  03/05/2026
________________________________________
7. Requisitos técnicos JDBC
El programa deberá utilizar obligatoriamente:
•	Connection
•	PreparedStatement
•	ResultSet
•	SQLException
Las consultas que reciban datos ingresados por el usuario deberán utilizar PreparedStatement.
No se permitirá construir consultas SQL concatenando directamente los datos ingresados por el usuario.
Por ejemplo, evitar:
String sql = "SELECT * FROM LIBRO WHERE ISBN = '" + isbn + "'";
Utilizar:
String sql = "SELECT * FROM LIBRO WHERE ISBN = ?";

PreparedStatement ps = conexion.prepareStatement(sql);
ps.setString(1, isbn);
________________________________________
8. Manejo de recursos
Los recursos utilizados por JDBC deberán cerrarse correctamente.
Se recomienda utilizar:
try-with-resources
Por ejemplo:
try (PreparedStatement ps = conexion.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {

    // Procesamiento del resultado

} catch (SQLException e) {
    System.out.println("Error: " + e.getMessage());
}
________________________________________
9. Organización mínima del proyecto
El proyecto deberá tener una estructura similar a:
BibliotecaJDBC
│
├── src
│   ├── Conexion.java
│   ├── Main.java
│   ├── Libro.java
│   ├── Socio.java
│   ├── Ejemplar.java
│   └── Prestamo.java
│
└── lib
    └── mariadb-java-client.jar
No es obligatorio utilizar un patrón de diseño.
El objetivo de esta actividad es trabajar específicamente los conceptos básicos de Java + JDBC + SQL.
________________________________________
10. Validaciones mínimas
La aplicación deberá controlar situaciones como:
•	ISBN inexistente.
•	DNI inexistente.
•	DNI duplicado.
•	Socio inexistente.
•	Ejemplar inexistente.
•	Ejemplar ya prestado.
•	Intentar devolver un préstamo ya devuelto.
•	Intentar eliminar un socio que posee préstamos.
•	Intentar ingresar datos obligatorios vacíos.
•	Errores producidos por la base de datos.
Los mensajes de error deberán ser comprensibles para el usuario.
________________________________________
Objetivo final
Al finalizar el trabajo, deberán ser capaces de desarrollar una aplicación Java que:
              ┌──────────────────┐
              │   Aplicación │
              │      Java    │
              └────────┬─────────┘
                     │
                    JDBC
                     │
                     ▼
              ┌────────────────────┐
              │     MariaDB    │
              │  Biblioteca_DB │
              └────────────────────┘
                       │
          ┌───────────────┼─────────────────┐
          ▼           ▼             ▼
       LIBRO         SOCIO      EJEMPLAR
                       │            │
                       └─────┬─────────┘
                           ▼
                        PRESTAMO
y comprender el flujo básico:
Java
  ↓
Connection
  ↓
PreparedStatement
  ↓
SQL
  ↓
MariaDB
  ↓
ResultSet
  ↓
Java
El propósito principal no es construir un sistema de biblioteca completo, sino utilizar la problemática
 de una biblioteca como contexto para aprender y practicar JDBC y las operaciones CRUD.
