package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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

public class ControladorCliente implements ActionListener, Runnable {

	private Ivista vista;
	private WindowListener escuchaVentana;
    private Sistema sistema = Sistema.getInstancia();
    private Cliente cliente = Cliente.getInstancia();
    private JTextField textField;
    private String msj;
    private static ControladorCliente instancia;
    private Thread comunicacion;
    
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
        	Usuario.getInstance().setPuerto( Integer.parseInt(ventana.getTextFieldTuPuerto().getText()));
        	
        	//DATOS DEL USUARIO AL QUE ME CONECTO
        	String ip = ventana.getTextField().getText();
        	int puerto = Integer.parseInt( ventana.getTextField_1().getText() );
        	System.out.println("me conecto a:"+ip+ "puerto"+puerto);
        	
        	//SOLICITO INICIAR CHAT
        	this.cliente.conectar(ip, puerto);
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
    	this.setVista(new SalaDeEspera());
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
			while (!Servidor.getInstancia().getSocket().isInputShutdown() ){
				String mensaje =  this.cliente.getInstancia().recibirMensaje();
				//Si el mensaje es null es debido a que el otro usuario cerro la comunicacion
				if (mensaje==null) 
	                break;
				else {
					vista.getTextArea().setEditable(true);
					//System.out.println("mensaje de:"+Sistema.getInstancia().getIn().readLine());
					System.out.println("el mensaje:"+mensaje);
					vista.getTextArea().append("Tu contacto: "+mensaje+"\n");
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
