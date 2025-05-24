package Controller;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConexionDB {

    public static Connection getConnection() {
        Connection con = null;
        try {
            
            Class.forName("org.mariadb.jdbc.Driver");

            con = (Connection) DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/asistencia", 
                "root", 
                "mementoMori"      
            );
            System.out.println("¡Conexión exitosa!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }

        return con;
    }
}
