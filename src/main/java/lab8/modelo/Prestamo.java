


package lab8.modelo;
import java.time.LocalDate;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class Prestamo {
    //esta clase relaciona un socio con un libro
    private int idPrestamo;   
    private int idSocio;
    private int numeroEjemplar;
    private String ISBNLibro = null;
    private LocalDate fechaPrestamo = null;
    private LocalDate fechaDevolucion = null;
    private LocalDate fechaDevolucionReal = null;
    
    public Prestamo() {
    }
    public Prestamo( int idSocio, String ISBNLibro, int numeroEjemplar) {   
        this.idSocio = idSocio;
        this.ISBNLibro = ISBNLibro;
        this.numeroEjemplar = numeroEjemplar; 
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = LocalDate.now().plusDays(7);//prestamo por 7 dias a partir del dia del prestamo 
    }
     
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public int getNumeroEjemplar() {
        return numeroEjemplar;
    }

    public void setNumeroEjemplar(int numeroEjemplar) {
        this.numeroEjemplar = numeroEjemplar;
    }

    public String getISBNLibro() {
        return ISBNLibro;
    }

    public void setISBNLibro(String ISBNLibro) {
        this.ISBNLibro = ISBNLibro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }
    
    
}
