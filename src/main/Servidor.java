package main;

import negocio.Sistema;
import controlador.ControladorServidor;

public class Servidor {

    public static void main(String[] args) {
    	Sistema sistema = Sistema.getInstancia();
        ControladorServidor controlador = new ControladorServidor();
        	
    } 

}

