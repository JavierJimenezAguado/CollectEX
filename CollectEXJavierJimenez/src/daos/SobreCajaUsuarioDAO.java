package daos;

import basededatos.Singleton;
import modelos.SobreCajaUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SobreCajaUsuarioDAO {

    public boolean insertar(SobreCajaUsuario sc) {
    	
	  /**
      * 19/05/2025
      * Programador: Javi
      * @param sc
      * v1.0 oinserta 
      * Salida boolean
      */
    	String sql = "INSERT INTO sobres_cajas_usuario (idUsuario, idElemento, nombre, tipo, precio, alertaMin, alertaMax) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	stmt.setInt(1, sc.getIdUsuario());
        	stmt.setInt(2, sc.getIdElemento());
        	stmt.setString(3, sc.getNombre());
        	stmt.setString(4, sc.getTipo());
        	stmt.setDouble(5, sc.getPrecio());
        	stmt.setObject(6, sc.getAlertaMin());
        	stmt.setObject(7, sc.getAlertaMax());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insertar SobreCajaUsuario: " + e.getMessage());
            return false;
        }
    }//public boolean insertar(SobreCajaUsuario sc) 
    

    public boolean insertarBasico(int idUsuario, int idElemento, String nombre, String tipo, double precio) {
  	  /**
         * 19/05/2025
         * Programador: Javi
	     * @param idUsuario
	     * @param idElemento
	     * @param nombre
	     * @param tipo
	     * @param precio
         * v1.0 oinserta 
         * Salida boolean
         */
        String sql = "INSERT INTO sobres_cajas_usuario (idUsuario, idElemento, nombre, tipo, precio) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idElemento);
            stmt.setString(3, nombre);
            stmt.setString(4, tipo);
            stmt.setDouble(5, precio);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insertarBasico SobreCajaUsuario: " + e.getMessage());
            return false;
        }
    }
    
    
    public List<SobreCajaUsuario> obtenerPorUsuario(int idUsuario) {
    	
  	  /**
         * 19/05/2025
         * Programador: Javi
         * @param idUsuario
         * v1.0 lista las cajas y sobres del usuario
         * Salida lista
         */
        List<SobreCajaUsuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM sobres_cajas_usuario WHERE idUsuario = ?";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	SobreCajaUsuario sc = new SobreCajaUsuario(
            		    rs.getInt("id"),
            		    rs.getInt("idUsuario"),
            		    rs.getInt("idElemento"),
            		    rs.getString("nombre"),          
            		    rs.getString("tipo"),
            		    rs.getDouble("precio"),
            		    (Double) rs.getObject("alertaMin"),
            		    (Double) rs.getObject("alertaMax")
            		);

                lista.add(sc);
            }

        } catch (SQLException e) {
            System.err.println("Error obtenerPorUsuario: " + e.getMessage());
        }

        return lista;
    }//public List<SobreCajaUsuario> obtenerPorUsuario(int idUsuario)

    //elimina sobres o cajas 
    public boolean eliminar(int id) {
    	
  	  /**
         * 19/05/2025
         * Programador: Javi
         * @param id
         * v1.0 elimina las cajas y sobres del usuario
         * Salida boolean
         */
        String sql = "DELETE FROM sobres_cajas_usuario WHERE id = ?";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error eliminar SobreCajaUsuario: " + e.getMessage());
            return false;
        }
    }//public boolean eliminar(int id)
    

    
    public boolean actualizarAlertas(int id, Double alertaMin, Double alertaMax) {
    	
  	  /**
         * 19/05/2025
         * Programador: Javi
	     * @param id
	     * @param alertaMin
	     * @param alertaMax
         * v1.0 actualiza alertas 
         * Salida boolean
         */
        String sql = "UPDATE sobres_cajas_usuario SET alertaMin = ?, alertaMax = ? WHERE id = ?";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, alertaMin);
            stmt.setObject(2, alertaMax);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualizarAlertas: " + e.getMessage());
            return false;
        }
    }//public boolean actualizarAlertas(int id, Double alertaMin, Double alertaMax)
    
    
    //lista las cajas y sobres con alertas pendientes
    public List<SobreCajaUsuario> conAlerta() {
   	  /**
         * 19/05/2025
         * Programador: Javi
         * v1.0 lista las cajas y sobres con alertas pendientes
         * Salida lista
         */
        String q = """
            SELECT * FROM sobres_cajas_usuario
            WHERE alertaMin IS NOT NULL OR alertaMax IS NOT NULL
        """;
        List<SobreCajaUsuario> l = new ArrayList<>();
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(q);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                l.add(new SobreCajaUsuario(
                    r.getInt("id"), r.getInt("idUsuario"), r.getInt("idElemento"),
                    r.getString("nombre"), r.getString("tipo"),
                    r.getDouble("precio"),
                    (Double) r.getObject("alertaMin"),
                    (Double) r.getObject("alertaMax")));
            }
        } catch (SQLException e){ e.printStackTrace(); }
        return l;
    }//public List<SobreCajaUsuario> conAlerta() 

    
    public void limpiarAlertas(int id) {
     	  /**
         * 19/05/2025
         * Programador: Javi
         * v1.0 limpia las alertas 
         * @param id
         * Salida void
         */
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(
                "UPDATE sobres_cajas_usuario SET alertaMin=NULL, alertaMax=NULL WHERE id=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e){ e.printStackTrace(); }
    }// public void limpiarAlertas(int id)


    public boolean eliminar(int idUsuario, int idItem, String tipo) {
   	  /**
       * 19/05/2025
       * Programador: Javi
       * v1.0 elimina items
       * @param idusuario
       * @param idItem
       * @param tipo
       * Salida lista
       */
        String tabla = tipo.equalsIgnoreCase("caja") ? "cajas_usuario" : "sobres_usuario";

        String sql = "DELETE FROM " + tabla + " WHERE idUsuario = ? AND id" + (tipo.equalsIgnoreCase("caja") ? "Caja" : "Sobre") + " = ?";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idItem);

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar " + tipo + ": " + e.getMessage());
            return false;
        }
    }//public boolean eliminar(int idUsuario, int idItem, String tipo)


}
