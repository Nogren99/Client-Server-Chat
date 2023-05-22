# Diseño e implementación de un sistema de mensajería Cliente - Servidor

<p align="center">
  <img src="https://user-images.githubusercontent.com/69020112/235544911-a53bc803-e16b-4991-855e-35cd702cffc0.png" />
</p>


Se construyó un sistema de mensajería Cliente-Servidor el cual sigue las características brindadas por la cátedra de Análisis y Diseño de Sistemas 2:
* Todos los mensajes deben pasar por el servidor. Cuando se quiere enviar un mensaje a un usuario, el mensaje es enviado al servidor, el cual luego envía elmensaje a la aplicación destino.
* Las aplicaciones de chat, deben registrarse primero en el servidor para poder luego establecer una conversación con otra instancia de la aplicación de chat.
* Los mensajes ingresados por los usuarios deben estar encriptados de extremo a extremo (Se encripta en la aplicación donde se escribe el mensaje y se desencripta en la aplicación destino).

# Arquitectura
Se optó por utilizar una arquitectura MVC (modelo, vista, controlador) la cual permite una separación en tres componentes. Los datos, la metodología y la interfaz gráfica de la aplicación. Algunas de las ventajas que brindó esta arquitectura fueron:
* Separación clara de dónde tiene que ir cada tipo de lógica, facilitando el mantenimiento y la escalabilidad de nuestra aplicación.
* Sencillez para crear distintas representaciones de los mismos datos.
* Reutilización de los componentes.

# ¿Como utilizar la aplicacion?
1.Descargue los archivos ejecutables .jar que se encuentran dentro de la carpeta Aplicación.  
2.Inicie una instancia de la aplicacion Server.jar e ingrese un puerto de preferencia,por defecto, al servidor se conectara mediante su ip.  
3.Inicie una o mas instancias de la aplicacion Cliente.jar.  
4.En cada una de ellas deberá completar con sus datos (nombre, ip y puerto) y los datos del servidor para poder conectarse al mismo mediante el boton "Conectarse".  
5.Luego de conectarse podrá visualizar una lista con todos los usuarios conectados a su servidor.  
6.Ahora mismo usted se encuentra a la escucha de solicitudes, pudiendo tambien enviar solicitudes a los otros usuarios.  
7.Al recibir una solicitud usted tiene la posibilidad de aceptarla o rechazarla, en caso de aceptarla, o de enviar una solicitud que es aceptada, será redirigido a la ventana de chat.  
8.Mediante el botón "enviar" se enviarán los mensajes ingresados en el área junto al mismo botón.  
9.En cualquier momento puede presionar el boton "cerrar" para finalizar la comunicacion.  
6.Al cerrar una de las instancias de chat, la del otro usuario se cerrará automáticamente.  
  
  
<hr>
  
GRUPO 6 - Universidad Nacional de Mar del Plata.
