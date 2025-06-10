package controladores;

import java.util.ArrayList;
import java.util.List;
import daos.ColeccionDAO;
import daos.UsuarioDAO;
import daos.CartaColeccionDAO;
import daos.CartaDAO;
import modelos.Carta;
import modelos.CartaColeccion;
import modelos.Usuario;
import utilidades.EnviaCorreo;



public class GestorAlertas {

    public static void comprobarYEnviarAlertas(int idUsuario) {
        /**
         * 28/05/2025
         * Programador: Javi
         * v1.0 Metodo que se encarga de comprobar las alertas y si existen llama a la clase 
         * 		utilidad de EnviarCorreo y envia correo con la aleta
         * @param idUsuario
         */
    	
        ColeccionDAO coleccionDAO = new ColeccionDAO();
        CartaColeccionDAO cartaColDAO = new CartaColeccionDAO();
        CartaDAO cartaDAO = new CartaDAO();

        List<Integer> idsColecciones = coleccionDAO.obtenerIdsPorUsuario(idUsuario);
        List<String> avisos = new ArrayList<>();

        //recorrido de colecions
        for (int idCol : idsColecciones) {
            List<CartaColeccion> cartas = cartaColDAO.listarPorColeccion(idCol);

            for (CartaColeccion cc : cartas) {
                Carta carta = cartaDAO.obtenerPorReferencia(cc.getReferencia());
                if (carta == null) continue;

                double precioActual = carta.getPrecio();

                //comprobacion de valores de alerta
                if (cc.getAlertaMin() != null && precioActual < cc.getAlertaMin()) {
                    avisos.add("ALERTA: " + carta.getNombre() + " ha bajado de " + cc.getAlertaMin() + "€. Precio actual: " + precioActual + "€");
                }
                if (cc.getAlertaMax() != null && precioActual > cc.getAlertaMax()) {
                    avisos.add("ALERTA: " + carta.getNombre() + " ha subido de " + cc.getAlertaMax() + "€. Precio actual: " + precioActual + "€");
                }
            }
        }

        // si encuentra aletas envia correo
        if (!avisos.isEmpty()) {
        	Usuario u = new UsuarioDAO().obtenerPorId(idUsuario);
        	if (u != null) {
                String asunto = "Alertas de cartas - CollectEX";
                String cuerpo = String.join("\n", avisos);
                EnviaCorreo.enviar(u.getCorreo(), asunto, cuerpo);
                System.out.println("Correo de alertas enviado a: " + u.getCorreo());
            }
        } else {
            System.out.println("No hay alertas para enviar.");
        }
    }//public static void comprobarYEnviarAlertas(int idUsuario) 
}


