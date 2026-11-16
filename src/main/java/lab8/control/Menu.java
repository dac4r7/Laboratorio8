
package lab8.control;


import java.util.Scanner;

import lab8.Servicio.interfaces.IMenuOpcion;
import lab8.util.Verificacion;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class Menu implements IMenuOpcion {
    Scanner num = new Scanner(System.in);

    MenuEjemplar menuejemplar=null;
    MenuLibro menulibro=null;
    MenuSocio menusocio=null;
    MenuPrestamo menuprestamo=null;
    
    public Menu() {
      menuejemplar = new MenuEjemplar();
      menulibro = new MenuLibro();
      menusocio = new MenuSocio();
      menuprestamo = new MenuPrestamo(menulibro , menusocio,menuejemplar);
     menuDeOpciones();
    }
    
    //------menu biblioteca-----------------------------------------------------------------
    
    @Override
    public void menuDeOpciones() {
         int opcion ;
       do{  
           System.out.println("======================================");
           System.out.println("         SISTEMA DE BIBLIOTECA       ");
           System.out.println("======================================");
           System.out.println("                                      ");      
           System.out.println("1. Gestionar Libros");
           System.out.println("2. Gestionar Socios");
           System.out.println("3. Gestionar Ejemplares");
           System.out.println("4. Gestionar Prestamos");          
           System.out.println("0. Salir");
           opcion = Verificacion.verificarNumero(4, 0);
           opcionDelMenu(opcion);          
       }while(opcion!=0);    

    }
       
    @Override
   public void opcionDelMenu(int opcion){
       
      switch (opcion){
          case 0-> System.exit(0);
          // gestión de libros
          case 1-> menulibro.menuDeOpciones();
           // gestion de socios   
          case 2-> menusocio.menuDeOpciones();
           // gestion de ejemplares    
          case 3->  menuejemplar.menuDeOpciones();
           // gestión de préstamos 
          case 4-> menuprestamo.menuDeOpciones();
          default -> System.out.println("Ingrese una opcion del 0 al 4");
             
      }
   }
}
