package modelos;

public class SobreCajaUsuario {

    private int id;
    private int idUsuario;
    private int idElemento;
    private String nombre;
	private String tipo; 
    private double precio;
    private Double alertaMin;
    private Double alertaMax;
    
    
    public SobreCajaUsuario(int id, int idUsuario, int idElemento, String nombre, String tipo, double precio,
			Double alertaMin, Double alertaMax) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.idElemento = idElemento;
		this.nombre = nombre;
		this.tipo = tipo;
		this.precio = precio;
		this.alertaMin = alertaMin;
		this.alertaMax = alertaMax;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getIdUsuario() {
		return idUsuario;
	}


	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}


	public int getIdElemento() {
		return idElemento;
	}


	public void setIdElemento(int idElemento) {
		this.idElemento = idElemento;
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


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
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
