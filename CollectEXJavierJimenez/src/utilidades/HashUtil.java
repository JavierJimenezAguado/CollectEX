package utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

	//encriptacion hash 
    public static String hashSHA256(String texto) {
       	/**
         *24/05/2025
         *Programador Javi 
         *v1.0 encripta contraseña
         *@param texto
         * salida string
         */
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes());
            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                String hexChar = Integer.toHexString(0xff & b);
                if (hexChar.length() == 1) hex.append('0');
                hex.append(hexChar);
            }

            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al generar hash: " + e.getMessage());
            return null;
        }
    }//public static String hashSHA256(String texto)
}