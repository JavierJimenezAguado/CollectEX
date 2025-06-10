package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import basededatos.Singleton;
import modelos.Usuario;

public class UsuarioDAO {
	
	  // insert nuevo usuario 
    public boolean insertarUsuario(Usuario user) {
        /**
         * 10/05/2025
         * Programador: Javi
         * @param user
         * v1.0 inserta un usuario 
         * Salida boolean
         */
    	
        String sql = "INSERT INTO usuarios(nombre, correo, contrasena) VALUES (?, ?, ?)";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, user.getNombre());
            p.setString(2, user.getCorreo());
            p.setString(3, user.getContrasenaHash());
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insertarUsuario: " + e.getMessage());
            return false;
        }
    }//public boolean insertarUsuario(Usuario user)
    
    //obtener usuario por nombre
    public Usuario obtenerPorNombre(String nombre) {
    	
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 obtine un usuario a partir de su nombre
         * @param nombre 
         * Salida usuario
         */
        String sql = "SELECT * FROM usuarios WHERE nombre = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, nombre);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerPorNombre: " + e.getMessage());
        }
        return null;
    }//public Usuario obtenerPorNombre(String nombre)


    //obtiene usuario por id
    public Usuario obtenerPorId(int idUsuario) {
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 obtine un usuario a partir de su id
         * @param idUsusario 
         * Salida usuario
         */
        String sql = "SELECT * FROM usuarios WHERE idUsuario = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, idUsuario);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerPorId Usuario: " + e.getMessage());
        }
        return null;
    }//public Usuario obtenerPorId(int idUsuario)

    //obtiene usuario por ccorreo
    public Usuario obtenerPorCorreo(String correo) {
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 obtine un usuario a partir de su correo
         * @param correo 
         * Salida usuario
         */
    	
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, correo);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerPorCorreo: " + e.getMessage());
        }
        return null;
    }//public Usuario obtenerPorCorreo(String correo)


    //cambio de contraseña
    public boolean cambiarContrasena(int idUsuario, String nuevaHash) {
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 hace update de la nueva contraseña 
         * @param idUsuario 
         * @param nuevaHash
         * Salida boolean
         */
        String sql = "UPDATE usuarios SET contrasena = ? WHERE idUsuario = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, nuevaHash);
            p.setInt(2, idUsuario);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error cambiarContrasena: " + e.getMessage());
            return false;
        }
    }//public boolean cambiarContrasena(int idUsuario, String nuevaHash)

    //elimina usuario por id
    public boolean eliminarUsuario(int idUsuario) {
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 elimina un usuario a partir de su id 
         * @param idUsusario
         * Salida boolean
         */
    	
        String sql = "DELETE FROM usuarios WHERE idUsuario = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, idUsuario);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error eliminarUsuario: " + e.getMessage());
            return false;
        }
    }//public boolean eliminarUsuario(int idUsuario)

    public boolean autenticar(String correo, String hashContrasena) {
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 autentica login
         * @param correo
         * @param hashContrasena
         * Salida boolean
         */
        String sql = "SELECT 1 FROM usuarios WHERE correo = ? AND contrasena = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, correo);
            p.setString(2, hashContrasena);
            ResultSet rs = p.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error autenticar: " + e.getMessage());
            return false;
        }
    }//public boolean autenticar(String correo, String hashContrasena)
    
    //cambio de contraseña
    public boolean cambiarContrasena(String correo, String nuevaHash) {
    	
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 cambia la contraseña
         * @param correo
         * @param hashContrasena
         * Salida boolean
         */
    	
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, nuevaHash);
            p.setString(2, correo);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error cambiarContrasena: " + e.getMessage());
            return false;
        }
    }// public boolean cambiarContrasena(String correo, String nuevaHash)
    
    //metodo para poder iniciar con correo o nombre de usuario
    public boolean autenticarPorNombreOCorreo(String entrada, String hashContrasena) {
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 autentica login con nombre de usario
         * @param entrada
         * @param hashContrasena
         * Salida boolean
         */
        String sql = "SELECT 1 FROM usuarios WHERE (correo = ? OR nombre = ?) AND contrasena = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, entrada);
            p.setString(2, entrada);
            p.setString(3, hashContrasena);
            ResultSet rs = p.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error autenticarPorNombreOCorreo: " + e.getMessage());
            return false;
        }
    }//public boolean autenticarPorNombreOCorreo(String entrada, String hashContrasena)


}
