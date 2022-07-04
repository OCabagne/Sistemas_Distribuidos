/*
//  Author: Oscar Cabagné
*/

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.google.gson.*;

class Usuario {
    int id_usuario;
    String email;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String fecha_nacimiento;
    String telefono;
    String genero;
    byte[] foto;
}

public class Cliente 
{
    public static void main(String[] args) throws Exception 
    {
        while(true)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("MENU");
            System.out.println("a. Alta usuario");
            System.out.println("b. Consulta usuario");
            System.out.println("c. Borra usuario");
            System.out.println("d. Salir");
            System.out.print("\nOpcion: ");

            char opc = br.readLine().charAt(0);

            switch(opc) 
            {
                case 'a':
                    System.out.println("    Alta Usuario");
                    Usuario usuario = new Usuario();

                    System.out.print(">: Email: ");
                    usuario.email = br.readLine();

                    System.out.print(">: Nombre: ");
                    usuario.nombre = br.readLine();

                    System.out.print(">: Apellido Paterno: ");
                    usuario.apellido_paterno = br.readLine();

                    System.out.print(">: Apellido Materno: ");
                    usuario.apellido_materno = br.readLine();
                    
                    System.out.print(">: Fecha de nacimiento: ");
                    usuario.fecha_nacimiento = br.readLine();

                    System.out.print(">: Telefono: ");
                    usuario.telefono = br.readLine();

                    System.out.print(">: Genero (M/F): ");
                    usuario.genero = br.readLine();
                    alta_usuario(usuario);
                    break;
                case 'b':
                    System.out.println(">: Consulta Usuario");
                    System.out.print(">: Ingresar ID: ");
                    consultar_usuario(Integer.parseInt(br.readLine()));
                    break;
                case 'c':
                    System.out.println(">: Borrar Usuario");
                    System.out.print(">: Ingresa el ID de usuario: ");
                    borrar_usuario(Integer.parseInt(br.readLine()));
                    break;
                case 'd':
                    br.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println(">: Opcion incorrecta. Volver a intentar.");
                    break;
            }
        }
    }

    public static void alta_usuario(Usuario usuario) throws IOException 
    {
        URL url = new URL("http://20.85.211.15:8080/Servicio/rest/ws/alta_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        Gson gson = builder.create();
        String body = gson.toJson(usuario);

        String parametros = "usuario=" + URLEncoder.encode(body, "UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();

        if (conexion.getResponseCode() == 200) 
        {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;

            while ((respuesta = br.readLine()) != null)
                System.out.println(">: Se agrego el usuario: ID- " + respuesta);
        } 
        else 
        {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException(">: " + conexion.getResponseCode());
        }

        conexion.disconnect();

    }

    public static void consultar_usuario(int id_usuario) throws IOException 
    {
        URL url = new URL("http://20.85.211.15:8080/Servicio/rest/ws/consulta_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String parametros = "id_usuario=" + URLEncoder.encode(String.valueOf(id_usuario), "UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();

        if (conexion.getResponseCode() == 200)
        {
            Usuario user = new Usuario();
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;

            Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            while ((respuesta = br.readLine()) != null)
            {
                user = (Usuario) j.fromJson(respuesta, Usuario.class);
                System.out.println(">: Nombre: " + user.nombre);
                System.out.println(">: Apellido Paterno: " + user.apellido_paterno);
                System.out.println(">: Apellido Materno: " + user.apellido_materno);
                System.out.println(">: Fecha de Nacimiento: " + user.fecha_nacimiento);
                System.out.println(">: Telefono: " + user.telefono);
                System.out.println(">: Genero: " + user.genero);
            }

            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);
            
            BufferedReader read  = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(">: Quieres modificar los datos? (S/N) ");
            char opc = read.readLine().charAt(0);
            switch(opc)
            {
                case 'S':
                    modificar_usuario(id_usuario, user);
                    br.close();
                    break;

                case 'N': 
                    br.close();
                    break;

                default: 
                    System.out.println(">: Opción incorrecta.");
                    break;
            }
        } 
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException(">: " + conexion.getResponseCode());
        }
        conexion.disconnect();
    }
    
    public static void modificar_usuario(int id_usuario, Usuario user) throws IOException 
    {
        Usuario usuario = new Usuario();
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        System.out.print(">: Email: ");
        usuario.email = read.readLine();

        System.out.print(">: Nombre: ");
        usuario.nombre = read.readLine();

        System.out.print(">: Apellido Paterno: ");
        usuario.apellido_paterno = read.readLine();

        System.out.print(">: Apellido Materno: ");
        usuario.apellido_materno = read.readLine();
        
        System.out.print(">: Fecha de nacimiento: ");
        usuario.fecha_nacimiento = read.readLine();

        System.out.print(">: Telefono: ");
        usuario.telefono = read.readLine();

        System.out.print(">: Genero (M/F): ");
        usuario.genero = read.readLine();

        if(usuario.email.equals("")) usuario.email = user.email;
        if(usuario.nombre.equals("")) usuario.nombre = user.nombre;
        if(usuario.apellido_paterno.equals("")) usuario.apellido_paterno = user.apellido_paterno;
        if(usuario.apellido_materno.equals("")) usuario.apellido_materno = user.apellido_materno;
        if(usuario.fecha_nacimiento.equals("")) usuario.fecha_nacimiento = user.fecha_nacimiento;
        if(usuario.telefono.equals("")) usuario.telefono = user.telefono;
        if(usuario.genero.equals("")) usuario.genero = user.genero;

        URL url = new URL("http://20.85.211.15:8080/Servicio/rest/ws/modifica_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        Gson gson = builder.create();
        String body = gson.toJson(usuario);

        String parametros = "usuario=" + URLEncoder.encode(body, "UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();

        if (conexion.getResponseCode() == 200) 
        {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;

            while ((respuesta = br.readLine()) != null)
                System.out.println(">: Se modifico el usuario: ID- " + respuesta);
        } 
        else 
        {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException(">: " + conexion.getResponseCode());
        }

        conexion.disconnect();
    }

    public static void borrar_usuario(int id_usuario) throws IOException 
    {
        URL url = new URL("http://20.85.211.15:8080/Servicio/rest/ws/borra_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String parametros = "id_usuario=" + URLEncoder.encode(String.valueOf(id_usuario), "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();

        if (conexion.getResponseCode() == 200)
        {
            System.out.println(">: El usuario ha sido borrado.");
        } 
        else 
        {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException(">: " + conexion.getResponseCode());
        }
        conexion.disconnect();
    }
}
