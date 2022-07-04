
/**
 * Desarrollo de Sistemas Distribuidos - Cliente // 19.08.2021
 * @author Oscar Cabagné
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args){
        try{
            Socket conexion = new Socket("localhost", 50000);   // Se abre conexión al servidor a través del puerto 50000
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream()); // Canal de comunicación para escribir mensajes al servidor
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());   // Canal de comunicación para leer mensajes del servidor.
            long start  = System.currentTimeMillis();
            for(double i = 0.0; i < 10000; i++){
                salida.writeDouble(i); // Se envía al servidor el valor de punto flotante 3.14   
            }
            long end = System.currentTimeMillis();
            System.out.println("Tiempo total de envío: " + (end - start));

            byte[] buffer = new byte[4];    // Se crea y asigna un arreglo vacío de 4 bytes. || Los arreglos de Bytes en Java se inicializan en cero.
            entrada.read(buffer, 0, 4);     // Se lee el arreglo de Bytes, estableciendo el punto de inicio y el total de datos.
            System.out.println(new String(buffer, "UTF-8"));    // Se crea un String con el arreglo de bytes y se define la codificación de texto UTF-8
            
            salida.close();
            entrada.close();
            conexion.close();   // Se cierra la conexión. El servidor es notificado automáticamente.
        }
        catch(Exception e){
            System.out.println("Ha ocurrido un error: " + e);
        }
    }
    
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{
        while(longitud > 0){
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }
}

