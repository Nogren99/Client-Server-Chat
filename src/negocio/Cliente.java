package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import controlador.ControladorCliente;
import modelo.MensajeCliente;
import modelo.Usuario;

public class Cliente implements Runnable{
	private static Cliente instancia;
    private Socket socket;
    private MensajeCliente paqueteMsj = new MensajeCliente();
    private ObjectOutputStream flujoSalida;
    private ObjectInputStream flujoEntrada;
    private MensajeCliente paqueteRecibido;
    
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
            socket = new Socket(host, 1);
            System.out.println("socket"+socket + "histo:"+host+"puerot"+puerto);
            flujoSalida = new ObjectOutputStream(socket.getOutputStream());
            flujoEntrada = new ObjectInputStream(socket.getInputStream());
            System.out.println("2");
            /*
            ObjectOutputStream paqueteDatos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("3");
            MensajeCliente datos = new MensajeCliente();
            System.out.println("4");
            datos.setIp(Usuario.getInstance().getIp());
            datos.setMsj(null);
            datos.setPuerto(Usuario.getInstance().getPuerto());
            datos.setName(Usuario.getInstance().getNombre());
            System.out.println("5");
            paqueteDatos.writeObject(datos); //envio los datos che
            System.out.println("estoy por abrir la ventana xq hace calor");*/
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
	
	
    public void run() {
        try {
        	while (true) {
        		Object objeto = flujoEntrada.readObject();
        		System.out.println("objeto recibido"+objeto);
                if (objeto instanceof MensajeCliente) {
                    
                }else if (objeto instanceof HashMap) {
                	System.out.println("HashMap"+objeto);
                	ControladorCliente.getInstancia().actualizaLista( (HashMap) objeto);
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
	
	
	
}