
package lab8.control;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import lab8.connection.ConexionMySql;
import lab8.Servicio.interfaces.IMenuOpcion;
import lab8.Servicio.interfaces.IMenuSocio;
import lab8.Servicio.interfaces.IValidaciones;
import lab8.modelo.Socio;
import lab8.util.Verificacion;
/**
 *
 * @author Diego Adrian Cesarin
 */
public class MenuSocio implements IMenuSocio,IMenuOpcion,IValidaciones{
    Scanner dato = new Scanner(System.in, "UTF-8");
    ConexionMySql cbd = new ConexionMySql();
    private final String formato = "%-12s %-22s %-12s %-16s %-14s%n";
    private final String formato1 = "%-12s %-22s %-12s %-16s %-20s %-16s%n";
 
    public MenuSocio() {      
    }
         
       @Override
       public void menuDeOpciones(){
           int opcion ;
       do{  
           System.out.println("===========================================");
           System.out.println("            GESTIONAR SOCIOS               ");
           System.out.println("===========================================");   
           System.out.println("1. Agregar Socio");
           System.out.println("2. Listar Socios");
           System.out.println("3. Buscar Socios");
           System.out.println("4. Modificar Socio");          
           System.out.println("5. Eliminar Socio");          
           System.out.println("0. Volver");
           opcion = Verificacion.verificarNumero(5, 0);
           opcionDelMenu(opcion);          
       }while(opcion!=0);    
    }
    
      @Override 
      public void opcionDelMenu(int opcion) {
         switch (opcion){
          case 0-> {return;}   
           //agregar socio  
          case 1-> agregarSocio();
           // listar socios   
          case 2-> listarSocios();
           // buscar socio
          case 3-> {System.out.print("Ingrese el DNI del Socio que desea buscar:  ");
                    String dni = dato.nextLine();
                    buscarSocio(dni.strip().trim());}
           // modificar datos del socio
          case 4-> { System.out.print("Ingrese el DNI del Socio: ");
                     Socio socio = buscarSocio( dato.nextLine() );     
                     System.out.println("id:"+ socio.getId());
                     if(socio != null){                            //si encontro el socio
                         modificarSocio(socio);}}                  //se pasan los datos de socio sin modificar 
                 
          // eliminar de la lista un socio
          case 5-> {System.out.print("Ingrese el número de ID del socio > ");
                     eliminarSocio(verificarNum());}
          default -> System.out.println("Ingrese una opcion del 0 al 5");            
        }
      }

     @Override
    public void agregarSocio() {
        Scanner num = new Scanner(System.in);           
       // se crea el nuevo socio
       Socio sociocreado = crearNuevoSocio();   
       //Query
       String sql = "INSERT INTO SOCIO  (`nombre`, `DNI`, `telefono`, `fecha_alta`) VALUES (?, ?, ? , ?)";
       //  Preparar la query indicando retornar las llaves generadas
      try (PreparedStatement ps = ConexionMySql.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

       // Se Setean los parámetros de la inserción
       ps.setString(1, sociocreado.getNombre());
       ps.setString(2, sociocreado.getDni());
       ps.setString(3, sociocreado.getTelefono());
       ps.setDate(4, java.sql.Date.valueOf(sociocreado.getFechadealta()));

       //  Inserción
       int filasAfectadas = ps.executeUpdate();
       if (filasAfectadas > 0) {
           System.out.println("Nuevo Socio Ingresado con Exito!");                
           }
       
          } catch (SQLException sqle) {
               //sqle.printStackTrace();
               System.out.println("Hubo un error durante la consulta de alta de socio");       
         }       
       }   

     @Override
     public void listarSocios() {
         String estado = "SI";
      System.out.println("   LISTADO DE SOCIOS");
      System.out.println("================================================================================================");
      System.out.printf(formato1,"id_socio","Nombre","DNI","Teléfono","Fecha_de_Alta","Activo");
      System.out.println("------------------------------------------------------------------------------------------------");
       try{
           cbd.conectarBDD();    //mostramos todos los de estado activo
            String query = "SELECT * FROM SOCIO WHERE activo = TRUE ";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if( rs != null ){
             while(rs.next()){ 
              if(rs.getBoolean("activo")){
                  estado="SI";
              }else{
                  estado="NO";
              }
              System.out.printf(formato1,rs.getString("id_socio"),rs.getString("nombre"),rs.getString("DNI"),rs.getString("telefono"),rs.getString("fecha_alta"),estado);
              }
            }else{
                System.out.println("No se encontraron socios en la Base de Datos");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
       }   
     }

     @Override
     public Socio buscarSocio(String dni){
         
         LocalDate fechadealta = null;
           //conexion y generacion de consulta
         try{ cbd.conectarBDD();//
            String query = "SELECT * FROM SOCIO WHERE DNI = ? ";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
           if( rs != null  ){
            if(rs.next()){
              System.out.println("==========================================================================================");
              System.out.printf(formato,"id_socio","Nombre","DNI","telefono","Fecha_de_Alta");
              System.out.println("------------------------------------------------------------------------------------------");             
              //recepcion de datos
                 int id = rs.getInt("id_socio"); 
                 String nombre = rs.getString("nombre");
                 String telefono = rs.getString("telefono");
                 java.sql.Date sqlDate = rs.getDate("fecha_alta");
                 fechadealta = sqlDate.toLocalDate();              
                 System.out.printf(formato,rs.getString("id_socio"),rs.getString("nombre"),rs.getString("DNI"),rs.getString("telefono"),rs.getString("fecha_alta"));        
                 Socio s = new Socio( nombre ,  dni, telefono, fechadealta);  //constructor por defecto
                 s.setId(id); //normalmente la id es generada cuando el socio se ingresa en la base(existe), por lo que al crear el objeto no tiene asignada
                return s;             
             }else{
                 System.out.println("No se encontraron registros !");
             }
            }else{
                System.out.println("No se encontro un Socio con ese DNI");
               }          
           }catch(SQLException ex){
              ex.printStackTrace();      
             } 
           return null;
        }

     @Override
     public void modificarSocio(Socio socio) {
       String nombre,telefono;   
       System.out.println("------------------------------------------------------------------------------");
       System.out.println("   MODIFICAR DATOS SOCIO : dni "+socio.getDni() + "    id "+socio.getId());  
       System.out.println("------------------------------------------------------------------------------");
      
       System.out.print("El Nombre actual es : "+ socio.getNombre() + "  Desea Modificarlo?(s/n)-> ");
         if(validarRespuesta()){
         System.out.print("Ingrese el nuevo Nombre-> ");
           nombre = dato.nextLine();
         }else{
           nombre = socio.getNombre();
         }
       System.out.print("El Teléfono actual es : "+ socio.getTelefono() + "  Desea Modificarlo?(s/n)-> ");
         if(validarRespuesta()){
         System.out.print("Ingrese el nuevo Teléfono-> ");
           telefono = dato.nextLine();
         }else{
           telefono = socio.getTelefono();
         }        
          String query = "UPDATE `SOCIO` SET nombre = ?, telefono = ? WHERE id_socio = ? ";
            try{
                cbd.conectarBDD();
                PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
                ps.setString(1, nombre);
                ps.setString(2, telefono);
                ps.setInt(3, socio.getId());         
                int filamodif = ps.executeUpdate();
                if(filamodif>0) 
                  System.out.println("Nuevos datos del Socio Modificados Correctamente! ");                   
            }catch(SQLException ex){
                 ex.printStackTrace();  
                 System.out.println("Hubo un error al enviar la consulta a la base de datos");
             }         
        }

    @Override
    public void eliminarSocio(int id) {
      if(!noTieneDevoluciones(id)){
          System.out.println("DESEA DESACTIVAR EL SOCIO CON id : " + id +" DEL GESTOR DE BIBLIOTECA ?" );
          System.out.println("LA BIBLIOTECA YA NO PODRÁ REALIZAR PRESTAMOS A EL SOCIO id: "+id+"? (si/s no/n)"  );
       if(validarRespuesta()){
        try{
           cbd.conectarBDD();
            //Se desactiva el autocommit
            ConexionMySql.conexion.setAutoCommit(false);
         try{ 
            String query = "UPDATE SOCIO SET activo = FALSE WHERE id_socio = ? ";
            
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ps.setInt(1, id);
             int filasActualizada = ps.executeUpdate();   
             
             //confirmacion de la operacion
             ConexionMySql.conexion.commit();
             
               if(filasActualizada>0){
                System.out.println("El SOCIO con id: " + id + " fue DESACTIVADO de la BD exitosamente");
               }
           }catch(SQLException ex){
              ConexionMySql.conexion.rollback();  //se retornan los cambios por si hubo algun problema durante el borrado
              ex.printStackTrace(); 
            }finally{
                    ConexionMySql.conexion.setAutoCommit(true);
                   }
          }catch(SQLException ex){
           ex.printStackTrace();  
          // System.out.println("No se enconro un socio con el DNI proporcionado.");
        }
       }// if validarRespuesta
       }else{System.out.println(">El Socio no puede ser DESACTIVADO , por que: TIENE DEVOLUCIONES PENDIENTES");
             System.out.println(">Realice las Devoluciones e intenete nuevamente");}      
     }
    
   //toma un dato desde consola ,verifica que sea un numero   
  private int verificarNum() {
     int nu=0 ;
     boolean e=true;
     while(e){
         String n = dato.nextLine();
        try{
            nu = Integer.parseInt(n);
            e= false;
         }catch(InputMismatchException ime){
              System.out.println("Debes ingresar un numero!");            
         }catch(Exception ex){
              ex.printStackTrace();
              }
       }return nu;
      }  
 
   //Retorna un obj Socio con sus Datos
   private Socio crearNuevoSocio() {
     Scanner num = new Scanner(System.in);
      String e="",c="s";
     
      String nombre="",dni="",telefono="";      
  
    while(!c.equals(e.strip().toLowerCase())){ 
         System.out.println("-------------------------------------------------------------------");
         System.out.println("   DATOS DEL NUEVO SOCIO");    
         System.out.println("-------------------------------------------------------------------");
         System.out.print("Ingrese el Nombre -> ");
         nombre = dato.nextLine();
         System.out.print("Ingrese el DNI -> ");
         dni = dato.nextLine();
         System.out.print("Ingrese el Telefono -> ");
         telefono = dato.nextLine();     
         System.out.println("Confirmar datos ?(s/n)( Nombre: "+nombre+"  dni: "+dni+"  telefono: "+telefono ) ;
         e= dato.nextLine();
      }
      //(String nombre, String dni, String telefono, LocalDate fechadealta, int id)
       return new Socio(nombre, dni, telefono, LocalDate.now());   
    }

    @Override
    public boolean validarRespuesta() {   //Evalua si una respuesta es S o N
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
    //realiza una verificacion con la tabla ejemplar para ver si tiene algun prestamo asociado
    //con su id que tenga el estado del libro "prestado" 
    private boolean noTieneDevoluciones(int id) {
       boolean tiene=true;
       try{ cbd.conectarBDD();
            String query = "SELECT COUNT(*) "
                     + "FROM PRESTAMO "
                     + "INNER JOIN EJEMPLAR "
                     + "ON PRESTAMO.ISBN = EJEMPLAR.ISBN "
                     + "AND PRESTAMO.nro_ejemplar = EJEMPLAR.nro_ejemplar "
                     + "WHERE PRESTAMO.id_socio = ? "
                     + "AND EJEMPLAR.estado = ?";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);  
            ps.setInt(1, id);
            ps.setString(2, "Prestado");
            
            ResultSet rs = ps.executeQuery();
           
          if( rs != null ){               
            if(rs.next()){             
               tiene = rs.getInt(1) > 0;
              }                                                                      
           }        
          }catch(SQLException ex){
              ex.printStackTrace();  
            System.out.println("Hubo un error en la consulta esta prestado");  
          }
       return tiene;   
    }
   
}
