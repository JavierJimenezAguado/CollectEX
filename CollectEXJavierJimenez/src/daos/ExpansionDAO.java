package daos;

import basededatos.Singleton;
import modelos.Expansion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpansionDAO {

	//
    public boolean insertarExpansion(Expansion e) {
        /**
	     * 07/05/2025
	     * Programador: Javi
	     * v1.0 inserta la expansion
	     * @param e
	     * Salida: boolean 
	     * 
	     */
        String sql = "INSERT INTO expansiones(codigo, nombre, idioma) VALUES (?,?,?)";
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, e.getCodigoExpansion());
            p.setString(2, e.getNombre());
            p.setString(3, e.getIdioma());
            return p.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error insertarExpansion: " + ex.getMessage());
            return false;
        }
    }// public boolean insertarExpansion(Expansion e) 


    public Expansion obtenerPorCodigo(String codigo) {
        /**
	     * 07/05/2025
	     * Programador: Javi
	     * v1.0 obtiene la expansion por codigo
	     * @param codigo
	     * Salida: Expansion 
	     * 
	     */
    	
        String sql = "SELECT * FROM expansiones WHERE codigo = ?";
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, codigo);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return mapFila(rs);

        } catch (SQLException ex) {
            System.err.println("Error obtenerPorCodigo: " + ex.getMessage());
        }
        return null;
    }//public Expansion obtenerPorCodigo(String codigo)
    
    //obtiene la expansion por id
    public Expansion obtenerPorId(int id) {
        /**
 	     * 07/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene la expansion por id
 	     * @param id
 	     * Salida: Expansion 
 	     * 
 	     */
    	
        String sql = "SELECT * FROM expansiones WHERE id_expansion = ?";
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return mapFila(rs);

        } catch (SQLException ex) {
            System.err.println("Error obtenerPorId: " + ex.getMessage());
        }
        return null;
    }//public Expansion obtenerPorId(int id)

    //lista las explansiones
    public List<Expansion> obtenerTodas() {
        /**
 	     * 07/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene todas la explansiones
 	     * Salida: lista 
 	     * 
 	     */
        List<Expansion> lista = new ArrayList<>();
        String sql = "SELECT * FROM expansiones";
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) lista.add(mapFila(rs));

        } catch (SQLException ex) {
            System.err.println("Error obtenerTodas: " + ex.getMessage());
        }
        return lista;
    }// public List<Expansion> obtenerTodas()
    

    //
    private Expansion mapFila(ResultSet rs) throws SQLException {
        /**
 	     * 07/05/2025
 	     * Programador: Javi
 	     * v1.0 convierte un resulset en una expansion 
 	     * @param rs
 	     * Salida: Expansion
 	     *  
 	     * @throws SQLException
 	     */
        return new Expansion(
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("idioma")
        );
    }
}
