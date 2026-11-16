
package lab8.control;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.InputMismatchException;
import java.util.Scanner;
import lab8.connection.ConexionMySql;
import lab8.Servicio.interfaces.IMenuOpcion;
import lab8.Servicio.interfaces.IMenuPrestamo;
import lab8.modelo.Libro;
import lab8.modelo.Prestamo;
import lab8.modelo.Socio;
import lab8.util.Verificacion;

/**
 *
 * @author Diego Adrian Cesarino
 */
public class MenuPrestamo implements IMenuPrestamo,IMenuOpcion{

     Scanner dato = new Scanner(System.in);
     Scanner num = new Scanner(System.in);
     ConexionMySql cbd = new ConexionMySql();
     private final String formato = "%-14s %-10s %-18s %-20s %-18s %-26s %-28s%n";
     private final String formato1 = "%-14s %-18s %-18s%n";
    
     private MenuLibro menulibro = null;  //necesarios para registrar un nuevo prestamo
     private MenuSocio menusocio = null;
     private MenuEjemplar menuejemplar = null ;

    public MenuPrestamo( MenuLibro menulibro , MenuSocio menusocio, MenuEjemplar menuejemplar ) {
        this.menulibro = menulibro;
        this.menusocio = menusocio;   
        this.menuejemplar = menuejemplar;
    }
                     
       @Override
        public void menuDeOpciones(){
           int opcion ;
       do{ System.out.println("======================================");
           System.out.println("          GESTIONAR PRESTAMO          ");
           System.out.println("======================================");
           System.out.println("                                      ");      
           System.out.println("1. Registrar Prestamo");
           System.out.println("2. Registrar Devolucion");
           System.out.println("3. Listar Prestamos");        
           System.out.println("0. Volver");
           opcion = Verificacion.verificarNumero(3, 0);
           opcionDelMenu(opcion);          
       }while(opcion!=0);    
    }
            
       @Override
       public void opcionDelMenu(int opcion) {
          switch (opcion){
          case 0-> {return;}   
           // se registra el prestamo de un libro
          case 1-> registrarPrestamo();
           // registra una devolucion 
          case 2->  registrarDevolucion();
           //mustrar un listado de prestamos   
          case 3->  listarPrestamos();
          default -> System.out.println("Ingrese una opcion del 0 al 3");          
      }      
    }   
    
    @Override
    public void registrarPrestamo() {
      //busqueda del socio(verificar existencia)
      System.out.print("Ingrese el DNI del Socio que desea Registrar para el prestamo:  ");
      String dni = dato.nextLine();
      Socio socioregistrado = this.menusocio.buscarSocio(dni.strip().trim());
     
      //adicionalmente consulta si el socio esta activo o fue desvinculado
      if( estaActivo( socioregistrado.getId()))
      {
        if(socioregistrado != null){                     
        //busqueda del isbn y seleccion del libro si existe
        System.out.print("Ingrese el Titulo del Libro: ");
        String titulo = dato.nextLine();     
       
        //muestra un listado de libros para seleccionar de acuerdo al titulo ingresado(retorna la seleccion)
        Libro libroparaprestar = this.menulibro.buscarySeleccionarLibro(titulo);    
        
        if(libroparaprestar != null){
         //consulta si el libro tiene algun ejemplar disponible y lo reserva
         int n = menuejemplar.seleccionarNumEjemplar(libroparaprestar.getISBN());
       
         if( n != -1){      //si n != -1 El ejemplar esta disponible y se confirmo
           //SE REGISTRA EL PRESTAMO EN LA DB
            Prestamo prestamo = new Prestamo( socioregistrado.getId() , libroparaprestar.getISBN() , n );
            try{ cbd.conectarBDD();
                 String query = "INSERT INTO PRESTAMO (`id_socio`, `ISBN`, `nro_ejemplar`,`fecha_prestamo`,"
                              + " `fecha_devolucion_prevista`, `fecha_devolucion_real`) VALUES (?,?,?,?,?,?)";
                 
                PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
                ps.setInt(1, socioregistrado.getId());
                ps.setString(2, libroparaprestar.getISBN());
                ps.setInt(3,n);
                Date fechaprestamo = Date.valueOf(prestamo.getFechaPrestamo());
                ps.setDate(4, fechaprestamo);
                Date fechadevolucion = Date.valueOf(prestamo.getFechaDevolucion());
                ps.setDate(5, fechadevolucion);
                Date fechadevoreal ;
                if( prestamo.getFechaDevolucionReal() == null ){
                fechadevoreal = null ;
                }else{
                 fechadevoreal = Date.valueOf(prestamo.getFechaDevolucionReal()); 
                }
                ps.setDate(6, fechadevoreal);
                int filainsertada = ps.executeUpdate();
                 if(filainsertada>0) 
                   System.out.println("EL PRESTAMO DEL LIBRO ("+libroparaprestar.getTitulo()+
                                     ") FUE REGISTRADO A NOMBRE DE "+socioregistrado.getNombre()+" con EXITO");
                }catch(SQLException ex){
                   ex.printStackTrace();
               }          
          }else{ System.out.println("El Prestamo no se pudo completar.");
               }
        }else{
          //  System.out.println("NO SE SELECCIONO UN LIBRO O NO SE ENCONTRO");
        }
      }else{
          System.out.println("No se encontro el socio con el DNI ingresado.");
          }
      }else{ System.out.println("EL SOCIO("+socioregistrado.getId()+") :" + socioregistrado.getNombre() +" no se encuentra activo" );
             System.out.println("Activelo nuevamente desde el menu de SOCIO para realizar un prestamo.");
            }
    }

    @Override
    public void registrarDevolucion( ){
      Prestamo prestamodevolucion = null;
      
      System.out.print("INGRESE A CONTINUACION EL Id_de_prestamo :  ");      
      int id_prestamo = verificarIngreso();
      //SE BUSCA EL PRESTAMO EN LA DB 
       prestamodevolucion = buscarPrestamo(id_prestamo);
      
      if(prestamodevolucion != null){  //Verificar que todavía no tenga fecha de devolución real.    
          if(prestamodevolucion.getFechaDevolucionReal() == null || !prestamodevolucion.getFechaDevolucionReal().isBefore(LocalDate.now())){          
           //confirma Cambio el estado a disponible
           if( menuejemplar.actualizarEstado(prestamodevolucion.getISBNLibro(), "Disponible", prestamodevolucion.getNumeroEjemplar())){
            try{          
                cbd.conectarBDD();              
                //SE REALIZA LA DEVOLUCION , se modifican fechas en tabla prestamo ,se modifica la tabla ejemplar como disponible
                String query = "UPDATE `PRESTAMO` SET fecha_devolucion_real = ? WHERE id_prestamo = ? ";                
                PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);  
                Date fechadevolucionreal = Date.valueOf(LocalDate.now());
                ps.setDate(1, fechadevolucionreal);
                ps.setInt(2, id_prestamo);
                int fm = ps.executeUpdate();

                if( fm > 0 ){
                    System.out.println("DEVOLUCION REALIZADA");             
                }else{
                 System.out.println("No se pudo actualizar la fecha de devolucion");
                }                           
             }catch(SQLException ex){
                  ex.printStackTrace();      
                 }
          }  //if de confirmacion actualizar estado
          }else{
              System.out.println(" ->EL LIBRO YA FUE DEVUELTO<- ");
          } 
      }else{
          System.out.println("NO SE ENCONTRO UN PRESTAMO CON EL ID INGRESADO.");
      }     
    }

    @Override
    public void listarPrestamos() {
      try{ cbd.conectarBDD();
         String query = "SELECT * FROM PRESTAMO";
         PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
         ResultSet rs = ps.executeQuery();
         if( rs != null ){
          //(`id_prestamo`, `id_socio`, `ISBN`, `nro_ejemplar`, `fecha_prestamo`, `fecha_devolucion_prevista`, `fecha_devolucion_real`)
           System.out.printf(formato,"Id_prestamo","Id_socio","ISBN","Nro_de_Ejemplar","Fecha_prestamo","Fecha_devolucion_prevista","Fecha_devolucion_real");
           System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
           while(rs.next()){  //             
            System.out.printf(formato,rs.getInt("id_prestamo"),rs.getInt("id_socio"),rs.getString("ISBN"),rs.getInt("nro_ejemplar"),rs.getDate("fecha_prestamo").toString(),
                              rs.getDate("fecha_devolucion_prevista").toString(),evaluarFecha(rs.getDate("fecha_devolucion_real")) );      
             }
         }else{
             System.out.println("No se encontraron Ejemplares");
         }          
       }catch(SQLException ex){
        ex.printStackTrace();      
      }     
    }
    
       private String evaluarFecha(Date fechadevolucionreal) {
            if(fechadevolucionreal == null){
                    return "Sin Devolucion";
                 }
        return fechadevolucionreal.toString();
    }
    
    private Prestamo buscarPrestamo( int id ){
     Prestamo p = null;
        //( int idSocio, String ISBN, int numeroEjemplar )
        //(`id_prestamo`, `id_socio`, `ISBN`, `nro_ejemplar`, `fecha_prestamo`, `fecha_devolucion_prevista`, `fecha_devolucion_real`)
       try{ cbd.conectarBDD();
            String query = "SELECT `id_Socio`,`ISBN` ,`nro_ejemplar`, `fecha_devolucion_real` FROM `PRESTAMO` WHERE id_prestamo = ? " ;
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);  
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

         if( rs != null  ){
            if(rs.next()){   
                int idsocio = rs.getInt("id_Socio") ;
                String ISBN = rs.getString("ISBN") ;
                int nro_ejemplar = rs.getInt("nro_ejemplar") ;
                Date fechadevolucionreal = rs.getDate("fecha_devolucion_real") ;
                p =  new Prestamo(  idsocio , ISBN, nro_ejemplar );
                if(fechadevolucionreal != null){
                p.setFechaDevolucionReal(fechadevolucionreal.toLocalDate());
                }             
          }  
         }else{
                System.out.println("No existe un EJEMPLAR registrado con el ISBN proporcionado o no existe el Nro_DE_EJEMPLAR.");
               }          
          }catch(SQLException ex){
              ex.printStackTrace();      
          }
     return p;
    }

    public int verificarIngreso() {       
        boolean verif = false;
        int n=0;
         while(!verif){
            try{ n = Integer.parseInt(num.nextLine());           
                 verif = true;
             }catch(InputMismatchException ime){
                System.out.println("Ingrese un valor Numerico");  
             }catch(Exception e){
                 System.out.println("Hubo un error durante el ingreso");
           }
         }
         return n;
       }

    private boolean estaActivo(int id) {        
        boolean estaactivo = false;
        try{
           cbd.conectarBDD();
            String query = "SELECT activo FROM SOCIO WHERE id_socio = ? ";
           // String query = "SELECT activo , id_socio , nombre FROM SOCIO WHERE id_socio = ? ";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if( rs != null ){          
             if(rs.next()){  
             // System.out.printf(formato1,"Estado","id_socio","Nombre");
             // System.out.println("-----------------------------------------------------------------------------------");
             estaactivo = rs.getBoolean("activo");
             // System.out.printf(formato1,estaactivo,rs.getString("id_socio"),rs.getString("nombre"));
             }
            }else{
                System.out.println("No se encontraron socios en la Base de Datos");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
       } 
       return estaactivo;
    }

}
