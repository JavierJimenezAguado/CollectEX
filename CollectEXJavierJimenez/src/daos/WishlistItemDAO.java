package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import basededatos.Singleton;
import modelos.WishlistItem;

public class WishlistItemDAO {

    public boolean insertarWishlistItem(WishlistItem item) {
        /**
         * 30/05/2025
         * Programador: Javi
         * @param item
         * v1.0 inserta un item 
         * Salida boolean
         */
    	
        String sql = "INSERT INTO wishlist_items(idWishlist, referencia, cantidadDeseada, precioMinimo, precioMaximo, alertaMinimo, alertaMaximo, gradeada, nota) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, item.getIdWishlist());
            p.setString(2, item.getReferencia());
            p.setInt(3, item.getCantidadDeseada());
            p.setDouble(4, item.getPrecioMinimo());
            p.setDouble(5, item.getPrecioMaximo());
            p.setBoolean(6, item.isAlertaMinimo());
            p.setBoolean(7, item.isAlertaMaximo());
            p.setBoolean(8, item.isGradeada());
            p.setInt(9, item.getNota());
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insertarWishlistItem: " + e.getMessage());
            return false;
        }
    }//public boolean insertarWishlistItem(WishlistItem item)

    //lista items de wishlist
    public List<WishlistItem> obtenerPorWishlist(int idWishlist) {
    	
        /**
         * 30/05/2025
         * Programador: Javi
         * @param idWishlist
         * v1.0 lista los items de la wishlist 
         * Salida lista
         */
        String sql = "SELECT * FROM wishlist_items WHERE idWishlist = ?";
        List<WishlistItem> lista = new ArrayList<>();
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, idWishlist);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                lista.add(new WishlistItem(
                    rs.getInt("idWishlistItem"),
                    rs.getInt("idWishlist"),
                    rs.getString("referencia"),
                    rs.getInt("cantidadDeseada"),
                    rs.getDouble("precioMinimo"),
                    rs.getDouble("precioMaximo"),
                    rs.getBoolean("alertaMinimo"),
                    rs.getBoolean("alertaMaximo"),
                    rs.getBoolean("gradeada"),
                    rs.getInt("nota")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerPorWishlist: " + e.getMessage());
        }
        return lista;
    }// public List<WishlistItem> obtenerPorWishlist(int idWishlist)

    //
    public boolean actualizarWishlistItem(WishlistItem item) {
    	
        /**
         * 30/05/2025
         * Programador: Javi
         * @param item
         * v1.0 actualiza un item 
         * Salida boolean
         */
    	
        String sql = "UPDATE wishlist_items SET cantidadDeseada = ?, precioMinimo = ?, precioMaximo = ?, alertaMinimo = ?, alertaMaximo = ?, gradeada = ?, nota = ? WHERE idWishlistItem = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, item.getCantidadDeseada());
            p.setDouble(2, item.getPrecioMinimo());
            p.setDouble(3, item.getPrecioMaximo());
            p.setBoolean(4, item.isAlertaMinimo());
            p.setBoolean(5, item.isAlertaMaximo());
            p.setBoolean(6, item.isGradeada());
            p.setInt(7, item.getNota());
            p.setInt(8, item.getIdWishlistItem());
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualizarWishlistItem: " + e.getMessage());
            return false;
        }
    }//public boolean actualizarWishlistItem(WishlistItem item)


    public boolean eliminarWishlistItem(int idWishlistItem) {
    	
        /**
         * 30/05/2025
         * Programador: Javi
         * @param idWishlistItem
         * v1.0 elimina un item de la wishlist
         * Salida boolean
         */
    	
        String sql = "DELETE FROM wishlist_items WHERE idWishlistItem = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, idWishlistItem);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error eliminarWishlistItem: " + e.getMessage());
            return false;
        }
    }///public boolean eliminarWishlistItem(int idWishlistItem)
    

    public boolean existeItem(int idWishlist, String referencia) {
        /**
         * 30/05/2025
         * Programador: Javi
         * @param idWishlist
         * @param referencia
         * v1.0 bonfirma que el item existe en la wishlist 
         * Salida boolean
         */
        String sql = "SELECT 1 FROM wishlist_items WHERE idWishlist = ? AND referencia = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, idWishlist);
            p.setString(2, referencia);
            ResultSet rs = p.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error existeItem: " + e.getMessage());
            return false;
        }
    }//public boolean existeItem(int idWishlist, String referencia)
    
    //
    public boolean eliminarItem(int idWishlist, String referencia) {
        /**
         * 30/05/2025
         * Programador: Javi
         * 
         * v1.0 elimina un item especifico 
         * Salida boolean
         */
    	
        String sql = "DELETE FROM wishlist_items WHERE idWishlist = ? AND referencia = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, idWishlist);
            p.setString(2, referencia);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error eliminarItem: " + e.getMessage());
            return false;
        }
    }//public boolean eliminarItem(int idWishlist, String referencia)
    
    //
    public List<WishlistItem> conAlerta() {
        /**
         * 30/05/2025
         * Programador: Javi
         * 
         * v1.0 lista los items de wishlist con alerta activa
         * Salida list
         */
        String q = """
            SELECT wi.*, w.idUsuario
            FROM wishlist_items wi
            JOIN wishlist w ON wi.idWishlist = w.idWishlist
            WHERE alertaMinimo=1 OR alertaMaximo=1
        """;
        List<WishlistItem> l = new ArrayList<>();
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(q);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                l.add(new WishlistItem(
                    r.getInt("idWishlistItem"), r.getInt("idWishlist"),
                    r.getString("referencia"), r.getInt("cantidadDeseada"),
                    r.getDouble("precioMinimo"), r.getDouble("precioMaximo"),
                    r.getBoolean("alertaMinimo"), r.getBoolean("alertaMaximo"),
                    r.getBoolean("gradeada"), r.getInt("nota"),
                    r.getInt("idUsuario")         
                ));
            }
        } catch(SQLException e){ e.printStackTrace(); }
        return l;
    }//public List<WishlistItem> conAlerta()
    
    //limpia alertas de la wishlist 
    public void limpiarAlertas(int idWishlistItem) {
    	
        /**
         * 30/05/2025
         * Programador: Javi
         * 
         * v1.0 Limpia el campo de las alertas enviadas para no spamear simepre que se actualice el precio al usuario
         * 
         * @param idWishlistItem
         * 
         * Salida void
         */
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(
                "UPDATE wishlist_items SET alertaMinimo=0, alertaMaximo=0 WHERE idWishlistItem=?")) {
            p.setInt(1, idWishlistItem);
            p.executeUpdate();
        } catch(SQLException e){ e.printStackTrace(); }
    }// public void limpiarAlertas(int idWishlistItem)



}
