package modelos;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasenaHash;

    public Usuario(int idUsuario, String nombre, String correo, String contrasenaHash) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
    }

    public int getIdUsuario() {
    	return idUsuario; 
    }
    
    public void setIdUsuario(int idUsuario) {
    	this.idUsuario = idUsuario; 
    }

    public String getNombre() {
    	return nombre; 
    }
    
    public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }

    public String getCorreo() { 
    	return correo; 
    }
    
    public void setCorreo(String correo) {
    	this.correo = correo; 
    }

    public String getContrasenaHash() { 
    	return contrasenaHash; 
    }
    
    public void setContrasenaHash(String contrasenaHash) {
    	this.contrasenaHash = contrasenaHash; 
    }
}
