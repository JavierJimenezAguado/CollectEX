package modelos;

public class Sobre {
    private int idSobre;
    private String nombre;
    private String urlPriceCharting;
    private Double precio;
	
    public Sobre(int idSobre, String nombre, String urlPriceCharting, Double precio) {
		super();
		this.idSobre = idSobre;
		this.nombre = nombre;
		this.urlPriceCharting = urlPriceCharting;
		this.precio = precio;
	}

	public int getIdSobre() {
		return idSobre;
	}

	public void setIdSobre(int idSobre) {
		this.idSobre = idSobre;
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
