
/**
 *
 * @author Oscar Eduardo López Cabagné
 *          T1- Cálculo de Pi
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class Pi 
{
    static Object obj = new Object();
    static float pi = 0;
    
    static class Worker extends Thread
    {
        Socket conexion;
        Worker(Socket conexion)
        {
            this.conexion = conexion;
        }
        
        public void run()
        {
            // Algoritmo 1
            
            try{
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                
                float suma = entrada.readFloat();
                
                synchronized(obj){
                    pi +=suma;
                }
                
                salida.close();
                entrada.close();
                conexion.close();
            }
            catch(Exception e){
                System.out.println(">: Ha ocurrido un error: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        
        if(args.length != 1)
        {
            System.err.println(">: Uso: ");
            System.err.println(">: java PI <nodo>");
            System.exit(0);
        }
        
        
        int nodo = Integer.valueOf(args[0]);
        if(nodo == 0)
        {
            // Algoritmo 2
            
            System.out.println(">: Argumento Cero");
            ServerSocket servidor = new ServerSocket(50000);
            Worker v[] = new Worker[4];
            int i = 0;
            System.out.println(">: Creando Workers...");
            for(; i < 4;){
                Socket conexion = servidor.accept();
                Worker w = new Worker(conexion);
                v[i] = w;
                v[i].start();
                i++;
            }
            System.out.println(">: Workers creados e iniciados. Despertando...");
            i = 0;
            for(; i < 4;){
                v[i].join();
                i++;
            }
            
            System.out.println(">: Pi = " + pi);
        }
        else
        {
            // Algoritmo 3
            
            System.out.println(">: Argumento MAYOR a cero");
            Socket conexion = null;
            for(;;){
                try{
                    conexion = new Socket("localhost", 50000);
                    break;
                }
                catch(Exception e){
                    Thread.sleep(100);
                }
            }
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());    
            float suma = 0;
            int i = 0;
            for(;i < 1000000;)
            {
                suma += 4.0/(8*i+2*(nodo-2)+3);
                i++;
            }
            suma = nodo%2==0?-suma:suma;
            
            salida.writeFloat(suma);
            
            entrada.close();
            salida.close();
            conexion.close();
        }
    }
}