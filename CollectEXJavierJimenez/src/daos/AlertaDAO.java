package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import modelos.CartaColeccion;

import basededatos.Singleton;

public class AlertaDAO {


    public List<CartaColeccion> obtenerPendientes() {
        /**
	     * 30/05/2025
	     * Programador: Javi
	     * v1.0 Obtiene la lista de alertas pendientes
	     * Salida: list 
	     * 
	     */
        String sql = """
            SELECT cc.*, u.email
            FROM cartas_coleccion cc
            JOIN usuarios u ON cc.idUsuario = u.idUsuario
            WHERE alertaMin IS NOT NULL OR alertaMax IS NOT NULL
        """;
		return null;
    }//public List<CartaColeccion> obtenerPendientes() ))



    public void limpiar(int idColeccion, String referencia) {
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
            UPDATE cartas_coleccion
            SET alertaMin = NULL, alertaMax = NULL
            WHERE idColeccion = ? AND referenciaCarta = ?
        """;
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, idColeccion);
            st.setString(2, referencia);
            st.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }//public void limpiar(int idColeccion, String referencia)
}
