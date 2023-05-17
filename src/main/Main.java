package main;

import negocio.Sistema;
import controlador.ControladorSistema;

public class Main {

    public static void main(String[] args) {
    	Sistema sistema = Sistema.getInstancia();
        ControladorSistema controlador = new ControladorSistema();
        	
    } 

}

