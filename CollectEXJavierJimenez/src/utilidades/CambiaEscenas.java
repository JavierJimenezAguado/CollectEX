package utilidades;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class CambiaEscenas {

    private static Stage stageActual;

    public static void setStage(Stage stage) {
        CambiaEscenas.stageActual = stage;
    }

    //
    public static void cambiarEscena(String nombreFXML) {
     	 /**
        *24/05/2025
        *Programador Javi 
        *v1.0 cambia escenas con en nombre fxml 
        *@param nombreFXML
        * salida void
        */
        try {
            FXMLLoader loader = new FXMLLoader(CambiaEscenas.class.getResource("/vistas/" + nombreFXML));
            Parent root = loader.load();
            
            

            Scene nuevaEscena = new Scene(root, 1280, 720);
            CambiaTemas.cargarAlInicio(nuevaEscena);
            stageActual.setScene(nuevaEscena);
        } catch (IOException e) {
            System.err.println("Error al cargar la escena: " + nombreFXML);
            e.printStackTrace();
        }
    }//public static void cambiarEscena(String nombreFXML)
    

    public static <T> void cambiarEscena(String nombreFXML, Consumer<T> initController) {
    	 /**
       *26/05/2025
       *Programador Javi 
       *v1.0 cambia la escena para cargar el controller tras cargar la pantalla 
       * @param <T>
       * @param nombreFXML
       * @param initController
       * salida void
       */
        try {
            FXMLLoader loader = new FXMLLoader(
                    CambiaEscenas.class.getResource("/vistas/" + nombreFXML));
            Parent root = loader.load();

            T controller = loader.getController();
            Scene nuevaEscena = new Scene(root, 1280, 720);

            CambiaTemas.cargarAlInicio(nuevaEscena);

            stageActual.setScene(nuevaEscena);
        } catch (IOException e) {
            System.err.println("Error al cargar la escena: " + nombreFXML);
            e.printStackTrace();
        }
    }//public static <T> void cambiarEscena(String nombreFXML, Consumer<T> initController)

    public static Stage getStage() {
        return stageActual;
    }//public static Stage getStage() 
}
