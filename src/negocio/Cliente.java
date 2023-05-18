package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import controlador.ControladorCliente;
import modelo.MensajeCliente;
import modelo.Usuario;

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

    public void conectar(String host, int puerto) { //que cuando lo conecte envie un objeto al servidor con los datos del cliente que se unio?
    	//asi podemos armar la lista y conocer su puerto (el puerto e ip que usamos en este metodo es del servidor)
    	//out.println(objeto con datos)
    	//esto asi el servidor conoce el puerto del cliente al que quiero conectarme y poder hacer el envio del mensaje
        try {
        	
            socket = new Socket(host, puerto);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream paqueteDatos = new ObjectOutputStream(socket.getOutputStream());
            MensajeCliente datos = new MensajeCliente();
            datos.setIp(Usuario.getInstance().getIp());
            datos.setMsj(null);
            datos.setPuerto(Usuario.getInstance().getPuerto());
            datos.setName(Usuario.getInstance().getNombre());
            paqueteDatos.writeObject(datos); //envio los datos che
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