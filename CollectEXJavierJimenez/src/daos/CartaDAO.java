package daos;

import modelos.Carta;
import basededatos.Singleton;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CartaDAO {

	
    public Carta obtenerPorReferencia(String referencia) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene una carta y carga el historial de esta
 	     * @param referencia
 	     * Salida: Carta
 	     */
        String sql = "SELECT * FROM cartas WHERE referencia = ?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, referencia);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                Carta carta = new Carta(
                    rs.getString("referencia"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    null,
                    null,
                    String.valueOf(rs.getInt("id_expansion")),
                    rs.getDouble("precio"),
                    null
                );

                Map<LocalDateTime, Map<Integer, Double>> historial = cargarHistorial(carta.getReferencia());
                carta.setHistorialPrecios(historial);
                return carta;
            }

        } catch (Exception e) {
            System.err.println("Error obtenerPorReferencia: " + e.getMessage());
        }
        return null;
    }//public Carta obtenerPorReferencia(String referencia) 

    
    public List<Carta> obtenerTodasOrdenadas() {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene todas las cartas de la base de datos ordenadas por expansion y referencia 
 	     * Salida: lista
 	     */
        List<Carta> cartas = new ArrayList<>();
        String sql = "SELECT * FROM cartas ORDER BY id_expansion ASC , referencia_comercial ASC";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Carta carta = new Carta(
                    rs.getString("referencia"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    null,
                    null,
                    String.valueOf(rs.getInt("id_expansion")),
                    rs.getDouble("precio"),
                    null
                );
                cartas.add(carta);
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerTodasOrdenadas: " + e.getMessage());
        }

        return cartas;
    }//public List<Carta> obtenerTodasOrdenadas() 

    //
    /**
     * 

     * @return
     */
    public List<Carta> buscarCartas(String texto, List<String> tipos, List<String> rarezas) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 filtrado de cartas  
 	     * @param texto
 	     * @param tipos
 	     * @param rarezas
 	     * Salida: lista
 	     */
        List<Carta> resultados = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM cartas WHERE 1=1");

        if (texto != null && !texto.trim().isEmpty()) {
            query.append(" AND (LOWER(nombre) LIKE ? OR LOWER(referencia_comercial) LIKE ?)");
        }
        if (!tipos.isEmpty()) {
            query.append(" AND tipo IN (")
                 .append(tipos.stream().map(t -> "?").reduce((a, b) -> a + "," + b).orElse(""))
                 .append(")");
        }
        if (!rarezas.isEmpty()) {
            query.append(" AND subtipo IN (")
                 .append(rarezas.stream().map(r -> "?").reduce((a, b) -> a + "," + b).orElse(""))
                 .append(")");
        }

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int index = 1;

            if (texto != null && !texto.trim().isEmpty()) {
                String valor = "%" + texto.toLowerCase() + "%";
                stmt.setString(index++, valor);
                stmt.setString(index++, valor);
            }

            for (String tipo : tipos) {
                stmt.setString(index++, tipo);
            }

            for (String rareza : rarezas) {
                stmt.setString(index++, rareza);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultados.add(convertirResultSetACarta(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar cartas: " + e.getMessage());
        }

        return resultados;
    }// public List<Carta> buscarCartas(String texto, List<String> tipos, List<String> rarezas)
    
 
    //
    private Carta convertirResultSetACarta(ResultSet rs) throws SQLException {
    	 /**
         *07/05/2025
         *Programador Javi 
         *v1.0 obtiene una carta de un result set
         * @param rs
         * salida Carta
         * @throws SQLException
         */
    	
        return new Carta(
            rs.getString("referencia"),
            rs.getString("nombre"),
            rs.getString("tipo"),
            null,
            null,
            rs.getString("id_expansion"),
            rs.getDouble("precio"),
            null
        );
    }//private Carta convertirResultSetACarta(ResultSet rs) throws SQLException

    //
    private Map<LocalDateTime, Map<Integer, Double>> cargarHistorial(String referencia) {
   	 /**
         *07/05/2025
         *Programador Javi 
         *v1.0 obitnene el historico de precios 
         * @param referencia
         * salida Mapa
         */
    	
        Map<LocalDateTime, Map<Integer, Double>> historial = new LinkedHashMap<>();
        String sql = "SELECT fecha, historico FROM precios_historicos WHERE referencia = ? ORDER BY fecha";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, referencia);
            ResultSet rs = p.executeQuery();
            Gson gson = new Gson();
            Type t = new TypeToken<Map<String, Double>>() {}.getType();

            while (rs.next()) {
                String fechaStr = rs.getString("fecha");
                LocalDate fecha = LocalDate.parse(fechaStr);
                LocalDateTime fechaHora = fecha.atStartOfDay();

                Map<String, Double> preciosRaw = gson.fromJson(rs.getString("historico"), t);

                Map<Integer, Double> precios = new HashMap<>();
                for (Map.Entry<String, Double> entry : preciosRaw.entrySet()) {
                    try {
                        int grado = (int) Double.parseDouble(entry.getKey());
                        precios.put(grado, entry.getValue());
                    } catch (NumberFormatException ignored) {}
                }

                historial.put(fechaHora, precios);
            }

        } catch (Exception e) {
            System.err.println("Error cargarHistorial: " + e.getMessage());
        }

        return historial;
    }//private Map<LocalDateTime, Map<Integer, Double>> cargarHistorial(String referencia)
    
   

    
}
