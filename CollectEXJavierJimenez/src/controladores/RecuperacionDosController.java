package controladores;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.util.Duration;
import utilidades.CambiaEscenas;
import utilidades.RecuperacionTemporal;


public class RecuperacionDosController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private Label txtNoValido;
    
    @FXML
    public void initialize() {

    	txtCodigo.setOnAction(e -> irAPasoTres(null));
  
    }
    
    @FXML
    private void irAPasoTres(ActionEvent event) {
  	   /**
         *  27/05/2025
         *  Programador: Javi
         *  v1.0 Valida que el codigo es el correcto 
         *  
         * @param event
         * Salida void
         */
    	
        String codigoIngresado = txtCodigo.getText();
        String codigoEsperado = RecuperacionTemporal.codigoRecuperacion;

        if (codigoIngresado != null && codigoIngresado.equals(codigoEsperado)) {
            CambiaEscenas.cambiarEscena("recuperacionTercero.fxml");
        } else {
            mostrarErrorTemporal("El código no es correcto");
        }
    }//private void irAPasoTres(ActionEvent event)
    
    
 
    private void mostrarErrorTemporal(String mensaje) {
        /**
         * 27/05/2025
         * Programador: Javi
         * v1.0 si falla el codigo muestra el mensaje durante 4 segundos 
         *  @param mensaje
         * 
         * Salida: void 
         */
        txtNoValido.setText(mensaje);
        txtNoValido.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(4));
        pausa.setOnFinished(e -> txtNoValido.setVisible(false));
        pausa.play();
    }//private void mostrarErrorTemporal(String mensaje)
}
