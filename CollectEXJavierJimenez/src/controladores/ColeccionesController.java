package controladores;

import daos.ColeccionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import modelos.Coleccion;
import utilidades.CambiaEscenas;
import utilidades.UsuarioLogeado;


import java.util.List;

public class ColeccionesController {
	
	@FXML private GridPane contenedorColecciones;
 
    private final ColeccionDAO dao = new ColeccionDAO();
    private final int idUsuarioActual = UsuarioLogeado.get();
 
    @FXML 
    private void irAColecciones(MouseEvent e){ 
    	CambiaEscenas.cambiarEscena("pantallaColecciones.fxml"); 
    }
    
    @FXML  
   private void irASobres(MouseEvent e){
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
    private void initialize() {
    	cargarColecciones(); 
    }
    

 
    private void cargarColecciones() {
    	  /**
    	  * 14/05/2025
    	  * Programador: Javi
    	  * v1.0 Carga colecciones del usuario y las pone en el scrolleable
    	  * Salida: void 
    	  * 
    	  */
    	
        contenedorColecciones.getChildren().clear();

        int idUsuario = UsuarioLogeado.get();
        List<Coleccion> colecciones = dao.obtenerPorUsuario(idUsuario);

        int col = 0;
        int row = 0;

        for (Coleccion c : colecciones) {
            try {
                FXMLLoader fx = new FXMLLoader(getClass().getResource("/vistas/coleccion.fxml"));
                AnchorPane panel = fx.load();

                TarjetaColeccionController tc = fx.getController();
                tc.inicializarDatos(c);

                panel.setOnMouseClicked(e -> {
                    InteriorColeccionController.setColeccionSeleccionada(c);
                    CambiaEscenas.cambiarEscena("interiorColeccion.fxml");
                });

                contenedorColecciones.add(panel, col, row);

                col++;
                if (col == 2) {          
                    col = 0;
                    row++;
                }

            } catch (Exception ex) {
                System.err.println("Error cargando tarjeta: " + ex.getMessage());
            }
        }
    }//private void cargarColecciones()
    


    @FXML
    private void nuevaColeccion() {
        /**
         * 19/05/2025
         * Programador: Javi
         * v1.0 Crea una ventana de dialogo para crear una coleccion 
         * salida void 
         */
        TextInputDialog emergente = new TextInputDialog();
        emergente.setTitle("Nueva colección");
        emergente.setHeaderText(null);
        emergente.setContentText("Nombre de la colección:");

        emergente.showAndWait().ifPresent(nombre -> {
            if (!nombre.isBlank()) {
                Coleccion c = new Coleccion(0, nombre.trim(), idUsuarioActual);
                if (dao.insertarColeccion(c)) cargarColecciones();
            }
        });
    }//private void nuevaColeccion() 
}
