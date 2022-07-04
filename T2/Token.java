
/**
 *
 * @author Oscar Eduardo López Cabagné
 *          T2- Token-Ring
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


class Token
{
    static DataInputStream entrada;
    static DataOutputStream salida;
    static boolean inicio = true;
    static String ip;
    static int nodo;
    static long token;

    static class Worker extends Thread
    {
        public void run(){
            // ALGORITMO 1
            try 
            {
                ServerSocket servidor = new ServerSocket(20000+nodo);
                System.out.println(">: Conectando...");
                Socket conexion = servidor.accept();
                System.out.println(">: Conectado!");
                entrada = new DataInputStream(conexion.getInputStream());
            } catch (Exception e) 
            {
                System.out.println(">: Ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        if(args.length != 2)
        {
            System.err.println("Se debe pasar como parametros el numero del nodo y la IP del siguiente nodo en el anillo.");
            System.exit(1);
        }

        nodo = Integer.valueOf(args[0]);
        ip = args[1];
        
        System.out.println(">: nodo: "+ nodo);
        System.out.println(">: ip: "+ ip);
        
        // ALGORITMO 2
        Worker w = new Worker();
        w.start();
        Socket conexion = null;
        for(;;)
        {
            try 
            {
                conexion = new Socket(ip, 20000+(nodo+1)%4);
                break;
            } catch (Exception e) 
            {
                Thread.sleep(500);
            }
        }

        salida = new DataOutputStream(conexion.getOutputStream());
        w.join();
        for(;;){
            if(nodo == 0){
                if(inicio){
                    inicio = false;
                    token = 1;
                }
                else{
                    token = entrada.readLong();
                    token++;
                    System.out.println(">: Nodo: " + nodo + "  Token: " + token);
                }
            }
            else{
                token = entrada.readLong();
                token++;
                System.out.println(">: Nodo: " + nodo + "  Token: " + token);
            }
            if(nodo == 0 && token >= 1000){
                break;
            }
            salida.writeLong(token);
        }
    }
}