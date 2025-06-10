package utilidades;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EnviaCorreo {

    private static final String REMITENTE = "j.jimenez@fp.safavalladolid.com";
    private static final String CONTRASENA = "ngsu wxeo xazm oemf"; 


    public static void enviarCorreoRegistro(String destinatario, String nombre) {
    	/**
         *24/05/2025
         *Programador Javi 
         *v1.0 envia correo de registro
         *@param destinatario
         *@param nombre
         * salida void
         */

        String asunto = "Bienvenido a CollectEX, " + nombre + "!";
        String cuerpo = "Tu cuenta se ha registrado correctamente. Gracias por unirte.";
        EnviaCorreo.enviar(destinatario, asunto, cuerpo);
    }//public static void enviarCorreoRegistro(String destinatario, String nombre)
    
    

    public static void enviarCodigoRecuperacion(String destinatario, String codigo) {
    	/**
         *24/05/2025
         *Programador Javi 
         *v1.0 envia correo de recuperacion
         *@param destinatario
         *@param codigo
         * salida void
         */
        String asunto = "Recuperación de cuenta - CollectEX";
        String cuerpo = "Tu código de recuperación es: " + codigo + "\n\nIntroduce este código en la aplicación para continuar.";
        EnviaCorreo.enviar(destinatario, asunto, cuerpo);
    }//public static void enviarCorreoRegistro(String destinatario, String nombre) 
    
    
    //enviar correo asecas
    public static void enviar(String destinatario, String asunto, String cuerpo) {
    	/**
         *24/05/2025
         *Programador Javi 
         *v1.0 envia correo
         *@param destinatario
         *@param asunto
         *@param cuerpo
         * salida void
         */
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CONTRASENA);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("Correo enviado a " + destinatario);
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
        }
    }// public static void enviar(String destinatario, String asunto, String cuerpo)
}
