package modelos;

public class PrecioHistorico {
    private int idHistorial;
    private String referencia;
    private String jsonPrecios;

    public PrecioHistorico(int idHistorial, String referencia, String jsonPrecios) {
        this.idHistorial = idHistorial;
        this.referencia = referencia;
        this.jsonPrecios = jsonPrecios;
    }

	public int getIdHistorial() {
		return idHistorial;
	}

	public void setIdHistorial(int idHistorial) {
		this.idHistorial = idHistorial;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getJsonPrecios() {
		return jsonPrecios;
	}

	public void setJsonPrecios(String jsonPrecios) {
		this.jsonPrecios = jsonPrecios;
	}

    
}
