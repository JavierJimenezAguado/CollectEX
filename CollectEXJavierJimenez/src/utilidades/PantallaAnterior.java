package utilidades;

public class PantallaAnterior {
    private static String pantallaAnterior;

    public static void setAnterior(String fxml) {
       	/**
         *1/06/2025
         *Programador Javi 
         *v1.0 guarda la pantalla anterior 
         *@param fxml
         * salida void
         */
        pantallaAnterior = fxml;
    }

    public static String getAnterior() {
       	/**
         *1/06/2025
         *Programador Javi 
         *v1.0 obtiene la pantalla anterior 
         * salida string
         */
        return pantallaAnterior;
    }

    public static void volver() {
       	/**
         *1/06/2025
         *Programador Javi 
         *v1.0 vuelve a la pantalla anterior 
         * salida void
         */
        if (pantallaAnterior != null) {
            CambiaEscenas.cambiarEscena(pantallaAnterior);
        }
    }
}
