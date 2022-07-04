// SERVIDOR
import java.rmi.Naming;

public class ServidorRMI
{
    public static void main(String[] args) throws Exception
    {
        //String url = "rmi://localhost/";
        String url = "rmi://localhost/C:/Users/Oscar/Desktop/ESCOM/9no/Distribuídos/RMI_2";
        ClaseRMI obj = new ClaseRMI();
        
        // registra la instancia en el rmiregistry
        Naming.rebind(url, obj);
    }
}
