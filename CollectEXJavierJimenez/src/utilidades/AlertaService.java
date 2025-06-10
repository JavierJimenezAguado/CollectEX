package utilidades;

import daos.CajaDAO;
import daos.CartaColeccionDAO;
import daos.CartaDAO;
import daos.ColeccionDAO;
import daos.SobreCajaUsuarioDAO;
import daos.SobreDAO;
import daos.UsuarioDAO;
import daos.WishlistItemDAO;
import modelos.Carta;
import modelos.CartaColeccion;
import modelos.SobreCajaUsuario;
import modelos.Usuario;
import modelos.WishlistItem;

public class AlertaService {

    private static final CartaColeccionDAO ccDAO = new CartaColeccionDAO();
    private static final CartaDAO cartaDAO = new CartaDAO();
    private static final ColeccionDAO colDAO = new ColeccionDAO();
    private static final SobreCajaUsuarioDAO scDAO = new SobreCajaUsuarioDAO();
    private static final SobreDAO sobreDAO = new SobreDAO();
    private static final CajaDAO cajaDAO  = new CajaDAO();
    private static final WishlistItemDAO wlItemDAO = new WishlistItemDAO();
    private static final UsuarioDAO usrDAO = new UsuarioDAO();

    //
    public static void comprobar() {
      	 /**
         *31/05/2025
         *Programador Javi 
         *v1.0 comprueba si hay alertas disponibles
         * salida void
         */

    	//comprueba cartas
        for (CartaColeccion cc : ccDAO.cartasConAlerta()) {

            Carta carta = cartaDAO.obtenerPorReferencia(cc.getReferencia());
            if (carta == null) continue;
            double precio = carta.getPrecio();

            if (dispara(cc.getAlertaMin(), cc.getAlertaMax(), precio)) {
                Usuario u = usrDAO.obtenerPorId(
                        colDAO.obtenerPorId(cc.getIdColeccion()).getIdUsuario());
                enviarYLimpiar(u.getCorreo(), cc.getReferencia(), precio,
                               cc.getAlertaMin(), cc.getAlertaMax());
                ccDAO.limpiarAlertas(cc.getIdColeccion(), cc.getReferencia());
            }
        }

        //comprueba en sobrescajas
        for (SobreCajaUsuario sc : scDAO.conAlerta()) {

            double precio = sc.getTipo().equals("sobre")
                    ? sobreDAO.obtenerTodos().stream()
                               .filter(s -> s.getIdSobre()==sc.getIdElemento())
                               .findFirst().orElseThrow().getPrecio()
                    : cajaDAO.obtenerTodas().stream()
                               .filter(c -> c.getIdCaja()==sc.getIdElemento())
                               .findFirst().orElseThrow().getPrecio();

            if (dispara(sc.getAlertaMin(), sc.getAlertaMax(), precio)) {
                Usuario u = usrDAO.obtenerPorId(sc.getIdUsuario());
                enviarYLimpiar(u.getCorreo(), sc.getNombre(), precio,
                               sc.getAlertaMin(), sc.getAlertaMax());
                scDAO.limpiarAlertas(sc.getId());
            }
        }

        //comprueba la wishlist 
        for (WishlistItem wi : wlItemDAO.conAlerta()) {

            Carta carta = cartaDAO.obtenerPorReferencia(wi.getReferencia());
            if (carta == null) continue;
            double precio = carta.getPrecio();

            if (dispara(wi.getPrecioMinimo(), wi.getPrecioMaximo(), precio,
                        wi.isAlertaMinimo(), wi.isAlertaMaximo())) {

                Usuario u = usrDAO.obtenerPorId(wi.getIdUsuario());
                enviarYLimpiar(u.getCorreo(), wi.getReferencia(), precio,
                               wi.getPrecioMinimo(), wi.getPrecioMaximo());
                wlItemDAO.limpiarAlertas(wi.getIdWishlistItem());
            }
        }
    }//public static void comprobar() 


    //verifica si el precio se sale de las alertas
    private static boolean dispara(Double min, Double max, double precio) {
     	 /**
        *31/05/2025
        *Programador Javi 
        *v1.0 verifica si el precio se sale de las alertas
        * @param min
        * @param max
        * @param precio
        * salida boolean
        */
        return (min!=null && precio<=min) || (max!=null && precio>=max);
    }
    /**
     * 

     * @return
     */
    
    private static boolean dispara(Double min, Double max, double precio, boolean useMin, boolean useMax) {
    	 /**
       *31/05/2025
       *Programador Javi 
       *v1.0 verifica si se sale de las alertas con flags
       *@param min
       * @param max
       * @param precio
       * @param useMin
       * @param useMax
       * salida boolean
       */
        return (useMin && min!=null && precio<=min) || (useMax && max!=null && precio>=max);
    }
    

    
    private static void enviarYLimpiar(String correo, String ref, double precio, Double min, Double max) {
        /**
         *31/05/2025
         *Programador Javi 
         *v1.0 envia el correo y limpia la alerta para no spammear 
         * @param correo
         * @param ref
         * @param precio
         * @param min
         * @param max
         */
        String cuerpo = (min!=null && precio<=min)
            ? String.format("El precio de %s ha BAJADO a %.2f € (umbral %.2f €).", ref, precio, min)
            : String.format("El precio de %s ha SUBIDO a %.2f € (umbral %.2f €).", ref, precio, max);
        EnviaCorreo.enviar(correo, "Alerta de precio – CollectEX", cuerpo);
    }// private static void enviarYLimpiar(String correo, String ref, double precio, Double min, Double max)
}
