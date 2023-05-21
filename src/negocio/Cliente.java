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
    ObjectOutputStream paqueteDatos;
    
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
            this.socket = new Socket(host, 1);
            System.out.println("socket "+socket + "histo: "+host+" puerot "+puerto);
            
            
           // this.flujoSalida = new ObjectOutputStream(socket.getOutputStream());
          //  this.flujoEntrada = new ObjectInputStream(socket.getInputStream());
            
            
            
            System.out.println("2");
            
            paqueteDatos = new ObjectOutputStream(socket.getOutputStream());
            MensajeCliente datos = new MensajeCliente();
            datos.setIp(Usuario.getInstance().getIp());
            datos.setMsj(null);
            datos.setPuerto(Usuario.getInstance().getPuerto());
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

    public void enviarMensaje(String mensaje) {
    	this.paqueteMsj.setMsj(mensaje);
        try {
			this.flujoSalida = new ObjectOutputStream(socket.getOutputStream());
			this.flujoSalida.writeObject(paqueteMsj);
			flujoSalida.flush();
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
    

    public MensajeCliente recibirMensaje() {
        try {
        	this.flujoEntrada = new ObjectInputStream(socket.getInputStream());
        	paqueteRecibido = (MensajeCliente) this.flujoEntrada.readObject();
            return paqueteRecibido;
        } catch (IOException | ClassNotFoundException e) {
            // Manejar la excepción apropiadamente
        }
        return null;
    }

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
        	while (true) {
        		
				// Usa el objeto cliente recibido según sea necesario
        		System.out.println("\n ====================================");
        		System.out.println("hoolaaaaa "+ this.socket.getInputStream());
        		ObjectInputStream hashMapInputStream = new ObjectInputStream(this.socket.getInputStream());
        		System.out.println("llegpo");

        		// Lee el objeto HashMap del segundo ObjectInputStream
        		Object object =   hashMapInputStream.readObject();
        		
        		if (object.getClass()==HashMap.class) {
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
        		} else if (object.getClass()==SolicitudMensaje.class) {
        			SolicitudMensaje solicitud = (SolicitudMensaje) object;
        			ObjectOutputStream flujoSalida = new ObjectOutputStream(this.socket.getOutputStream());
        			int dialogButton = JOptionPane.showConfirmDialog (null, solicitud.getNombre() + " quiere iniciar una conversación contigo. ¿Aceptar?","WARNING", 0); //0 es si, 1 es no
        			if (dialogButton ==0) { // si
        				flujoSalida.writeObject(true); //cambiar        				
        				ControladorCliente.getInstancia().ventanaChat(); // abro ventana chat
        			} else { // no
        				flujoSalida.writeObject(false);
        			}
        		} else if (object instanceof Boolean) {
        			boolean bool = (boolean) object;
        			if (bool) {
        				ControladorCliente.getInstancia().ventanaChat();
        			} else {
        				JOptionPane.showMessageDialog(null, "Tu solicitud ha sido rechazada :(");
        			}
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
	
	
	
}