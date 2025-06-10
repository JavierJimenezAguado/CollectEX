package controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import modelos.Caja;
import modelos.Carta;
import modelos.CartaColeccion;
import modelos.Sobre;
import modelos.SobreCajaUsuario;
import utilidades.CambiaEscenas;
import utilidades.PantallaAnterior;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;

public class TarjetaCartaController {

    @FXML private Label nombreCarta;
    @FXML private Label precioCarta;
    @FXML private ImageView imagenCarta;

    private Carta carta;
    private String pantallaOrigen;
    private int idColeccionOrigen = -1; 
    

    public void inicializarDatos(Carta carta, CartaColeccion cc) {
    	
        /**
         * 19/05/2025
         * Programador: Javi
         * v1.0 Incicializa los datos de la tarjeta
         * 
         * @param carta
         * @param cc
         * Salida: void 
         */
    	
        nombreCarta.setText(carta.getNombre());
        this.carta = carta;
        this.idColeccionOrigen = cc.getIdColeccion(); 
        
        double precio = -1;
        int grado = (cc.getGraduacion() == 0) ? 11 : cc.getGraduacion();  // 11 = sin gradear

        if (carta.getHistorialPrecios() != null && !carta.getHistorialPrecios().isEmpty()) {

            LocalDateTime ultimaFecha = carta.getHistorialPrecios().keySet()
                                             .stream()
                                             .max(LocalDateTime::compareTo)
                                             .orElse(null);
            //se busca el prexcio mas reciente 
            if (ultimaFecha != null) {
                Map<Integer, Double> preciosPorGrado = carta.getHistorialPrecios().get(ultimaFecha);
                if (preciosPorGrado != null && preciosPorGrado.containsKey(grado)) {
                    precio = preciosPorGrado.get(grado);
                }
            }
        }

        // estiqueda de precio si esta gradeada o no 
        if (precio >= 0) {
            if (cc.getGraduacion() > 0) {
                String notaStr = (cc.getGraduacion() == 95) ? "9.5" : String.valueOf(cc.getGraduacion());
                precioCarta.setText(String.format("Precio: %.2f € (PSA %s)", precio, notaStr));
            } else {
                precioCarta.setText(String.format("Precio: %.2f €", precio));
            }
        } else {
            precioCarta.setText("Precio: sin datos");
        }

        File img = new File("src/imagenes_cartas/" + carta.getReferencia() + ".jpg");
        if (img.exists()) {
            imagenCarta.setImage(new Image(img.toURI().toString()));
        }
    }//public void inicializarDatos(Carta carta, CartaColeccion cc) 
    
    
    
    @FXML
    private void click(MouseEvent e) {
    	
        /**
         * 19/05/2025
         * Programador Javi
         * v1.0 comportamiento en el click de la tarjeta dela carta
         * @param e
         * Salida void
         */
        if (e.getButton() == MouseButton.PRIMARY && carta != null) {      
            PantallaAnterior.setAnterior(pantallaOrigen);
            CartaController.setColeccionOrigen(-1);
            CartaController.setReferenciaCarta(carta.getReferencia());
            CartaController.setColeccionOrigen(idColeccionOrigen);        // id válido (>0) si venimos de una colección
            CambiaEscenas.cambiarEscena("interiorCartas.fxml");
        }
    }//private void handleClick(MouseEvent e)
    
    /**
     * 19/05/2025
     * Programador: Javi
     * v1.0 inicializa la tarjeta con los datos de carta
     * @param carta
     * Salida void
     */
    
    //inicializa la tarjeta a partir de datos carta
    public void datosCarta(Carta carta) {
        this.carta = carta;
        nombreCarta.setText(carta.getNombre());

        if (carta.getPrecio() > 0) {
            precioCarta.setText(String.format("Precio: %.2f €", carta.getPrecio()));
        } else {
            precioCarta.setText("Precio: sin datos");
        }

        File img = new File("src/imagenes_cartas/" + carta.getReferencia() + ".jpg");
        if (img.exists()) {
            imagenCarta.setImage(new Image(img.toURI().toString()));
        }
    }// public void setDatosCarta(Carta carta)
    
    
    public void inicializarDesdeCaja(Caja caja) {
    	   /**
         * 31/05/2025
         * Programador: Javi
         * v1.0 inicializa la tarjeta con los datos de caja
         * @param caja
         * Salida void
         */
        nombreCarta.setText(caja.getNombre());

        if (caja.getPrecio() != null && caja.getPrecio() > 0)
            precioCarta.setText(String.format("Precio: %.2f €", caja.getPrecio()));
        else
            precioCarta.setText("Precio: sin datos");

        File img = new File("src/imagenes_cartas/"
                + caja.getNombre().toLowerCase().replaceAll("[^a-z0-9]","") + ".jpg");
        if (img.exists()) imagenCarta.setImage(new Image(img.toURI().toString()));

        imagenCarta.setOnMouseClicked(e ->
            { SobreCajaController.vistaCaja(caja.getIdCaja(), caja.getNombre());
              CambiaEscenas.cambiarEscena("cajassobre.fxml"); });
    }//public void inicializarDesdeCaja(Caja caja) 

 
    public void inicializarDesdeSobre(Sobre sobre) {
    	   /**
         * 31/05/2025
         * Programador: Javi
         * v1.0 inicializa la tarjeta con los datos de sobre
         * @param sobre
         * Salida void
         */
        nombreCarta.setText(sobre.getNombre());

        if (sobre.getPrecio() != null && sobre.getPrecio() > 0)
            precioCarta.setText(String.format("Precio: %.2f €", sobre.getPrecio()));
        else
            precioCarta.setText("Precio: sin datos");

        File img = new File("src/imagenes_cartas/"
                + sobre.getNombre().toLowerCase().replaceAll("[^a-z0-9]","") + ".jpg");
        if (img.exists()) imagenCarta.setImage(new Image(img.toURI().toString()));

        imagenCarta.setOnMouseClicked(e ->
            { SobreCajaController.vistaSobre(sobre.getIdSobre(), sobre.getNombre());
              CambiaEscenas.cambiarEscena("cajassobre.fxml"); });
    }//public void inicializarDesdeSobre(Sobre sobre)
    
    
 
    public void inicializarDesdeUsuarioItem(SobreCajaUsuario sc) {
 	   /**
      * 31/05/2025
      * Programador: Javi
      * v1.0 inicializa la tarjeta con los datos de sobreUsuario
      * @param sc
      * Salida void
      */
    	
        nombreCarta.setText(sc.getNombre());

        if (sc.getPrecio() > 0)
            precioCarta.setText(String.format("Pagado: %.2f €", sc.getPrecio()));
        else
            precioCarta.setText("Pagado: –");

        File img = new File("src/imagenes_cartas/"
                + sc.getNombre().toLowerCase().replaceAll("[^a-z0-9]", "") + ".jpg");
        if (img.exists()) imagenCarta.setImage(new Image(img.toURI().toString()));
    }//public void inicializarDesdeUsuarioItem(SobreCajaUsuario sc)


    public void setPantallaOrigen(String origen) {
        this.pantallaOrigen = origen;
    }


    
}
