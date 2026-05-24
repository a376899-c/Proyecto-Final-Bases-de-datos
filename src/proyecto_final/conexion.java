/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_final;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author moral
 */
public class conexion {
    
    public static Connection conectar() {

        try {

            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                "UNIRIDE",
                "123"
            );

            System.out.println("Conexión exitosa");

            return con;

        } catch (SQLException e) {

            System.out.println("Error: " + e);

            return null;
        }
    }
}
