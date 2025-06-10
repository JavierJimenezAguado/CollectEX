package modelos;

public class Coleccion {
	
    private int idColeccion;
    private String nombre;
    private int idUsuario;

    public Coleccion(int idColeccion, String nombre, int idUsuario) {
        this.idColeccion = idColeccion;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
    }

    public int getIdColeccion() { 
    	return idColeccion; 
    }
    
    public void setIdColeccion(int idColeccion) { 
    	this.idColeccion = idColeccion; 
    }

    public String getNombre() { 
    	return nombre; 
    }
    
    public void setNombre(String nombre) { 
    	this.nombre = nombre;
    }

    public int getIdUsuario() { 
    	return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) { 
    	this.idUsuario = idUsuario; 
    }
}
