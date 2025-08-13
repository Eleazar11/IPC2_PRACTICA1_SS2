/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author eleaz
 */
public class ConexionDB {

    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/HyruleEvents?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Eleazar123Colop";

    // Única instancia de la conexión
    private static Connection connection;

    // Constructor privado para que no se pueda instanciar
    private ConexionDB() { }

    // Método para obtener la conexión
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL_MYSQL, USER, PASSWORD);
                System.out.println("Conexión establecida con la base de datos HyruleEvents");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL no encontrado");
            e.printStackTrace();
        }
        return connection;
    }

    // Método opcional para cerrar conexión
    public static void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }
}
