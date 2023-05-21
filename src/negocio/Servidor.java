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
					System.out.println("Conexion establecida!!! Iniciando hilo \n");
					System.out.println("Socket: "+ socket.toString());
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
            	System.out.println("Entrando al run de servidor, ahora intentaremos leer objetos ^^ ");
            	ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
        		ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
            	while (true) {	
            		
            		
            		System.out.println(" \n RUN ESCUCHARCLIENTE ");

            		
                    Object object = flujoEntrada.readObject();
                    
                    System.out.println("Objeto recibido: "+ object.toString());
                    if (object instanceof MensajeCliente) {
                    	MensajeCliente datos = (MensajeCliente) object;
                    	System.out.println("=========================");
                    	System.out.println(datos.getName()+ datos.getPuerto());
                    	Servidor.getInstancia().addCliente(datos.getName(), datos.getPuerto());
                    	//if (Servidor.getInstancia().isSecambio()) {
	                		for (int k=0; k < sockets.size() ; k++) { 			
			                    ObjectOutputStream listaClientes = new ObjectOutputStream(sockets.get(k).getOutputStream());
			                    System.out.println("MANDE ESTO:"+listaClientes+" por "+sockets.get(k).getOutputStream());
			                    listaClientes.writeObject(Servidor.getInstancia().getClientes());
			                    listaClientes.flush();
	                		}
		                    //Servidor.getInstancia().setSecambio(false);	
		                    System.out.println("=============Iterator============");
	                		Iterator<Map.Entry<String, Integer>> iterator = clientes.entrySet().iterator();
		                    while (iterator.hasNext()) {
		                        Map.Entry<String, Integer> entry = iterator.next();
		                        String nombre = entry.getKey();
		                        Integer puerto = entry.getValue();
		                        System.out.println("Cliente: " + nombre + ", Puerto: " + puerto);
		                    }		
	                	}
                	
                
                	//}
                      else if (object instanceof SolicitudMensaje) {
                    	  // nombre del usuario al que le escribo ----- nombre mio
                    	System.out.println("RECIBI SOLICITUD!!!! :O");
                    	SolicitudMensaje soli = (SolicitudMensaje) object;
                    	System.out.println(Arrays.asList(clientes));
                    	
                    	System.out.println(Servidor.getInstancia().getClientes().get(soli.getNombre()));
                    	int puerto = Servidor.getInstancia().getClientes().get(soli.getNombre());
                    	int i=0;
                    	while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                    		System.out.println("Sochet numero "+ i + " Puerto :" +sockets.get(i).getLocalPort());
                    		i++;
                    		//soli (nombre q busco)
                    		// 
                    	}
                    	
                    	socketSolicitante= sockets.get(i); //cambiar esto luego
                    	
                    	try {
                			flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
                			flujoSalida.writeObject(new SolicitudMensaje(soli.getNombre(),soli.getNombrePropio())); //cambiar 
                			System.out.println("Solicitud enviada!");
                		} catch (IOException e) {
                			
                		} 
                    	
                		
                      }else if (object.getClass()==boolean.class){
                    	System.out.println("El servidor recibió la rta de confirmación!!");
                    	ObjectOutputStream salidaConfirmacion = new ObjectOutputStream(socketSolicitante.getOutputStream());
                      	boolean rta = (boolean) object;
                      	System.out.println("La rta de la confirmacion "+ rta + "llegó al server");
                      	salidaConfirmacion.writeObject(rta);
  
                      } else {
                    	System.out.println("recibi cualquier cosa");
                    	System.out.println(object.toString());
                    }
            	}
            } catch (IOException | ClassNotFoundException e) {

            }
            
        }
    }
                /*ObjectInputStream paquete = new ObjectInputStream(this.socket.getInputStream());
                Object object = paquete.readObject();
                System.out.println("Objeto recibido: "+ object.toString());
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
                } else if (object instanceof SolicitudMensaje) {
                	SolicitudMensaje soli = (SolicitudMensaje) object;
                	System.out.println(Arrays.asList(clientes));
                	System.out.println(this.clientes.get(soli.getNombre()));
                	int puerto = this.clientes.get(soli.getNombre());
                	int i=0;
                	while (i<sockets.size() && sockets.get(i).getPort()!=puerto) {
                		System.out.println("Puerto numero "+ i + "  :" +puerto);
                		i++;
                	}
                	
                	this.socketSolicitante= sockets.get(i); //cambiar esto luego
                	
                	try {
            			ObjectOutputStream flujoSalida = new ObjectOutputStream(sockets.get(i).getOutputStream());
            			flujoSalida.writeObject(new SolicitudMensaje(null,soli.getNombrePropio())); //cambiar 
            			System.out.println("Solicitud enviada!");
            		} catch (IOException e) {
            			
            		}
                }
            }*/
           
         
            // Manejar la excepción apropiadamente
        
    
    
    
  /*  public void solicitudChat(String nombre, String nombrePropio) {
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
    	
    	
    } */
    
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


		private class ObjetosRecibidos implements Runnable {

			
			
			
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
					
		}




   
}