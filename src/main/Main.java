package main;

import negocio.Sistema;
import controlador.ControladorCliente;

public class Main {

    public static void main(String[] args) {
    	Sistema sistema = Sistema.getInstancia();
        ControladorCliente controlador = new ControladorCliente();
        	
    } 

}

