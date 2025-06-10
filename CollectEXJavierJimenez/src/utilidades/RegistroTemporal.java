package utilidades;

//variables estaticas para mantener en memoria entre pantallas 
public class RegistroTemporal {
    public static String nombre;
    public static String correo;
    public static String contrasenaHash;

    
    //limpia la informacion 
    public static void limpiar() {
       	/**
         *24/05/2025
         *Programador Javi 
         *v1.0 limpia las variables que contienen la informacion temporal del registro despues de que se crea el nuevo usuario
         * salida string
         */
        nombre = null;
        correo = null;
        contrasenaHash = null;
    }// public static void limpiar()
}
