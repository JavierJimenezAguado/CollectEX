package controladores;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import utilidades.CambiaEscenas;
import utilidades.EnviaCorreo;
import utilidades.HashUtil;
import utilidades.RegistroTemporal;
import modelos.Usuario;
import daos.UsuarioDAO;

public class RegistroDosController {

    @FXML private PasswordField txtContrasena;
    @FXML private PasswordField txtValidarContrasena;
    @FXML private Label txtNoValido;
    @FXML private HBox contenedorCarga;
    @FXML private ImageView imgCargando;

    public static String correoTemporal;
    public static String nombreTemporal;
    
    
    @FXML
    public void initialize() {
    	txtContrasena.setOnAction(e -> irALogin());
    	txtContrasena.setOnAction(e -> irALogin());
    	
        imgCargando.setImage(new Image("file:src/imagenes/cargando.gif"));
    }
    
    //si todo va bien mete el usuario en base de datos y va al login
    @FXML
    private void irALogin() {
        /**
         * 17/05/2025
         * Programador: Javi
         * v1.0 Introduce el nuevo usuario en base de datos, muestra animacion de carga y manda un correo de bienvenida 
         * 
         * Salida: void 
         */
        String pass1 = txtContrasena.getText();
        String pass2 = txtValidarContrasena.getText();
        String correoUsuario = RegistroTemporal.correo;
        String nombre = RegistroTemporal.nombre;

        if (pass1 == null || pass2 == null || !pass1.equals(pass2)) {
            mostrarErrorTemporal("Las contraseñas no coinciden");
            return;
        }
        
        contenedorCarga.setVisible(true);             // muestra spinner

        new Thread(() -> {
            boolean emailenviado = true;
            try {
                EnviaCorreo.enviarCorreoRegistro(correoUsuario, nombre);
            } catch (Exception ex) {
            	emailenviado = false;
            }

            boolean insertado = false;
            if (emailenviado) {
                String hash = HashUtil.hashSHA256(pass1);
                Usuario nuevo = new Usuario(0, nombre, correoUsuario, hash);
                insertado = new UsuarioDAO().insertarUsuario(nuevo);
            }

            boolean okFinal = emailenviado && insertado;

            Platform.runLater(() -> {
                contenedorCarga.setVisible(false);
                if (okFinal) {
                    CambiaEscenas.cambiarEscena("login.fxml");
                } else {
                    mostrarErrorTemporal("Error");
                }
            });
        }).start();
    
    }// private void irALogin() 
    



    private void mostrarErrorTemporal(String mensaje) {
        /**
         * 17/05/2025
         * Programador: Javi
         * v1.0 Si las contraseñas no coinciden si no  lo hacen lanza el mensaje de error
         *  @param mensaje
         *  
         * Salida: void 
         */
    	
        txtNoValido.setText(mensaje);
        txtNoValido.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(e -> txtNoValido.setVisible(false));
        pause.play();
    }// private void mostrarErrorTemporal(String mensaje)
}
