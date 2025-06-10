package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import basededatos.Singleton;
import modelos.CartaColeccion;



public class CartaColeccionDAO {

	
	public boolean guardar(CartaColeccion cc) {
		
		/**
		 * 07/05/2025
		 * Programador: Javi
		 * 
		 * v1.0 Guarda una carta en la coleccion  
		 * @param cc
		 * Salida: boolean 
		 */
		
	    String sql = """
	        INSERT OR REPLACE INTO colecciones_cartas 
	        (idColeccion, referencia, cantidad, graduacion, precioPagado, alertaMin, alertaMax)
	        VALUES (?, ?, ?, ?, ?, ?, ?)
	    """;
	    try (Connection c = Singleton.getInstance().getConnection();
	         PreparedStatement p = c.prepareStatement(sql)) {

	        p.setInt(1, cc.getIdColeccion());
	        p.setString(2, cc.getReferencia());
	        p.setInt(3, cc.getCantidad());
	        p.setInt(4, cc.getGraduacion());
	        p.setDouble(5, cc.getPrecioPagado());
	        if (cc.getAlertaMin() != null) p.setDouble(6, cc.getAlertaMin()); else p.setNull(6, java.sql.Types.REAL);
	        if (cc.getAlertaMax() != null) p.setDouble(7, cc.getAlertaMax()); else p.setNull(7, java.sql.Types.REAL);

	        int filas = p.executeUpdate();
	        System.out.println("Filas insertadas/actualizadas: " + filas);
	        return filas > 0;

	    } catch (Exception e) {
	        System.err.println("Error guardar carta: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}//public boolean guardar(CartaColeccion cc) 


    public CartaColeccion obtener(int idColeccion, String referencia){
    	/**
    	 *  07/05/2025
    	 * Programador: Javi
    	 * v1.0obtiene una carta de una determinada coleccion mediante idColeecion y referencia de la carta
    	 * 
    	 * @param idColeccion
    	 * @param referencia
    	 * Salida CartaColeccion
    	 */
    	
    	
        String sql = "SELECT * FROM colecciones_cartas WHERE idColeccion=? AND referencia=?";
        try(Connection c = Singleton.getInstance().getConnection();
            PreparedStatement p = c.prepareStatement(sql)){

            p.setInt(1, idColeccion);
            p.setString(2, referencia);
            ResultSet rs = p.executeQuery();
            if(rs.next()) return mapRow(rs);

        }catch(SQLException e){ System.err.println("obtener CC: "+e.getMessage()); }
        return null;
    }// public CartaColeccion obtener(int idColeccion, String referencia)

  
 
    public List<CartaColeccion> listarPorColeccion(int idColeccion){
    	  /**
         *  07/05/2025
         * Programador: Javi
         * v1.0 devuelve la lista de cartas de una coleccion 
         * @param idColeccion
         * Salida: list
         */
        String sql = "SELECT * FROM colecciones_cartas WHERE idColeccion=?";
        List<CartaColeccion> lista = new ArrayList<>();
        try(Connection c = Singleton.getInstance().getConnection();
            PreparedStatement p = c.prepareStatement(sql)){

            p.setInt(1, idColeccion);
            ResultSet rs = p.executeQuery();
            while(rs.next()) lista.add(mapRow(rs));

        }catch(SQLException e){ System.err.println("listar CC: "+e.getMessage()); }
        return lista;
    }//public List<CartaColeccion> listarPorColeccion(int idColeccion)
    
    //obtiene las caras con alerta
    public List<CartaColeccion> cartasConAlerta(){
  	  /**
         *  07/05/2025
         * Programador: Javi
         * v1.0 obtiene las caras con alerta
         * 
         * Salida: list
         */
        String q = """
           SELECT * FROM colecciones_cartas
           WHERE alertaMin IS NOT NULL OR alertaMax IS NOT NULL
        """;
        List<CartaColeccion> l = new ArrayList<>();
        try(Connection c = Singleton.getInstance().getConnection();
            Statement  st = c.createStatement();
            ResultSet  rs = st.executeQuery(q)){
            while(rs.next()) l.add(mapRow(rs));
        }catch(SQLException e){ System.err.println("alertas CC: "+e.getMessage()); }
        return l;
    }// public List<CartaColeccion> cartasConAlerta()
    

    

    public boolean eliminar(int idColeccion, String referencia){
        /**
         *  07/05/2025
         * Programador: Javi
         * v1.0 elimina una carta de una coleccion concreta
         * @param idColeccion
         * @param referencia
         * Salida: boolean
         */
    	
        String sql = "DELETE FROM colecciones_cartas WHERE idColeccion=? AND referencia=?";
        try(Connection c = Singleton.getInstance().getConnection();
            PreparedStatement p = c.prepareStatement(sql)){
            p.setInt(1,idColeccion);
            p.setString(2,referencia);
            return p.executeUpdate()>0;
        }catch(SQLException e){
            System.err.println("eliminar CC: "+e.getMessage());
            return false;
        }
    }// public boolean eliminar(int idColeccion, String referencia)

    
   
    private CartaColeccion mapRow(ResultSet rs) throws SQLException{

    	 /**
    	  *  07/05/2025
    	  * Programador: Javi
    	  * v1.0 transforma un resulset en una carta
    	  * @param rs
    	  * Salida: CartaColeccion
    	  * @throws SQLException
    	  */
    	return new CartaColeccion(
    		    rs.getInt("idColeccion"),
    		    rs.getString("referencia"),
    		    rs.getInt("cantidad"),
    		    rs.getInt("graduacion"),
    		    rs.getDouble("precioPagado"),
    		    leerDouble(rs, "alertaMin"),
    		    leerDouble(rs, "alertaMax")
    		);

    }//private CartaColeccion mapRow(ResultSet rs) throws SQLException
    

   
    private Double leerDouble(ResultSet rs, String columna) {
    	
        /**
         *  07/05/2025
         * Programador: Javi
         * v1.0 lectura de doubles con control de errores
         * @param rs
         * @param columna
         * Salida: double
         */
        
    	
        try {
            double valor = rs.getDouble(columna);
            return rs.wasNull() ? null : valor;
        } catch (Exception e) {
            System.err.println("Valor no convertible a double en columna " + columna + ": " + e.getMessage());
            return null;
        }
    }//private Double leerDouble(ResultSet rs, String columna)
    
    //elimina las alertas
    public void limpiarAlertas(int idColeccion, String referencia) {
        /**
         * 30/05/2025
         * Programador: Javi
         * 
         * v1.0 Limpia el campo de las alertas enviadas para no spamear simepre que se actualice el precio al usuario
         * 
         * @param idColeccion
         * @param referencia
         * 
         * Salida void
         */
        String sql = """
            UPDATE colecciones_cartas
            SET alertaMin = NULL, alertaMax = NULL
            WHERE idColeccion = ? AND referencia = ?
        """;
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idColeccion);
            p.setString(2, referencia);
            p.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }//public void limpiarAlertas(int idColeccion, String referencia)


    
    
}
