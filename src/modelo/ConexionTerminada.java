package modelo;

import java.io.Serializable;

public class ConexionTerminada implements Serializable {
	private String nombreDestinatario;

	public String getNombreDestinatario() {
		return nombreDestinatario;
	}

	public ConexionTerminada(String nombreDestinatario) {
		super();
		this.nombreDestinatario = nombreDestinatario;
	}
	
	

}
