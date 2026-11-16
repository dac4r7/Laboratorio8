
package lab8.util;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class Verificacion {
    
     private static Scanner num = new Scanner(System.in);
        
     public static int verificarNumero(int max, int min) {
    
     boolean verif = false;
     int n=0;
        while(!verif){
            try{
             n = Integer.parseInt(num.nextLine());           
             if(n > max || n<min){
             throw new InputMismatchException();} 
             verif = true;
            }catch(InputMismatchException ime){
                System.out.println("El Numero ingresado no es una opcion valida , reintente. ");  
            }         
        }
      return n;
    }
}
