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

public class ControladorCliente implements ActionListener {

	private Ivista vista;
	private Cliente cliente = Cliente.getInstancia();
	private boolean isSolicitante;
	private static ControladorCliente instancia;
	private Thread comunicacion;
	private String nombreDestinatario;
	private String nombreSolicitante;

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

	public Ivista getVista() {
		return vista;
	}

	private void setVista(Ivista vista) {
		this.vista = vista;
		this.vista.setActionListener(this);
		this.vista.mostrar();
	}

	/**
	 *Metodo que se encarga de escuchar la interaccion del cliente
	 *
	 *<b> Conectarse: </b>
	 *El cliente ingresa sus datos y los del servidor al cual desea conectarse
	 *
	 *<b> Enviar: </b>
	 *Dentro de la ventana de chat, cada vez que el usuario envia un mensaje, se borra del campo de texto lo que escribio,
	 *luego se imprime por su pantalla y dependiendo de quien envia el mensaje (solicitante o solicitado)es el orden de los parametros
	 *que se envian al metodo enviarMensaje
	 *
	 *<b> Conectar: </b>
	 *Se selecciona un cliente de la lista y se le solicita iniciar una conversacion
	 *si no hay ningun cliente seleccionado no es posible enviar dicha solicitud
	 *
	 *<b> Cerrar: </b>
	 *Cierra la conversacion actualy notifica al otro usuario que se cerro la conversacion.
	 *devuelve al cliente a la ventana de sala de espera cliente
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();

		if (comando.equalsIgnoreCase("Conectarse")) {
			Bienvenido ventana = (Bienvenido) this.vista;

			// MIS DATOS:
			Usuario.getInstance().setNombre(ventana.getTextField_2().getText());
			try {
				Usuario.getInstance().setIp(InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			Usuario.getInstance().setPuerto(Integer.parseInt(ventana.getTextField_1().getText()));

			this.cliente.getInstancia().setDatos(Usuario.getInstance().getIp(), Usuario.getInstance().getPuerto(),Usuario.getInstance().getNombre());

			// DATOS DEL SERVIDOR
			String ip = ventana.getTextFieldIPSV().getText();
			int puerto = Integer.parseInt(ventana.getTextFieldPuertoSV().getText());

			// Conectar al servidor
			this.cliente.conectar(ip, puerto);
			this.vista.cerrar();

			// ====VENTANA DE CHAT====
		} else if (comando.equalsIgnoreCase("Enviar")) {
			Chat ventana = (Chat) this.vista;
			String msj = ventana.getTextField().getText();
			ventana.getTextField().setText("");

			if (msj != null && !msj.isEmpty()) {
				ventana.getTextArea().append(Usuario.getInstance().getNombre() + " (Tu) : " + msj + "\n");
				if (this.isSolicitante) {
					Cliente.getInstancia().enviarMensaje(msj, Usuario.getInstance().getNombre(),this.nombreDestinatario);
				} else if (this.isSolicitante == false) {
					Cliente.getInstancia().enviarMensaje(msj, Usuario.getInstance().getNombre(),Cliente.getInstancia().getNombreInterlocutor());
				}

			}
			//====Lista de Usuarios conectados al servidor====
		} else if (comando.equalsIgnoreCase("Conectar")) {
			SalaDeEsperaCliente vistaSeleccion = (SalaDeEsperaCliente) this.vista;
			// anular boton sin seleccion
			String nombre = (String) vistaSeleccion.getList().getSelectedValue();

			Cliente.getInstancia().solicitudChat(nombre, Usuario.getInstance().getNombre());
			this.nombreDestinatario = nombre;


		} else if (comando.equalsIgnoreCase("Cerrar")) {
			this.vista.cerrar();
			this.setVista(new SalaDeEsperaCliente());
			if (this.isSolicitante())
				Cliente.getInstancia().cerrarConexion(this.nombreDestinatario);
			else
				Cliente.getInstancia().cerrarConexion(this.nombreSolicitante);
			Cliente.getInstancia().actualizarLista(Usuario.getInstance().getNombre());

		}
	}

	public void ventanaEspera() {
		this.vista.cerrar();
		this.setVista(new SalaDeEsperaCliente());
		Thread hilo = new Thread(Cliente.getInstancia());
		hilo.start();
	}

	/**
	 * @param clientes
	 * 
	 * Recibe un HashMap de clientes y actualiza la lista que es mostrada en la ventana 
	 * donde el cliente puede esperar una conexion y/o solicitar una
	 */
	public void actualizaLista(HashMap clientes) {
		if (this.vista instanceof SalaDeEsperaCliente) {
			SalaDeEsperaCliente ventana = (SalaDeEsperaCliente) this.vista;
			ventana.getModeloLista().clear();
			Iterator<Map.Entry<String, Integer>> iterator = clientes.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, Integer> entry = iterator.next();
				String nombre = entry.getKey();
				Integer puerto = entry.getValue();

				if (!nombre.equals(Usuario.getInstance().getNombre()))
					ventana.getModeloLista().addElement(nombre);

			}
			ventana.repaint();
		}
	}

	public boolean isSolicitante() {
		return isSolicitante;
	}

	public void setSolicitante(boolean isSolicitante) {
		this.isSolicitante = isSolicitante;
	}

	public void ventanaChatSolicitante() {
		this.vista.cerrar();
		this.setVista(new Chat());
		Chat chat = (Chat) this.vista;
		chat.getLblChatCon().setText("Chat con: " + this.nombreDestinatario);
	}

	public void ventanaChatSolicitado(String nombre) {
		this.vista.cerrar();
		this.setVista(new Chat());
		this.nombreSolicitante = nombre;
		Chat chat = (Chat) this.vista;
		chat.getLblChatCon().setText("Chat con: " + nombre);
	}

	public void actualizaChat(String nombre, String mensaje) {
		Chat chat = (Chat) this.vista;
		chat.getTextArea().append(nombre + " : " + mensaje + "\n");
	}

	public void cerrarVentana() {
		System.out.println("cierro ventana");
		Sistema.getInstancia().cerrarSockets();
		this.comunicacion.interrupt();
	}

	public void abrirVentanaEspera() {
		this.vista.cerrar();
		JOptionPane.showMessageDialog(null, "Tu contacto terminó la conexión!");
		this.setVista(new SalaDeEsperaCliente());
		Cliente.getInstancia().actualizarLista(Usuario.getInstance().getNombre());
	}



}
