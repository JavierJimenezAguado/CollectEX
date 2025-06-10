package controladores;

import daos.SobreCajaUsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import modelos.SobreCajaUsuario;
import utilidades.CambiaEscenas;
import utilidades.PantallaAnterior;
import utilidades.UsuarioLogeado;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import com.google.gson.Gson;

public class SobreCajaController {
	
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
	       
	    }

	    @FXML
	    private void irAAjustes(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("ajustes.fxml");
	    }
	    

	    @FXML
	    private void volverPantallaAnterior(MouseEvent event) {
	        String origen = PantallaAnterior.getAnterior();
	        if (origen != null && !origen.isEmpty()) {
	            CambiaEscenas.cambiarEscena(origen);
	        } else {
	            CambiaEscenas.cambiarEscena("pantallaColecciones.fxml");
	        }
	    }

    private static int selId;
    private static String selNombre;
    private static String selTipo;    

    public static void vistaCaja(int idCaja, String nombreCaja) {
        selId = idCaja;  selNombre = nombreCaja;  selTipo = "caja";

    }

    public static void vistaSobre(int idSobre, String nombreSobre) {
        selId = idSobre; selNombre = nombreSobre; selTipo = "sobre";
       
    }

    @FXML private Label      nombreElemento;
    @FXML private Label      tipoElemento;
    @FXML private BorderPane imagenElemento;
    @FXML private Button     btnAnadir;
    @FXML private LineChart<String,Number> graficoPrecios;
    @FXML private CheckBox ckMin;
    @FXML private CheckBox ckMax;
    @FXML private TextField lblalertaMin;
    @FXML private TextField lblalertaMax;
    @FXML private TextField lblprecioPagado;

    @FXML
    public void initialize() {
        nombreElemento.setText(selNombre);
        tipoElemento.setText("Tipo: " + selTipo);
        

        File img = new File("src/imagenes_cartas/"
                + selNombre.toLowerCase().replaceAll("[^a-z0-9]", "") + ".jpg");
        if (img.exists()) {
            ImageView iv = new ImageView(img.toURI().toString());
            iv.setPreserveRatio(true);
            iv.setFitWidth(475);
            iv.setFitHeight(300); 
            imagenElemento.setCenter(iv);
        }

        btnAnadir.setOnAction(e -> guardarItemUsuario());
        
        cargarGrafica();
    }
    


    @FXML
    private void guardarItemUsuario() {
    	  /**
         * 29/05/2025
         * Programador: Javi
         * v1.0 guarda el item cerrado en la base de datos y lee las alertas 
         *  
         *  
         * Salida: void 
         */
        int idUsuario = UsuarioLogeado.get();

        //lee precio si esta vacio se queda a 0 si no parsea
        double precioPagado = 0.0;
        String textoPrecio = lblprecioPagado.getText().trim();
        if (!textoPrecio.isBlank()) {
            try {
                precioPagado = Double.parseDouble(textoPrecio.replace(",", "."));
            } catch (NumberFormatException ex) {
                // Si no es un número válido, seguimos con precioPagado = 0.0
                System.err.println("Precio pagado inválido; se usa 0.0");
            }
        }

        //lee si alerta min esta seleccionada
        Double alertaMin = null;
        if (ckMin.isSelected()) {
            String txtMin = lblalertaMin.getText().trim();
            if (!txtMin.isBlank()) {
                try {
                    alertaMin = Double.parseDouble(txtMin.replace(",", "."));
                } catch (NumberFormatException ex) {
                	   //si no es numero valido se fueraza el null/0
                    System.err.println("Alerta mínima inválida; se ignora");
                }
            }
        }

        //lee si alerta max esta seleccionada 
        Double alertaMax = null;
        if (ckMax.isSelected()) {
            String txtMax = lblalertaMax.getText().trim();
            if (!txtMax.isBlank()) {
                try {
                    alertaMax = Double.parseDouble(txtMax.replace(",", "."));
                } catch (NumberFormatException ex) {
                    //si no es numero valido se fueraza el null/0
                    System.err.println("Alerta máxima inválida; se ignora");
                }
            }
        }

        //crea nuevo objeto
        SobreCajaUsuario sc = new SobreCajaUsuario(
            0,              
            idUsuario,
            selId,
            selNombre,
            selTipo,
            precioPagado,
            alertaMin,
            alertaMax
        );

        //inserta en base de datos 
        boolean ok = new SobreCajaUsuarioDAO().insertar(sc);
        if (ok) {
            CambiaEscenas.cambiarEscena("sobrescajas.fxml");
        } else {
            System.err.println("No se pudo insertar en la base de datos");
        }
    }//   private void guardarItemUsuario()
 
    //carga la grafica del item cerrado
    private void cargarGrafica() {
    	  /**
         * 28/05/2025
         * Programador: Javi
         * v1.0 Carda a grfica de los productos cerrados 
         *  
         * Salida: void 
         */
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:collectex.db");
             PreparedStatement st = cn.prepareStatement(
                     "SELECT fecha,historico FROM precios_historicos " +
                     "WHERE referencia=? ORDER BY fecha")) {

            st.setString(1, selTipo + "-" + selId);
            ResultSet rs = st.executeQuery();

            XYChart.Series<String,Number> serie = new XYChart.Series<>();

            Gson gson = new Gson();
            while (rs.next()) {
                String fecha = rs.getString("fecha");
                String json  = rs.getString("historico");

                // claves como String
                Map<String,Double> map = gson.fromJson(json, Map.class);

                Double precio = map.get("11.0"); 
                if (precio != null)
                    serie.getData().add(new XYChart.Data<>(fecha, precio));
            }

            graficoPrecios.getData().setAll(serie);

        } catch (Exception ex) {
            System.err.println("Gráfica: " + ex.getMessage());
        }
    }//private void cargarGrafica()

}
