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
import modelo.ActualizarLista;
import modelo.ClienteNoDisponible;
import modelo.ConexionTerminada;
import modelo.ConfirmacionSolicitud;
import modelo.Mensaje;
import modelo.MensajeCliente;
import modelo.SolicitudMensaje;
import modelo.Usuario;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cliente implements Runnable{
	private static Cliente instancia;
    private Socket socket;
    private MensajeCliente paqueteMsj = new MensajeCliente();
    private ObjectOutputStream flujoSalida;
    private ObjectInputStream flujoEntrada;
    private String nombreInterlocutor;
    ObjectOutputStream paqueteDatos;
    private boolean aceptada = false;
    private boolean estoyEnLlamada=false;
    
	public static Cliente getInstancia() {
		if (instancia == null)
			instancia = new Cliente();
	    return instancia;
	}

    public void conectar(String host, int puerto) { 
        try {
            this.socket = new Socket(host, 1); //acordarse q el server esta harcodeado en 1
            paqueteDatos = new ObjectOutputStream(socket.getOutputStream());
            MensajeCliente datos = new MensajeCliente();
            datos.setIp(Usuario.getInstance().getIp());
            datos.setMsj(null);
            datos.setPuerto(socket.getLocalPort());
            datos.setName(Usuario.getInstance().getNombre());
            paqueteDatos.writeObject(datos);
            paqueteDatos.flush();
            
            System.out.println("Mis datos : Nombre: "+Usuario.getInstance().getNombre()+" socket: "+socket + "histo: "+host+" puerot "+puerto);
            this.flujoEntrada = new ObjectInputStream(socket.getInputStream());
            ControladorCliente.getInstancia().ventanaEspera();//ventana sala de espera con listita
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void enviarMensaje(String mensaje, String nombre, String nombreDestinatario) {
        try {     	
        	//encriptacion
    		byte[] textoEncriptado = encriptar("12345678", mensaje, "DES");
    		String textoEncriptadoBase64 = Base64.getEncoder().encodeToString(textoEncriptado);
    		textoEncriptado = Base64.getDecoder().decode(textoEncriptadoBase64);

    		paqueteDatos.writeObject(new Mensaje(textoEncriptado,nombre,nombreDestinatario));
    		paqueteDatos.flush();

    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}   
    }

    public static byte[] encriptar(String pass, String texto, String algoritmo) throws Exception {
    	java.security.Key key = new SecretKeySpec(pass.getBytes(), algoritmo);
    	Cipher cipher = Cipher.getInstance(algoritmo);
    	cipher.init(Cipher.ENCRYPT_MODE, key);
    	return cipher.doFinal(texto.getBytes());
    }

    public static String desencriptar(String pass, byte[] encriptado, String algoritmo) throws Exception {
    	java.security.Key key = new SecretKeySpec(pass.getBytes(), algoritmo);
    	Cipher cipher = Cipher.getInstance(algoritmo);
    	cipher.init(Cipher.DECRYPT_MODE, key);
    	byte[] bytes = cipher.doFinal(encriptado);
    	return new String(bytes);
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
        		
        		ObjectInputStream hashMapInputStream = new ObjectInputStream(this.socket.getInputStream());
        		Object object =   hashMapInputStream.readObject();
        		
        		if (object.getClass()==HashMap.class) {     //Me llega un hashmap actualizado con todos los usuarios que tiene nuestro sistema para actualizar la lista
        			HashMap<String, Integer> clientesRecibidos = (HashMap<String, Integer>) object;	
	                Iterator<Map.Entry<String, Integer>> iterator = clientesRecibidos.entrySet().iterator();
	
	                while (iterator.hasNext()) {
	                    Map.Entry<String, Integer> entry = iterator.next();
	                    String nombre = entry.getKey();
	                    Integer puerto = entry.getValue();
	                    //System.out.println("Cliente: " + nombre + ", Puerto: " + puerto);
	                }
	                ControladorCliente.getInstancia().actualizaLista( (HashMap) clientesRecibidos);
	                
        		} else if (object.getClass()==SolicitudMensaje.class) {   //Me llega una solicitud de chat de otro usuario
        			SolicitudMensaje solicitud = (SolicitudMensaje) object;
        			if (this.estoyEnLlamada) {
        				paqueteDatos.writeObject(new ClienteNoDisponible(solicitud.getNombrePropio()));
        			} else {
	        			int dialogButton = JOptionPane.showConfirmDialog (null, solicitud.getNombrePropio() + " quiere iniciar una conversación contigo. ¿Aceptar?","Solicitud entrante", 0); //0 es si, 1 es no
	        			if (dialogButton ==0) { // si
	        				ControladorCliente.getInstancia().setSolicitante(false);
	        				this.nombreInterlocutor=solicitud.getNombrePropio();
	        				paqueteDatos.writeObject(new ConfirmacionSolicitud(true,solicitud.getNombrePropio()));    //escribir con este o con flujoSalida???				
	        				ControladorCliente.getInstancia().ventanaChatSolicitado(solicitud.getNombrePropio()); 
	        				this.estoyEnLlamada=true;
	        			} else { // no
	        				paqueteDatos.writeObject(new ConfirmacionSolicitud(false,solicitud.getNombrePropio()));  //escribir con este o con flujoSalida???	
	        			}
        			}
        		} else if (object instanceof Boolean) { //Me llega la confirmación de la solicitud de chat que envié anteriormenta
        			boolean bool = (boolean) object;
        			if (bool) {
        				ControladorCliente.getInstancia().setSolicitante(true);
        				this.aceptada=true;
        				this.estoyEnLlamada=true;
        				ControladorCliente.getInstancia().ventanaChatSolicitante();
        			} else {
        				JOptionPane.showMessageDialog(null, "Tu solicitud ha sido rechazada :(");
        			}
        		}else if (object instanceof Mensaje){   //Me llega un mensaje
        			Mensaje mensaje = (Mensaje) object;
        			//lo desencripto
        			String textoOriginal = desencriptar("12345678", mensaje.getMensaje(), "DES");
        			ControladorCliente.getInstancia().actualizaChat(mensaje.getNombreMio(), textoOriginal);
        			
        		} else if (object instanceof ClienteNoDisponible){
        			JOptionPane.showMessageDialog(null, "El usuario no está disponible!");
        		} else if (object instanceof ConexionTerminada){
        			this.estoyEnLlamada=false;
        			ControladorCliente.getInstancia().abrirVentanaEspera();       			
        		} else {
        			System.out.println(object.toString());
        		}
        		
            }
            
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {} 
    }

	public void solicitudChat(String nombre, String nombrePropio) { 
		try {
			SolicitudMensaje msj = new SolicitudMensaje(nombre,nombrePropio);
			this.paqueteDatos.writeObject( (SolicitudMensaje) msj);  
			this.paqueteDatos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void actualizarLista(String nombre) {
		try {
			paqueteDatos.writeObject(new ActualizarLista(nombre));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cerrarConexion(String nombreDestinatario) {
		try {
			this.estoyEnLlamada=false;
			this.paqueteDatos.writeObject(new ConexionTerminada(nombreDestinatario));
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