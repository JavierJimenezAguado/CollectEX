 package controladores;

import daos.CartaDAO;
import daos.WishlistDAO;
import daos.WishlistItemDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import modelos.Carta;
import modelos.Wishlist;
import modelos.WishlistItem;
import utilidades.CambiaEscenas;
import utilidades.UsuarioLogeado;

public class WishlistController {
	
	//navegacion basica
	@FXML private GridPane contenedorWishlist;
	
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
	    private void initialize() {
	    	/**18/05/2025
	    	 * Programador Javi
	    	 * v1.0 Inicializa la wishlist del usuario con sesion activa
	    	 * 
	    	 * Salida: void
	    	 */

	        int idUsuario = UsuarioLogeado.get();
	        WishlistDAO wlDao   = new WishlistDAO();
	        WishlistItemDAO itDao = new WishlistItemDAO();
	        CartaDAO cartaDao  = new CartaDAO();

	        Wishlist wl = wlDao.obtenerPorUsuario(idUsuario);
	        if (wl == null) {
	            System.err.println("El usuario aún no tiene wishlist.");
	            return;
	        }

	        contenedorWishlist.getChildren().clear();
	        int col = 0, row = 0;

	        for (WishlistItem it : itDao.obtenerPorWishlist(wl.getIdWishlist())) {

	            Carta carta = cartaDao.obtenerPorReferencia(it.getReferencia());
	            if (carta == null) continue;

	            try {
	                FXMLLoader fx = new FXMLLoader(getClass().getResource("/vistas/tarjetaCarta.fxml"));
	                AnchorPane nodo = fx.load();

	                TarjetaCartaController ctrl = fx.getController();
	                ctrl.datosCarta(carta);
	                ctrl.setPantallaOrigen("wishList.fxml");

	                contenedorWishlist.add(nodo, col, row);

	                if (++col == 4) {      // 4 columnas por fila
	                    col = 0;
	                    row++;
	                }

	            } catch (Exception ex) {
	                System.err.println("Error cargando carta: " + ex.getMessage());
	            }
	        }
	    }
	}
