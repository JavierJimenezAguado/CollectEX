package controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modelos.CartaColeccion;
import modelos.Coleccion;
import utilidades.CambiaEscenas;
import utilidades.CambiaTemas;
import utilidades.ScraperService;
import java.io.Writer;
import java.io.Reader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import daos.CartaColeccionDAO;
import daos.ColeccionDAO;


public class AjustesController {
    private final ColeccionDAO coleccionDAO = new ColeccionDAO();
    private final CartaColeccionDAO ccDAO = new CartaColeccionDAO();
    private final int idUsuarioActual = utilidades.UsuarioLogeado.get();
	
    @FXML private HBox contenedorCarga;
    @FXML private ImageView imgCargando;
    @FXML private Label lblEstadoScraper; 
    @FXML private Label lblCerrarSesion; 
    @FXML private javafx.scene.layout.AnchorPane panelPrincipal;

	
	//navegacion entre secciones
	 @FXML
	    private void irAColecciones(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("pantallaColecciones.fxml");
	    }

	    @FXML
	    private void irASobres(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("sobrescajas.fxml");
	    }

	    @FXML
	    private void irAWishList(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("wishList.fxml");
	    }

	    @FXML
	    private void irABusqueda(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("busquedaCartas.fxml");
	    }
	    
	    @FXML
	    private void irAAjustes(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("ajustes.fxml");
	    }

	    
	    @FXML private Button btnModeloNormal, btnModeloVerde, btnModeloAzul, btnModeloNaranja;
	    @FXML private Button btnScraper;

	    @FXML
	
	    public void initialize() {
	        /**24/5/2025
		     * v2.0 Incializa en controlador y maneja los botones
		     * Programador: Javi
		     * Salida: void 
		     * 
		     * 7/6/2025
		     * Programador Javi
		     * v3.0 añadido boton de scraping
		     * 
		     */
	    	
	    	imgCargando.setImage(new Image("file:src/imagenes/cargandoScrapper.gif"));
	    	
	    	//cambios de escenas
	        btnModeloNormal .setOnAction(e ->
	            CambiaTemas.aplicar(btnModeloNormal.getScene(), CambiaTemas.Tema.MORADO));
	        btnModeloVerde  .setOnAction(e ->
	            CambiaTemas.aplicar(btnModeloNormal.getScene(), CambiaTemas.Tema.VERDE));
	        btnModeloAzul   .setOnAction(e ->
	            CambiaTemas.aplicar(btnModeloNormal.getScene(), CambiaTemas.Tema.AZUL));
	        btnModeloNaranja.setOnAction(e ->
	            CambiaTemas.aplicar(btnModeloNormal.getScene(), CambiaTemas.Tema.NARANJA));
	        
	        btnScraper.setOnAction(e -> ejecutarScrapper());
	    }// public void initialize()


	
	    @FXML
	    private void ejecutarScrapper() {
	        /**
		     * 04/06/2025
		     * Programador: Javi
		     * v1.0 Lanza el scraping de precios mostrando el gif de carga
		     * Salida: void 
		     * 
		     */
	    	 contenedorCarga.setVisible(true);
	         lblEstadoScraper.setVisible(true);

	        new Thread(() -> {
	            ScraperService.ejecutarScrapingCompleto();   

	            Platform.runLater(() -> contenedorCarga.setVisible(false));
	        }).start();
	    }
	    
	    @FXML
	    private void cerrarSesion(MouseEvent e){
	        CambiaEscenas.cambiarEscena("login.fxml");
	    }

	    
	    
	    @FXML
	    private void exportarColecciones(ActionEvent ev) {
	        /**
		     * 10/06/2025
		     * Programador: Javi
		     * v1.0 Exporta todas las colecciones del usuario
		     * @param ev
		     * Salida: void 
		     * 
		     */
	        FileChooser escoge = new FileChooser();
	        escoge.setTitle("Guardar exportación de colecciones");
	        escoge.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
	        Window win = panelPrincipal.getScene().getWindow();
	        File file = escoge.showSaveDialog(win);
	        if (file == null) return;

	        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
	            List<Coleccion> cols = coleccionDAO.obtenerPorUsuario(idUsuarioActual);
	            List<Map<String,Object>> exportData = new ArrayList<>();
	            for (Coleccion c : cols) {
	                Map<String,Object> m = new LinkedHashMap<>();
	                m.put("nombre", c.getNombre());
	                List<CartaColeccion> cartas = ccDAO.listarPorColeccion(c.getIdColeccion());
	                List<Map<String,Object>> cartasData = new ArrayList<>();
	                for (CartaColeccion cc : cartas) {
	                    Map<String,Object> cm = new LinkedHashMap<>();
	                    cm.put("referencia", cc.getReferencia());
	                    cm.put("cantidad",   cc.getCantidad());
	                    cm.put("graduacion", cc.getGraduacion());
	                    cm.put("precioPagado", cc.getPrecioPagado());
	                    cm.put("alertaMin",  cc.getAlertaMin());
	                    cm.put("alertaMax",  cc.getAlertaMax());
	                    cartasData.add(cm);
	                }
	                m.put("cartas", cartasData);
	                exportData.add(m);
	            }
	            Gson gson = new GsonBuilder().setPrettyPrinting().create();
	            Type tipo = new TypeToken<List<Map<String,Object>>>(){}.getType();
	            gson.toJson(exportData, tipo, w);

	            new Alert(Alert.AlertType.INFORMATION,"Se han exportado " + cols.size() + " colecciones.").show();
	        } catch (IOException ex) {
	            new Alert(Alert.AlertType.ERROR,
	                      "Error al exportar: " + ex.getMessage()).show();
	        }
	    }
	    
	    
	    
	    @FXML
	    private void importarColecciones(ActionEvent ev) {
	        /**
		     * 10/06/2025
		     * Programador: Javi
		     * v1.0 Importa un json con colecciones y las crea en su apartado de colecciones
		     * @param ev
		     * Salida: void 
		     * 
		     */
	        FileChooser chooser = new FileChooser();
	        chooser.setTitle("Selecciona archivo JSON de colecciones");
	        chooser.getExtensionFilters().add(
	            new FileChooser.ExtensionFilter("JSON", "*.json")
	        );
	        Window win = panelPrincipal.getScene().getWindow();
	        File file = chooser.showOpenDialog(win);
	        if (file == null) return;

	        try (Reader r = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
	            Gson gson = new Gson();
	            JsonElement raiz = JsonParser.parseReader(r);

	            List<Map<String,Object>> importData = new ArrayList<>();

	            if (raiz.isJsonArray()) {
	                // Si es un array se convierte derenctamente
	                Type tipoLista = new TypeToken<List<Map<String,Object>>>(){}.getType();
	                importData = gson.fromJson(raiz, tipoLista);

	            } else if (raiz.isJsonObject()) {
	                // si es un objeto lo hacemos una lista
	                Type tipoMapa = new TypeToken<Map<String,Object>>(){}.getType();
	                Map<String,Object> single = gson.fromJson(raiz, tipoMapa);
	                importData.add(single);

	            } else {
	                new Alert(Alert.AlertType.ERROR,
	                    "Formato JSON no reconocido, debe ser un objeto o un array.")
	                  .show();
	                return;
	            }

	            //import data siempre de una lista
	            int creadas = 0;
	            for (Map<String,Object> cm : importData) {
	                String nombre = (String)cm.get("nombre");
	                Coleccion c = new Coleccion(0, nombre, idUsuarioActual);
	                if (coleccionDAO.insertarColeccion(c)) {
	                    // creamos el id se la nueva coleccion 
	                    List<Coleccion> ult = coleccionDAO.obtenerPorUsuario(idUsuarioActual);
	                    int nuevoId = ult.get(ult.size() - 1).getIdColeccion();

	                    @SuppressWarnings("unchecked")
	                    List<Map<String,Object>> cartas =
	                        (List<Map<String,Object>>)cm.get("cartas");

	                    for (Map<String,Object> cardData : cartas) {
	                        CartaColeccion cc = new CartaColeccion(
	                            nuevoId,
	                            (String)   cardData.get("referencia"),
	                            ((Number)  cardData.get("cantidad")).intValue(),
	                            ((Number)  cardData.get("graduacion")).intValue(),
	                            ((Number)  cardData.get("precioPagado")).doubleValue(),
	                            cardData.get("alertaMin") == null ? null
	                              : ((Number)cardData.get("alertaMin")).doubleValue(),
	                            cardData.get("alertaMax") == null ? null
	                              : ((Number)cardData.get("alertaMax")).doubleValue()
	                        );
	                        ccDAO.guardar(cc);
	                    }
	                    creadas++;
	                }
	            }

	            new Alert(Alert.AlertType.INFORMATION,
	                      "Importadas " + creadas + " colecciones.")
	              .show();
	        } catch (IOException ex) {
	            new Alert(Alert.AlertType.ERROR,
	                      "Error al importar: " + ex.getMessage())
	              .show();
	        }
	    }


}
