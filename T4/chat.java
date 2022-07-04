
/**
 *
 * @author Oscar Eduardo López Cabagné
 *          T4- Chat Multicast
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.Charset;
import java.util.Scanner;

// chcp 1252 en cmd para habilitar la visualización de acentos y caractéres especiales

public class chat
{
    public static int size = 256;
	static class Worker extends Thread 
    {
		public void run()
        {
            while(true) {
                try{
                    InetAddress grupo = InetAddress.getByName("230.0.0.0"); // Grupo 230.0.0.0
                    MulticastSocket socket = new MulticastSocket(20000);    // Puerto 20,000
                    socket.joinGroup(grupo);

                    byte[] buffer = recibe_mensaje_multicast(socket, size);
                    System.out.println(new String(buffer, "UTF-8"));

                    socket.leaveGroup(grupo);
                    socket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
		}

	}

    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException
    {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }

    static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException
    {
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
        socket.close();
    }

	public static void main(String[] args) throws Exception 
    {
		if (args.length != 1) 
        {
			System.err.println(">: Uso: java Chat <usuario>");
			System.exit(0);
		}

        Worker w = new Worker();
        w.start();
        String nombre = args[0];         // hugo, paco, luis
        Scanner leer = new Scanner(System. in);
    
        while(true) 
        {
            System.out.println("Ingrese el mensaje a enviar:");
            String mensaje = leer. nextLine();
            byte buffer[] = String.format("%s:%s", nombre, mensaje).getBytes(Charset.forName("UTF-8"));
            envia_mensaje_multicast(buffer, "230.0.0.0", 20000);    // Grupo 230.0.0.0, Puerto 20,000
        }
	}
}

