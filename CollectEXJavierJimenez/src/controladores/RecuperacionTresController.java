package controladores;

import daos.UsuarioDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.util.Duration;
import utilidades.CambiaEscenas;
import utilidades.HashUtil;
import utilidades.RecuperacionTemporal;

public class RecuperacionTresController {

    @FXML private PasswordField campoNueva;
    @FXML private PasswordField campoRepetida;
    @FXML private Label txtNoValido;
    
    @FXML
    public void initialize() {
 
    	campoNueva.setOnAction(e -> volverALogin());
    	campoRepetida.setOnAction(e -> volverALogin());
  
    }

    //si todos es correcto codifica la contraseña y 
    @FXML
    private void volverALogin() {
        /**
         * 28/05/2025
         * Programador: Javi
         * v1.0 Recoge la nueva contraseña la codifica y la actualiza 
         * 
         * Salida: void 
         */
        String nueva = campoNueva.getText();
        String repetida = campoRepetida.getText();

        if (nueva == null || repetida == null || !nueva.equals(repetida)) {
            mostrarErrorTemporal("Las contraseñas no coinciden");
            return;
        }

        String hash = HashUtil.hashSHA256(nueva);
        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.cambiarContrasena(RecuperacionTemporal.correo, hash);

        if (exito) {
            CambiaEscenas.cambiarEscena("login.fxml");
        } else {
            mostrarErrorTemporal("Error al cambiar contraseña");
        }
    }//private void volverALogin()

    //muestra el error 4 segundos si no coinciden las contraseñas 
    private void mostrarErrorTemporal(String mensaje) {
        /**
         * 28/05/2025
         * Programador: Javi
         * v1.0 Si las contraseñas no coinciden si no  lo hacen lanza el mensaje de error
         *  @param mensaje
         *  
         * Salida: void 
         */
        txtNoValido.setText(mensaje);
        txtNoValido.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(4));
        pausa.setOnFinished(e -> txtNoValido.setVisible(false));
        pausa.play();
    }// private void mostrarErrorTemporal(String mensaje)
}
