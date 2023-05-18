package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import controlador.ControladorCliente;
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

    private Servidor() {
        user = Usuario.getInstance();
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
            System.out.println("Modo escucha activado.");
            this.socketServer = new ServerSocket(user.getPuerto());
            controlador.getInstancia().ventanaEspera();

            while (true) {
                System.out.println(user.getPuerto());
                socket = socketServer.accept();

                // Crear un hilo para manejar la conexión entrante
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            }

        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
    }

    // Clase interna para manejar las conexiones entrantes
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Configurar las entradas y salidas del cliente
                InputStreamReader inSocket = new InputStreamReader(clientSocket.getInputStream());
                in = new BufferedReader(inSocket);
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Leer el nombre del cliente y enviarlo al otro cliente
                String clientName = in.readLine();
                System.out.println("El nombre del cliente es: " + clientName);
                out.println(user.getNombre());

                // Obtener el nombre del otro cliente
                //String otherClientName = controlador.obtenerNombreOtroCliente();

                // Enviar el nombre del otro cliente al cliente actual
                //out.println(otherClientName);

                //controlador.ventanaChat();
            } catch (IOException e) {
                // Manejar la excepción apropiadamente
            } finally {
                // Cerrar las conexiones y liberar los recursos
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    // Manejar la excepción apropiadamente
                }
            }
        }
    }
}