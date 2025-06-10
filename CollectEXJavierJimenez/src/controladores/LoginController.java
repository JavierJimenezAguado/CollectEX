package controladores;

import java.util.prefs.Preferences;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import modelos.Usuario;
import utilidades.CambiaEscenas;
import utilidades.HashUtil;
import utilidades.UsuarioLogeado;

public class LoginController {
	

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblError;
    @FXML private Pane logoLogin;

    private final GestorUsuarios gestor = new GestorUsuarios();

    private static final String PREF_CLAVE = "temaSeleccionado";
    private static final Preferences PREF = Preferences.userNodeForPackage(utilidades.CambiaTemas.class);

    public void initialize() {
    	  /**
         * 09/06/2025
         * Programador: Javi
         * v1.0 Busca el tema que se esta usando y aplica el brillo alrededor del logo
         * 
         * Salida: void 
         */
    	txtCorreo.setOnAction(e -> inicioSesion());      
        txtContrasena.setOnAction(e -> inicioSesion()); 

        Platform.runLater(() -> {
            String tema = PREF.get(PREF_CLAVE,
                                   utilidades.CambiaTemas.Tema.MORADO.name());

            Color colorBrillo = switch (tema) {
                case "VERDE"   -> Color.web("#D2FF4C");
                case "AZUL"    -> Color.web("#3E7E9A");
                case "NARANJA" -> Color.web("#FFB347");
                case "MORADO"  -> Color.web("#FFFFFF");
                default        -> Color.GOLD;
            };

            crearBrillo(colorBrillo);

           
        });
    }

    
    @FXML
    private void inicioSesion() {
        /**
         * 26/05/2025
         * Programador: Javi
         * v1.0 Valida el inicio de sesion.
         * 
         * Salida: void 
         */
    	
    	
        String entrada = txtCorreo.getText().trim();         
        String contrasena = txtContrasena.getText();

        if (entrada.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Rellena ambos campos");
            return;
        }

        String hash = HashUtil.hashSHA256(contrasena);
        boolean valido = gestor.login(entrada, hash);

        if (valido) {
            Usuario usuario = gestor.obtenerPorEntrada(entrada);
            if (usuario != null) {
                UsuarioLogeado.setId(usuario.getIdUsuario());
                CambiaEscenas.cambiarEscena("pantallaColecciones.fxml");
            } else {
                mostrarError("No se pudo cargar el usuario");
            }
        } else {
            mostrarError("Correo o contraseña incorrectos");
        }
        
    }

    
    private void mostrarError(String mensaje) {
        /**
         * 26/05/2025
         * Programador: Javi
         * v1.0 si falla el nombre o contraseña muestra el mensaje durante 4 segundos 
         * 
         * Salida: void 
         */
    	
        lblError.setText(mensaje);
        lblError.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(4));
        pausa.setOnFinished(e -> lblError.setVisible(false));
        pausa.play();
    }//private void mostrarError(String mensaje)

    @FXML
    private void crearCuentaClick() {
        /**
         * 26/05/2025
         * Programador: Javi
         * v1.0 va a la pantalla de registro 
         * 
         * Salida: void 
         */
        CambiaEscenas.cambiarEscena("registro.fxml");
    }

    @FXML
    private void recuperarCuentaClick() {
        /**
         * 26/05/2025
         * Programador: Javi
         * v1.0 va a la pantalla de recuperacion 
         * 
         * Salida: void 
         */
        CambiaEscenas.cambiarEscena("recuperacion.fxml");
    }

    private void crearBrillo(Color c) {
    	
  	  /**
         * 09/06/2025
         * Programador: Javi
         * v1.0 crea la animacion del brillo 
         * 
         * Salida: void 
         */
        DropShadow glow = new DropShadow();
        glow.setColor(c);
        glow.setSpread(0.8);
        glow.setRadius(0);
        logoLogin.setEffect(glow);

        var interp = javafx.animation.Interpolator.EASE_BOTH;

        Timeline t = new Timeline(
            new KeyFrame(Duration.ZERO,new KeyValue(glow.radiusProperty(), 0,  interp)),
            new KeyFrame(Duration.seconds(2.5), new KeyValue(glow.radiusProperty(), 8,  interp))
        );
        
        t.setAutoReverse(true); 
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
    }
      
}
