package modelo;

import java.io.Serializable;

public class Mensaje implements Serializable {
	String mensaje;
	String nombreMio;
	String nombreDestinatario;
	
	public String getMensaje() {
		return mensaje;
	}
	public String getNombreMio() {
		return nombreMio;
	}
	public String getNombreDestinatario() {
		return nombreDestinatario;
	}
	
	public Mensaje(String mensaje, String nombreMio, String nombreDestinatario) {
		super();
		this.mensaje = mensaje;
		this.nombreMio = nombreMio;
		this.nombreDestinatario = nombreDestinatario;
	}
	@Override
	public String toString() {
		return "Mensaje [mensaje=" + mensaje + ", nombreMio=" + nombreMio + ", nombreDestinatario=" + nombreDestinatario
				+ "]";
	}

	
	
	

}
