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
import modelo.ActualizarLista;
import modelo.ClienteNoDisponible;
import modelo.ConexionTerminada;
import modelo.ConfirmacionSolicitud;
import modelo.Mensaje;
import modelo.MensajeCliente;
import modelo.SolicitudMensaje;
import modelo.Usuario;

public class Servidor implements Runnable {
    private static Servidor instancia;
    private static ControladorServidor controlador;
    
    private boolean secambio=false;

    private Usuario user;
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
					Thread clientThread = new Thread(new EscucharCliente(socket));
	                clientThread.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
    }
    
    private class EscucharCliente implements Runnable {
        private Socket cliente;

        public EscucharCliente(Socket cliente) {
            this.cliente = cliente;
        }

        @Override
        public void run() {
            try {
            	ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
        		ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
            	while (true) {	
            		
                    Object object = flujoEntrada.readObject();
                    
                    if (object instanceof MensajeCliente) {
                    	MensajeCliente datos = (MensajeCliente) object;
                    	Servidor.getInstancia().addCliente(datos.getName(), datos.getPuerto());

	                		for (int k=0; k < sockets.size() ; k++) { 			
			                    ObjectOutputStream listaClientes = new ObjectOutputStream(sockets.get(k).getOutputStream());
			                    listaClientes.writeObject(Servidor.getInstancia().getClientes());
			                    listaClientes.flush();
	                		}

	                		Iterator<Map.Entry<String, Integer>> iterator = clientes.entrySet().iterator();
		                    while (iterator.hasNext()) {
		                        Map.Entry<String, Integer> entry = iterator.next();
		                        String nombre = entry.getKey();
		                        Integer puerto = entry.getValue();
		                    }		
	                	}
                	
                      else if (object instanceof SolicitudMensaje) {
                    	//Estructura recibida:  (nombre del usuario al que le escribo , nombre mio)
                    	SolicitudMensaje soli = (SolicitudMensaje) object;
                    	//System.out.println(Servidor.getInstancia().getClientes().get(soli.getNombre()));
                    	int puerto = Servidor.getInstancia().getClientes().get(soli.getNombre());
                    	int i=0;
                    	while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                    		//System.out.println("Sochet numero "+ i + " Puerto :" +sockets.get(i).getLocalPort());
                    		i++;
                    	}
                    	
                    	try {
                			flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
                			flujoSalida.writeObject(new SolicitudMensaje(soli.getNombre(),soli.getNombrePropio()));
                		} catch (IOException e) {
                			
                		} 
                    	
                		
                      }else if (object instanceof ConfirmacionSolicitud){
                    	ConfirmacionSolicitud confirmacion = (ConfirmacionSolicitud) object; 
                    	int puerto = Servidor.getInstancia().getClientes().get(confirmacion.getNombreSolicitante());
                    	
                    	int i=0;
                    	while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                    		//System.out.println("Sokket numero "+ i + " Puerto :" +sockets.get(i).getLocalPort());
                    		i++;
                    	}
                    	
                    	flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
                      	flujoSalida.writeObject(confirmacion.isConfirmacion());
  
                      } else if (object instanceof Mensaje){
                    	  Mensaje mensaje = (Mensaje) object;
                    	  int puerto = Servidor.getInstancia().getClientes().get(mensaje.getNombreDestinatario());
                    	  int i=0;
                    	  
                      	  while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                      		//System.out.println("Soquet numero "+ i + " Puerto :" +sockets.get(i).getLocalPort());
                      		i++;
                      	  }
                      	  
                      	  //System.out.println("Enviando mensaje "+ mensaje.toString() + "al cliente "+ mensaje.getNombreDestinatario());
                      	  flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
                          flujoSalida.writeObject(mensaje);
                    	  
                      } else if (object instanceof ClienteNoDisponible){
                    	  ClienteNoDisponible cdp = (ClienteNoDisponible) object;
                    	  int puerto = Servidor.getInstancia().getClientes().get(cdp.getNombre());
                    	  int i=0;
                    	  
                      	  while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                      		//System.out.println("Soquet numero "+ i + " Puerto :" +sockets.get(i).getLocalPort());
                      		i++;
                      	  }
                      	  
                      	flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
                        flujoSalida.writeObject(cdp);
                    	  
                      }else if (object instanceof ConexionTerminada){
                    	  ConexionTerminada conexionTerminada = (ConexionTerminada) object;
                    	  int puerto = Servidor.getInstancia().getClientes().get(conexionTerminada.getNombreDestinatario());
                    	  int i=0;
                    	  
                      	  while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                      		//System.out.println("Soquet numero "+ i + " Puerto :" +sockets.get(i).getLocalPort());
                      		i++;
                      	  }
                      	  
                      	flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
                        flujoSalida.writeObject(conexionTerminada);
                    	  
                    	  
                      } else if (object instanceof ActualizarLista){
                    	  ActualizarLista act = (ActualizarLista) object;
                    	  
                    	  int puerto = Servidor.getInstancia().getClientes().get(act.getNombre());
                    	  int i=0;
                    	  
                      	  while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                      		//System.out.println("Soquet numero "+ i + " Puerto :" +sockets.get(i).getLocalPort());
                      		i++;
                      	  }
                      	  
                      	flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
                        flujoSalida.writeObject(Servidor.getInstancia().getClientes());
                    	  
                      } else {
                    	System.out.println(object.toString());
                    }
            	}
            } catch (IOException | ClassNotFoundException e) {

            }
            
        }
    }
               

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

   
}