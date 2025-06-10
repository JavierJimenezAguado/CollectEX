package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import basededatos.Singleton;
import modelos.PrecioHistorico;

public class PrecioHistoricoDAO {

    //insert historico
    public boolean insertarPrecioHistorico(PrecioHistorico historial) {
   	  /**
       * 23/05/2025
       * Programador: Javi
       * v1.0 inserta precio historico
       * @param historial
       * Salida boolean
       */
        String sql = "INSERT INTO precios_historicos(referencia, jsonPrecios) VALUES (?, ?)";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, historial.getReferencia());
            p.setString(2, historial.getJsonPrecios());
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insertarPrecioHistorico: " + e.getMessage());
            return false;
        }
    }//public boolean insertarPrecioHistorico(PrecioHistorico historial) 

  
    public PrecioHistorico obtenerPorReferencia(String referencia) {
     	  /**
         * 23/05/2025
         * Programador: Javi
         * v1.0 obtiene Historico segun referencia de carta
         * @param referencia
         * Salida PrecioHistorico
         */
    	
        String sql = "SELECT * FROM precios_historicos WHERE referencia = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, referencia);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return new PrecioHistorico(
                    rs.getInt("idHistorial"),
                    rs.getString("referencia"),
                    rs.getString("jsonPrecios")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerPorReferencia: " + e.getMessage());
        }
        return null;
    }// public PrecioHistorico obtenerPorReferencia(String referencia)

    //
    public boolean actualizarPrecioHistorico(String referencia, String nuevoJson) {
   	  /**
       * 23/05/2025
       * Programador: Javi
       * v1.0 actualizar el precio historico 
       * @param referencia
       * @param nuevoJson
       * Salida boolean
       */
        String sql = "UPDATE precios_historicos SET jsonPrecios = ? WHERE referencia = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, nuevoJson);
            p.setString(2, referencia);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualizarPrecioHistorico: " + e.getMessage());
            return false;
        }
    }//public boolean actualizarPrecioHistorico(String referencia, String nuevoJson)

}
