
package lab8.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class ConexionMySql {
    
    public static Connection conexion;
    public PreparedStatement sentencia;
    public ResultSet resultado;
    
    public void conectarBDD(){
       try{
           //final String CONTROLLER = "org.mysql.cj.jdbc.Driver";
           final String CONTROLLER = "org.mariadb.jdbc.Driver";          
           Class.forName(CONTROLLER);//carga del controlador          
           conexion = DriverManager.getConnection("jdbc:mariadb://localhost/Biblioteca_DB","admin","1234");
           
       }catch(ClassNotFoundException nfe){
           nfe.printStackTrace();
           javax.swing.JOptionPane.showMessageDialog(null, "Error Carga Driver");
       }catch(SQLException sqe) {
           sqe.printStackTrace();
           javax.swing.JOptionPane.showMessageDialog(null, "Error en la creacion del Statement");
           System.exit(1);
       }         
    }
    
    public void desconectarBDD(){
        try{
            if(conexion != null && !conexion.isClosed() ){           
                conexion.close();
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }      
    }
    
    
}
