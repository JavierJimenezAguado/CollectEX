package main;

import basededatos.TablasSQLite;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilidades.CambiaEscenas;
import utilidades.CambiaTemas;
import utilidades.ScraperService;

public class MainCollectEX extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/login.fxml"));
	    Parent root = loader.load();
	    Scene scene = new Scene(root, 1280, 720);
	    
	    CambiaTemas.cargarAlInicio(scene);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("CollectEX");
	    primaryStage.setResizable(false);
	    primaryStage.show();

	    CambiaEscenas.setStage(primaryStage); 
	}

    public static void main(String[] args) {
    	TablasSQLite.crearTablas();
    	TablasSQLite.insertarDatos();
    	//ScraperService.ejecutarScrapingCompleto();
    	//TablasSQLite.insertarSobresYCajasBase();
    	
        launch(args);
        
    }
    
    
    
}
