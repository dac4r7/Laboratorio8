
package lab8.Servicio.interfaces;


import lab8.modelo.Socio;

/**
 *
 * @author Diego Adrian Cesarin
 */
public interface IMenuSocio {
    
   //Gestión de socios
        public abstract void agregarSocio();
        public abstract void listarSocios();
	public abstract Socio buscarSocio(String dni);
	public abstract void modificarSocio(Socio socioanterior);
	public abstract void eliminarSocio(int id); 
    
}

