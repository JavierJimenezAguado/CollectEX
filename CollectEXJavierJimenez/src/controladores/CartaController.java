package controladores;

import basededatos.Singleton;
import daos.CartaColeccionDAO;
import daos.CartaDAO;
import daos.ColeccionDAO;
import daos.ExpansionDAO;
import daos.WishlistDAO;
import daos.WishlistItemDAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import modelos.Carta;
import modelos.CartaColeccion;
import modelos.Expansion;
import modelos.Wishlist;
import modelos.WishlistItem;
import utilidades.CambiaEscenas;
import utilidades.PantallaAnterior;
import utilidades.UsuarioLogeado;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartaController {

    private static String referenciaCarta; //alamacena la carta seleccionada
    
    public  static void setReferenciaCarta(String ref){
    	referenciaCarta = ref;
    }

    @FXML private Label nombreCarta;
    @FXML private Label expansion;
    @FXML private BorderPane imgCarta;
    @FXML private LineChart<String,Number> graficoPrecios;
    @FXML private CheckBox ckGradeada;
    @FXML private ComboBox<String> cboxGrade;
    @FXML private ComboBox<String> cboxColeccion;
    @FXML private CheckBox ckMin, ckMax;
    @FXML private TextField lblalertaMin, lblalertaMax;
    @FXML private Button btnAnadirColeccion;
    @FXML private TextField lblprecioPagado;
    @FXML private Label labelColecciones;
    @FXML private Label labelSobres;
    @FXML private Label labelWishlist;
    @FXML private Label labelBusqueda;
    @FXML private Label labelAjustes;
    @FXML private ImageView imgCorazon;
    @FXML private Button btnVolver;
    @FXML private Button btnEliminarColeccion;
    private boolean estaEnWishlist = false;
    
    private static int coleccionOrigen = -1;

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
    	CambiaEscenas.cambiarEscena("wishlist.fxml");
    }

    @FXML
    private void irABusqueda(MouseEvent event) {
        CambiaEscenas.cambiarEscena("busquedaCartas.fxml");
    }

    @FXML
    private void irAAjustes(MouseEvent event) {
        CambiaEscenas.cambiarEscena("ajustes.fxml");
    }

    
    //convierte las notas a texto para el json
    private static final Map<String,String> nota2Key = Map.ofEntries(
    	    Map.entry("Ungraded","11.0"),
    	    Map.entry("2","2.0"),  
    	    Map.entry("3","3.0"),
    	    Map.entry("4","4.0"), 
    	    Map.entry("5","5.0"),
    	    Map.entry("6","6.0"),
    	    Map.entry("7","7.0"),
    	    Map.entry("8","8.0"),
    	    Map.entry("9","9.0"),
    	    Map.entry("9.5","9.5"),
    	    Map.entry("10","10.0")
    	);// private static final Map<String,String> nota2Key = Map.ofEntries

    private Map<String,Map<String,Double>> historicoJSON;//estructura de precios historicos 
    


    @FXML
    private void initialize(){
        /**
         * 04/06/2025
         * Programador: Javi 
         * v3.0 Carga toda la informacion de cartas si esta en wishlist o no etc
         * Salida void.
         */
    	
    	btnEliminarColeccion.setVisible(coleccionOrigen > 0);   
    	
    	imgCorazon.setImage(new Image(new File("src/imagenes/corazonvacio.png").toURI().toString())); //busca el estado de corazon 
    	int idUsuario = UsuarioLogeado.get(); //obtiene el usuario actual 
    	WishlistDAO wishlistDAO = new WishlistDAO();
    	Wishlist wl = wishlistDAO.obtenerPorUsuario(idUsuario);

    	//comprueba si esta en la wishlist
    	if (wl != null) {
    	    int idWishlist = wl.getIdWishlist();
    	    WishlistItemDAO itemDAO = new WishlistItemDAO();
    	    estaEnWishlist = itemDAO.existeItem(idWishlist, referenciaCarta);
    	    actualizarIconoCorazon();
    	}
    	
        if(referenciaCarta==null){
        	System.err.println("Sin referencia"); 
        	return;
        }

        //cargar datos de la carta
        Carta carta = new CartaDAO().obtenerPorReferencia(referenciaCarta);
        if(carta==null){ System.err.println("Carta no encontrada"); return; }

        nombreCarta.setText(carta.getNombre());

        //nombre de la expansion 
        Expansion exp = new ExpansionDAO().obtenerPorId(Integer.parseInt(carta.getCodigoExpansion()));
        if(exp!=null) expansion.setText(exp.getNombre());

        File imgFile = new File("src/imagenes_cartas/"+carta.getReferencia()+".jpg");
        if(imgFile.exists()){
            ImageView iv = new ImageView(new Image(imgFile.toURI().toString()));
            iv.setFitWidth(475); iv.setPreserveRatio(true);
            imgCarta.setCenter(iv);
        }
        
        //inicializa el cbox de nota 
        cboxGrade.getItems().addAll(
            "Ungraded","2","3","4","5","6","7","8","9","9.5","10"
        );
        cboxGrade.setDisable(true);

        //inicializa el cbox colecciones
        int idUsuarioActual = UsuarioLogeado.get();
        cboxColeccion.getItems().setAll(new ColeccionDAO().obtenerNombresPorUsuario(idUsuarioActual));


        //pintar el grafico y lee el json de preciohistorico segun la referencia 
        historicoJSON = leerHistoricoJSON(referenciaCarta);
        pintarSerie("Ungraded");

        ckGradeada.setOnAction(e -> {
            boolean grad = ckGradeada.isSelected();
            cboxGrade.setDisable(!grad);
            if(!grad) pintarSerie("Ungraded");
            else if(cboxGrade.getValue()!=null) pintarSerie(cboxGrade.getValue());
        });
        cboxGrade.setOnAction(e -> {
            if(ckGradeada.isSelected() && cboxGrade.getValue()!=null)
                pintarSerie(cboxGrade.getValue());
        });
    }//private void initialize()
    

    
    //metodo de lectura del json en tablas de historico
    private Map<String,Map<String,Double>> leerHistoricoJSON(String ref){
        /**
         * 08/05/2025
         * Programador: Javi
         * v1.0 Lee el json y lo pasa a un mapa 
         * @param ref
         * @return mapaHistorico
         */
    	
        String sql="""
            SELECT fecha,historico FROM precios_historicos
            WHERE referencia=? ORDER BY fecha
        """;
        Map<String,Map<String,Double>> mapaHistorico = new LinkedHashMap<>();
        Type t = new TypeToken<Map<String,Double>>(){}.getType();
        try(Connection c=Singleton.getInstance().getConnection();
            PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,ref);
            ResultSet rs=p.executeQuery();
            Gson g=new Gson();
            while(rs.next()){
                Map<String,Double> m = g.fromJson(rs.getString("historico"),t);

                Map<String,Double> norm=new HashMap<>();
                m.forEach((k,v)->norm.put(k.toString(),v));
                mapaHistorico.put(rs.getString("fecha"),norm);
            }
        }catch(Exception ex){ System.err.println("Hist JSON: "+ex.getMessage()); }
        return mapaHistorico;
    }//private Map<String,Map<String,Double>> leerHistoricoJSON(String ref)


    private void pintarSerie(String nota){
        /**
         * 10/05/2025
         * Programador: Javi
         * v1.0 Pinta la grafica 
         * @param nota
         * Salida: void 
         */
    	
        String clave = nota2Key.getOrDefault(nota,"11.0");

        XYChart.Series<String,Number> s = new XYChart.Series<>();
        s.setName(nota);

        historicoJSON.forEach((fecha,m)->{
            Double precio=m.get(clave);
            if(precio!=null) s.getData().add(new XYChart.Data<>(fecha,precio));
        });

        graficoPrecios.getData().setAll(s);
    }//private void pintarSerie(String nota)
    

    
    @FXML
    private void anadirAColeccion() {
         /**
         * 07/05/2025
         * Programador: Javi
         * v1.0 Añade las cartas a la coleccion 
         * Salida: void
         */
        String nombreColeccion = cboxColeccion.getValue();
        if (nombreColeccion == null || nombreColeccion.isBlank()) {
            System.out.println("Selecciona una colección");
            return;
        }

        ColeccionDAO colDAO = new ColeccionDAO();
        int idUsuarioActual = UsuarioLogeado.get();
        int idColeccion = colDAO.obtenerIdPorNombre(nombreColeccion, idUsuarioActual);


        if (idColeccion <= 0) {
            System.out.println("No se encontró el ID de la colección");
            return;
        }

        int graduacion = 0;
        if (ckGradeada.isSelected() && cboxGrade.getValue() != null) {
            try {
                String val = cboxGrade.getValue();
                graduacion = val.equals("Ungraded") ? 0 : (val.equals("9.5") ? 95 : Integer.parseInt(val));
            } catch (Exception e) {
                System.out.println("Graduación inválida, se usará 0");
            }
        }

        double precioPagado = parsear(lblprecioPagado.getText());
        Double alertaMin = ckMin.isSelected() ? parsear(lblalertaMin.getText()) : null;
        Double alertaMax = ckMax.isSelected() ? parsear(lblalertaMax.getText()) : null;

        CartaColeccion cc = new CartaColeccion(
            idColeccion,
            referenciaCarta,
            1,                 
            graduacion,
            precioPagado,
            alertaMin,
            alertaMax
        );

        boolean ok = new CartaColeccionDAO().guardar(cc);
        if (ok) {
            System.out.println("Carta añadida a la colección correctamente");
        } else {
            System.out.println("No se pudo guardar la carta");
        }
    }
    


    private double parsear(String txt) {
        /**
         * 08/05/2025
         * Programador Javi
         * v1.0 Parsea los doubles 
         * @param txt
         * @return double
         */
    	
        try {
            return Double.parseDouble(txt.replace(",", "."));
        } catch (Exception e) {
            return 0.0;
        }
    }
    

    
    @FXML
    private void botonWishlist() {
        /**
         * 15/05/2025
         * Programador: Javi
         * v1.0 El boton de meter o no meter en wishlist es un boton toggle
         * Salida: void
         */
    	
        int idUsuario = UsuarioLogeado.get();
        WishlistDAO wishlistDAO = new WishlistDAO();
        Wishlist wl = wishlistDAO.obtenerPorUsuario(idUsuario);

        //si el usuario no tiene wislist la crea
        if (wl == null) {
            wl = new Wishlist(0, idUsuario, "Mi Wishlist", null);
            boolean creada = wishlistDAO.insertarWishlist(wl);
            if (!creada) {
                System.err.println("No se pudo crear la wishlist por defecto.");
                return;
            }
            wl = wishlistDAO.obtenerPorUsuario(idUsuario);
            if (wl == null) {
                System.err.println("Error crítico: se creó la wishlist pero no se pudo recuperar.");
                return;
            }
        }

        int idWishlist = wl.getIdWishlist();  
        
        //comprueba si existe o no si existe lo borra de la wishlist 
        WishlistItemDAO itemDAO = new WishlistItemDAO();
        if (itemDAO.existeItem(idWishlist, referenciaCarta)) {
            boolean eliminado = itemDAO.eliminarItem(idWishlist, referenciaCarta);
            if (eliminado) {
                estaEnWishlist = false;
                System.out.println("Carta eliminada de wishlist");
            } else {
                System.err.println("No se pudo eliminar la carta");
            }
        } else {

            //inicializamos 
            double precioMinimo = 0.0;
            double precioMaximo = 0.0;
            boolean alarmaMinimo = false;
            boolean alarmaMaximo = false;

            //se comprueban si estan marcadas las alertas y se lee la cantidad si lo estan 
            if (ckMin.isSelected()) {
                alarmaMinimo = true;
                String txtMin = lblalertaMin.getText().trim();
                if (!txtMin.isBlank()) {
                    try {
                        precioMinimo = Double.parseDouble(txtMin.replace(",", "."));
                    } catch (NumberFormatException ex) {
                        System.err.println("Precio de alerta no valido");
                    }
                }
            }

            //se comprueban si estan marcadas las alertas y se lee la cantidad si lo estan 
            if (ckMax.isSelected()) {
                alarmaMaximo = true;
                String txtMax = lblalertaMax.getText().trim();
                if (!txtMax.isBlank()) {
                    try {
                        precioMaximo = Double.parseDouble(txtMax.replace(",", "."));
                    } catch (NumberFormatException ex) {
                        System.err.println("Precio de alerta no valido");
                    }
                }
            }
            
            //vrear objeto de wishlist
            WishlistItem item = new WishlistItem(
                0,            
                idWishlist,    
                referenciaCarta,
                1,           
                precioMinimo,  
                precioMaximo,  
                alarmaMinimo,  
                alarmaMaximo, 
                false,         
                0,            
                idUsuario     
            );

            boolean insertado = itemDAO.insertarWishlistItem(item);
            if (insertado) {
                estaEnWishlist = true;
                System.out.println("Carta añadida a wishlist con alertas: " +
                                   "precioMin=" + precioMinimo + 
                                   ", precioMax=" + precioMaximo);
            } else {
                System.err.println("No se pudo insertar la carta");
            }
        }

        actualizarIconoCorazon();
    }//private void toggleWishlist() 
    
    /**
     * 15/05/2025
     * Programador: Javi
     * v1.0 Actualiza la imagen del corazon si se encuentra en wishlist 
     * Salida: void
     */

    private void actualizarIconoCorazon() {
        String nombreImagen = estaEnWishlist ? "corazonlleno.png" : "corazonvacio.png";
        File imgFile = new File("src/imagenes/" + nombreImagen);
        System.out.println("Actualizando imagen: " + imgFile.getAbsolutePath());
        if (imgFile.exists()) {
            imgCorazon.setImage(new Image(imgFile.toURI().toString()));
        } else {
            System.err.println("NO se encontró la imagen del corazón: " + imgFile.getAbsolutePath());
        }
    }//private void actualizarIconoCorazon() 



    @FXML
    private void volverPantallaAnterior(MouseEvent event) {
        /**
         * 03/06/2025
         * Programador: Javi
         * v1.0 Metodo para volver a la pantalla anterios al pulsar la recibiendo la inormacion de la clase Pantalla anterio
         * @param event
         */
    	String origen = PantallaAnterior.getAnterior();

        if (origen != null && !origen.isEmpty()) {
            CambiaEscenas.cambiarEscena(origen);
        } else {
            CambiaEscenas.cambiarEscena("pantallaColecciones.fxml"); 
        }
    }
    
    /**
     * 03/06/2025
     * Progamador: Javi
     * v1.0 Metodo que obtiene la coleccion de origen de la carta 
     * @param id
     * Salida void
     */
    
    public static void setColeccionOrigen(int id){ 
    	coleccionOrigen = id; 
    }
    
    /**
     * 03/06/2025	
     * Programador Javi
     * v1.0 Metodo para eliminar cartas de una coleccion 
     * Salida: void 
     */
    
    @FXML
    private void eliminarDeColeccion() {
        if (coleccionOrigen > 0) {
            boolean ok = new CartaColeccionDAO().eliminar(coleccionOrigen, referenciaCarta); 
            if (ok) {
                coleccionOrigen = -1;          
                CambiaEscenas.cambiarEscena(PantallaAnterior.getAnterior());
            }
        }
    }


   


}
