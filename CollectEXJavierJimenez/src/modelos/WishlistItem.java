package modelos;

public class WishlistItem {
    private int idWishlistItem;
    private int idWishlist;
    private String referencia;
    private int cantidadDeseada;
    private Double precioMinimo;
    private Double precioMaximo;
    private boolean alertaMinimo;
    private boolean alertaMaximo;
    private boolean gradeada;
    private Integer nota;
    private int idUsuario; 

	public WishlistItem(int idWishlistItem, int idWishlist, String referencia, int cantidadDeseada, Double precioMinimo,
			Double precioMaximo, boolean alertaMinimo, boolean alertaMaximo, boolean gradeada, Integer nota,
			int idUsuario) {
		super();
		this.idWishlistItem = idWishlistItem;
		this.idWishlist = idWishlist;
		this.referencia = referencia;
		this.cantidadDeseada = cantidadDeseada;
		this.precioMinimo = precioMinimo;
		this.precioMaximo = precioMaximo;
		this.alertaMinimo = alertaMinimo;
		this.alertaMaximo = alertaMaximo;
		this.gradeada = gradeada;
		this.nota = nota;
		this.idUsuario = idUsuario;
	}

	public WishlistItem(int idWishlistItem, int idWishlist, String referencia, int cantidadDeseada, Double precioMinimo,
			Double precioMaximo, boolean alertaMinimo, boolean alertaMaximo, boolean gradeada, Integer nota) {
		super();
		this.idWishlistItem = idWishlistItem;
		this.idWishlist = idWishlist;
		this.referencia = referencia;
		this.cantidadDeseada = cantidadDeseada;
		this.precioMinimo = precioMinimo;
		this.precioMaximo = precioMaximo;
		this.alertaMinimo = alertaMinimo;
		this.alertaMaximo = alertaMaximo;
		this.gradeada = gradeada;
		this.nota = nota;
	}
    
	public int getIdWishlistItem() { 
    	return idWishlistItem; 
    }
    public void setIdWishlistItem(int idWishlistItem) {
    	this.idWishlistItem = idWishlistItem; 
    }

    public int getIdWishlist() { 
    	return idWishlist; 
    }
    public void setIdWishlist(int idWishlist) {
    	this.idWishlist = idWishlist;
    }

    public String getReferencia() { 
    	return referencia; 
    }
    public void setReferencia(String referencia) { 
    	this.referencia = referencia; 
    }

    public int getCantidadDeseada() { 
    	return cantidadDeseada; 
    }
    public void setCantidadDeseada(int cantidadDeseada) { 
    	this.cantidadDeseada = cantidadDeseada;
    }

    public Double getPrecioMinimo() { 
    	return precioMinimo; 
    }
    public void setPrecioMinimo(Double precioMinimo) {
    	this.precioMinimo = precioMinimo; 
    }

    public Double getPrecioMaximo() { 
    	return precioMaximo; 
    }
    public void setPrecioMaximo(Double precioMaximo) { 
    	this.precioMaximo = precioMaximo;
    }

    public boolean isAlertaMinimo() { 
    	return alertaMinimo; 
    }
    public void setAlertaMinimo(boolean alertaMinimo) {
    	this.alertaMinimo = alertaMinimo; 
    }

    public boolean isAlertaMaximo() {
    	return alertaMaximo; 
    }
    public void setAlertaMaximo(boolean alertaMaximo) {
    	this.alertaMaximo = alertaMaximo;
    }

    public boolean isGradeada() { 
    	return gradeada; 
    }
    public void setGradeada(boolean gradeada) { 
    	this.gradeada = gradeada;
    }

    public Integer getNota() { 
    	return nota; 
    }
    public void setNota(Integer nota) { 
    	this.nota = nota; 
    }
    
    public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

}
