package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import controlador.ControladorCliente;

public class Cliente {
	private static Cliente instancia;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
	public static Cliente getInstancia() {
		if (instancia == null)
			instancia = new Cliente();
	    return instancia;
	}

    public void conectar(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ControladorCliente.getInstancia().ventanaChat();
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
    }

    public void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }

    public String recibirMensaje() {
        try {
            return in.readLine();
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
        return null;
    }

    public void desconectar() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
    }
}