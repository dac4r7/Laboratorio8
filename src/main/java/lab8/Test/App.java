

package lab8.Test;

import lab8.control.Menu;

/**
 *
 * @author Diego Adrian Cesarin
 */
public class App {

    public static void main(String[] args) {
        try { // Fuerza a que System.out reconozca acentos y eñes correctamente en la consola
                 System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            }catch(java.io.UnsupportedEncodingException e) {
             e.printStackTrace();
            }
        
             Menu m = new Menu();
             m.menuDeOpciones();
    }
}
