package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import controlador.ControladorCliente;
import controlador.ControladorServidor;
import modelo.MensajeCliente;
import modelo.SolicitudMensaje;
import modelo.Usuario;

public class Servidor implements Runnable {
    private static Servidor instancia;
    private static ControladorServidor controlador;
    
    private boolean secambio=false;
    private Socket socketSolicitante;

    private Usuario user;
    private MensajeCliente msj;
    private ServerSocket socketServer;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private InputStreamReader inSocket;
    private ArrayList<Socket> sockets = new ArrayList<Socket>();
    private HashMap<String, Integer> clientes; //Nombre / puerto

    private Servidor() {
        user = Usuario.getInstance();
        clientes = new HashMap<>();
    }

    public static Servidor getInstancia() {
        if (instancia == null) {
            instancia = new Servidor();
        }
        return instancia;
    }
    
    public void addCliente(String nombre, int puerto) {
    	this.clientes.put(nombre, puerto);
    }
    
	public Socket getSocket() {
		return socket;
	}

    @Override
    public void run() {
        try {
            this.socketServer = new ServerSocket(user.getPuerto());
            System.out.println("Servidor iniciado. Puerto: " + user.getPuerto());
            controlador.getInstancia().ventanaEspera();

            while (true) {
                socket = socketServer.accept();
                sockets.add(socket);
                ObjectInputStream paquete = new ObjectInputStream(this.socket.getInputStream());
                Object object = paquete.readObject();
              //  this.msj = (MensajeCliente) paquete.readObject();
                if (object instanceof MensajeCliente) {    //hacer otro objeto despues para que lea los datos
                	this.msj = (MensajeCliente) object;
                	Servidor.getInstancia().addCliente(msj.getName(), msj.getPuerto());
	               // this.clientes.put(msj.getName(), msj.getPuerto() );
	                this.secambio=true;
	                System.out.println("Nuevo cliente conectado: " + socket.getInetAddress().getHostAddress());
	                // Crear un hilo para manejar la conexión entrante
	                Thread clientThread = new Thread(new EscucharCliente(socket));
	                clientThread.start();
                } else if (object instanceof Boolean) {
                	ObjectOutputStream salidaConfirmacion = new ObjectOutputStream(this.socketSolicitante.getOutputStream());
                	boolean rta = (boolean) object;
                	salidaConfirmacion.writeObject(rta);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            // Manejar la excepción apropiadamente
        }
    }
    
    
    public void solicitudChat(String nombre, String nombrePropio) {
    	System.out.println(Arrays.asList(clientes));
    	System.out.println(this.clientes.get(nombre));
    	int puerto = this.clientes.get(nombre);
    	int i=0;
    	while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
    		System.out.println("Puerto numero "+ i + "  :" +puerto);
    		i++;
    	}
    	
    	this.socketSolicitante= sockets.get(i); //cambiar esto luego
    	
    	try {
			ObjectOutputStream flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
			flujoSalida.writeObject(new SolicitudMensaje(nombrePropio)); //cambiar 
			System.out.println("Solicitud enviada!");
		} catch (IOException e) {
			
		}
    	
    	
    }
    
    /*
    public void enviarObjetoACliente(String nombreCliente, Object objeto) {
        if (flujosSalida.containsKey(nombreCliente)) {
            try {
                ObjectOutputStream flujoSalida = flujosSalida.get(nombreCliente);
                flujoSalida.writeObject(objeto);
                flujoSalida.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    
    
    

    // Clase interna para manejar las conexiones entrantes

        public static ControladorServidor getControlador() {
		return controlador;
	}

	public Usuario getUser() {
		return user;
	}

	public ServerSocket getSocketServer() {
		return socketServer;
	}

	public PrintWriter getOut() {
		return out;
	}

	public BufferedReader getIn() {
		return in;
	}

	public InputStreamReader getInSocket() {
		return inSocket;
	}

	public HashMap<String, Integer> getClientes() {
		return clientes;
	}






		public boolean isSecambio() {
		return secambio;
	}

	public void setSecambio(boolean secambio) {
		this.secambio = secambio;
	}






		private class EscucharCliente implements Runnable {
            private Socket cliente;
            private ObjectInputStream flujoEntrada;
            private ObjectOutputStream flujoSalida;
            private String nombreCliente;

            public EscucharCliente(Socket cliente) {
                this.cliente = cliente;
                try {
                    flujoEntrada = new ObjectInputStream(cliente.getInputStream());
                    flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void run() {
                try {
                	
                	while (true) {		
	                	if (Servidor.getInstancia().isSecambio()) {
	                		for (int k=0; k < sockets.size() ; k++) {
	                			System.out.println("La lista fue cambiada, actualizando...");
			                    ObjectOutputStream listaClientes = new ObjectOutputStream(sockets.get(k).getOutputStream());
			                    listaClientes.writeObject(Servidor.getInstancia().getClientes());
			                    listaClientes.flush();
			                                                                
 	
	                		}
	                		System.out.println("Seteamos secambió a falso");
		                    Servidor.getInstancia().setSecambio(false);	
	                		Iterator<Map.Entry<String, Integer>> iterator = clientes.entrySet().iterator();
		                    while (iterator.hasNext()) {
		                        Map.Entry<String, Integer> entry = iterator.next();
		                        String nombre = entry.getKey();
		                        Integer puerto = entry.getValue();
		                      //  System.out.println("Cliente: " + nombre + ", Puerto: " + puerto);
		                    }		
                	}
	                	
	                
	                	
                    }
                } catch (IOException e) {

                }
            }
        }
   
}