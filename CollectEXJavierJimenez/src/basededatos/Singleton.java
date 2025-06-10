package basededatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Singleton {
	
	 private static Singleton instance;
	    private Connection connection;

	    private static final String DB_URL = "jdbc:sqlite:collectex.db";

	    private Singleton() {
	        try {
	            connection = DriverManager.getConnection(DB_URL);
	            System.out.println("Conexión a la base de datos establecida correctamente.");
	        } catch (SQLException e) {
	            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
	        }
	    }

	    public static synchronized Singleton getInstance() {
	        if (instance == null) {
	            instance = new Singleton();
	        }
	        return instance;
	    }

	    public Connection getConnection() {
	        try {
	            if (connection == null || connection.isClosed()) {
	                connection = DriverManager.getConnection(DB_URL);
	                System.out.println("Conexión a la base de datos restablecida.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al obtener conexión: " + e.getMessage());
	        }
	        return connection;
	    }


	    public void cerrarConexion() {
	        try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	                System.out.println("Conexión cerrada correctamente.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al cerrar la conexión: " + e.getMessage());
	        }
	    }

}
