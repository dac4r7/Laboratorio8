
package lab8.modelo;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class Ejemplar {
    
   private String ISBN = null;
   private int nro_ejemplar;
   private EstadoEjemplar estado = EstadoEjemplar.DISPONIBLE;

    public Ejemplar() {
       
    }

    public Ejemplar(String ISBN , int nro_ejemplar , EstadoEjemplar estado) {
        this.ISBN = ISBN;
        this.nro_ejemplar= nro_ejemplar;
        this.estado = estado;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    @Override
    public String toString() {
        return "Ejemplar{" + "ISBN=" + ISBN + ", nro_ejemplar=" + nro_ejemplar + ", estado=" + estado + '}';
    }

  
}
