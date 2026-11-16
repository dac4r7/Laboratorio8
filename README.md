

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
| Layer           | Technology                                                 |
| --------------- | ---------------------------------------------------------- |
| Language        | Java 26                                                    |
| Database        | mariadb-java-client   2.7.2                                |
| Build           | Maven (Maven Wrapper included)                             |



* @author Diego Adrian Cesarin.
 * <p> Copyright (C) 2026 para Diego Adrian Cesarin.
 * Contacto: diegoacart@gmail.com  * - con licencia GNU GPL3.
 * <p> Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo los términos de la
 * Licencia Pública General de GNU según es publicada por la Free Software Foundation, 
 * bien con la versión 3 de dicha Licencia o bien (según su elección) con cualquier versión posterior. 
 * Este programa se distribuye con la esperanza de que sea útil, pero SIN NINGUNA GARANTÍA, 
 * incluso sin la garantía MERCANTIL implícita o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO
