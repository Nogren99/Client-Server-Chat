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
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import controlador.ControladorCliente;
import modelo.MensajeCliente;
import modelo.Usuario;

public class Servidor implements Runnable {
    private static Servidor instancia;
    private static ControladorCliente controlador;

    private ServerSocket servidor;
    private Usuario user;
    private ServerSocket socketServer;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private InputStreamReader inSocket;
    private Map<String, Socket> clientes;
    private Map<String, ObjectOutputStream> flujosSalida;

    private Servidor() {
        user = Usuario.getInstance();
        clientes = new HashMap<>();
        flujosSalida = new HashMap<>();
    }

    public static Servidor getInstancia() {
        if (instancia == null) {
            instancia = new Servidor();
        }
        return instancia;
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
                System.out.println("Nuevo cliente conectado: " + socket.getInetAddress().getHostAddress());

                //ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());

                // Crear un hilo para manejar la conexión entrante
                Thread clientThread = new Thread(new EscucharCliente(socket));
                clientThread.start();
            }

        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
    }
    
    
    
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
    }
    
    
    

    // Clase interna para manejar las conexiones entrantes

        private class EscucharCliente implements Runnable {
            private Socket cliente;
            private ObjectInputStream flujoEntrada;
            private ObjectOutputStream flujoSalida;
            private String nombreCliente;

            public EscucharCliente(Socket cliente) {
                this.cliente = cliente;
                try {
                    flujoEntrada = new ObjectInputStream(cliente.getInputStream());
                    flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void run() {
                try {
                    // El cliente debe enviar su nombre primero
                	MensajeCliente msj = new MensajeCliente();
                	msj =(MensajeCliente) flujoEntrada.readObject();
                	System.out.println(msj);
                	
                    nombreCliente = msj.getName();
                    clientes.put(nombreCliente, cliente);
                    flujosSalida.put(nombreCliente, flujoSalida);

                    while (true) {
                        Object objeto = flujoEntrada.readObject();
                        if (objeto instanceof MensajeCliente) {
                        	MensajeCliente objetoMensaje = (MensajeCliente) objeto;
                        	String destinatario = objetoMensaje.getName();
                            if (clientes.containsKey(destinatario)) {
                                Socket destinatarioSocket = clientes.get(destinatario);
                                ObjectOutputStream destinatarioFlujoSalida = flujosSalida.get(destinatario);
                                destinatarioFlujoSalida.writeObject(objetoMensaje);
                                destinatarioFlujoSalida.flush();
                            } else {
                                // Manejar el caso en el que el destinatario no exista
                                System.out.println("Destinatario no encontrado: " + destinatario);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    clientes.remove(nombreCliente);
                    flujosSalida.remove(nombreCliente);
                    System.out.println("Cliente desconectado: " + nombreCliente);
                }
            }
        }
   
}