---
## Laboratorio8 
ENUNCIADO

## Objetivo
Desarrollar una aplicación de consola en Java que permita administrar información de una biblioteca utilizando Hibernate.
El objetivo principal es que puedan conectarse desde Java a una base de datos MariaDB/MySQL, ejecutar sentencias SQL mediante Hibernate y realizar
 las operaciones básicas de un CRUD:
 
- **Create** → Insertar registros.
- **Read**→ Consultar registros.
- **Update**→ Modificar registros.
- **Delete**→ Eliminar registros.

___
## Base de datos
 Se utilizará la base de datos Biblioteca_DB, adjunta en /resources/Biblioteca_DB.sql .
 Cuenta con toda la estructura y datos.
___
## Consigna
Desarrollar una aplicación Java de consola denominada:
Sistema de Gestión de Biblioteca
La aplicación deberá conectarse a la base de datos Biblioteca_hi mediante JDBC y permitir al usuario realizar operaciones CRUD sobre las tablas indicadas.

### 1. Conexión a la base de datos
 Implementar la conexión utilizando Hibernate.
 La aplicación deberá:
    1.	Cargar el driver y la configuración correspondiente.
    2.	Establecer una conexión con MariaDB/MySQL.
    3.	Utilizar un objeto Connection.
    4.	Informar al usuario si la conexión fue exitosa.
    5.	Manejar correctamente las posibles excepciones.
    La información de conexión deberá estar centralizada en el programa, evitando repetir la URL, usuario y contraseña en distintos lugares.
____
### 2. Menú principal
 La aplicación deberá presentar un menú similar al siguiente:
____
                                                 ====================================
                                                         SISTEMA DE BIBLIOTECA
                                                 ====================================                                     
                                                  1. Gestionar libros
                                                  2. Gestionar socios
                                                  3. Gestionar ejemplares
                                                  4. Gestionar préstamos
                                                  0. Salir
____
### 3. CRUD de LIBROS

- Implementar las siguientes operaciones:
- Agregar libro( datos ).
  - ISBN.
  - Título.
  - Autor.
  - Editorial.
  - Año de publicación.
____
Modificar libro
- Permitir modificar
  -Título.
  - Título.
  - Autor.
  - Editorial.
  - Año de publicación.
_____
El ISBN no deberá modificarse.
Antes de realizar la actualización, verificar que el libro exista.
_____
Solicitar el ISBN del libro que se desea eliminar.
Antes de eliminarlo, informar qué será eliminado y solicitar confirmación.
Tener en cuenta que la base de datos posee una relación entre LIBRO y EJEMPLAR.
____
 ### 4. CRUD de SOCIOS
  Implementar las operaciones(Solicitar):
  
  #### 4.1 Agregar socio
  -	Nombre
  -	DNI
  -	Teléfono
  - Fecha de alta
  - Año de publicación
 > El id_socio deberá ser generado automáticamente por la base de datos. 
_____
#### 4.2. Listar socios
Mostrar todos los socios registrados.
Ejemplo:

                                       ID    NOMBRE              DNI        TELÉFONO
                                   ---------------------------------------------------------
                                       1     Ana García          30111222   11-4455-1001
                                       2     Martín Pérez        29876543   11-4455-1002
                                
______
#### 4.3. Buscar socio

  Permitir buscar un socio mediante su DNI.
_____
#### 4.4. Modificar socio

   Permitir modificar:
  -	Nombre
  - Teléfono
  El DNI y el id_socio no deberán modificarse.
_____
#### 4.5. Eliminar socio

 Permitir eliminar un socio a partir de su id_socio.
 
 Antes de eliminarlo deberá verificarse si existen préstamos asociados a dicho socio.
 
 Si existen préstamos, no deberá eliminarse directamente el socio.
_____
### 5. Gestión de EJEMPLARES

En esta parte no es necesario implementar un CRUD completo.

La aplicación deberá permitir:
Agregar ejemplar(Solicitar)

-	ISBN del libro
-	Número de ejemplar
  
El estado inicial deberá ser: Disponible

 > Antes de insertar el ejemplar, verificar que el ISBN corresponda a un libro existente.
____
### 5.1 Listar ejemplares de un libro

Solicitar un ISBN y mostrar todos sus ejemplares.

                                                    Ejemplar     Estado
                                                 -------------------------
                                                  1            Disponible
                                                  2            Disponible
                                                  3            Disponible
_____
### 5.2. Modificar estado
Permitir cambiar el estado de un ejemplar entre:
- Disponible
- Prestado
______
### 6. Gestión de PRÉSTAMOS

Implementar las siguientes operaciones básicas:

 ## 6.1. Registrar préstamo(datos)
  
-	ID del socio
-	ISBN
-	Número de ejemplar
-	Fecha del préstamo
-	Fecha prevista de devolución

Antes de registrar el préstamo verificar:
1.	Que el socio exista.
2.	Que el libro exista.
3.	Que el ejemplar exista.
4.	Que el ejemplar se encuentre Disponible.
   
Si todas las condiciones se cumplen:
•	Insertar el préstamo.
•	Cambiar el estado del ejemplar a Prestado.
_____
   ## 6.2. Registrar devolución
   
   Solicitar el id_prestamo.
   
El programa debe:

1.	Buscar el préstamo.
2.	Verificar que todavía no tenga fecha de devolución real.
3.	Registrar la fecha de devolución actual.
4.	Cambiar el estado del ejemplar a Disponible.
_____
 ## 6.3. Listar préstamos

Mostrar los préstamos registrados.
La consulta relaciona las tablas correspondientes y muestra la información .

                                          ID   SOCIO          LIBRO                    F. PRÉSTAMO
                                       ----------------------------------------------------------------
                                           1    Ana García     Cien Años de Soledad     01/05/2026
                                           2    Martín Pérez   Rayuela                  03/05/2026
_____
 ## 7. Requisitos técnicos JDBC
 El programa deberá utilizar obligatoriamente:
 
•	Connection
•	PreparedStatement
•	ResultSet
•	SQLException

Las consultas que reciban datos ingresados por el usuario deberán utilizar PreparedStatement.
No se permitirá construir consultas SQL concatenando directamente los datos ingresados por el usuario.
_____
### Estructura del Proyecto
_____
```
src/
├── main/java/lab8/
     │          └── Servicio/
     │          │    └── interfaces/
     │          │    ├── IMenuBiblioteca.java
     │          │    ├── IMenuEjemplar.java
     │          │    ├── IMenuLibro.java
     │          │    ├── IMenuOpcion.java
     │          │    ├── IMenuPrestamo.java
     │          │    ├── IMenuSocio.java
     │          │    ├── IMenuBiblioteca.java
     │          └── connection/                        
     │          │    └── ConexionMySql.java   # Configuración para conectar con el controlador de bd 
     │          └── Test/                        
     │          │    └── App.java
     │          └── control/                  # Clases de control
     │          │    ├── Menu.java
     │          │    ├── MenuEjemplar.java
     │          │    ├── MenuLibro.java
     │          │    ├── MenuPrestamo.java
     │          │    └── MenuSocio.java
     │          └── modelo/                   # Entidades 
     │          │    ├── Ejemplar.java            
     │          │    ├── EstadoEjemplar.java                     
     │          │    ├── Libro.java                   
     │          │    ├── Prestamo.java              
     │          │    └── Socio.java               
     │          └── util/ 
     │                 └── Verificación.java          
     └── resources/
           └── Biblioteca_DB.sql

```

* @author Diego Adrian Cesarin.
 * <p> Copyright (C) 2026 para Diego Adrian Cesarin.
 * Contacto: diegoacart@gmail.com  * - con licencia GNU GPL3.
 * <p> Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo los términos de la
 * Licencia Pública General de GNU según es publicada por la Free Software Foundation, 
 * bien con la versión 3 de dicha Licencia o bien (según su elección) con cualquier versión posterior. 
 * Este programa se distribuye con la esperanza de que sea útil, pero SIN NINGUNA GARANTÍA, 
 * incluso sin la garantía MERCANTIL implícita o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO
 * PARTICULAR. Véase la Licencia Pública General de GNU para más detalles.
 * Debería haber recibido una copia de la Licencia Pública General junto con este programa. 
 * Si no ha sido así ingrese a <a href = "http://www.gnu.org/licenses/"> GNU org </a>


