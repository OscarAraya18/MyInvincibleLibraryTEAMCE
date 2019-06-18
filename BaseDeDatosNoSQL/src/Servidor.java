import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase encargada de la comunicación y del manejo de mensajes entre el cliente y el servidor
 */
public class Servidor {

	/**
	 * Atributos que hacen posible la comunicación entre el cliente y el servidor dentro de la galería
	 */
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner escaner = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";
    private ImageController RAID5 = new ImageController();
    private BaseDeMetadata METADATADB = new BaseDeMetadata();

    /**
     * Metodo encargado de establecer la conexión entre el cliente y el servidor
     * @param puerto Puerto por el cual se conectan cliente y servidor
     */
    public void levantarConexion(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            mostrarTexto("Esperando conexión entrante en el puerto " + String.valueOf(puerto) + "...");
            socket = serverSocket.accept();
            mostrarTexto("Conexión establecida con: " + socket.getInetAddress().getHostName() + "\n\n\n");
        } catch (Exception e) {
            mostrarTexto("Error en levantarConexion(): " + e.getMessage());
            System.exit(0);
        }
    }
    
    
    /**
     * Metodo que maneja los hilos de escucha y envío de datos desde el servidor
     */
    public void flujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error en la apertura de flujos");
        }
    }

    /**
     * Metodo que recibe los datos desde el buffer destinado al cliente
     * @throws JSONException
     */
	public void recibirDatos() throws JSONException {
        try {
            do {
                byte[] buffer = new byte[1024];
                int read;
                
                String output = "";
                                
                while((read = bufferDeEntrada.read(buffer)) != -1) {
           
                	
                	
                   output += new String(buffer, 0, read);
                                        
                    

                    if(output.contains("]}") || output.contains(":3}") || output.contains(":4}") || output.contains(":5,")
                    		|| output.contains("Disco")) {
                    	System.out.println("MENSAJE "+ output);
                    	transformer(output);
                    	output="";
                    	buffer = new byte[1024];
                    }
            
                    System.out.flush();

                    
                    
                }
                
                
            } while (true);
        } catch (IOException e) {
            cerrarConexion();
        }
    }

	
	
	/**
	 * Metodo que se encarga de interpretar el mensaje que llega desde el cliente
	 * @param output Mensaje que llega desde el cliente
	 * @throws JSONException
	 * @throws IOException
	 */
	public void transformer(String output) throws JSONException, IOException {
		JSONObject jsonMensaje = new JSONObject(String.valueOf(output));
		if (jsonMensaje.getInt("ID")==1){
			ArrayList<Integer> listaImagen = new ArrayList<>();
			JSONArray listaJSON = jsonMensaje.getJSONArray("Imagen");
			for(int i=0; i<listaJSON.length(); i++) {
				listaImagen.add(listaJSON.getInt(i));
				
			}
			String nombre = jsonMensaje.getString("Nombre");
			RAID5.convertImagefromBytes(listaImagen, nombre);
			
			
			
			
			
			
			
		}else if(jsonMensaje.getInt("ID")==2) {
			String mensaje = jsonMensaje.getJSONArray("Scripts").getString(0);
			String orden = mensaje.substring(0,6);
			
			
			if(orden.equals("DELETE")) {
				mensaje = mensaje.substring(12);
				
				int contador = 0;
				StringBuilder sb = new StringBuilder();
				while(mensaje.charAt(contador) != ' ') {
					
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				String lugar = sb.toString();
				System.out.println(lugar);
				
				mensaje = mensaje.substring(contador+7);
				
				contador = 0;
				sb = new StringBuilder();
				while(mensaje.charAt(contador) != '=') {
					
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				String dato = sb.toString();
				System.out.println(dato);
				
				mensaje = mensaje.substring(contador+1);
				contador = 0;
				sb = new StringBuilder();
				while(mensaje.charAt(contador) != ';') {
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				
				String comparativo = sb.toString();
				System.out.println(comparativo);
				
				
				METADATADB.eliminarMetadata(dato,comparativo,lugar,RAID5);
				
		
				
				
			}else if(orden.equals("SELECT")) {
				mensaje = mensaje.substring(7);
				
				int contador = 0;
				StringBuilder sb = new StringBuilder();
				while(mensaje.charAt(contador) != ' ') {
					
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				String nombreDato = sb.toString();
				System.out.println(nombreDato);
				
				mensaje = mensaje.substring(contador+6);
				
				contador = 0;
				sb = new StringBuilder();
				while(mensaje.charAt(contador) != ' ') {
					
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				String nombreGaleria = sb.toString();
				System.out.println(nombreGaleria);
				
				mensaje = mensaje.substring(contador+7);
				contador = 0;
				sb = new StringBuilder();
				while(mensaje.charAt(contador) != '=') {
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				
				String datoDeseado = sb.toString();
				System.out.println(datoDeseado);
				mensaje = mensaje.substring(contador+1);
				
				String comparativa = mensaje.substring(0, mensaje.length()-1);
				System.out.println(comparativa);
				
				
				METADATADB.mostrarMetadata(nombreGaleria,nombreDato,datoDeseado,comparativa, RAID5, this);

				
				
				
			}else if(orden.equals("UPDATE")) {
				mensaje = mensaje.substring(7);
				
				int contador = 0;
				StringBuilder sb = new StringBuilder();
				while(mensaje.charAt(contador) != ' ') {
					
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				String nombreGaleria = sb.toString();
				System.out.println(nombreGaleria);
				
				mensaje = mensaje.substring(contador+5);
				
				contador = 0;
				sb = new StringBuilder();
				while(mensaje.charAt(contador) != '=') {
					
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				String datoModificar = sb.toString();
				System.out.println(datoModificar);
				
				mensaje = mensaje.substring(contador+1);
				contador = 0;
				sb = new StringBuilder();
				while(mensaje.charAt(contador) != ' ') {
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				
				String nuevoDato = sb.toString();
				System.out.println(nuevoDato);
				mensaje = mensaje.substring(contador+7);
				
				
				contador = 0;
				sb = new StringBuilder();
				while(mensaje.charAt(contador) != '=') {
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				
				String datoObjetivo = sb.toString();
				System.out.println(datoObjetivo);
				mensaje = mensaje.substring(contador+1);
				
				String comparativa = mensaje.substring(0, mensaje.length()-1);
				System.out.println(comparativa);
				
				METADATADB.modificarMetadata(nombreGaleria,datoModificar,nuevoDato,datoObjetivo,comparativa);
				
				
			}else{
				mensaje = mensaje.substring(12);
				
				
				int contador = 0;
				StringBuilder sb = new StringBuilder();
				while(mensaje.charAt(contador) != ' ') {
					
					sb.append(mensaje.charAt(contador));
					contador++;
				}
				String nombreGaleria = sb.toString();
				
				mensaje = mensaje.substring(contador+9, mensaje.length()-2);
				
				String[] partes = mensaje.split(",");
				
				System.out.println(mensaje);
				System.out.println(partes[0] + partes[1] + partes[2] + partes[3] + partes[4]);
				System.out.println(mensaje);
				
				METADATADB.agregarMetadata(RAID5,nombreGaleria, partes[0] , partes[1] , partes[2] , partes[3] , partes[4]);
			}
		}else if(jsonMensaje.getInt("ID") == 3) {
			METADATADB.commit();
		}else if(jsonMensaje.getInt("ID") == 4){
			METADATADB.rollback();
		}else if(jsonMensaje.getInt("ID")==5) {
			String nombre = jsonMensaje.getString("nombre");
			File archivo = new File("MetadataDB/"+nombre);		
			if(!archivo.exists()) {
				archivo.mkdir();
			}
			
		}else {
			int disco = jsonMensaje.getInt("Disco");
			File archivo = new File("DISCO"+ disco);		
			if(archivo.exists()) {
				vaciarDisco(archivo);
			}
		}
	}
	
	/**
	 * Metodo que se encarga de vaciar uno de los discos del RAID5 para luego utilizar el concepto del bit de paridad
	 * @param disco Disco que se quiere eliminar
	 * @return booleano que indica si se pudo o no eliminar el disco del RAID5
	 */
	public static boolean vaciarDisco(File disco) {
        if (disco.isDirectory()) {
            File[] children = disco.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = vaciarDisco(children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        System.out.println("removing file or directory : " + disco.getName());
        return disco.delete();
    }
	

	/**
	 * Metodo que se encarga de enviar los messajes desde el servidor hasta el cliente
	 * @param s Mensaje que se quiere enviar a los clientes
	 */
    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error en enviar(): " + e.getMessage());
        }
    }

    
    /**
     * Metodo que se encarga de mostrar el texto que es enviado al cliente
     * @param s Mensaje que se envia al cliente
     */
    public static void mostrarTexto(String s) {
        System.out.print(s);
    }

    /**
     * Metodo que se encarga de escribir en pantalla lo que envia el servidor
     */
    public void escribirDatos() {
        while (true) {
            System.out.print("[Usted] => ");
            enviar(escaner.nextLine());   
        }
    }

    /**
     * Metodo que se encarga de cerrar la conexión entre el cliente y el servidor
     */
    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
        } catch (IOException e) {
          mostrarTexto("Excepción en cerrarConexion(): " + e.getMessage());
        } finally {
            mostrarTexto("Conversación finalizada....");
            System.exit(0);

        }
    }

    /**
     * Metodo que se encarga de levantar la conexióon entre cliente y servidor
     * @param puerto Puerto por el cual se van a comunicar los componentes vía sockets
     */
    public void ejecutarConexion(int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        levantarConexion(puerto);
                        flujos();
                        recibirDatos();
                    } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
    }

    /**
     * Metodo que se encarga de iniciar el sistema de MyInvincibleLibrary
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	
    	File database = new File("MetadataDB");
		if(database.exists()) {
		    database.delete();
		    database.mkdir();
		}
		
		if(!database.exists()) {
			try {
				FileWriter archivoCrear = new FileWriter("MetadataDB");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
        Servidor s = new Servidor();
        Scanner sc = new Scanner(System.in);

        mostrarTexto("Ingresa el puerto [5050 por defecto]: ");
        String puerto = sc.nextLine();
        if (puerto.length() <= 0) puerto = "5050";
        s.ejecutarConexion(Integer.parseInt(puerto));
        s.escribirDatos();
    }
}

	
