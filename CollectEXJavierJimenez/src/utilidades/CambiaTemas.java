package utilidades;

import javafx.scene.Scene;
import java.util.prefs.Preferences;


public final class CambiaTemas {

    private static final String temaSeleccionado = "temaSeleccionado";
    private static final Preferences preferencias =
            Preferences.userNodeForPackage(CambiaTemas.class);

    public enum Tema {
        MORADO  ("temaMorado.css"),
        VERDE   ("temaVerde.css"),
        AZUL    ("temaAzul.css"),
        NARANJA ("temaNaranja.css");

        private final String css;
        Tema(String css) { this.css = css; }
        String ruta() { return "/estilos/" + css; }
    }

    private CambiaTemas() {
    	
    }   

    public static void aplicar(Scene escena, Tema nuevo) {
    	/**
       *24/05/2025
       *Programador Javi 
       *v1.0 aplica el tema 
       *@param escena
       *@param nuevo
       * salida void
       */

    	escena.getStylesheets().removeIf(s ->
                s.contains("temaMorado.css") || s.contains("temaVerde.css") ||
                s.contains("temaAzul.css")  || s.contains("temaNaranja.css"));

    	escena.getStylesheets().add(
                CambiaTemas.class.getResource(nuevo.ruta()).toExternalForm());

    	escena.getRoot().applyCss();   
    	escena.getRoot().layout();      

        preferencias.put(temaSeleccionado, nuevo.name());
    }// public static void aplicar(Scene escena, Tema nuevo)


    public static void cargarAlInicio(Scene escena) {
    	/**
         *24/05/2025
         *Programador Javi 
         *v1.0 aplica el tema que haya seleccionado el usuario la ultima vez del cambio al iniciar
         *@param escena
         * salida void
         */
        String guardado = preferencias.get(temaSeleccionado, Tema.MORADO.name());
        aplicar(escena, Tema.valueOf(guardado));
    }// public static void cargarAlInicio(Scene escena)
    
    
}
