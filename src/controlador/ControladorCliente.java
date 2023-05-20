package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import modelo.MensajeCliente;
import modelo.Usuario;
import negocio.Sistema;
import negocio.Cliente;
import negocio.Servidor;
import vista.Ivista;
import vista.SistemaDeMensajeria;
import vista.Bienvenido;
import vista.Chat;
import vista.Inicio;
import vista.SalaDeEspera;
import vista.SalaDeEsperaCliente;

public class ControladorCliente implements ActionListener, Runnable {

	private Ivista vista;
	private WindowListener escuchaVentana;
    private Sistema sistema = Sistema.getInstancia();
    private Cliente cliente = Cliente.getInstancia();
    private JTextField textField;
    private String msj;
    private static ControladorCliente instancia;
    private Thread comunicacion;
    private MensajeCliente paquete;
    
    public void setMsj(String msj) {
		this.msj = msj;
	}
    
    public static ControladorCliente getInstancia() {
        if (instancia == null)
            instancia = new ControladorCliente();
        return instancia;
    }

	public ControladorCliente() {
        this.vista = new Bienvenido();
        this.vista.setActionListener(this);
        this.vista.mostrar();
    }
    
    public Ivista getVista(){
        return vista;
    }

    private void setVista(Ivista vista) {
        this.vista=vista;
        this.vista.setActionListener(this);
        this.vista.mostrar();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        //System.out.println("Comando: " + comando);
       
        if (comando.equalsIgnoreCase("Conectarse")) {
        	Bienvenido ventana = (Bienvenido) this.vista;
        	
        	//MIS DATOS:
        	Usuario.getInstance().setNombre(ventana.getTextField_2().getText());
        	try {
				Usuario.getInstance().setIp(InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
        	Usuario.getInstance().setPuerto( Integer.parseInt(ventana.getTextField_1().getText()));
        	
        	this.cliente.getInstancia().setDatos(Usuario.getInstance().getIp(),Usuario.getInstance().getPuerto(),Usuario.getInstance().getNombre());
        	
        	//DATOS DEL SERVIDOR
        	String ip = ventana.getTextFieldIPSV().getText();
        	int puerto = Integer.parseInt( ventana.getTextFieldPuertoSV().getText());
        	System.out.println("me conecto a:"+ip+ "puerto"+puerto);
        	
        	System.out.println("soy"+Usuario.getInstance().getNombre()+Usuario.getInstance().getIp()+Usuario.getInstance().getPuerto());
        	System.out.println("Datos del servier"+ip+" puerto "+puerto);
        	
        	
        	
        	//Conectar al servidor
        	this.cliente.conectar(ip, puerto);
        	//this.cliente.esperarConexion();
        	this.vista.cerrar();
        	
        //====VENTANA DE CHAT====
        }else if (comando.equalsIgnoreCase("Enviar")){
        	Chat ventana = (Chat) this.vista;
        	String msj = ventana.getTextField().getText();
        	System.out.println("mensaje: "+msj);
        	
        	if (msj != null && !msj.isEmpty()) {
			    this.cliente.getInstancia().enviarMensaje(msj);
			    ventana.getTextArea().append(Usuario.getInstance().getNombre()+" : " +msj+"\n");
			}
        }
    }
    
    public void ventanaEspera() {
    	this.vista.cerrar();
    	this.setVista(new SalaDeEsperaCliente());
    	Thread hilo = new Thread(Cliente.getInstancia());
        hilo.start();
    	//this.cliente.esperarConexion();
    }
    
    public void actualizaLista(HashMap clientes) {
    	SalaDeEsperaCliente ventana = (SalaDeEsperaCliente) this.vista;
    	ventana.getModeloLista().clear();
    	
    	Iterator<Map.Entry<String, Integer>> iterator = clientes.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String nombre = entry.getKey();
            Integer puerto = entry.getValue();
            System.out.println("Cliente: " + nombre + ", Puerto: " + puerto);
            
        	ventana.getModeloLista().addElement(nombre);
        	
        }
        ventana.repaint();
    	
    	
    }
    
    
    public void ventanaChat() {
    	this.vista.cerrar();
    	this.setVista(new Chat());
    	this.comunicacion = new Thread(this);
    	this.comunicacion.start();
    }
    
    public void cerrarVentana() {
    	System.out.println("cierro ventana");
    	Sistema.getInstancia().cerrarSockets();
    	this.comunicacion.interrupt();
    }
    


	@Override
	public void run() {
		Chat vista = (Chat) this.vista;
		try {
			while (true ){
				MensajeCliente paquete =  this.cliente.getInstancia().recibirMensaje();
				//Si el mensaje es null es debido a que el otro usuario cerro la comunicacion
				if (paquete.getMsj()==null) 
	                break;
				else {
					vista.getTextArea().setEditable(true);
					//System.out.println("mensaje de:"+Sistema.getInstancia().getIn().readLine());
					System.out.println("el mensaje:"+paquete.getMsj());
					vista.getTextArea().append("Tu contacto ( "+paquete.getName()+ " ) : "+paquete.getMsj()+"\n");
					vista.getTextArea().setEditable(false);
				}
			}
			this.vista.cerrar();
			this.cerrarVentana();
		}
		finally {
		}
	}
	
}
