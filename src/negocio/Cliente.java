package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import controlador.ControladorCliente;
import modelo.ClienteNoDisponible;
import modelo.ConfirmacionSolicitud;
import modelo.Mensaje;
import modelo.MensajeCliente;
import modelo.SolicitudMensaje;
import modelo.Usuario;

public class Cliente implements Runnable{
	private static Cliente instancia;
    private Socket socket;
    private MensajeCliente paqueteMsj = new MensajeCliente();
    private ObjectOutputStream flujoSalida;
    private ObjectInputStream flujoEntrada;
    private MensajeCliente paqueteRecibido;
    private String nombreInterlocutor;
    ObjectOutputStream paqueteDatos;
    private boolean aceptada = false;
    private boolean estoyEnLlamada=false;
    
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
        	System.out.println("0");
            this.socket = new Socket(host, 1); //acordarse q el server esta harcodeado en 1
            System.out.println("socket "+socket + "histo: "+host+" puerot "+puerto);
            
            
           // this.flujoSalida = new ObjectOutputStream(socket.getOutputStream());
          //  this.flujoEntrada = new ObjectInputStream(socket.getInputStream());
            
            
            
            System.out.println("2");
            
            paqueteDatos = new ObjectOutputStream(socket.getOutputStream());
            MensajeCliente datos = new MensajeCliente();
            datos.setIp(Usuario.getInstance().getIp());
            datos.setMsj(null);
            datos.setPuerto(socket.getLocalPort());
            datos.setName(Usuario.getInstance().getNombre());
            paqueteDatos.writeObject(datos);
            paqueteDatos.flush();
            
            //this.flujoSalida = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("SALIDITA1 "+flujoSalida);
            this.flujoEntrada = new ObjectInputStream(socket.getInputStream());
            System.out.println("3");
            ControladorCliente.getInstancia().ventanaEspera();//ventana sala de espera con listita
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void enviarMensaje(String mensaje, String nombre, String nombreDestinatario) {
        try {
        	System.out.println("Enviando mensaje "+ mensaje + "al servidor");
			//this.paqueteDatos = new ObjectOutputStream(socket.getOutputStream());
			paqueteDatos.writeObject(new Mensaje(mensaje,nombre,nombreDestinatario));
			paqueteDatos.flush();
			//flujoSalida.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    }
    /*
    public void esperarConexion() {
        try {
            while (true) {
                Object objeto = flujoEntrada.readObject();
                if (objeto instanceof MensajeCliente) {
                    MensajeCliente mensaje = (MensajeCliente) objeto;
                    System.out.println("Mensaje recibido: " + mensaje.getMsj());
                    // Procesar el mensaje recibido
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Manejar la excepción apropiadamente
        }
    }*/
    

  /*  public MensajeCliente recibirMensaje() {
        try {
        	this.flujoEntrada = new ObjectInputStream(socket.getInputStream());
        	paqueteRecibido = (MensajeCliente) this.flujoEntrada.readObject();
            return paqueteRecibido;
        } catch (IOException | ClassNotFoundException e) {
            // Manejar la excepción apropiadamente
        }
        return null;
    } */

    public void desconectar() {
        try {
            flujoEntrada.close();
            flujoSalida.close();
            socket.close();
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
    }
    



	public void setDatos(String ip, int puerto, String nombre) {
		this.paqueteMsj.setIp(ip);
		this.paqueteMsj.setPuerto(puerto);
		this.paqueteMsj.setName(nombre);
		
	}
	
	
	
    public Socket getSocket() {
		return socket;
	}

	public void run() {
        try {
        	System.out.println("hoolaaaaa "+ this.socket.getInputStream());
    		//ObjectInputStream hashMapInputStream = new ObjectInputStream(this.socket.getInputStream());
    		System.out.println("llegpo");
        	while (true) {
        		
				// Usa el objeto cliente recibido según sea necesario
        		System.out.println("\n ====================================");
        		ObjectInputStream hashMapInputStream = new ObjectInputStream(this.socket.getInputStream());

        		// Lee el objeto HashMap del segundo ObjectInputStream
        		Object object =   hashMapInputStream.readObject();
        		
        		if (object.getClass()==HashMap.class) {     //Me llega un hashmap actualizado con todos los usuarios que tiene nuestro sistema para actualizar la lista
        			HashMap<String, Integer> clientesRecibidos = (HashMap<String, Integer>) object;
        		
	        		System.out.println("recibi esto"+hashMapInputStream);
	
	        		System.out.println("barrilete cosmico el diego"+clientesRecibidos);
	        		
	                Iterator<Map.Entry<String, Integer>> iterator = clientesRecibidos.entrySet().iterator();
	
	                while (iterator.hasNext()) {
	                    Map.Entry<String, Integer> entry = iterator.next();
	                    String nombre = entry.getKey();
	                    Integer puerto = entry.getValue();
	                    System.out.println("Cliente: " + nombre + ", Puerto: " + puerto);
	                }
	                
	                ControladorCliente.getInstancia().actualizaLista( (HashMap) clientesRecibidos);
        		} else if (object.getClass()==SolicitudMensaje.class) {   //Me llega una solicitud de chat de otro usuario
        			SolicitudMensaje solicitud = (SolicitudMensaje) object;
        			System.out.println("Estoy en llamada: "+ this.estoyEnLlamada);
        			if (this.estoyEnLlamada) {
        				paqueteDatos.writeObject(new ClienteNoDisponible(solicitud.getNombrePropio()));
        			} else {
	        			//ObjectOutputStream flujoSalida = new ObjectOutputStream(this.socket.getOutputStream());
	        			int dialogButton = JOptionPane.showConfirmDialog (null, solicitud.getNombrePropio() + " quiere iniciar una conversación contigo. ¿Aceptar?","WARNING", 0); //0 es si, 1 es no
	        			if (dialogButton ==0) { // si
	        				System.out.println("CONFIRMADO PAPA");
	        				ControladorCliente.getInstancia().setSolicitante(false);
	        				this.nombreInterlocutor=solicitud.getNombrePropio();
	        				paqueteDatos.writeObject(new ConfirmacionSolicitud(true,solicitud.getNombrePropio()));    //escribir con este o con flujoSalida???				
	        				ControladorCliente.getInstancia().ventanaChat(); 
	        				this.estoyEnLlamada=true;
	        			} else { // no
	        				paqueteDatos.writeObject(new ConfirmacionSolicitud(false,solicitud.getNombrePropio()));  //escribir con este o con flujoSalida???	
	        			}
        			}
        		} else if (object instanceof Boolean) { //Me llega la confirmación de la solicitud de chat que envié anteriormenta
        			boolean bool = (boolean) object;
        			System.out.println("La rta de la confirmación "+ bool  + "llegó al cliente ");
        			if (bool) {
        				ControladorCliente.getInstancia().setSolicitante(true);
        				this.aceptada=true;
        				this.estoyEnLlamada=true;
        				ControladorCliente.getInstancia().ventanaChat();
        			} else {
        				JOptionPane.showMessageDialog(null, "Tu solicitud ha sido rechazada :(");
        			}
        		}else if (object instanceof Mensaje){   //Me llega un mensaje
        			Mensaje mensaje = (Mensaje) object;
        			ControladorCliente.getInstancia().actualizaChat(mensaje.getNombreMio(), mensaje.getMensaje());
        			
        		} else if (object instanceof ClienteNoDisponible){
        			JOptionPane.showMessageDialog(null, "El usuario no está disponible!");
        		} else {
        			System.out.println("MANDASTE CUALQUIERA");
        		}
        		
            }
            
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {} 
    }

	public void solicitudChat(String nombre, String nombrePropio) { 
		try {
			System.out.println("Entrando a solicitud chat");
			System.out.println("socket "+socket );
			SolicitudMensaje msj = new SolicitudMensaje(nombre,nombrePropio);
			System.out.println("SALIDITA2 "+ this.flujoSalida);
			this.paqueteDatos.writeObject( (SolicitudMensaje) msj);  
			this.paqueteDatos.flush();
			System.out.println("Finalizando solicitud chat");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	

	public String getNombreInterlocutor() {
		return nombreInterlocutor;
	}

	public boolean isAceptada() {
		return aceptada;
	}
	
	
	
}