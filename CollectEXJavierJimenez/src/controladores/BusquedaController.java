package controladores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import daos.CajaDAO;
import daos.CartaDAO;
import daos.SobreDAO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import modelos.Caja;
import modelos.Carta;
import modelos.Sobre;
import utilidades.CambiaEscenas;
import utilidades.PantallaAnterior;

public class BusquedaController {
	
	//navegacion entre secciones
	
	 @FXML
	    private void irAColecciones(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("pantallaColecciones.fxml");
	    }

	    @FXML
	    private void irASobres(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("sobrescajas.fxml");
	    }

	    @FXML
	    private void irAWishList(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("wishList.fxml");
	    }

	    @FXML
	    private void irABusqueda(MouseEvent event) {
	       
	    }

	    @FXML
	    private void irAAjustes(MouseEvent event) {
	        CambiaEscenas.cambiarEscena("ajustes.fxml");
	    }

	    @FXML private TextField campoBusqueda;
	    @FXML private CheckBox ckPokemon, ckObjeto, ckEntrenador, ckSR, ckSIR, ckUR,ckSAR, ckHolo;


	    /**
	     * 28/04/2025
	     * Programador: Javi
	     * Metodo de busqueda y filtrado de cartas mediante textfield y/o cbox de rareza o tipo de carta
	     * @param event
	     * Salida: void
	     */
	    @FXML
	    private void buscar(ActionEvent event) {
	        String texto = campoBusqueda.getText();

	        List<String> tipos = new ArrayList<>();
	        if (ckPokemon.isSelected()) tipos.add("Pokemon");
	        if (ckObjeto.isSelected()) tipos.add("Objeto");
	        if (ckEntrenador.isSelected()) tipos.add("Entrenador");

	        List<String> rarezas = new ArrayList<>();
	        if (ckSR.isSelected()) rarezas.add("SR");
	        if (ckSIR.isSelected()) rarezas.add("SIR");
	        if (ckUR.isSelected()) rarezas.add("UR");
	        if (ckSAR.isSelected()) rarezas.add("SAR");
	        if (ckHolo.isSelected()) rarezas.add("Holo");


	        contenedorCartas.getChildren().clear();

	        List<Carta> resultados = new CartaDAO().buscarCartas(texto, tipos, rarezas);
	        mostrarCartas(resultados);
	        mostrarCajas(new CajaDAO().obtenerTodas());
	        mostrarSobres(new SobreDAO().obtenerTodos());

	    
	    
	    
	        
	    }//private void handleBuscar(ActionEvent event) 
	    
	   
	        
	    @FXML private TilePane contenedorCartas;


	    @FXML
	    public void initialize() {
		    /**
		     * 01/05/2025
		     * Programador Javi
		     * v1.0 Inicializa el controlador y carga las cartas
		     * Salida void
		     */
	        cargarCartas();
	    }
	    
	    

	    private void cargarCartas() {
		    /**
		     * 01/05/2025
		     * Programador: Javi
		     * v1.0 Metodo de carga de todas las cartas y las muestra sin bloquear GUI
		     * Salida: void
		     * 
		     */
	    	
	        Task<Void> task = new Task<>() {
	            @Override
	            protected Void call() { //crea una tarea en segundo plano para evitar bloquear la interfaz
	                List<Carta> cartas = new CartaDAO().obtenerTodasOrdenadas();

	                for (Carta carta : cartas) {
	                    try {
	                    	//carga cada carta individualmente
	                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/tarjetaCarta.fxml"));
	                        AnchorPane tarjeta = loader.load();
	                        
	                        
	                   
	                        ImageView img = (ImageView) tarjeta.lookup("#imagenCarta");
	                        Label lblNombre = (Label) tarjeta.lookup("#nombreCarta");
	                        Label lblPrecio = (Label) tarjeta.lookup("#precioCarta");

	                        lblNombre.setText(carta.getNombre());
	                        lblPrecio.setText(String.format("Precio: %.2f€", carta.getPrecio()));

	                        File imgFile = new File("src/imagenes_cartas/" + carta.getReferencia() + ".jpg");
	                        if (imgFile.exists()) {
	                            Image image = new Image(imgFile.toURI().toString());
	                            img.setImage(image);
	                        }
	                        
	                        
	                        //añade la tarjeta al scroll
	                        Platform.runLater(() -> {
	                        	CartaController.setColeccionOrigen(-1);
	                            tarjeta.setOnMouseClicked(e -> {

	    	                        PantallaAnterior.setAnterior("busquedaCartas.fxml");
	                                CartaController.setReferenciaCarta(carta.getReferencia());
	                                CambiaEscenas.cambiarEscena("interiorCartas.fxml");
	                            });
	                            contenedorCartas.getChildren().add(tarjeta);
	                        });


	                        try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

	                    } catch (IOException e) {
	                        System.err.println("Error cargando tarjeta: " + e.getMessage());
	                    }
	                }
	                
	                //carga sobres y cajas 
	                Platform.runLater(() -> {
	                   	CartaController.setColeccionOrigen(-1);
	                    mostrarCajas(new CajaDAO().obtenerTodas());
	                    mostrarSobres(new SobreDAO().obtenerTodos());
	                });
	                
	                return null;
	            }
	            
	        };

	        new Thread(task).start();
	    }//private void cargarCartas() 
	    
	    

	    private void mostrarCartas(List<Carta> cartas) {
		    /**
		     * 01/05/2025
		     * Programador: Javi
		     * v1.0 Muestra la lista de cartas en el panel 
		     * @param cartas
		     * Salida: Void
		     */
	    	
	        contenedorCartas.getChildren().clear();

	        for (Carta carta : cartas) {
	            try {
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/tarjetaCarta.fxml"));
	                AnchorPane tarjeta = loader.load();
	                
	                TarjetaCartaController controller = loader.getController();
	                controller.datosCarta(carta);
	              

	                ImageView img = (ImageView) tarjeta.lookup("#imagenCarta");
	                Label lblNombre = (Label) tarjeta.lookup("#nombreCarta");
	                Label lblPrecio = (Label) tarjeta.lookup("#precioCarta");

	                lblNombre.setText(carta.getNombre());
	                lblPrecio.setText(String.format("Precio: %.2f€", carta.getPrecio()));

	                File imgFile = new File("src/imagenes_cartas/" + carta.getReferencia() + ".jpg");
	                if (imgFile.exists()) {
	                    img.setImage(new Image(imgFile.toURI().toString()));
	                }
	                
	                
	                String ref = carta.getReferencia(); 
	                tarjeta.setOnMouseClicked(e -> {
	                	PantallaAnterior.setAnterior("busquedaCartas.fxml");
	                	CartaController.setColeccionOrigen(-1);
	                    CartaController.setReferenciaCarta(ref);
	                    CambiaEscenas.cambiarEscena("interiorCartas.fxml");
	                });


	                contenedorCartas.getChildren().add(tarjeta);
	            } catch (IOException e) {
	                System.err.println("Error cargando tarjeta: " + e.getMessage());
	            }
	        }
	    }//private void mostrarCartas(List<Carta> cartas)
	    

	    private void mostrarCajas(List<Caja> cajas) {
		    /**
		     * 01/05/2025
		     * Programador: Javi
		     * v1.0 Muestra la lista de cajas en el panel 
		     * @param cajas
		     * Salida: Void
		     */
	    	
	        for (Caja caja : cajas) {
	            try {
	                FXMLLoader loader = new FXMLLoader(
	                        getClass().getResource("/vistas/tarjetaCarta.fxml"));
	                AnchorPane tarjeta = loader.load();

	                TarjetaCartaController controller = loader.getController();
	                controller.setPantallaOrigen("busquedaCartas.fxml");
	               	 // rellena la tarjeta
	                loader.<TarjetaCartaController>
	                       getController().inicializarDesdeCaja(caja);

	           
	                tarjeta.setOnMouseClicked(e -> {
	                    SobreCajaController.vistaCaja(caja.getIdCaja(), caja.getNombre());
	                    CambiaEscenas.cambiarEscena("cajassobre.fxml");
	                });

	                contenedorCartas.getChildren().add(tarjeta);
	            } catch (IOException ex) {
	                System.err.println("Error cargando caja: " + ex.getMessage());
	            }
	        }
	    }// private void mostrarCajas(List<Caja> cajas)

	  
	    private void mostrarSobres(List<Sobre> sobres) {
	    	  /**
		     * 01/05/2025
		     * Programador: Javi
		     * v1.0 Muestra la lista de sobres en el panel 
		     * @param sobres
		     * Salida: Void
		     */
	    	
	        for (Sobre sobre : sobres) {
	            try {
	                FXMLLoader loader = new FXMLLoader(
	                        getClass().getResource("/vistas/tarjetaCarta.fxml"));
	                AnchorPane tarjeta = loader.load();
	                
	                TarjetaCartaController controller = loader.getController();
	                controller.setPantallaOrigen("busquedaCartas.fxml");
	                PantallaAnterior.setAnterior("busquedaCartas.fxml");

	                loader.<TarjetaCartaController>
	                       getController().inicializarDesdeSobre(sobre);

	                tarjeta.setOnMouseClicked(e -> {
	                    SobreCajaController.vistaSobre(
	                            sobre.getIdSobre(), sobre.getNombre());
	                    CambiaEscenas.cambiarEscena("cajassobre.fxml");
	                });

	                contenedorCartas.getChildren().add(tarjeta);
	            } catch (IOException ex) {
	                System.err.println("Error cargando sobre: " + ex.getMessage());
	            }
	        }
	    }//private void mostrarSobres(List<Sobre> sobres) 



}
