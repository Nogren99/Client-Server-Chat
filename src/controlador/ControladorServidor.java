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
import negocio.Servidor;
import negocio.Sistema;
import vista.Ivista;
import vista.SistemaDeMensajeria;
import vista.Bienvenido;
import vista.Chat;
import vista.Inicio;
import vista.SalaDeEspera;

public class ControladorServidor implements ActionListener, Runnable {

	private Ivista vista;
    private static ControladorServidor instancia;
    private Thread comunicacion;
    

    
    public static ControladorServidor getInstancia() {
        if (instancia == null)
            instancia = new ControladorServidor();
        return instancia;
    }

	public ControladorServidor() {
        this.vista = new Inicio();
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
    


    /**
     *Metodo que se encarga de escuchar la interaccion del servidor
     *Se ingresa el puerto por pantalla y se toma el ip de quien lo inicia
     *Luego se inicia el hilo correspondiente al servidor
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
       
        if (comando.equalsIgnoreCase("Iniciar Sesión")) {
        	Inicio ventana = (Inicio) this.vista;
        	
        	//Ingreso datos del servidor
        	int puerto = Integer.parseInt( ventana.getTextField_1().getText() );
        	Usuario.getInstance().setPuerto(puerto);
        	try {
        		Usuario.getInstance().setIp(InetAddress.getLocalHost().getHostAddress());
			}catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
        	System.out.println("Inicio en ip: "+ Usuario.getInstance().getIp() + "y en puerto: "+puerto);
        	
        	//Inicio hilo para que el servidor empieze a escuchar clientes
            Thread hilo = new Thread(Servidor.getInstancia());
            hilo.start();
        	this.vista.cerrar();
        	
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
		// TODO Auto-generated method stub
		
	}

	
	
}
