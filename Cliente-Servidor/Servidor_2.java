
/**
 * Desarrollo de Sistemas Distribuidos - Cliente // 23.08.2021
 * @author Oscar Cabagné
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Serivdor {
    public static void main(String[] args){
        try{
            ServerSocket servidor = new ServerSocket(50000);
            Socket conexion = servidor.accept();
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            
            long start  = System.currentTimeMillis();
            byte[] a = new byte[10000*8];
            read(entrada, a, 0, 10000*8);
            ByteBuffer b = ByteBuffer.wrap(a);  // Crea un objeto ByteBuffer a partir del arreglo de Bytes "a"
            
            for(int i = 0; i < 10000; i++){
                System.out.println(b.getDouble());  // Se sacan los 5 doubles uno por uno, en secuencia
            }
            long end = System.currentTimeMillis();
            System.out.println("Tiempo total de lectura: " + (end - start));
            
            salida.close();
            entrada.close();
            conexion.close();   // Se cierra la conexión.
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
