package controladores;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.Writer;
import java.io.OutputStreamWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import basededatos.Singleton;
import daos.CartaColeccionDAO;
import daos.CartaDAO;
import daos.ColeccionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modelos.Carta;
import modelos.CartaColeccion;
import modelos.Coleccion;
import utilidades.CambiaEscenas;
import utilidades.UsuarioLogeado;

public class InteriorColeccionController {

    private static Coleccion coleccionSeleccionada;
    @FXML private LineChart<String, Number> graficoColeccion;

    @FXML private AnchorPane panelPrincipal; 
    private final ColeccionDAO coleccionDAO = new ColeccionDAO();
    private final CartaColeccionDAO ccDAO  = new CartaColeccionDAO();
    private final int idUsuarioActual = UsuarioLogeado.get();

    
   
    
    @FXML
    private void irASobres (MouseEvent e){ 
    	CambiaEscenas.cambiarEscena("sobrescajas.fxml");
    }
    
    @FXML
    private void irAWishList(MouseEvent e){
    	CambiaEscenas.cambiarEscena("wishList.fxml");           
    }
    
    @FXML 
    private void irABusqueda(MouseEvent e){ 
    	CambiaEscenas.cambiarEscena("busquedaCartas.fxml");     
    }
    @FXML
    private void irAAjustes(MouseEvent e){
    	CambiaEscenas.cambiarEscena("ajustes.fxml");   
    }
    
    @FXML
    private void irAColecciones(MouseEvent e){ 
    	CambiaEscenas.cambiarEscena("pantallaColecciones.fxml");
    }

    public static void setColeccionSeleccionada(Coleccion c) {
        coleccionSeleccionada = c;
    }

    @FXML private GridPane contenedorCartas;
    

    @FXML
    private void initialize() {
        /**
         * 26/05/2025
         * Programador: Javi
         * v1.0 Inicializa el controlador, pinta la grafica de la coleccion 
         * 		y pone todas las cartas de la coleccion. 
         * 
         * Salida: void 
         */
    	
    	pintarGraficoColeccion();
    	
        if (coleccionSeleccionada == null) return;

        contenedorCartas.getChildren().clear();

        CartaColeccionDAO dao = new CartaColeccionDAO();
        CartaDAO cartaDAO = new CartaDAO();

        List<CartaColeccion> cartas = dao.listarPorColeccion(coleccionSeleccionada.getIdColeccion());
        System.out.println("Cartas encontradas para coleccion " + coleccionSeleccionada.getIdColeccion() + ":");

        int col = 0;
        int row = 0;

        for (CartaColeccion cc : cartas) {
            Carta carta = cartaDAO.obtenerPorReferencia(cc.getReferencia());
            if (carta != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/tarjetaCarta.fxml"));
                    AnchorPane cartaPane = loader.load();
                    
                    TarjetaCartaController controller = loader.getController();
                    controller.inicializarDatos(carta, cc); 
                    controller.setPantallaOrigen("interiorColeccion.fxml");


                    contenedorCartas.add(cartaPane, col, row);

                    col++;
                    if (col == 4) {
                        col = 0;
                        row++;
                    }

                } catch (Exception e) {
                    System.err.println("Error al cargar tarjeta de carta: " + e.getMessage());
                }
            }
        }
    }//private void initialize()

    
    private void pintarGraficoColeccion() {
        /**
         * 26/05/2025
         * Programador: Javi
         * v1.0 Pinta la grafica de la coleccion, segun el usuario
         * 		y el id de la coleccion.
         * 
         * Salida: void 
         */
        CartaColeccionDAO dao = new CartaColeccionDAO();
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Double>>(){}.getType();

        Map<String, Double> totalPorFecha = new LinkedHashMap<>();

        for (CartaColeccion cc : dao.listarPorColeccion(coleccionSeleccionada.getIdColeccion())) {
            String ref = cc.getReferencia();
            int grado = cc.getGraduacion();
            String claveGrado = (grado == 0) ? "11.0" : (grado == 95 ? "9.5" : String.format("%d.0", grado));

            try (Connection con = Singleton.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(
                     "SELECT fecha, historico FROM precios_historicos WHERE referencia = ? ORDER BY fecha")) {

                ps.setString(1, ref);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String fecha = rs.getString("fecha");
                    Map<String, Double> precios = gson.fromJson(rs.getString("historico"), type);

                    if (precios.containsKey(claveGrado)) {
                        double precio = precios.get(claveGrado) * cc.getCantidad(); 
                        totalPorFecha.merge(fecha, precio, Double::sum);

                        System.out.println("[" + fecha + "] " + ref + " x" + cc.getCantidad() + 
                            " (" + claveGrado + ") = +" + precio + "€");
                    }
                }

            } catch (Exception e) {
                System.err.println("Error al leer historico para " + ref + ": " + e.getMessage());
            }
        }
        
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Valor total");

        totalPorFecha.forEach((fecha, valor) -> {
            XYChart.Data<String, Number> datos = new XYChart.Data<>(fecha, valor);
            serie.getData().add(datos);

            Tooltip tooltip = new Tooltip("Fecha: " + fecha + "\nValor: " + String.format("%.2f €", valor));

            datos.nodeProperty().addListener((obs, viejonodo, nuevonodo) -> {
                if (nuevonodo != null) {
                    Tooltip.install(nuevonodo, tooltip);
                }
            });
        });
        
        graficoColeccion.getData().setAll(serie);

    }// private void pintarGraficoColeccion()
    
    
    @FXML
    private void exportarColeccionActual() {
        /**
         * 10/06/2025
         * Programador: Javi
         * v1.0 exporta la coleccion en la que se encuentra el usuario
         * 
         * Salida: void 
         */
    	
        if (coleccionSeleccionada == null) {
            new Alert(Alert.AlertType.WARNING, "No hay ninguna colección seleccionada").show();
            return;
        }

        FileChooser escoge = new FileChooser();
        escoge.setTitle("Guardar exportación de la colección");
        escoge.getExtensionFilters()
               .add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        Window ventana = panelPrincipal.getScene().getWindow();
        File destino = escoge.showSaveDialog(ventana);
        if (destino == null) return;

        try (Writer w = new OutputStreamWriter(new FileOutputStream(destino),StandardCharsets.UTF_8)) {

            Map<String,Object> datos = new LinkedHashMap<>();
            datos.put("nombre", coleccionSeleccionada.getNombre());

            List<CartaColeccion> lista = ccDAO.listarPorColeccion(coleccionSeleccionada.getIdColeccion());
            List<Map<String,Object>> cartasData = new ArrayList<>();
            for (CartaColeccion cc : lista) {
                Map<String,Object> m = new LinkedHashMap<>();
                m.put("referencia", cc.getReferencia());
                m.put("cantidad", cc.getCantidad());
                m.put("graduacion", cc.getGraduacion());
                m.put("precioPagado", cc.getPrecioPagado());
                m.put("alertaMin", cc.getAlertaMin());
                m.put("alertaMax", cc.getAlertaMax());
                cartasData.add(m);
            }
            datos.put("cartas", cartasData);

            //serializa con Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type tipo = new TypeToken<Map<String,Object>>(){}.getType();
            gson.toJson(datos, tipo, w);

            new Alert(Alert.AlertType.INFORMATION,
                      "Colección '" + coleccionSeleccionada.getNombre()
                      + "' exportada correctamente").show();

        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR,
                      "Error al exportar: " + ex.getMessage()).show();
        }
    }//private void exportarColeccionActual()

    

    

} 
