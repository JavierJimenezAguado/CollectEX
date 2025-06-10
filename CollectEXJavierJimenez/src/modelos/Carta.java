package modelos;

import java.time.LocalDateTime;
import java.util.Map;

public class Carta {
    private String referencia;
    private String nombre;
    private String tipo;
    private String urlCardMarket;
    private String urlPriceCharting;
    private String codigoExpansion;
    private double precio;
    private Map<LocalDateTime, Map<Integer, Double>> historialPrecios;


    public Carta(String referencia, String nombre, String tipo, String urlCardMarket, String urlPriceCharting,
			String codigoExpansion, double precio, Map<LocalDateTime, Map<Integer, Double>> historialPrecios) {
		super();
		this.referencia = referencia;
		this.nombre = nombre;
		this.tipo = tipo;
		this.urlCardMarket = urlCardMarket;
		this.urlPriceCharting = urlPriceCharting;
		this.codigoExpansion = codigoExpansion;
		this.precio = precio;
		this.historialPrecios = historialPrecios;
	}

	public String getReferencia() { 
    	return referencia; 
    }
    
    public void setReferencia(String referencia) { 
    	this.referencia = referencia; 
    }

    public String getNombre() { 
    	return nombre;
    }
    
    public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }

    public String getTipo() {
    	return tipo; 
    }
    public void setTipo(String tipo) { 
    	this.tipo = tipo; 
    }

    public String getUrlCardMarket() { 
    	return urlCardMarket;
    }
    public void setUrlCardMarket(String urlCardMarket) { 
    	this.urlCardMarket = urlCardMarket; 
    }

    public String getUrlPriceCharting() { 
    	return urlPriceCharting;
    }
    
    public void setUrlPriceCharting(String urlPriceCharting) {
    	this.urlPriceCharting = urlPriceCharting; 
    }

    public String getCodigoExpansion() { 
    	return codigoExpansion; 
    }
    public void setCodigoExpansion(String codigoExpansion) {
    	this.codigoExpansion = codigoExpansion;
    }

    public double getPrecio() { 
    	return precio; 
    }
    public void setPrecio(double precio) {
    	this.precio = precio; 
    }

    public Map<LocalDateTime, Map<Integer, Double>> getHistorialPrecios() { 
    	return historialPrecios; 
    }
    
    public void setHistorialPrecios(Map<LocalDateTime, Map<Integer, Double>> historialPrecios) {
        this.historialPrecios = historialPrecios;
    }
}
