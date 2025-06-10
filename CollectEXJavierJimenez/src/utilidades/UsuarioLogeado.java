package utilidades;

public class UsuarioLogeado {
    private static int idUsuario;

    public static void setId(int id) {
    	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 setea el usuario que se acaba de logear por id 
         *@param id
         * salida void
         */
        idUsuario = id;
    }

    public static int get() {
    	/**
         *29/04/2025
         *Programador Javi 
         *v1.0 obntiene  el id del usuario logeado 
         *@param id
         * salida ind
         */
        return idUsuario;
    }
}
