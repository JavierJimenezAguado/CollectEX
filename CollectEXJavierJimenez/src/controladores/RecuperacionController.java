package controladores;

import daos.UsuarioDAO;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import utilidades.CambiaEscenas;
import utilidades.EnviaCorreo;
import utilidades.RecuperacionTemporal;

public class RecuperacionController {

    @FXML private TextField txtCorreo;
    @FXML private Label txtNoValido;
    @FXML private HBox contenedorCarga;
    @FXML private ImageView imgCargando;
    
    @FXML
    public void initialize() {
        /**
         * 27/05/2025
         * Programador: Javi
         * v1.0 activa una animacion de carga  
         * 
         * Salida: void 
         */
    	txtCorreo.setOnAction(e -> continuarRecuperacion(null));
        imgCargando.setImage(new Image("file:src/imagenes/cargando.gif"));
    }

 

    @FXML
    private void continuarRecuperacion(ActionEvent event) {
    	   /**
         *  27/05/2025
         *  Programador: Javi
         *  v1.0 Valida que el correo exista y vincula a un usuario,pasa a la siguiente pantalla y envia un correo de recuperacion 
         *  
         * @param event
         * Salida void
         */
    	
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty() || new UsuarioDAO().obtenerPorCorreo(correo) == null) {
            mostrarErrorTemporal();
            return;
        }

        RecuperacionTemporal.correo = correo;
        RecuperacionTemporal.codigoRecuperacion = RecuperacionTemporal.generarCodigo();

        contenedorCarga.setVisible(true); 

        new Thread(() -> {
            EnviaCorreo.enviarCodigoRecuperacion(RecuperacionTemporal.correo, RecuperacionTemporal.codigoRecuperacion);

            Platform.runLater(() -> {
            	contenedorCarga.setVisible(false);
                CambiaEscenas.cambiarEscena("recuperacionSegundo.fxml");
            });
        }).start();
    }// private void handleContinuarRecuperacion(ActionEvent event) 


    private void mostrarErrorTemporal() {
        /**
         * 2/05/2025
         * Programador: Javi
         * v1.0 si falla el correo muestra el mensaje durante 4 segundos 
         * 
         * Salida: void 
         */
    	
        txtNoValido.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(4));
        pausa.setOnFinished(e -> txtNoValido.setVisible(false));
        pausa.play();
    }//private void mostrarErrorTemporal()
}
