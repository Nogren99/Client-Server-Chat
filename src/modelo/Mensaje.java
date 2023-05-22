package modelo;

import java.io.Serializable;

public class Mensaje implements Serializable {
	byte[] mensaje;
	String nombreMio;
	String nombreDestinatario;
	
	public byte[] getMensaje() {
		return mensaje;
	}
	public String getNombreMio() {
		return nombreMio;
	}
	public String getNombreDestinatario() {
		return nombreDestinatario;
	}
	
	public Mensaje(byte[] textoEncriptado, String nombreMio, String nombreDestinatario) {
		super();
		this.mensaje = textoEncriptado;
		this.nombreMio = nombreMio;
		this.nombreDestinatario = nombreDestinatario;
	}
	@Override
	public String toString() {
		return "Mensaje [mensaje=" + mensaje + ", nombreMio=" + nombreMio + ", nombreDestinatario=" + nombreDestinatario
				+ "]";
	}

	
	
	

}
