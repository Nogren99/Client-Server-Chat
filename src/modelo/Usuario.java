package modelo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import modelo.Usuario;

public class Usuario implements Runnable {
	
	public static Usuario instancia;
	private String nombre;
	private String ip;
	private static int puerto;
	
	
	public static Usuario getInstance()  {
        if (instancia == null)
            instancia = new Usuario();
        return instancia;
    }
	
	
	



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int port) {
		this.puerto = port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
