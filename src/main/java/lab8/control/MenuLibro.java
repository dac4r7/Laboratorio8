
package lab8.control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import lab8.connection.ConexionMySql;
import lab8.Servicio.interfaces.IMenuLibro;
import lab8.Servicio.interfaces.IMenuOpcion;
import lab8.Servicio.interfaces.IValidaciones;
import lab8.modelo.Libro;
import lab8.util.Verificacion;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class MenuLibro implements IMenuLibro,IMenuOpcion,IValidaciones{
    Scanner dato = new Scanner(System.in, "UTF-8");
    Scanner num = new Scanner(System.in, "UTF-8");
    ConexionMySql cbd = new ConexionMySql();
    private final String formato = "%-32s %-16s %-25s %-15s %-14s%n";
    private final String formato1 = "%-6s %-26s %-12s %-25s %-15s %-14s%n";
    
    public MenuLibro() { 
    }
    
    @Override
    public void menuDeOpciones(){
           int opcion ;
       do{  
           System.out.println("============================================");
           System.out.println("            GESTIONAR LIBROS                ");
           System.out.println("============================================");   
           System.out.println("1. Agregar Libro");
           System.out.println("2. Listar Libros");
           System.out.println("3. Buscar Libros");
           System.out.println("4. Modificar Libro");          
           System.out.println("5. Eliminar Libro");          
           System.out.println("0. Volver");
           opcion = Verificacion.verificarNumero(5, 0);
           opcionDelMenu(opcion);          
       }while(opcion!=0);    
    }
    
    @Override
     public void opcionDelMenu(int opcion) {
     
         switch (opcion){
          case 0-> {return;}   
           //agregar libro
          case 1-> { Libro l = crearNuevoLibro();
                     agregarLibro(l.getISBN(),l.getTitulo(),l.getAutor(),l.getEditorial(), l.getAnio() );}                
           // listar libros   
          case 2->  listarLibros();
           // buscar libros   
          case 3-> { System.out.print("Ingrese el Titulo del Libro: ");
                     buscarLibro( dato.nextLine() );}
           // modificar libro 
          case 4-> { System.out.print("Ingrese el Titulo del Libro: ");
                     Libro l = buscarySeleccionarLibro( dato.nextLine() );  // busca el libro si existe 
                     if(l != null){                                         //si encontro algun libro por su titulo
                     Libro libromodif = modificarDatos(l);            //pregunta que datos modificar y devuelve un libro modificado
                     // Actualiza la base de datos con los nuevos datos
                     modificarLibro(libromodif.getISBN(),libromodif.getTitulo(),libromodif.getAutor(),libromodif.getEditorial(), libromodif.getAnio() );}}
          // eliminar libros
          case 5->  { eliminarLibro(verificarId());}         
          default -> System.out.println("Ingrese una opcion del 0 al 5");            
       }
     }    

      private String verificarId() {
           String s = ""; 
           int n=0;
           boolean esnumero = false;                       
      while(!esnumero){
        System.out.println("Ingrese el numero de ISBN(13 digitos)");       
           s = dato.nextLine();
       try{// n = Integer.parseInt(s);
            System.out.println("s.length():"+s.length());
            if( s.length() != 13 ) //hay solo digitos del 0 al 9 y un total de 13 
            { throw new NumberFormatException();}
                esnumero = true;
        }catch(NumberFormatException e){
            System.out.println("El id no contiene 13 digitos o contiene alguna letra");  
        }
       }
       return s;          
        }
     
       
       
    @Override  //crea un nuevo libro
    public void agregarLibro(String ISBN,String titulo, String autor, String editorial,int anio_publicacion) {   
      try{    
          cbd.conectarBDD();
          String query = "INSERT INTO LIBRO (`ISBN`, `titulo`, `autor`, `editorial`, `anio_publicacion`) VALUES(?,?,?,?,?) ";
          PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
           ps.setString(1, ISBN);
           ps.setString(2, titulo);
           ps.setString(3, autor);
           ps.setString(4, editorial);
           ps.setInt(5, anio_publicacion);        
           int filainsertada = ps.executeUpdate();
           if(filainsertada>0) 
               System.out.println("Libro Registrado con Exito!");          
        }catch(SQLException ex){
          ex.printStackTrace();
      }
    }

    @Override
    public void listarLibros() {       
        System.out.println("   LISTA DE LIBROS");
        System.out.println("===========================================================================================================");
        System.out.printf(formato,"Titulo","Publicacion","Autor","Editorial","ISBN");
        System.out.println("----------------------------------------------------------------------------------------------------------");
       try{
           cbd.conectarBDD();
            String query = "SELECT * FROM LIBRO";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if( rs != null ){
             while(rs.next()){   
              //  System.out.println("  "+rs.getString("titulo")+"  "+rs.getString("anio_publicacion")+"  "+rs.getString("autor")+" "+ rs.getString("editorial")+" "+ rs.getString("isbn"));
              System.out.printf(formato,rs.getString("titulo"),rs.getString("anio_publicacion"),rs.getString("autor"),rs.getString("editorial"),rs.getString("isbn"));
             }
            }else{
                System.out.println("No se encontraron datos en la Base de Datos");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
       }       
    }
      //se encarga de buscar un libro desde un token ,en la base de datos , seleccionarlo y devolverlo 
    public  Libro buscarySeleccionarLibro(String titulo) {
       Libro libro = null;
       ArrayList<Libro> listlibros= null;
       
       try{ cbd.conectarBDD();
            String query = "SELECT * FROM LIBRO WHERE titulo LIKE ? ";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ps.setString(1, titulo.trim().strip()+"%");
            ResultSet rs = ps.executeQuery();
            if( rs != null ){
               int i=0;
               listlibros= new ArrayList<>();
               System.out.println("=======================================================================================================");
               System.out.printf(formato1,"Nro","Titulo","Publicacion","Autor","Editorial","ISBN");
               System.out.println("-------------------------------------------------------------------------------------------------------"); 
               while(rs.next()){ 
                //  System.out.println(" "+i+" "+rs.getString("titulo")+"  "+rs.getInt("anio_publicacion")+"  "+rs.getString("autor")+"  "+ rs.getString("editorial")+"  "+ rs.getString("isbn"));
                System.out.printf(formato1,i,rs.getString("titulo"),rs.getString("anio_publicacion"),rs.getString("autor"),rs.getString("editorial"),rs.getString("isbn"));
                 listlibros.add(new Libro(rs.getString("isbn"),rs.getString("titulo"),rs.getString("autor"),rs.getString("editorial"),rs.getInt("anio_publicacion")));
                 i++;
               }               
               libro = seleccionLibro(listlibros);  // libro buscado
                         
            }else{
                System.out.println("No se encontro un libro con el Id buscado!");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
        }
           return libro;       
      }

    @Override
    public  void buscarLibro(String titulo) {
         // tambien busca un libro con un token pero no lo devuelve
       try{ cbd.conectarBDD();
            String query = "SELECT * FROM LIBRO WHERE titulo LIKE ? ";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ps.setString(1, titulo+"%");
            ResultSet rs = ps.executeQuery();
            if( rs != null ){
               System.out.println("=====================================================================================================");
               System.out.printf(formato,"Titulo","Publicacion","Autor","Editorial","ISBN");
               System.out.println("-----------------------------------------------------------------------------------------------------"); 
               while(rs.next()){ 
              //    System.out.println(" "+rs.getString("titulo")+"  "+rs.getString("anio_publicacion")+"  "+rs.getString("autor")+"  "+ rs.getString("editorial")+"  "+ rs.getString("isbn"));
              System.out.printf(formato,rs.getString("titulo"),rs.getString("anio_publicacion"),rs.getString("autor"),rs.getString("editorial"),rs.getString("isbn")); 
               }             
            }else{
                System.out.println("No se encontro un libro con el Id buscado!");
            }          
       }catch(SQLException ex){
           ex.printStackTrace();      
       }         
    }

    @Override  //actualiza los nuevos datos del libro en la base de datos
    public void modificarLibro( String ISBN,String titulo, String autor, String editorial, int anio_publicacion) {
     System.out.println("   MODIFICAR LIBRO isbn: "+ISBN);
     System.out.println("=================================================================");
     String query = "UPDATE `LIBRO` SET ISBN = ?, titulo = ?, autor = ?, editorial = ?, anio_publicacion = ? WHERE ISBN = ? ";
      try{
          cbd.conectarBDD();
          PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
          ps.setString(1, ISBN);
          ps.setString(2, titulo);
          ps.setString(3, autor);
          ps.setString(4, editorial);
          ps.setInt(5, anio_publicacion);
          ps.setString(6, ISBN);  
         int filamodif = ps.executeUpdate();
           if(filamodif>0) 
                System.out.println("Datos del libro Modificado Correctamente! ");                   
     }catch(SQLException ex){
           ex.printStackTrace();      
       }  
    }

    @Override
    public void eliminarLibro(String isbn) {     //desvincula el libro de las tablas ejemplar y libro 
    
    if(existeElLibro(isbn)){   //consultamos si existe el libro si no existe no se puede eliminar
     if(!estaPrestado(isbn)){  
      System.out.println("El isbn del LIBRO A ELIMINAR es :"+isbn +"  desea eliminarlo?(s/n)");
      
     if(validarRespuesta()){
        String queryejemplar = "DELETE FROM EJEMPLAR WHERE ISBN = ? ";
        String querylibro = "DELETE FROM LIBRO WHERE ISBN = ? ";
      try{ 
          cbd.conectarBDD();
          cbd.conexion.setAutoCommit(false); 
        try(PreparedStatement psejemplar = ConexionMySql.conexion.prepareStatement(queryejemplar)){ 
            // Se desvincula el libro del ejemplar      
            psejemplar.setString(1, isbn);
            psejemplar.executeUpdate();
            }
        try(PreparedStatement pslibro = ConexionMySql.conexion.prepareStatement(querylibro)){            
             //luego se elimina el libro
           pslibro.setString(1, isbn);
           pslibro.executeQuery();            
         }
          cbd.conexion.commit();  //aceptamos los cambios
          System.out.println("El LIBRO isbn: " + isbn + " fue eliminado correctamente !"); 
        }catch(SQLException ex){
           // System.out.println("No se pudo borrar el Ejemplar");  
           try {
            if (cbd.conexion != null) cbd.conexion.rollback(); //volvemos los cambios
               }catch (SQLException e) {
                  e.printStackTrace();
                  }
        }finally{
           try{
                cbd.conexion.setAutoCommit(true); // Restauramos el comportamiento por defecto de aceptacion de cambios
              } catch (SQLException e) {
                  e.printStackTrace();
                 }       
        }
          }else{  System.out.println("Primero necesita hacer la devolución del LIBRO, desde el menu DEVOLUCIONES");        
               }
       }
      }else{
            System.out.println("El libro con ISBN: "+ isbn+ " No Existe en la Base de Datos");
           }
    }

    private Libro crearNuevoLibro() {
     Scanner num = new Scanner(System.in);
      String e="",c="s";
      int anio_pub;
      String titulo,autor,editorial,isbn;      
      do{ 
      System.out.println("-------------------------------------------------------------------");
      System.out.println("   DATOS DEL LIBRO(Los datos ingresados no pueden quedar vacios)");    
      System.out.println("-------------------------------------------------------------------");
      System.out.print("Ingrese el Titulo-> ");
      titulo = evaluarCadena();
      System.out.print("Ingrese el Autor-> ");
      autor = evaluarCadena();
      System.out.print("Ingrese la editorial-> ");
      editorial = evaluarCadena();          
      System.out.print("Ingrese el año de publicacion-> ");
      anio_pub =  evaluarNum();
      System.out.print("Ingrese el ISBN del Libro -> ");
      isbn = verificarId();
      System.out.println("Confirmar datos ?(s/n)(titulo :"+titulo+"  autor:"+autor+"  editorial:"+editorial+"  isbn:"+isbn+"  anio:"+anio_pub+")");
      e= dato.nextLine();
      } while(!c.equals(e.strip().toLowerCase())) ;
       return new Libro(isbn, titulo, autor, editorial, anio_pub);   
    }
    
    // devuelve un libro de una list seleccionado por el usuario por su indice
    private Libro seleccionLibro(ArrayList<Libro> listlibros) {     
      Libro libro =null; 
      Scanner num = new Scanner(System.in);
      int n = -1;
      boolean s = false;
     try{
      while(n < 0 || n >= listlibros.size()){
        System.out.println("SELECCIONE UN LIBRO DE LA LISTA(INGRESE EL NÚMERO): " );
         n = num.nextInt();
        }
        libro = listlibros.get(n);
     }catch(Exception e){
         System.out.println("Hubo un error durante la eleccion del libro");
     }
     return libro;           
    }
      //consulta al usuario que dato del libro desea modificar y devuelve un libro modificado
    private Libro modificarDatos(Libro libro) {      
     String nuevoISBN = "",nuevoTitulo = "", nuevoAutor = "", nuevaEditorial = "" ;
     int nuevoanio = 0;
   
      System.out.println("-------------------------------------------------------------------");
      System.out.println("   MODIFICAR DATOS DEL LIBRO");    
      System.out.println("-------------------------------------------------------------------");
      System.out.println("ISBN actual: "+ libro.getISBN() + "  Desea modificarlo(s/n)");
      if(validarRespuesta()){
         System.out.print("Ingrese el nuevo ISBN-> ");
         nuevoISBN = dato.nextLine();
      }else{
          nuevoISBN = libro.getISBN();
      }
      System.out.println("TITULO actual: "+ libro.getTitulo() + "  Desea modificarlo(s/n)");
      if(validarRespuesta()){
         System.out.print("Ingrese el nuevo Titulo-> ");
         nuevoTitulo = dato.nextLine();
      }else{
          nuevoTitulo = libro.getTitulo();
      }
       System.out.println("AUTOR actual: "+ libro.getAutor() + "  Desea modificarlo(s/n)");
      if(validarRespuesta()){
         System.out.print("Ingrese el nuevo Autor-> ");
         nuevoAutor = dato.nextLine();
      }else{
          nuevoAutor = libro.getAutor();
      }
      System.out.println("Editorial actual: "+ libro.getEditorial()+ "  Desea modificarlo(s/n)");
      if(validarRespuesta()){
         System.out.print("Ingrese el nueva Editorial-> ");
         nuevaEditorial = dato.nextLine();
      }else{
          nuevaEditorial = libro.getEditorial();
      }
      System.out.println("Año actual: "+ libro.getAnio()+ "  Desea modificarlo(s/n)");
      if(validarRespuesta()){
         System.out.print("Ingrese el nuevo Año-> ");
         nuevoanio = validarNumero();
      }else{
          nuevoanio = libro.getAnio();
      }    
     return new Libro(nuevoISBN, nuevoTitulo, nuevoAutor, nuevaEditorial, nuevoanio);
    }

    @Override
    public boolean validarRespuesta() {                    //Evalua si una respuesta es s/S o n/N
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
      
  public boolean estaPrestado( String ISBN){   //consulta si existe un isbn en la tabla prestamo 
      try{ cbd.conectarBDD();
            String query = "SELECT id_prestamo FROM PRESTAMO WHERE ISBN = ? " ;
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);  
            ps.setString(1, ISBN);
            ResultSet rs = ps.executeQuery();

           if( rs != null  ){
            if(rs.next()){
               StringBuilder sb = new StringBuilder();              
            do{   
               sb.append( rs.getInt("id_prestamo") + " -") ;        
              }while(rs.next());
                 System.out.println("El ejemplar se encuentra en prestamo con una id_de_prestamo: "+sb.toString() );
                 System.out.println("ACTUALICE su DEVOLUCION antes de ELIMINARLO de la Base de Datos. " );
              return true;
           }}        
          }catch(SQLException ex){
              ex.printStackTrace();  
            System.out.println("Hubo un error en la consulta esta prestado");  
          }
       return false;
     }  
       
  public int validarNumero() {  
    int n = 0;  
    boolean verif = false;
     while(!verif){
        try{ n = Integer.parseInt(num.nextLine());           
             if(n < 0){
             throw new InputMismatchException();} 
             verif = true;
         }catch(InputMismatchException ime){
            System.out.println("Ingrese un numero mayor a 0");  
         }catch(Exception e){
             System.out.println("Solo se puede ingresar un numero.");
       }}
     return n;
   }  
       //devuelve true o false de acuerdo a si existe , consulta por el isbn
    private boolean existeElLibro(String isbn) {
      boolean existe= false;
      try{ cbd.conectarBDD();
            String query = "SELECT COUNT(*) FROM LIBRO WHERE ISBN = ? ";
            PreparedStatement ps = ConexionMySql.conexion.prepareStatement(query);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
          if( rs != null ){                        
              if(rs.next()){             
               existe = rs.getInt(1) > 0;
              }                                                                                                                            
            }          
           }catch(SQLException ex){
             ex.printStackTrace();      
           }         
          return existe;
      }

    private String evaluarCadena() {
      String d = "";     
      do{
           d =  dato.nextLine(); 
           if(d.isBlank()){
               System.out.println("Intente nuevamente! El campo ingresado no puede estar vacio."); 
           }
       }while(d.isBlank() );
       return d;
    }

    private int evaluarNum() {
        int n = 0;        
        boolean esnumero = false;
      while(!esnumero){
          String entrada= dato.nextLine();
       try{        
          n = Integer.parseInt(entrada);
          esnumero = true;
        }catch(NumberFormatException e){
            System.out.println("Debe ingresar un numero para el año!");  
        }
       }
       return n;
    }
}
