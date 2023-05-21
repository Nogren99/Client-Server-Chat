package modelo;

import java.io.Serializable;

public class SolicitudMensaje implements Serializable {
	private String nombre;
	private String nombrePropio;
	
	public SolicitudMensaje(String nombre, String nombrePropio) {
		super();
		this.nombre = nombre;
		this.nombrePropio=nombrePropio;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNombrePropio() {
		return nombrePropio;
	}
	
	
	

}
