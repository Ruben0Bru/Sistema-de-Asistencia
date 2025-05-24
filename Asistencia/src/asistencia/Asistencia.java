package asistencia;

import Controller.*;

/**
 *
 * @author Nemo
 */
public class Asistencia {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        ConexionDB con = new ConexionDB();
        con.getConnection();
        */
        
        Controller c = new Controller();
        c.Run();
        
    }
    
}
