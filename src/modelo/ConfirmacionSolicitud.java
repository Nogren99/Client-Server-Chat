package modelo;

import java.io.Serializable;

public class ConfirmacionSolicitud implements Serializable {
	private boolean confirmacion;
	private String nombreSolicitante;

	public ConfirmacionSolicitud(boolean confirmacion, String nombreSolicitante) {
		super();
		this.confirmacion = confirmacion;
		this.nombreSolicitante = nombreSolicitante;
	}

	public boolean isConfirmacion() {
		return confirmacion;
	}

	public String getNombreSolicitante() {
		return nombreSolicitante;
	}

}
