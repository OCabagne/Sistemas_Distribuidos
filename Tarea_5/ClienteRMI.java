//CLIENTE
import java.rmi.Naming;

// JR2015070715-<nodo>
public class ClienteRMI
{
    static int N = 9;
    //static int N = 3000;
    static double[][] A = new double[N][N];
    static double[][] B = new double[N][N];
    static double[][] C = new double[N][N];

    public static void main(String args[]) throws Exception
    {
        //String url1 = "rmi://localhost/C:/Users/Oscar/Desktop/ESCOM/9no/Distribuídos/Prácticas/Tarea4/RMI_3"; // IP Nodo 1
        //String url2 = "rmi://localhost/C:/Users/Oscar/Desktop/ESCOM/9no/Distribuídos/Prácticas/Tarea4/RMI_3"; // IP Nodo 2
        //String url3 = "rmi://localhost/C:/Users/Oscar/Desktop/ESCOM/9no/Distribuídos/Prácticas/Tarea4/RMI_3"; // IP Nodo 3
        String url1 = "rmi://10.1.0.4/prueba";
        String url2 = "rmi://10.2.0.4/prueba";
        String url3 = "rmi://10.3.0.4/prueba";
        
        System.out.println(">: Multiplicacion de Matrices");
        InterfaceRMI nodo1 = (InterfaceRMI)Naming.lookup(url1);
        InterfaceRMI nodo2 = (InterfaceRMI)Naming.lookup(url2);
        InterfaceRMI nodo3 = (InterfaceRMI)Naming.lookup(url3);       
// ----------------------------------------------------------------------------------------
        
        System.out.println(">: N = " + N);
        double chksum = 0;

        inicializar();
        if(N == 6)
        {
            System.out.println(">: Matriz A:");
            imprimirMatriz(A,N,N);
            System.out.println(">: Matriz B:");
            imprimirMatriz(B,N,N);
        }

        double[][] A1 = seapara_matriz(A,0);
        double[][] A2 = seapara_matriz(A,N/3);
        double[][] A3 = seapara_matriz(A, (2*N)/3);

        double[][] B1 = seapara_matriz(B,0);
        double[][] B2 = seapara_matriz(B,N/3);
        double[][] B3 = seapara_matriz(B, (2*N)/3);

        double[][] C1 = nodo1.multiplica_matrices(A1,B1, N);  // NODO 1
        double[][] C2 = nodo1.multiplica_matrices(A1,B2, N);
        double[][] C3 = nodo1.multiplica_matrices(A1,B3, N);

        double[][] C4 = nodo2.multiplica_matrices(A2,B1, N);  // NODO 2
        double[][] C5 = nodo2.multiplica_matrices(A2,B2, N);
        double[][] C6 = nodo2.multiplica_matrices(A2,B3, N);

        double[][] C7 = nodo3.multiplica_matrices(A3,B1, N);  // NODO 3
        double[][] C8 = nodo3.multiplica_matrices(A3,B2, N);
        double[][] C9 = nodo3.multiplica_matrices(A3,B3, N);

        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, N/3);
        acomoda_matriz(C, C3, 0, (2*N)/3);

        acomoda_matriz(C, C4, N/3, 0);
        acomoda_matriz(C, C5, N/3, N/3);
        acomoda_matriz(C, C6, N/3, (2*N)/3);

        acomoda_matriz(C, C7, (2*N)/3, 0);
        acomoda_matriz(C, C8, (2*N)/3, N/3);
        acomoda_matriz(C, C9, (2*N)/3, (2*N)/3);

        chksum = checksum(C);
        if(N == 9)
        {
            System.out.println(">: Matriz A:");
            imprimirMatriz(A,N,N);
            System.out.println(">: Matriz B:");
            imprimirMatriz(B,N,N);
            System.out.println(">: Matriz C:");
            imprimirMatriz(C,N,N);
        }
        
        System.out.println(">: Checksum = " + chksum);
    }

	static void imprimirMatriz(double matrix[][], int rows, int cols)
    {
		for (int i = 0; i < rows; i++) 
        {
			for (int j = 0; j < cols; j++) 
            {
                if(matrix[i][j] < 10 && matrix[i][j] >= 0)
                    System.out.print(" ");
                System.out.print(" " + matrix[i][j]);
			}
			System.out.println("");
		}

        System.out.println("");
	}

    static double checksum(double[][] C)
    {
        double checksum = 0;

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                checksum += C[i][j];

        return checksum;
    }

    public static void inicializar()
    {
        for (int i = 0; i < N; i++)     // ------------------ Inicializa las matrices A y B
        {
            for (int j = 0; j < N; j++)
            {
                A[i][j] = 4 * i + j;
                B[i][j] = 4 * i - j;
                C[i][j] = 0;
            }
        }

        //---------------------------------------------------- Transpuesta de B
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < i; j++)
            {
                double x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
    }

    static double[][] seapara_matriz(double[][] A, int inicio)
    {
        double[][] M = new double[N/3][N];    // N/3
        for(int i = 0; i < N/3; i++)
            for(int j = 0; j < N; j++)
                M[i][j] = A[i + inicio][j];
        return M;
    }

    static void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna)
    {
        for(int i = 0; i < N/3; i++)
            for(int j = 0; j < N/3 ; j++)
                C[i + renglon][j + columna] = c[i][j];
    }
}