
package lab8.modelo;

import java.time.LocalDate;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class Socio {
    
   private int id;
   private String nombre = null;
   private String dni = null;
   private String telefono = null;
   private LocalDate fechadealta = null;

    public Socio() {
    }

    public Socio(String nombre, String dni, String telefono, LocalDate fechadealta) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.fechadealta = fechadealta;
    }
   /*   public Socio(int id_socio, String nombre, String dni, String telefono, LocalDate fechadealta) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.fechadealta = fechadealta;
    }
   */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechadealta() {
        return fechadealta;
    }

    public void setFechadealta(LocalDate fechadealta) {
        this.fechadealta = fechadealta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Socio{" + "nombre=" +
                nombre + ", dni=" + dni + 
                ", telefono=" + telefono + 
                ", fechadealta=" + fechadealta + 
                ", id=" + id + '}';
    }
    
}
