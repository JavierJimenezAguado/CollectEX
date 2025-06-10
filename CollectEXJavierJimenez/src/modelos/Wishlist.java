package modelos;

import java.util.List;

public class Wishlist {
    private int idWishlist;
    private int idUsuario;
    private String nombre;
    private List<WishlistItem> items;

    public Wishlist(int idWishlist, int idUsuario, String nombre, List<WishlistItem> items) {
        this.idWishlist = idWishlist;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.items = items;
    }

    public int getIdWishlist() { 
    	return idWishlist; 
    }
    public void setIdWishlist(int idWishlist) { 
    	this.idWishlist = idWishlist; 
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

    public List<WishlistItem> getItems() {
    	return items; 
    }
    public void setItems(List<WishlistItem> items) {
    	this.items = items;
    }
}

