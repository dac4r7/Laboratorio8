
package lab8.modelo;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class Libro {
    
  private String ISBN = null;
  private String titulo = null;
  private String autor = null;
  private String editorial = null;
  private int anio_publicacion ;
  private Integer numerodeejemplar=null; 
    public Libro() {
    }

    public Libro(String ISBN, String titulo, String autor, String editorial, int anio) {
        this.ISBN = ISBN;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anio_publicacion = anio;
        
    }

    public void setNumerodeejemplar(Integer numerodeejemplar) {
        this.numerodeejemplar = numerodeejemplar;
    }
    
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAnio() {
        return anio_publicacion;
    }

    public void setAnio(int anio) {
        this.anio_publicacion = anio;
    }

    @Override
    public String toString() {
        return "Libro{" + "ISBN=" + ISBN + 
                ", titulo=" + titulo +
                ", autor=" + autor +
                ", editorial=" + editorial +
                ", anio=" + anio_publicacion + '}';
    } 
}
