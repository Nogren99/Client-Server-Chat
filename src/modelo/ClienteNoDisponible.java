package modelo;

import java.io.Serializable;

public class ClienteNoDisponible implements Serializable{
	private String nombre;

	
	
	public ClienteNoDisponible(String nombre) {
		super();
		this.nombre = nombre;
	}



	public String getNombre() {
		return nombre;
	}
	
}
