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
import vista.Ivista;
import vista.SistemaDeMensajeria;
import vista.Bienvenido;
import vista.Chat;
import vista.Inicio;
import vista.SalaDeEspera;

public class ControladorSistema implements ActionListener, Runnable {

	private Ivista vista;
	private WindowListener escuchaVentana;
    private Sistema sistema = Sistema.getInstancia();
    private JTextField textField;
    private String msj;
    private static ControladorSistema instancia;
    private Thread comunicacion;
    
    public void setMsj(String msj) {
		this.msj = msj;
	}
    
    public static ControladorSistema getInstancia() {
        if (instancia == null)
            instancia = new ControladorSistema();
        return instancia;
    }

	public ControladorSistema() {
        this.vista = new SistemaDeMensajeria();
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
       
        if (comando.equalsIgnoreCase("Crear")) {
        	this.vista.cerrar();
        	this.setVista(new Inicio());
        }else if (comando.equalsIgnoreCase("Unirse")) {
        	this.vista.cerrar();
        	this.setVista(new Bienvenido());
        	
        //====VENTANA DE CONFIGURACION=====
        }else if (comando.equalsIgnoreCase("Iniciar Sesión")) {
        	Inicio ventana = (Inicio) this.vista;
        	
        	int puerto = Integer.parseInt( ventana.getTextField_1().getText() );
        	String nombre = ventana.getTextField().getText();
        	Usuario.getInstance().setNombre(nombre);
        	Usuario.getInstance().setPuerto(puerto);
        	
        	System.out.println("Inicio en puerto: "+ puerto+" username: "+nombre);
        	try {
        		Usuario.getInstance().setIp(InetAddress.getLocalHost().getHostAddress());
				System.out.println("Inicio en ip: "+ Usuario.getInstance().getIp());
			}catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally { } 

            Thread hilo = new Thread(Sistema.getInstancia());
            hilo.start();

        	this.vista.cerrar();
        	//this.setVista(new Chat());        	

        //=====VENTANA DE BIENVENIDO====
        } else if (comando.equalsIgnoreCase("Conectarse")){
        	Bienvenido ventana = (Bienvenido) this.vista;
        	
        	//MIS DATOS:
        	Usuario.getInstance().setNombre(ventana.getTextField_2().getText());
        	try {
				Usuario.getInstance().setIp(InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
        	
        	//DATOS DEL USUARIO AL QUE ME CONECTO
        	String ip = ventana.getTextField().getText();
        	int puerto = Integer.parseInt( ventana.getTextField_1().getText() );
        	System.out.println("me conecto a:"+ip+ "puerto"+puerto);
        	
        	//SOLICITO INICIAR CHAT
        	this.sistema.solicitarChat(ip, puerto);
        	this.vista.cerrar();
        	
        //====VENTANA DE CHAT====
        }else if (comando.equalsIgnoreCase("Enviar")){
        	Chat ventana = (Chat) this.vista;
        	String msj = ventana.getTextField().getText();
        	System.out.println("mensaje: "+msj);
        	
        	try {
                if (msj != null && !msj.isEmpty()) {
                    this.sistema.enviarMensaje(msj);
                    ventana.getTextArea().append(Usuario.getInstance().getNombre()+" : " +msj+"\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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
			while (!Sistema.getInstancia().getSocket().isInputShutdown() ){
				String mensaje =  this.sistema.recibirMensaje();
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
