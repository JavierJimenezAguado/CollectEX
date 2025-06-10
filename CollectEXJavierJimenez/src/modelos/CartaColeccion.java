package modelos;

public class CartaColeccion {

    private int idColeccion;
    private String referencia;
    private int cantidad;      
    private int graduacion; 
    private double precioPagado;
    private Double alertaMin;   
    private Double alertaMax;
    
    
	public CartaColeccion(int idColeccion, String referencia, int cantidad, int graduacion, double precioPagado,
			Double alertaMin, Double alertaMax) {
		super();
		this.idColeccion = idColeccion;
		this.referencia = referencia;
		this.cantidad = cantidad;
		this.graduacion = graduacion;
		this.precioPagado = precioPagado;
		this.alertaMin = alertaMin;
		this.alertaMax = alertaMax;
	}


	public int getIdColeccion() {
		return idColeccion;
	}


	public void setIdColeccion(int idColeccion) {
		this.idColeccion = idColeccion;
	}


	public String getReferencia() {
		return referencia;
	}


	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}


	public int getCantidad() {
		return cantidad;
	}


	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public int getGraduacion() {
		return graduacion;
	}


	public void setGraduacion(int graduacion) {
		this.graduacion = graduacion;
	}


	public double getPrecioPagado() {
		return precioPagado;
	}


	public void setPrecioPagado(double precioPagado) {
		this.precioPagado = precioPagado;
	}


	public Double getAlertaMin() {
		return alertaMin;
	}


	public void setAlertaMin(Double alertaMin) {
		this.alertaMin = alertaMin;
	}


	public Double getAlertaMax() {
		return alertaMax;
	}


	public void setAlertaMax(Double alertaMax) {
		this.alertaMax = alertaMax;
	}   
	
	
	
    
    
    
}
