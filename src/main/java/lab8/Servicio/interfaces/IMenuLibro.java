
package lab8.Servicio.interfaces;


/**
 *
 * @author Diego Adrian Cesarin
 */
public interface IMenuLibro {
    
     //Gestión de libros
        public  void agregarLibro(String ISBN,String titulo, String autor, String editorial,int anio_publicacion);
        public  void listarLibros();
	public  void buscarLibro (String isbn);  //retorna el isbn del libro
	public  void modificarLibro(String ISBN,String titulo, String autor, String editorial,int anio_publicacion );
	public  void eliminarLibro(String isbn);
    
}
