
/**
 *
 * @author Oscar Eduardo López Cabagné
 *          T3- Multiplicación de Matrices Distribuida Utilizando
 * 							Paso de Mensajes
 */

import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class Matrices2{
	static Object lock = new Object();
	static long checksum = 0;
    static int N = 10;
    static long[][] A = new long[N][N];
    static long[][] B = new long[N][N];
    static long[][] C = new long[N][N];
	//static String ip = "20.102.66.111";
	static String ip = "localhost";

	static class Worker extends Thread 
    {
		Socket connection;
        int nodo;
		Worker(Socket connection)
        {
			this.connection = connection;
		}

		public void run()
        {
			try {
    			DataInputStream entrada = new DataInputStream(connection.getInputStream());
                DataOutputStream salida = new DataOutputStream(connection.getOutputStream());
				long ai[][] = new long[N/2][N];
				long bi[][] = new long[N/2][N];
				
				int nodo = entrada.readInt(); // ------------ Obtención del nodo

                if(nodo == 1)
                {
                    for (int i = 0; i < N/2; i++) 
                    {
                        for (int j = 0; j < N; j++) 
                        {
                            ai[i][j] = A[i][j];     // ------ Obtención de A1
                            bi[i][j] = B[i][j];     // ------ Obtención de B1
                        }
                    }
                }else if(nodo == 2)
                {
                    for (int i = 0; i < N/2; i++) 
                    {
                        for (int j = 0; j < N; j++) 
                        {
                            ai[i][j] = A[i][j];     // ------ Obtención de A1
                            bi[i][j] = B[i + N/2][j]; // -- Obtención de B2
                        }
                    }
                }
                else if(nodo == 3)
                {
                    for (int i = 0; i < N/2; i++) 
                    {
                        for (int j = 0; j < N; j++) 
                        {
                            ai[i][j] = A[i + N/2][j]; // -- Obtención de A2
                            bi[i][j] = B[i][j];         // -- Obtención de B1
                        }
                    }
                }
                else if(nodo == 4)
                {
                    for (int i = 0; i < N/2; i++) 
                    {
                        for (int j = 0; j < N; j++) 
                        {
                            ai[i][j] = A[i + N/2][j]; // -- Obtención de A2
                            bi[i][j] = B[i + N/2][j]; // -- obtención de B2
                        }
                    }
                }

                enviarMatriz(ai, N/2, N, salida);
				enviarMatriz(bi, N/2, N, salida);

				long ci[][] = recibirMatriz(N/2, N/2, entrada); // --- Recepción de Matrices c1, c2, c3 y c4

                // ----------------------------------------------- Unificación de las matrices para formar C
				if(nodo == 1)
                {
					for (int i = 0; i < N/2; i++) 
                    {
						for (int j = 0; j < N/2; j++) 
                        {
							C[i][j] = ci[i][j];
						}
					}
				}
                else if(nodo == 2)
                {
					for (int i = 0; i < N/2; i++) 
                    {
						for (int j = 0; j < N/2; j++) 
                        {
							C[i][j +  N/2] = ci[i][j];
						}
					}
				}
                else if(nodo == 3)
                {
					for (int i = 0; i < N/2; i++) 
                    {
						for (int j = 0; j < N/2; j++) 
                        {
							C[i + N/2][j] = ci[i][j];
						}
					}
				} 
                else if(nodo == 4)
                {
					for (int i = 0; i < N/2; i++) 
                    {
						for (int j = 0; j < N/2; j++) 
                        {
							C[i + N/2][j +  N/2] = ci[i][j];
						}
					}
				}

				entrada.close();
                salida.close();
				connection.close();
			} 
            catch (IOException e)
            {
				e.printStackTrace();
			}
		}

	}
	
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception 
    {
		while (longitud > 0) {
			int n = f.read(b,posicion,longitud);
			posicion += n;
			longitud -= n;
		}
	}

	static void imprimirMatriz(long matrix[][], int rows, int cols)
    {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.printf("%4d", matrix[i][j]);
			}
			System.out.println("");
		}
	}

	static void enviarMatriz(long matrix[][], int rows, int cols, DataOutputStream salida)
    {
		for(int i = 0; i < rows; i++) 
        {
			ByteBuffer bf = ByteBuffer.allocate(cols*8);
			for(int j = 0; j < cols; j++) 
            {
				bf.putLong(matrix[i][j]);
			}
			byte[] bytes = bf.array();
			try{
				salida.write(bytes);
			}
            catch (Exception e)
            {
				e.printStackTrace();
			}
		}
	}

	static long[][] recibirMatriz(int rows, int cols, DataInputStream entrada)	
    {
		long matrix[][] = new long[rows][cols];
		for(int i = 0; i < rows; i++)
        {
			byte[] bytes = new byte[cols * 8];
			try
            {
				read(entrada, bytes, 0, cols * 8);
				ByteBuffer bf = ByteBuffer.wrap(bytes);
				for (int j = 0; j < cols; j++)
					matrix[i][j] = bf.getLong();
			}
            catch (Exception e)
            {
				e.printStackTrace();
			}
		}
		return matrix;
	}

    static void inicializar()
    {
        for (int i = 0; i < N; i++)     // ------------------ Inicializa las matrices A y B
        {
            for (int j = 0; j < N; j++)
            {
                A[i][j] = 2 * i + j;
                B[i][j] = 2 * i - j;
                C[i][j] = 0;
            }
        }
        if(N == 10){
            System.out.println(">: Matriz A:");
            imprimirMatriz(A, N, N);
            System.out.println(">: Matriz B:");
            imprimirMatriz(B, N, N);
        }

        //---------------------------------------------------- Transpuesta de B
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < i; j++)
            {
                long x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
    }

	public static void main(String[] args) throws Exception 
    {
		if (args.length != 1) {
			System.err.println(">: Uso:  java Matrices <nodo>");
			System.exit(0);
		}

		int nodo = Integer.valueOf(args[0]);

		if (nodo == 0) // ---------------------------------------------- Conexión del Servidor
        { 
			ServerSocket server = new ServerSocket(20000);
			Worker w[] = new Worker[4];

            inicializar();
			
			for(int i=0; i < 4; i++)
            {
				Socket cliente = server.accept();
				w[i] = new Worker(cliente);
				w[i].start();
			}

			for(int i=0; i < 4; i++)
            {
				w[i].join();
			}

			for (int i = 0; i < N; i++)
            {
				for (int j = 0; j < N; j++)
                {
					checksum += C[i][j];
				}
			}

            if(N == 10)
            {
                System.out.println(">: Matriz C:");
                imprimirMatriz(C, N, N);
            }

			System.out.println(">: Checksum = " + checksum);
			server.close();
		} 

        else // ----------------------------------------------------------- Conexión de nodo
        {
			Socket connection = null;

			for(;;)
			try {
                System.out.println(">: Conectando... ");
				connection = new Socket(ip, 20000);
                System.out.println(">: Conectado! ");
				break;
			}
            catch (Exception e)
            {
				Thread.sleep(100);
			}
			
			DataInputStream entrada = new DataInputStream(connection.getInputStream());
            DataOutputStream salida = new DataOutputStream(connection.getOutputStream());

			salida.writeInt(nodo);

			long ai[][] = recibirMatriz(N/2, N, entrada);
			long bi[][] = recibirMatriz(N/2, N, entrada);
			long ci[][] = new long[N/2][N/2];

			for (int i = 0; i < N/2; i++)   // ------------------------------ Multiplicación
				for (int j = 0; j < N/2; j++)
					for (int k = 0; k < N; k++)
						ci[i][j] += ai[i][k] * bi[j][k];

			enviarMatriz(ci, N/2, N/2, salida);

			entrada.close();
			salida.close();
			connection.close();
		}
	}
}