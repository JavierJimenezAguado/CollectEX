package controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelos.Carta;
import modelos.CartaColeccion;
import modelos.Coleccion;

import java.util.List;

import daos.CartaColeccionDAO;
import daos.CartaDAO;

public class TarjetaColeccionController {

    @FXML private Label nombreColeccion;
    @FXML private ImageView imagenColeccion;
    @FXML private Label carta1, carta2, carta3, carta4;
    @FXML private Label numCartas, cartasGradeadas, precioPagado, valorEstimado;

    private Coleccion coleccion;  
    
    //inicializa datos de coleccion 
    public void inicializarDatos(Coleccion c){
 	   /**
      * 18/05/2025
      * Programador: Javi
      * v1.0 inicializa la tarjeta decoleccuones 
      * @param c
      * Salida void
      * 
      * 09/06/2025
      * Programador Javi 
      * v2.0
      * añadido inicializado de labels de tarjeta
      */
    	 this.coleccion = c;
    	    nombreColeccion.setText(coleccion.getNombre());
    	    imagenColeccion.setImage(new Image(getClass().getResource("/imagenes/generic_set.png").toString()));

    	    CartaColeccionDAO daoCC = new CartaColeccionDAO();
    	    CartaDAO daoCarta = new CartaDAO();
    	    List<CartaColeccion> cartasColeccion = daoCC.listarPorColeccion(coleccion.getIdColeccion());

    	    // Totales
    	    int totalCartas = 0;
    	    int totalGradeadas = 0;
    	    double totalPagado = 0.0;
    	    double totalEstimado = 0.0;

    	    // Muestra máximo 4 cartas destacadas
    	    for (int i = 0; i < 4; i++) {
    	        Label labelCarta = switch (i) {
    	            case 0 -> carta1;
    	            case 1 -> carta2;
    	            case 2 -> carta3;
    	            case 3 -> carta4;
    	            default -> null;
    	        };

    	        if (i < cartasColeccion.size()) {
    	            CartaColeccion cc = cartasColeccion.get(i);
    	            Carta carta = daoCarta.obtenerPorReferencia(cc.getReferencia());

    	            if (labelCarta != null && carta != null) {
    	                labelCarta.setText(carta.getNombre() + " (" + carta.getPrecio() + "€)");
    	            }

    	            totalCartas += cc.getCantidad();
    	            if (cc.getGraduacion() > 0) totalGradeadas++;
    	            totalPagado += cc.getPrecioPagado();
    	            totalEstimado += carta != null ? carta.getPrecio() * cc.getCantidad() : 0.0;

    	        } else if (labelCarta != null) {
    	            labelCarta.setText("Carta " + (i + 1));
    	        }
    	    }

    	    numCartas.setText("Cartas: " + totalCartas);
    	    cartasGradeadas.setText("Gradeadas: " + totalGradeadas);
    	    precioPagado.setText("Pagado: " + String.format("%.2f€", totalPagado));
    	    valorEstimado.setText("Estimado: " + String.format("%.2f€", totalEstimado));
    	
    }//public void inicializarDatos(Coleccion c)

}
