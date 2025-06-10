package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class ScraperService {

    private static final int TIMEOUT = 25000;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String URL_DB = "jdbc:sqlite:collectex.db";
    private static final int NUM_HILOS = 4;
    private static final Gson gson = new Gson();

    //lista de notas de grading 
    private static final List<String> GRADOS_VALIDOS = List.of(
        "Ungraded", 
        "Grade 2",
        "Grade 3",
        "Grade 4", 
        "Grade 5", 
        "Grade 6",
        "Grade 7",
        "Grade 8", 
        "Grade 9",
        "Grade 9.5",
        "PSA 10"
    );//private static final List<String> GRADOS_VALIDOS = List.of
    
    //convierte el texto del grado a valor numerico
    private static double gradoToNumero(String grado) {
       	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 convierte el texto del grado a valor numerico 
         *@param grado
         * salida double
         */
        return switch (grado) {
            case "Ungraded"  -> 11.0;
            case "Grade 2"   -> 2.0;
            case "Grade 3"   -> 3.0;
            case "Grade 4"   -> 4.0;
            case "Grade 5"   -> 5.0;
            case "Grade 6"   -> 6.0;
            case "Grade 7"   -> 7.0;
            case "Grade 8"   -> 8.0;
            case "Grade 9"   -> 9.0;
            case "Grade 9.5" -> 9.5;
            case "PSA 10"    -> 10.0;
            default          -> -1.0;
        };
    }//private static double gradoToNumero(String grado)
    
    
    public static void ejecutarScrapingCompleto() {
       	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 ejecuta el scrapping completo
         * salida void 
         */
        List<String[]> tareas = new ArrayList<>();

        try (Connection cn = DriverManager.getConnection(URL_DB);
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT c.referencia, e.codigo AS codigo_expansion, c.nombre " +
                "FROM cartas c JOIN expansiones e ON c.id_expansion = e.id_expansion " +
                "ORDER BY c.referencia")) {

            while (rs.next()) {
                String ref = rs.getString("referencia");
                String cod = rs.getString("codigo_expansion");
                String nom = rs.getString("nombre");
                tareas.add(new String[]{ref, cod, nom});
            }

        } catch (Exception e) {
            System.err.println("Error al leer la base de datos: " + e.getMessage());
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_HILOS);
        LocalDate hoy = LocalDate.now();

        for (String[] t : tareas) {
            executor.execute(() -> procesarCarta(t[0], t[1], t[2], hoy));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        for (String[] c : obtenerElementos(
                "SELECT idCaja,nombre,url_pricecharting FROM cajas")) {

            int    id  = Integer.parseInt(c[0]);
            String url = c[2];

            if (url != null && !url.isBlank())
                procesarCajaSobre(id, url, "cajas", hoy);
        }

        for (String[] s : obtenerElementos(
                "SELECT idSobre,nombre,url_pricecharting FROM sobres")) {

            int    id  = Integer.parseInt(s[0]);
            String url = s[2];

            if (url != null && !url.isBlank())
                procesarCajaSobre(id, url, "sobres", hoy);
        }
        
        AlertaService.comprobar(); 

        System.out.println("Scraping completado.");
    }//public static void ejecutarScrapingCompleto()
    
    
    private static void procesarCarta(String referencia, String codigoExp, String nombre, LocalDate fecha) {
       	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 procesa la url de carta
         * salida void 
         */
    	
        String url = "https://www.pricecharting.com/game/pokemon-" + codigoExp + "/" + referencia;
        System.out.printf("%s (%s): %s%n", referencia, codigoExp, url);

        Map<Double, Double> preciosPorGrado = new HashMap<>();

        try {
            Document doc = Jsoup.connect(url)
                                .timeout(TIMEOUT)
                                .userAgent(USER_AGENT)
                                .get();

            Elements filas = doc.select("#full-prices tbody tr");
            if (filas.isEmpty()) {
                System.out.println("   (no hay tabla de precios)");
                return;
            }

            for (Element fila : filas) {
                String gradoTexto = fila.select("td").first().text();
                if (!GRADOS_VALIDOS.contains(gradoTexto)) continue;

                String txt = fila.select(".price.js-price").text()
                                 .replace("$", "").replace(",", "").trim();
                if (txt.isEmpty() || txt.equals("-")) continue;

                double precio = Double.parseDouble(txt);
                double grado = gradoToNumero(gradoTexto);
                preciosPorGrado.put(grado, precio);

                if (grado == 11.0) {
                    System.out.println("   → Precio sin gradear detectado: " + precio);
                }
            }

            if (!preciosPorGrado.isEmpty()) {
                guardarPrecioCarta(referencia, preciosPorGrado, fecha);
            } else {
                System.out.println("   No se extrajeron precios válidos");
            }

        } catch (Exception ex) {
            System.out.println("   ERROR: " + ex.getMessage());
        }
    }//private static void procesarCarta(String referencia, String codigoExp, String nombre, LocalDate fecha) 
    
    /**
     * 

     */
    private static void guardarPrecioCarta(String referencia, Map<Double, Double> preciosPorGrado, LocalDate fecha) {
       	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 guarda los precios de la carta en historico 
         * @param referencia
         * @param preciosPorGrado
         * @param fecha
         * salida void 
         */
        String fechaStr = fecha.toString();

        try (Connection conn = DriverManager.getConnection(URL_DB)) {
            String checkSQL = "SELECT COUNT(*) FROM precios_historicos WHERE referencia = ? AND fecha = ?";
            try (PreparedStatement check = conn.prepareStatement(checkSQL)) {
                check.setString(1, referencia);
                check.setString(2, fechaStr);
                ResultSet rs = check.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("   Ya existía histórico de " + fechaStr + " para " + referencia);
                    return;
                }
            }

            String insertSQL = "INSERT OR IGNORE INTO precios_historicos (referencia, fecha, historico) VALUES (?, ?, ?)";
            try (PreparedStatement insert = conn.prepareStatement(insertSQL)) {
                String json = gson.toJson(preciosPorGrado);
                insert.setString(1, referencia);
                insert.setString(2, fechaStr);
                insert.setString(3, json);
                insert.executeUpdate();
                System.out.println("Histórico insertado para " + referencia);
            }

            if (preciosPorGrado.containsKey(11.0)) {
                String updateSQL = "UPDATE cartas SET precio = ? WHERE referencia = ?";
                try (PreparedStatement update = conn.prepareStatement(updateSQL)) {
                    update.setDouble(1, preciosPorGrado.get(11.0));
                    update.setString(2, referencia);
                    update.executeUpdate();
                    System.out.println("Precio actualizado a " + preciosPorGrado.get(11.0));
                }
            } else {
                System.out.println("No se encontró precio sin gradear (grado 11.0)");
            }

        } catch (Exception e) {
            System.err.println("Error guardando histórico de " + referencia + ": " + e.getMessage());
        }
    }//private static void guardarPrecioCarta(String referencia, Map<Double, Double> preciosPorGrado, LocalDate fecha)
    
    
    private static void procesarCajaSobre(int idElemento, String url, String tablaPlural, LocalDate fecha) {
       	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 procesa las cajas y sobres
         * @param idElemento
         * @param url
         * @param tablaPlural
         * @param fecha
         * salida void 
         */
		String tipoSingular = tablaPlural.equals("cajas") ? "caja" : "sobre";
		String referencia   = tipoSingular + "-" + idElemento;   
		try {
			Document doc = Jsoup.connect(url)
			  .timeout(TIMEOUT)
			  .userAgent(USER_AGENT)
			  .get();
			
			Element priceEl = doc.selectFirst(".price.js-price");
			if (priceEl == null) return;
			
			double precio = Double.parseDouble(
			priceEl.text().replace("$", "").replace(",", "").trim());
			
			guardarPrecioProducto(referencia, tipoSingular,   
			    idElemento, precio, fecha);
		
		} catch (Exception ex) {
			System.out.println("ERROR " + tablaPlural + " " + idElemento + ": " + ex.getMessage());
		}
	}//private static void procesarCajaSobre(int idElemento, String url, String tablaPlural, LocalDate fecha)

    
    private static void guardarPrecioProducto(String referencia, String tipoSingular,int idElemento, double precio, LocalDate fecha) {
       	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 guarda precios de productos cerrados en historico
         * @param referencia
         * @param tipoSingular
         * @param idElemento
         * @param precio
         * @param fecha
         * salida void 
         */
    	

		String tabla = tipoSingular.equals("caja") ? "cajas" : "sobres";
		String idCol = tipoSingular.equals("caja") ? "idCaja" : "idSobre";
		
		try (Connection c = DriverManager.getConnection(URL_DB)) {
		
			try (PreparedStatement u = c.prepareStatement(
				"UPDATE " + tabla + " SET precio=? WHERE " + idCol + "=?")) {
				u.setDouble(1, precio);
				u.setInt   (2, idElemento);
				u.executeUpdate();
			}
			
			Map<Double, Double> map = new HashMap<>();
			map.put(11.0, precio);
			
			try (PreparedStatement p = c.prepareStatement(
			"INSERT OR IGNORE INTO precios_historicos " +
			"(referencia, fecha, historico) VALUES (?,?,?)")) {
			p.setString(1, referencia);           
			p.setString(2, fecha.toString());
			p.setString(3, gson.toJson(map));
			p.executeUpdate();
		}
		
		} catch (Exception e) {
			System.err.println("Err precio " + referencia + ": " + e.getMessage());
		}
	}//private static void guardarPrecioProducto(String referencia, String tipoSingular,int idElemento, double precio, LocalDate fecha)

		//l
		private static List<String[]> obtenerElementos(String sql) {
	    	/**
	         *29/04/2025
	         *Programador Javi 
	         *v1.0 ista los elementos cerrados
	         *@param sql
	         * salida lista
	         */
		    List<String[]> lista = new ArrayList<>();
		    try (Connection c = DriverManager.getConnection(URL_DB);
		         Statement  s = c.createStatement();
		         ResultSet  r = s.executeQuery(sql)) {

		        while (r.next()) {
		            lista.add(new String[]{
		                r.getString(1), // id
		                r.getString(2), // nombre
		                r.getString(3)  // url_pricecharting
		            });
		        }
		    } catch (Exception e) {
		        System.err.println("Err obtenerElementos: " + e.getMessage());
		    }
		    return lista;
		}//private static List<String[]> obtenerElementos(String sql) 


}
