package modelo;

import java.io.Serializable;

public class ActualizarLista implements Serializable{
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public ActualizarLista(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	

}
