
package lab8.control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import lab8.connection.ConexionMySql;
import lab8.modelo.Ejemplar;
import lab8.Servicio.interfaces.IMenuEjemplar;
import lab8.Servicio.interfaces.IMenuOpcion;
import lab8.Servicio.interfaces.IValidaciones;
import lab8.modelo.EstadoEjemplar;
import lab8.modelo.Libro;
import lab8.util.Verificacion;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class MenuEjemplar implements IMenuEjemplar,IMenuOpcion,IValidaciones {
       Scanner dato = new Scanner(System.in, "UTF-8");
       Scanner num = new Scanner(System.in);
       ConexionMySql cbd = new ConexionMySql();
       private final String formato = "%-16s %-18s %-18s%n";
       private final String formato1 = "%-18s %-18s%n";
       
    public MenuEjemplar() {
    }
             
       @Override
    public void menuDeOpciones(){
           int opcion ;
       do{  
           System.out.println("============================================");
           System.out.println("            GESTIONAR EJEMPLAR              ");
           System.out.println("============================================");
           System.out.println("                                      ");      
           System.out.println("1. Agregar Ejemplar");
           System.out.println("2. Listar Ejemplar");
           System.out.println("3. Modificar Estado");        
           System.out.println("0. Volver");
           opcion = Verificacion.verificarNumero(3, 0);
           opcionDelMenu(opcion);          
       }while(opcion!=0);    
    }
    
       @Override
     public void opcionDelMenu(int opcion) {
          switch (opcion){
                //SALIR
                case 0-> {return;}
       
                //AGREGAR EJEMPLARES
                case 1->  agregarEjemplar(); 
                          
                // LISTAR EJEMPLARES
                case 2->  listarEjemplar();

                // MODIFICAR ESTADO  
                case 3->   modificarEstado();

                default -> System.out.println("Ingrese una opcion del 0 al 3");            
        }   
      }    
       
       @Override
    public void agregarEjemplar() {  
       //Busqueda del libro en la base de datos (si existe)
        System.out.print("Ingrese el ISBN del LIBRO : ");
         Libro l = buscarLibro( dato.nextLine().trim().strip());
    
      //Se crea el nuevo ejemplar
      if(l != null)
       { try{ cbd.conectarBDD();
        //La query inserta una nueva fila con el numero de ejemplar incrementado en 1(si fuera null lo transforma a 0) para el isbn del libro
            String  query = "INSERT INTO `EJEMPLAR` (`ISBN`, `nro_ejemplar`, `estado`)"
                  + " VALUES ( ?, (SELECT COALESCE(MAX(`e`.`nro_ejemplar`), 0) + 1 FROM `EJEMPLAR` AS `e` WHERE `e`.`ISBN` = ?),"
                  +"'Disponible') ";
             PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
             ps.setString(1, l.getISBN());
             ps.setString(2, l.getISBN());            
             int filainsertada = ps.executeUpdate();
             if(filainsertada>0) 
             System.out.println("Ejemplar Registrado con Exito!");          
          }catch(SQLException ex){
             System.out.println("Hubo un Error al insertar el registro del Ejemplar."); 
           // ex.printStackTrace();
           } 
         }else{
           System.out.println("No esta registrado el ISBN del Libro, ingrese el LIBRO desde el menu_de_libros.");
          }
     }

    @Override
    public void listarEjemplar() {
        System.out.println("   LISTA DE EJEMPLARES ");
        try{ cbd.conectarBDD();
            String query = "SELECT * FROM EJEMPLAR";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if( rs != null ){
                System.out.println("--------------------------------------------------");
                System.out.printf(formato,"ISBN","Nro_de_Ejemplar","Estado");
                System.out.println("--------------------------------------------------");
                while(rs.next()){  //  
                    System.out.printf(formato,rs.getString("ISBN"),rs.getInt("nro_ejemplar"),rs.getString("estado") );         
                }
            }else{
                System.out.println("No se encontraron Ejemplares");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
       }     
    }
    //Modifica el estado de un ejemplar para lo cual tiene que estar registrado
    @Override
   public void modificarEstado() {
     String query= "" , estadoactual="";
    //DATOS DEL LIBRO PARA MODIFICAR SU ESTADO
     System.out.print("INGRESE EL ISBN DEL LIBRO: ");
     String isbnconsulta = dato.nextLine();
     Libro l = buscarLibro( isbnconsulta.trim().strip());  
     if(l != null){
      //SELECCION DEL NUMERO DE EJEMPLAR dentro de los disponibles(dentro del metodo se modifica el estado)
        seleccionarNumEjemplar(isbnconsulta);
       //verifico que existe y obtengo el ejemplar buscado            
      }else{
             System.out.println("EL LIBRO NO SE ENCONTRO");  
           }
    }
    
    //actualiza el estado de un ejemplar
    public boolean actualizarEstado(String isbn,String estadonuevo, int num_ejemplar) {
        System.out.println("SE MODIFICARA el ejemplar ("+num_ejemplar+") del LIBRO CON ISBN("+isbn+")"+" al estado "+estadonuevo+" (1.si 2.no)" );
        int n = verificarIngreso(2,1);
        if(n==2){
            return false;
        }else{
            String query = "UPDATE `EJEMPLAR` SET estado = ? WHERE ISBN = ? AND `nro_ejemplar` = ? ";
          try{ //cbd.conectarBDD();
               PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
               ps.setString(1, estadonuevo);
               ps.setString(2, isbn);
               ps.setInt(3, num_ejemplar);
               int fm = ps.executeUpdate();
               if(fm>0) 
                   System.out.println("Estado del Ejemplar Modificado Correctamente! "); 
            }catch(SQLException ex){
               //ex.printStackTrace();
                System.out.println("ERROR AL ACTUALIZAR ESTADO");
             }
          } 
        return true;
    }
    
    //busca un libro registrado en la bd, con el isbn  retorna el resultado o null si no lo encuentra
     public Libro buscarLibro(String isbn) {
       Libro libro = null ;
        try{ cbd.conectarBDD();
            String query = "SELECT `ISBN`, `titulo`, `autor`, `editorial`, `anio_publicacion` "
                          + "FROM `LIBRO` WHERE ISBN = ? " ;
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);  
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            
         if( rs != null  ){        
             String ISBN,titulo, autor,editorial; 
             int anio_publicacion;                      
           
            if(rs.next()){                            
              ISBN = rs.getString("ISBN") ;
              titulo = rs.getString("titulo") ;
              autor = rs.getString("autor") ;
              editorial = rs.getString("editorial");
              anio_publicacion = rs.getInt("anio_publicacion");                    
              libro =  new Libro(isbn, titulo, autor, editorial, anio_publicacion);          
             }
           }else{
             System.out.println("No existe un EJEMPLAR registrado con el ISBN proporcionado o no existe el Nro_DE_EJEMPLAR.");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
       }
           return libro;       
    }
  
   //busca un ejemplar registrado en la bd, con el isbn y nro_de_ejempar pasados y retorna el Ejemplar o null si no lo encuentra
    public Ejemplar buscarEjemplar(String isbn, int numero_ejemplar) {
       Ejemplar ejemplar = null ;
         try{ cbd.conectarBDD();
            String query = "SELECT `ISBN`, `nro_ejemplar`, `estado` "
                          + "FROM `EJEMPLAR` WHERE ISBN = ? AND `nro_ejemplar` = ? " ;
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);  
            ps.setString(1, isbn);
            ps.setInt(2,numero_ejemplar);
            ResultSet rs = ps.executeQuery();
            
            if( rs != null  ){
             String ISBN = rs.getString("ISBN") ;
             int nro_ejemplar = rs.getInt("nro_ejemplar");                    
              ejemplar =  new Ejemplar(ISBN,nro_ejemplar,actualizarEstado(rs.getString("estado") ) );
            }else{
             System.out.println("No existe un EJEMPLAR registrado con el ISBN proporcionado o no existe el Nro_DE_EJEMPLAR.");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
       }
           return ejemplar;  
    }
    
   //retorna un int ingresado por el usuario, entre el max y el min   
    public int verificarIngreso(int max, int min) {   
     if(max ==1){
         return max;
     }   
     boolean verif = false;
     int n=0;
      while(!verif){
         try{ n = Integer.parseInt(num.nextLine());           
              if(n > max || n<min){
              throw new InputMismatchException();} 
              verif = true;
          }catch(InputMismatchException ime){
             System.out.println("Ingrese una opcion valida("+min+"/"+max+")");  
          }catch(Exception e){
              System.out.println("Solo se puede ingresar un numero.");
        }}
      return n;
    }
    
   //MUESTRA UN LISTADO DE EJEMPLARES DISPONIBLES Y RETORNA EL NUMERO DEL EJEMPLAR SELECCIONADO
    public int seleccionarNumEjemplar(String isbnconsulta) {
       int numero = -1 , max = 0;
       String estadoejemplar = "";
       
        try{ cbd.conectarBDD();  //conecta y recibe estadi y numero de ejemplar
            String query = "SELECT `nro_ejemplar`, `estado` FROM `EJEMPLAR` WHERE ISBN = ? " ;
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);  
            ps.setString(1, isbnconsulta);         
            ResultSet rs = ps.executeQuery();
            
          if( rs != null ){
             System.out.println("==============================================");
             System.out.printf(formato1,"Nro_de_ejemplar","Estado");
             System.out.println("----------------------------------------------"); 
          if( rs.next() ){                
           do{ 
               max = rs.getInt("nro_ejemplar");               //retorna el numero del ultimo ejemplar
               System.out.printf(formato1,max,rs.getString("estado"));
              }while( rs.next() );
              
              System.out.print("Seleccione el numero de ejemplar :");
              numero = verificarIngreso(max, 1);                    //selecciona uno de entre todos los disponibles/
              
              //consultamos nuevamente por el estado con el nuemero de ejemplar seleccionado
            try{   String queryestado = "SELECT `estado` FROM `EJEMPLAR` WHERE ISBN = ? AND nro_ejemplar = ? " ;
                   PreparedStatement psestado = ConexionMySql.conexion.prepareStatement(queryestado);  
                   psestado.setString(1, isbnconsulta); 
                   psestado.setInt(2, numero); 
                   ResultSet rsestado = ps.executeQuery(); 
                  
                   if( rsestado != null ){
                     if(rsestado.next()){
                       estadoejemplar = rsestado.getString("estado");  // el estado del ejemplar consultado
                   }}
             }catch(SQLException ex){
                ex.printStackTrace(); 
               }           
              System.out.println("EL ESTADO ES: "+estadoejemplar+" DESEA CAMBIAR EL ESTADO ?(s/n)") ;           
             if(validarRespuesta()){                       //si es un si , cambio los estados del ejemplar
                if(estadoejemplar.equals("Disponible")){ 
                 actualizarEstado(isbnconsulta ,"Prestado", numero); //actualiza el estado del ejemplar 
                }else if(estadoejemplar.equals("Prestado")){
                        actualizarEstado(isbnconsulta ,"Disponible", numero); //actualiza el estado del ejemplar  
                }
              }else{
                    numero = -1;         //no se confirmo el prestamo y no se reservo el ejemplar
                   }         
            }else{
                 System.out.println("No existe un EJEMPLAR con el ISBN consultado");
                }
           }          
        }catch(SQLException ex){
           ex.printStackTrace();      
          }           
        return numero;
    }
      
       @Override
     public boolean validarRespuesta() {                    //Evalua si una respuesta es S o N
        boolean verificar = false;
        String d;
        while (!verificar) {
          try {  d = dato.nextLine();
                if(d.trim().strip().equalsIgnoreCase("s")) {                               
                    return true;
                } else if (d.trim().strip().equalsIgnoreCase("n")) {                 
                    verificar = true;
                } else {
                    throw new InputMismatchException("Solo puede ingresar s(para SI) o n(para NO)");
                }
              } catch(Exception e) {
                 System.out.println( e.getMessage() );
             }
        }
        return false ;
    }

    private EstadoEjemplar actualizarEstado(String estado) {
      EstadoEjemplar e = EstadoEjemplar.RESERVADO;
      if(estado.equals("Disponible")){
          e = EstadoEjemplar.DISPONIBLE;
      }
      return e;
    }
}
