package controladores;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import utilidades.CambiaEscenas;
import utilidades.RegistroTemporal;
import daos.UsuarioDAO;

public class RegistroController {

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private TextField txtCorreo;

    @FXML
    private Label txtNoValido;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void initialize() {
      txtCorreo.setOnAction(e -> irASiguientePaso(null));
      txtNombreUsuario.setOnAction(e -> irASiguientePaso(null));
    }
    
    @FXML
    private void irASiguientePaso(ActionEvent event) {
        
        /**
         * 17/05/2025
         * Programador Javi
         * v1.0 Validaque no esten vacios los campos y valida que no existe 
         * @param event
         */
    	
        String nombre = txtNombreUsuario.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            mostrarError("Nombre y correo requeridos");
            return;
        }

        // valida nombre de usuario
        if (usuarioDAO.obtenerPorNombre(nombre) != null) {
            mostrarError("El nombre de usuario ya está en uso");
            return;
        }

        // validar correo
        if (usuarioDAO.obtenerPorCorreo(correo) != null) {
            mostrarError("El correo electrónico ya está registrado");
            return;
        }

        //guarda temporalmente
        RegistroTemporal.nombre = nombre;
        RegistroTemporal.correo = correo;

        CambiaEscenas.cambiarEscena("registroSegundo.fxml");
    }// private void irASiguientePaso(ActionEvent event)
    


    private void mostrarError(String mensaje) {
        /**
         * 17/05/2025
         * Programador: Javi
         * v1.0 Si las contraseñas no coinciden si no  lo hacen lanza el mensaje de error
         * 
         * @param mensaje
         * Salida: void 
         */
    	
        txtNoValido.setText(mensaje);
        txtNoValido.setVisible(true);

        PauseTransition pausa = new PauseTransition(Duration.seconds(4));
        pausa.setOnFinished(e -> txtNoValido.setVisible(false));
        pausa.play();
    }//private void mostrarError(String mensaje)
}
