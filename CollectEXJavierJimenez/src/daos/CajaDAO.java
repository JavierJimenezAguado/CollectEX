package daos;

import basededatos.Singleton;
import modelos.Caja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CajaDAO {


	public boolean insertar(Caja c) {
		
		/**
		 * 07/05/2025
		 * Programador: Javi
		 * 
		 * v1.0 Inserta una caja a base de datos
		 * @param c
		 * Salida: boolean
		 */
		
	    String sql = "INSERT INTO cajas (nombre, url_pricecharting, precio) VALUES (?,?,?)";
	    try (Connection cn = Singleton.getInstance().getConnection();
	         PreparedStatement p = cn.prepareStatement(sql)) {

	        p.setString(1, c.getNombre());
	        p.setString(2, c.getUrlPriceCharting());
	        p.setDouble(3, c.getPrecio());
	        return p.executeUpdate() > 0;

	    } catch (SQLException e) { System.err.println(e.getMessage()); return false; }
	}//public boolean insertar(Caja c)
	

	//obtiene todas las cajas de la base de datos
	public List<Caja> obtenerTodas() {
		/**
		 * 07/05/2025
		 * Programador: Javi
		 * 
		 * v1.0 Lista todas las cajas 
		 * 
		 * Salida: list
		 */
	    List<Caja> lista = new ArrayList<>();
	    String sql = "SELECT idCaja, nombre, url_pricecharting, precio FROM cajas";
	    try (Connection cn = Singleton.getInstance().getConnection();
	         PreparedStatement p = cn.prepareStatement(sql);
	         ResultSet r = p.executeQuery()) {

	        while (r.next()) {
	            lista.add(new Caja(
	                r.getInt("idCaja"),
	                r.getString("nombre"),
	                r.getString("url_pricecharting"),
	                r.getDouble("precio")
	            ));
	        }
	    } catch (SQLException e){ System.err.println(e.getMessage()); }
	    return lista;
	}//public List<Caja> obtenerTodas()
	
	//obtiene una caja por id
	public Caja obtenerPorId(int idCaja) {
		/**
		 * 07/05/2025
		 * Programador: Javi
		 * 
		 * v1.0 obtiene una caja por id  
		 * @param idCaja
		 * Salida: Caja
		 */
	    String sql = "SELECT idCaja, nombre, url_pricecharting, precio FROM cajas WHERE idCaja = ?";
	    try (Connection cn = Singleton.getInstance().getConnection();
	         PreparedStatement p = cn.prepareStatement(sql)) {

	        p.setInt(1, idCaja);
	        ResultSet r = p.executeQuery();
	        return r.next()
	             ? new Caja(r.getInt("idCaja"), r.getString("nombre"),
	                        r.getString("url_pricecharting"), r.getDouble("precio"))
	             : null;

	    } catch (SQLException e){ System.err.println(e.getMessage()); return null; }
	}//public Caja obtenerPorId(int idCaja)

}
