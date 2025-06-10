package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import basededatos.Singleton;
import modelos.Wishlist;

public class WishlistDAO {

	//insertar wishlist
    public boolean insertarWishlist(Wishlist wishlist) {
        String sql = "INSERT INTO wishlist(idUsuario, nombre) VALUES (?, ?)";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, wishlist.getIdUsuario());
            p.setString(2, wishlist.getNombre());
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insertarWishlist: " + e.getMessage());
            return false;
        }
    }// public boolean insertarWishlist(Wishlist wishlist)
    
    //obtiene wishlist de un usuario
    public Wishlist obtenerPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM wishlist WHERE idUsuario = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, idUsuario);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return new Wishlist(
                    rs.getInt("idWishlist"),
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    null 
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerPorUsuario: " + e.getMessage());
        }
        return null;
    }//public Wishlist obtenerPorUsuario(int idUsuario)
    
    //actualiza la wishlist
    public boolean actualizarWishlist(Wishlist wishlist) {
        String sql = "UPDATE wishlist SET nombre = ? WHERE idUsuario = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, wishlist.getNombre());
            p.setInt(2, wishlist.getIdUsuario());
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualizarWishlist: " + e.getMessage());
            return false;
        }
    }//public boolean actualizarWishlist(Wishlist wishlist) 

    
}
