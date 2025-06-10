package controladores;

import java.io.IOException;
import java.util.List;

import daos.CajaDAO;
import daos.SobreCajaUsuarioDAO;
import daos.SobreDAO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import modelos.Caja;
import modelos.Sobre;
import modelos.SobreCajaUsuario;
import utilidades.CambiaEscenas;
import utilidades.PantallaAnterior;
import utilidades.UsuarioLogeado;

public class SobresCajasController {
	
	
	//navegacion basico
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

    @FXML
    public void initialize() {                       
        cargarItemsDelUsuario();
    }
    
    @FXML private GridPane contenedorCajas;


    
    private void cargarItemsDelUsuario() {
  	  /**
         * 28/05/2025
         * Programador: Javi
         * v1.0 Carga los items cerrados correspondientes al usuario en 
         *  
         * Salida: void 
         */
    	
        Task<Void> task = new Task<>() {
            int columna = 0;
            int fila = 0;
            @Override
            protected Void call() { //llama a un hilo separado para no bloquear la interfaz mientras carga los productos cerrados 
                int idUser = UsuarioLogeado.get();
                List<SobreCajaUsuario> items = new SobreCajaUsuarioDAO().obtenerPorUsuario(idUser);

                for (SobreCajaUsuario sc : items) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/tarjetaCarta.fxml")); //carga las tarjetas de sobres y cajas 
                        AnchorPane tarjeta = loader.load();

                        //obtencion de datos para las tarjetas
                        double precioActual = 0;
                        if ("caja".equals(sc.getTipo())) {
                            Caja c = new CajaDAO().obtenerPorId(sc.getIdElemento());
                            if (c != null) precioActual = (c.getPrecio() != null) ? c.getPrecio() : 0;
                            loader.<TarjetaCartaController>getController().inicializarDesdeCaja(c);
                        } else {
                            Sobre s = new SobreDAO().obtenerPorId(sc.getIdElemento());
                            if (s != null) precioActual = (s.getPrecio() != null) ? s.getPrecio() : 0;
                            loader.<TarjetaCartaController>getController().inicializarDesdeSobre(s);
                        }

                        Label lblPrecio = (Label) tarjeta.lookup("#precioCarta");
                        lblPrecio.setText(String.format("Precio: %.2f €", precioActual));
                        
                        //comportamiento en clic si es caja abre de una manera si no abre como sobre 
                        tarjeta.setOnMouseClicked(ev -> {
                        	PantallaAnterior.setAnterior("sobrescajas.fxml");
                            if ("caja".equals(sc.getTipo())) {
                                SobreCajaController.vistaCaja(sc.getIdElemento(), sc.getNombre());
                            } else {
                                SobreCajaController.vistaSobre(sc.getIdElemento(), sc.getNombre());
                            }
                            CambiaEscenas.cambiarEscena("cajassobre.fxml");
                        });

                         int fCol = columna;
                         int fFila = fila;

                        Platform.runLater(() -> contenedorCajas.add(tarjeta, fCol, fFila));

                        columna++;
                        if (columna == 4) {
                            columna = 0;
                            fila++;
                        }


                    } catch (IOException ex) {
                        System.err.println("Error cargando tarjeta: " + ex.getMessage());
                    }

                }
                return null;
            }
        };
        new Thread(task).start();     
    }//private void cargarItemsDelUsuario() 
    
    @FXML
    private void volverPantallaAnterior(MouseEvent e) {
        String origen = PantallaAnterior.getAnterior();
        if (origen != null && !origen.isEmpty()) {
            CambiaEscenas.cambiarEscena(origen);
        } else {
            CambiaEscenas.cambiarEscena("pantallaColecciones.fxml");
        }
    }


}
