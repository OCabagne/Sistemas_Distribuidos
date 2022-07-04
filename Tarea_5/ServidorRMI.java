// SERVIDOR
import java.rmi.Naming;

public class ServidorRMI
{
    public static void main(String[] args) throws Exception
    {
        String url = "rmi://localhost/prueba";
        //String url = "rmi://localhost/C:/Users/Oscar/Desktop/ESCOM/9no/Distribuídos/Prácticas/Tarea4/RMI_3";

        ClaseRMI obj = new ClaseRMI();
        
        // registra la instancia en el rmiregistry
        Naming.rebind(url, obj);
    }
}
