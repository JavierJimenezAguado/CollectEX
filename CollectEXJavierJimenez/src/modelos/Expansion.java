package modelos;

public class Expansion {
    private String codigoExpansion;
    private String nombre;
    private String idioma;

    public Expansion(String codigoExpansion, String nombre, String idioma) {
        this.codigoExpansion = codigoExpansion;
        this.nombre = nombre;
        this.idioma = idioma;
    }

    public String getCodigoExpansion() {
    	return codigoExpansion; 
    }
    
    public void setCodigoExpansion(String codigoExpansion) { 
    	this.codigoExpansion = codigoExpansion; 
    }

    public String getNombre() { 
    	return nombre; 
    }
    
    public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }

    public String getIdioma() { 
    	return idioma; 
    }
    
    public void setIdioma(String idioma) { 
    	this.idioma = idioma; 
    }
    
}
