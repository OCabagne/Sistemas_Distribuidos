//CLIENTE
import java.rmi.Naming;

public class ClienteRMI
{
    static int N = 6;
    static int[][] A = new int[N][N];
    static int[][] B = new int[N][N];
    static int[][] C = new int[N][N];

    public static void main(String args[]) throws Exception
    {
        
        //String url = "rmi://localhost/";
        String url = "rmi://localhost/C:/Users/Oscar/Desktop/ESCOM/9no/Distribuídos/RMI_2";
        
        InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);
        
// ----------------------------------------------------------------------------------------
        System.out.println(">: Multiplicacion de Matrices");

        long chksum = 0;

        inicializar();
        int[][] A1 = seapara_matriz(A,0);
        int[][] A2 = seapara_matriz(A,N/2);

        int[][] B1 = seapara_matriz(B,0);
        int[][] B2 = seapara_matriz(B,N/2);

        int[][] C1 = r.multiplica_matrices(A1,B1);
        int[][] C2 = r.multiplica_matrices(A1,B2);
        int[][] C3 = r.multiplica_matrices(A2,B1);
        int[][] C4 = r.multiplica_matrices(A2,B2);

        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, N/2);
        acomoda_matriz(C, C3, N/2, 0);
        acomoda_matriz(C, C4, N/2, N/2);

        chksum = checksum(C);

        System.out.println(">: Checksum = " + chksum);
    }

    static long checksum(int[][] C)
    {
        int checksum = 0;

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
                A[i][j] = 2 * i - j;
                B[i][j] = i + 2 * j;
                C[i][j] = 0;
            }
        }

        //---------------------------------------------------- Transpuesta de B
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < i; j++)
            {
                int x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
    }

    static int[][] seapara_matriz(int[][] A, int inicio)
    {
        int[][] M = new int[N/2][N];    // N/3
        for(int i = 0; i < N/2; i++)
            for(int j = 0; j < N; j++)
                M[i][j] = A[i + inicio][j];
        return M;
    }

    static void acomoda_matriz(int[][] C, int[][] c, int renglon, int columna)
    {
        for(int i = 0; i < N/2; i++)
            for(int j = 0; j < N/2; j++)
                C[i + renglon][j + columna] = c[i][j];
    }
}