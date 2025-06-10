package utilidades;

import java.util.Random;

public class RecuperacionTemporal {
    public static String correo;
    public static String codigoRecuperacion;

    //genera codigo 
    public static String generarCodigo() {
       	/**
         *24/05/2025
         *Programador Javi 
         *v1.0 geenera el codigo de recuperacion
         * salida string
         */
        int numero = 100000 + new Random().nextInt(900000);
        return String.valueOf(numero);
    }//public static String generarCodigo()
}
