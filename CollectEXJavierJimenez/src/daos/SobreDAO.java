package daos;

import basededatos.Singleton;
import modelos.Sobre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SobreDAO {

	//inserta sobres
	public boolean insertar(Sobre s) {
        /**
         * 19/05/2025
         * Programador: Javi
         * @param s
         * v1.0 inserta un sobre
         * Salida boolean
         */
	    String sql = "INSERT INTO sobres (nombre, url_pricecharting, precio) VALUES (?,?,?)";
	    try (Connection cn = Singleton.getInstance().getConnection();
	         PreparedStatement p = cn.prepareStatement(sql)) {

	        p.setString(1, s.getNombre());
	        p.setString(2, s.getUrlPriceCharting());
	        p.setDouble(3, s.getPrecio());
	        return p.executeUpdate() > 0;

	    } catch (SQLException e){ System.err.println(e.getMessage()); return false; }
	}//public boolean insertar(Sobre s)

	
	public List<Sobre> obtenerTodos() {
        /**
         * 19/05/2025
         * Programador: Javi
         * 
         * v1.0 lista todos los sobres 
         * Salida lista
         */
	    List<Sobre> lista = new ArrayList<>();
	    String sql = "SELECT idSobre, nombre, url_pricecharting, precio FROM sobres";
	    try (Connection cn = Singleton.getInstance().getConnection();
	         PreparedStatement p = cn.prepareStatement(sql);
	         ResultSet r = p.executeQuery()) {

	        while (r.next()) {
	            lista.add(new Sobre(
	                r.getInt("idSobre"),
	                r.getString("nombre"),
	                r.getString("url_pricecharting"),
	                r.getDouble("precio")
	            ));
	        }
	    } catch (SQLException e){ System.err.println(e.getMessage()); }
	    return lista;
	}//public List<Sobre> obtenerTodos()
	
	//
	public Sobre obtenerPorId(int idSobre) {
		
	       /**
         * 19/05/2025
         * Programador: Javi
         * @param idSobre
         * v1.0 obtiene soobres por id
         * Salida sobre
         */
		
	    String sql = "SELECT idSobre, nombre, url_pricecharting, precio FROM sobres WHERE idSobre = ?";
	    try (Connection cn = Singleton.getInstance().getConnection();
	         PreparedStatement p = cn.prepareStatement(sql)) {

	        p.setInt(1, idSobre);
	        ResultSet r = p.executeQuery();
	        return r.next()
	             ? new Sobre(r.getInt("idSobre"), r.getString("nombre"),
	                         r.getString("url_pricecharting"), r.getDouble("precio"))
	             : null;

	    } catch (SQLException e){ System.err.println(e.getMessage()); return null; }
	}//public Sobre obtenerPorId(int idSobre)

}
