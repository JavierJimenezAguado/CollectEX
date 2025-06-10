package modelos;

public class Caja {
    private int idCaja;
    private String nombre;
    private String urlPriceCharting;
    private Double precio;
    
	public Caja(int idCaja, String nombre, String urlPriceCharting, Double precio) {
		super();
		this.idCaja = idCaja;
		this.nombre = nombre;
		this.urlPriceCharting = urlPriceCharting;
		this.precio = precio;
	}
	public int getIdCaja() {
		return idCaja;
	}
	public void setIdCaja(int idCaja) {
		this.idCaja = idCaja;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrlPriceCharting() {
		return urlPriceCharting;
	}
	public void setUrlPriceCharting(String urlPriceCharting) {
		this.urlPriceCharting = urlPriceCharting;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}     
    
    

   
}
