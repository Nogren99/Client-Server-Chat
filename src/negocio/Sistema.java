package negocio;
import java.io.*;
import java.net.*;

import controlador.ControladorSistema;
import modelo.Usuario;

public class Sistema implements Runnable {

	private static Sistema instancia;
	private static ControladorSistema controlador;
	private ServerSocket servidor = null;
	private Usuario user = Usuario.getInstance();
	private ServerSocket socketServer;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private InputStreamReader  inSocket;
	boolean escucha;
	
	public static Sistema getInstancia() {
		if (instancia == null)
			instancia = new Sistema();
	    return instancia;
	}
	
    public void solicitarChat(String ip, int puerto) {
    	System.out.println(this.user.getNombre()+" | meotod solicitarChat | "+ "ip:"+ip+" puerto :"+puerto);
    	try {
    		this.socket = new Socket(ip,puerto);
            this.inSocket= new InputStreamReader(socket.getInputStream());
            this.in = new BufferedReader(inSocket);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.out.println(Usuario.getInstance().getNombre());
            ControladorSistema.getInstancia().ventanaChat();
    	}catch (IOException e) {
    	}
    }
    
    public void enviarMensaje(String mensaje) throws IOException {
        this.out.println(mensaje);
        System.out.println(this.user.getNombre()+" |metodo enviarMensaje| : "+mensaje);
    }

    @Override
    public void run() {
        try {
        	//ACTIVO MODO ESCUCHA
        	System.out.println("Modo escucha activado.");
        	this.socketServer = new ServerSocket(this.user.getPuerto());
        	System.out.println(this.user.getPuerto());
        	ControladorSistema.getInstancia().ventanaEspera();
            this.socket = socketServer.accept();
            
            socketServer.close();
            socketServer = null;
            
            //INICIO ENTRADAS Y SALIDAS
            this.inSocket= new InputStreamReader(socket.getInputStream());
            this.in = new BufferedReader(inSocket);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.out.println(Usuario.getInstance().getNombre());
            System.out.println("mi nombre es "+this.user.getNombre()+" y me conecte con mi amiguito "+ this.in.readLine());

            ControladorSistema.getInstancia().ventanaChat();
  
        }catch (IOException e) {
        }
    }

	public BufferedReader getIn() {
		return in;
	}

	public Socket getSocket() {
		return socket;
	}

	public String recibirMensaje() {
		String mensaje="";
		try {
			mensaje = this.in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.user.getNombre()+" |metodo recibirMensaje: | "+mensaje);
		return mensaje;
	}

	public void cerrarSockets() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.socket = null;
        this.out = null;
        this.in = null;
        this.inSocket = null;
	}
	    
}