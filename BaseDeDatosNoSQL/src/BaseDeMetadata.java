import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase encargada de manejar la base de datos no relacional NoSQL
 * @author OscarAraya
 *
 */
public class BaseDeMetadata {
	
	/**
	 * Constructor de la clase BaseDeMetada. Se encarga de crear una base para el Commit y el Rollback, además de crear un
	 * ArrayList que contenga los arboles de decripting de Huffman
	 */
	public BaseDeMetadata() {
		File archivoCommit = new File("RecuperacionPorCommit.json");
		if(archivoCommit.exists()) {
		    archivoCommit.delete();
		}

		if(!archivoCommit.exists()) {
			try {
				FileWriter archivoCrear = new FileWriter("RecuperacionPorCommit.json");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		arbolesParaDecript = new ArrayList<CompresionHuffman>();
	}
	
	
	/**
	 * Metodo que se encarga de agregar arboles de decripting al ArrayList
	 * @param compresion arbol de decripting a agregar
	 */
	public void agregarListaDeArbolesHuffman(CompresionHuffman compresion) {
		boolean flag = false;
		for(int i=0; i<arbolesParaDecript.size(); i++) {
			if(arbolesParaDecript.get(i).arbol.datoOriginal.equals(compresion.arbol.datoOriginal)) {
				flag = true;
			}
		}
		if(!flag) {
			arbolesParaDecript.add(compresion);
		}
	}
	
	
	/**
	 * Metodo que se encarga de buscar su en la lista existe el arbol de decripting para un string especifico
	 * @param dato String que quiere ser verificado
	 * @return
	 */
	public String encontrarDecriptListaArbolesHuffman(String dato) {
		for(int i=0; i<arbolesParaDecript.size(); i++) {
			if(arbolesParaDecript.get(i).arbol.resultado.equals(dato)) {
				//System.out.println(arbolesParaDecript.get(i).arbol.datoOriginal);
				return(arbolesParaDecript.get(i).arbol.datoOriginal);
			}
		}
		return "";
	}
	
	/**
	 * Metodo que se encarga de agregar a la metadata
	 * @param RAID5
	 * @param nombreGaleria
	 * @param nombreFoto
	 * @param autor
	 * @param anioCreacion
	 * @param tamano
	 * @param descripcion
	 * @throws JSONException
	 */
	public void agregarMetadata(ImageController RAID5,String nombreGaleria,String nombreFoto, String autor, 
			String anioCreacion, String tamano, String descripcion) throws JSONException {
		
		this.RAID5 = RAID5;
		
		File archivo = new File("MetadataDB/"+nombreGaleria);		
		if(!archivo.exists()) {
			archivo.mkdir();
		}
		
		File base = new File("MetadataDB/"+nombreGaleria+"/BaseDeMetadata.json");		
		if(!base.exists()) {
			try {
				FileWriter archivoCrear = new FileWriter("MetadataDB/"+nombreGaleria+"/BaseDeMetadata.json");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		CompresionHuffman compresionNombre;
		CompresionHuffman compresionAutor;
		CompresionHuffman compresionAnio;
		CompresionHuffman compresionTamano;
		CompresionHuffman compresionDescripcion;
		CompresionHuffman compresionGaleria;
		
		JSONObject json = new JSONObject();
		
		JSONObject jsonCommit = new JSONObject();
		jsonCommit.accumulate("ID", 1);

		
		compresionGaleria = new CompresionHuffman(nombreGaleria);
		jsonCommit.accumulate("galeria", compresionGaleria.arbol.resultado);
		agregarListaDeArbolesHuffman(compresionGaleria);
		
		compresionNombre = new CompresionHuffman(nombreFoto);
		json.accumulate("nombre", (compresionNombre).arbol.resultado);
		jsonCommit.accumulate("nombre", (compresionNombre).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionNombre);
		
		compresionAutor = new CompresionHuffman(autor);
		json.accumulate("autor", (compresionAutor).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionAutor);
		
		compresionAnio = new CompresionHuffman(anioCreacion);
		json.accumulate("anio", (compresionAnio).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionAnio);
	
		compresionTamano = new CompresionHuffman(tamano);
		json.accumulate("tamano", (compresionTamano).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionTamano);
		
		compresionDescripcion = new CompresionHuffman(descripcion);
		json.accumulate("descripcion", (compresionDescripcion).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionDescripcion);
		
        try(FileWriter fw = new FileWriter("MetadataDB/"+nombreGaleria+"/BaseDeMetadata.json", true);
        	    BufferedWriter bw = new BufferedWriter(fw);
        	    PrintWriter out = new PrintWriter(bw))
        	    {
        	        out.println(json);
        	} catch (IOException e) {
				e.printStackTrace();
        	}
        
        try(FileWriter fw = new FileWriter("RecuperacionPorCommit.json", true);
        	    BufferedWriter bw = new BufferedWriter(fw);
        	    PrintWriter out = new PrintWriter(bw))
        	    {
        	        out.println(jsonCommit);
        	} catch (IOException e) {
				e.printStackTrace();
        	}
        
	}
	
	
	/**
	 * Metodo que se encarga de modificar la metadata
	 * @param nombreGaleria
	 * @param datoModificar
	 * @param nuevoDato
	 * @param datoObjetivo
	 * @param comparativa
	 * @throws IOException
	 * @throws JSONException
	 */
	public void modificarMetadata(String nombreGaleria, String datoModificar, String nuevoDato, String datoObjetivo, String comparativa) 
			throws IOException, JSONException {
		ArrayList<String> listaLineas = new ArrayList<String>();		
		String linea;
	    FileReader fw = new FileReader("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    
	    
	    JSONObject jsonCommit = new JSONObject();
	    jsonCommit.accumulate("ID", 2);
	   

	
	    while((linea = br.readLine())!=null) {
	    	
    		JSONObject json = new JSONObject(linea);
    		
    		
    		CompresionHuffman compresionNombre;
    		CompresionHuffman compresionAutor;
    		CompresionHuffman compresionTamano;
    		CompresionHuffman compresionAnio;
    		CompresionHuffman compresionDescripcion;

    		CompresionHuffman compresionDatoModificado;
    		CompresionHuffman compresionValorAntiguo;
    		
    		
	    	if(encontrarDecriptListaArbolesHuffman((String) json.get(datoObjetivo)).equals(comparativa)) {
	    		JSONObject jsonNuevo = new JSONObject();
	    		
	    		if(datoModificar.equals("nombre")) {
	    			compresionNombre = new CompresionHuffman(nuevoDato);
		    		agregarListaDeArbolesHuffman(compresionNombre);
	    		}else{
	    			compresionNombre = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("nombre"))));
	    		}
	    		jsonNuevo.accumulate("nombre", (compresionNombre).arbol.resultado);
	    		
	    		
	    		if(datoModificar.equals("autor")) {
	    			compresionAutor = new CompresionHuffman(nuevoDato);
		    		agregarListaDeArbolesHuffman(compresionAutor);
	    		}else{
	    			compresionAutor = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("autor"))));
	    		}
	    		jsonNuevo.accumulate("autor", (compresionAutor).arbol.resultado);
	    		
	    		
	    		if(datoModificar.equals("anio")) {
	    			compresionAnio = new CompresionHuffman(nuevoDato);
		    		agregarListaDeArbolesHuffman(compresionAnio);

	    		}else{
	    			compresionAnio = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("anio"))));
	    		}
	    		jsonNuevo.accumulate("anio", (compresionAnio).arbol.resultado);
	    		
	    		
	    		if(datoModificar.equals("tamano")) {
	    			compresionTamano = new CompresionHuffman(nuevoDato);
		    		agregarListaDeArbolesHuffman(compresionTamano);

	    		}else{
	    			compresionTamano = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("tamano"))));
	    		}
	    		jsonNuevo.accumulate("tamano", (compresionTamano).arbol.resultado);
	    		
	    		
	    		
	    		if(datoModificar.equals("descripcion")) {
	    			compresionDescripcion = new CompresionHuffman(nuevoDato);
		    		agregarListaDeArbolesHuffman(compresionDescripcion);
	    		}else{
	    			compresionDescripcion = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("descripcion"))));
	    		}
	    		jsonNuevo.accumulate("descripcion", (compresionTamano).arbol.resultado);
	    		
	    		
	    		compresionDatoModificado = new CompresionHuffman(datoModificar);
	    		compresionValorAntiguo = new CompresionHuffman(nuevoDato);
	    		
	    		
	    		jsonCommit.accumulate("nombre", (compresionNombre).arbol.resultado);
	    		jsonCommit.accumulate("datoModificado", (compresionDatoModificado).arbol.resultado);
	    		jsonCommit.accumulate("valorAntiguo", (compresionValorAntiguo).arbol.resultado);
	    			    		
	    		
	    		listaLineas.add(jsonNuevo.toString());
	    	}else {
	    		listaLineas.add(linea);
	    	}
	    }
	    
	    
	    
	    br.close();
	    File archivo = new File("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
	    archivo.delete();
	    
	    try {
			FileWriter archivoCrear = new FileWriter("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
	    for(int i=0; i<listaLineas.size(); i++) {
	    	 try(FileWriter fw2 = new FileWriter("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json", true);
	         	    BufferedWriter bw2 = new BufferedWriter(fw2);
	         	    PrintWriter out = new PrintWriter(bw2))
	         	    {
	         	    	out.println(listaLineas.get(i));
	         	} catch (IOException e) {
					e.printStackTrace();
	         	}
	    }
	    
	    try(FileWriter fw3 = new FileWriter("RecuperacionPorCommit.json", true);
	        	    BufferedWriter bw = new BufferedWriter(fw3);
	        	    PrintWriter out = new PrintWriter(bw))
	        	    {
	        	        out.println(jsonCommit);
	        	} catch (IOException e) {
					e.printStackTrace();
	        	}
	 
	}
	
	
	
	
	
	
	/**
	 * Metodo que se encarga de eliminar la metadata
	 * @param atributo
	 * @param comparativo
	 * @param galeria
	 * @param RAID5
	 * @throws IOException
	 * @throws JSONException
	 */
	public void eliminarMetadata(String atributo, String comparativo, String galeria, ImageController RAID5) 
			throws IOException, JSONException {
		ArrayList<String> listaLineas = new ArrayList<String>();		
		String linea;
	    FileReader fw = new FileReader("MetadataDB/" + galeria +"/BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    
	    JSONObject jsonCommit = new JSONObject();
		jsonCommit.accumulate("ID", 3);
		
		
	    while((linea = br.readLine())!=null) {
	    	CompresionHuffman compresionNombreAntiguo;
    		CompresionHuffman compresionAutorAntiguo;
    		CompresionHuffman compresionAnioAntiguo;
    		CompresionHuffman compresionTamanoAntiguo;
    		CompresionHuffman compresionDescripcionAntiguo;
    		CompresionHuffman compresionGaleria;
    		
    		JSONObject json = new JSONObject(linea);
    		
	    	if(encontrarDecriptListaArbolesHuffman((String)json.get(atributo)).equals(comparativo)) {
	    		
	    		eliminarRAID(RAID5, encontrarDecriptListaArbolesHuffman((String) json.get("nombre")));
	    	
	    		compresionGaleria = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String)json.get("galeria")));
	    		compresionNombreAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("nombre")));
	    		compresionAutorAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("autor")));
	    		compresionAnioAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("anio")));
	    		compresionTamanoAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("tamano")));
	    		compresionDescripcionAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("descripcion")));

	    		jsonCommit.accumulate("galeria", (compresionGaleria).arbol.resultado);
	    		jsonCommit.accumulate("nombre", (compresionNombreAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("autor", (compresionAutorAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("anio", (compresionAnioAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("tamano", (compresionTamanoAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("descripcion", (compresionDescripcionAntiguo).arbol.resultado);
	    	}else{
	    		listaLineas.add(linea);
	    	}
	    }
	    br.close();
	    File archivo = new File("MetadataDB/" + galeria +"/BaseDeMetadata.json");
	    archivo.delete();
	    
	    try {
			FileWriter archivoCrear = new FileWriter("MetadataDB/" + galeria +"/BaseDeMetadata.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    try(FileWriter fw3 = new FileWriter("RecuperacionPorCommit.json", true);
        	    BufferedWriter bw = new BufferedWriter(fw3);
        	    PrintWriter out = new PrintWriter(bw))
        	    {
        	        out.println(jsonCommit);
        	} catch (IOException e) {
				e.printStackTrace();
        	}
	    
	    for(int i=0; i<listaLineas.size(); i++) {
	    	 try(FileWriter fw2 = new FileWriter("MetadataDB/" + galeria +"/BaseDeMetadata.json", true);
	         	    BufferedWriter bw2 = new BufferedWriter(fw2);
	         	    PrintWriter out = new PrintWriter(bw2))
	         	    {
	         	    	out.println(listaLineas.get(i));
	         	} catch (IOException e) {
					e.printStackTrace();
	         	}
	    }
	    
	}
	
	/**
	 * Metodo que se encarga de eliminar una imagen de los discos del RAID5
	 * @param RAID5
	 * @param nombre
	 */
	public void eliminarRAID(ImageController RAID5, String nombre) {
		RAID5.deleteImageFromRaid(nombre);
	}
	
	/**
	 * Metodo que se encarga de mostrar datos de la metadata
	 * @param galeria
	 * @param datoMostrar
	 * @param datoComparar
	 * @param comparativo
	 * @param RAID
	 * @param server
	 * @throws JSONException
	 * @throws IOException
	 */
	public void mostrarMetadata(String galeria, String datoMostrar, String datoComparar, String comparativo, ImageController RAID
			,Servidor server) 
			throws JSONException, IOException {
		String linea;
	    FileReader fw = new FileReader("MetadataDB/" + galeria +"/BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    
	    //ALISTAR ARRAYLIST PARA ENVIAR TODOS LOS DATOS POR JSON
	    JSONObject jsonSolicitado = new JSONObject();
	    while((linea = br.readLine())!=null) {
    		JSONObject json = new JSONObject(linea);
	    	if(encontrarDecriptListaArbolesHuffman((String)json.get(datoComparar)).equals(comparativo)) {
	    		String nombre = encontrarDecriptListaArbolesHuffman((String) json.get("nombre"));
	    		String autor = encontrarDecriptListaArbolesHuffman((String) json.get("autor"));
	    		String anioCreacion = encontrarDecriptListaArbolesHuffman((String) json.get("anio"));
	    		String tamano = encontrarDecriptListaArbolesHuffman((String) json.get("tamano"));
	    		String descripcion = encontrarDecriptListaArbolesHuffman((String) json.get("descripcion"));
	    		
	    		////se accede a la imagen en el raid//////////////////////
	    		////si no esta algun disco o archivo se reconstruye///////
	    		RAID.addImage(nombre, 1);
	    		/////////////////////////////////////////////////////////
	    		
	    		if(datoMostrar.equals("nombre")) {
		    		jsonSolicitado.accumulate("nombre", nombre);
	    		}else {
		    			if(datoMostrar.equals("autor")) {
		    				jsonSolicitado.accumulate("autor", autor);
		    			}else if(datoMostrar.equals("anio")) {
		    				jsonSolicitado.accumulate("anio", anioCreacion);
		    			}else if(datoMostrar.equals("tamano")) {
		    				jsonSolicitado.accumulate("tamano", tamano);
		    			}else {
		    				jsonSolicitado.accumulate("descripcion", descripcion);
		    		}
			    	jsonSolicitado.accumulate("nombre", nombre);
	    		}

	
	    			    		
	    	}
	    	
	    }
		server.enviar(jsonSolicitado.toString());
	    br.close();
	}
	
	
	
	/**
	 * Metodo que se encarga de hacer el opuesto al agregar dentro del Rollback
	 * @param nombreFoto
	 * @param nombreGaleria
	 * @throws IOException
	 * @throws JSONException
	 */
	public void eliminarRollback(String nombreFoto, String nombreGaleria) throws IOException, JSONException {
		ArrayList<String> listaLineas = new ArrayList<String>();		
		String linea;
	    FileReader fw = new FileReader("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    
		
	    while((linea = br.readLine())!=null) {
    
    		JSONObject json = new JSONObject(linea);
    		
	    	if(encontrarDecriptListaArbolesHuffman((String)json.getString("nombre")).equals(nombreFoto)) {
	    		
	    		eliminarRAID(RAID5, encontrarDecriptListaArbolesHuffman((String) json.get("nombre")));
	    	
	    		
	    	}else{
	    		listaLineas.add(linea);
	    	}
	    }
	    br.close();
	    File archivo = new File("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
	    archivo.delete();
	    
	    try {
			FileWriter archivoCrear = new FileWriter("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
	    for(int i=0; i<listaLineas.size(); i++) {
	    	 try(FileWriter fw2 = new FileWriter("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json", true);
	         	    BufferedWriter bw2 = new BufferedWriter(fw2);
	         	    PrintWriter out = new PrintWriter(bw2))
	         	    {
	         	    	out.println(listaLineas.get(i));
	         	} catch (IOException e) {
					e.printStackTrace();
	         	}
	    }
	}
	
	/**
	 * Metodo que se encarga de hacer el opuesto al eliminar dentro del Rollback
	 * @param nombre
	 * @param autor
	 * @param anio
	 * @param tamano
	 * @param descripcion
	 * @param nombreGaleria
	 * @throws JSONException
	 * @throws IOException
	 */
	public void agregarRollback(String nombre, String autor, String anio, String tamano, String descripcion, String nombreGaleria) throws JSONException, IOException {	
		File base = new File("MetadataDB/"+nombreGaleria+"/BaseDeMetadata.json");				
		
		CompresionHuffman compresionNombre;
		CompresionHuffman compresionAutor;
		CompresionHuffman compresionAnio;
		CompresionHuffman compresionTamano;
		CompresionHuffman compresionDescripcion;
		CompresionHuffman compresionGaleria;
		
		JSONObject json = new JSONObject();
		

		
		compresionGaleria = new CompresionHuffman(nombreGaleria);
		agregarListaDeArbolesHuffman(compresionGaleria);
		
		compresionNombre = new CompresionHuffman(nombre);
		json.accumulate("nombre", (compresionNombre).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionNombre);
		
		compresionAutor = new CompresionHuffman(autor);
		json.accumulate("autor", (compresionAutor).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionAutor);
		
		compresionAnio = new CompresionHuffman(anio);
		json.accumulate("anio", (compresionAnio).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionAnio);
	
		compresionTamano = new CompresionHuffman(tamano);
		json.accumulate("tamano", (compresionTamano).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionTamano);
		
		compresionDescripcion = new CompresionHuffman(descripcion);
		json.accumulate("descripcion", (compresionDescripcion).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionDescripcion);
		
        try(FileWriter fw = new FileWriter("MetadataDB/"+nombreGaleria+"/BaseDeMetadata.json", true);
        	    BufferedWriter bw = new BufferedWriter(fw);
        	    PrintWriter out = new PrintWriter(bw))
        	    {
        	        out.println(json);
        	} catch (IOException e) {
				e.printStackTrace();
        	}
        
        RAID5.loadImage("Papelera/"+nombre+".png", nombre);
        
        File imagen = new File("Papelera/"+nombre+".png");
        imagen.delete();
        
	}
	
	/**
	 * Metodo que se encarga de devolver las modificaciones de la metadata dentro del Rollback
	 * @param nombreGaleria
	 * @param nombre
	 * @param datoModificado
	 * @param valorAntiguo
	 * @throws IOException
	 * @throws JSONException
	 */
	public void modificarRollback(String nombreGaleria, String nombre, String datoModificado, String valorAntiguo) 
			throws IOException, JSONException {
		ArrayList<String> listaLineas = new ArrayList<String>();		
		String linea;
	    FileReader fw = new FileReader("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    

	    while((linea = br.readLine())!=null) {
	    	
    		JSONObject json = new JSONObject(linea);
    		
    		
    		CompresionHuffman compresionNombre;
    		CompresionHuffman compresionAutor;
    		CompresionHuffman compresionTamano;
    		CompresionHuffman compresionAnio;
    		CompresionHuffman compresionDescripcion;
    		
    		
	    	if(encontrarDecriptListaArbolesHuffman((String) json.get("nombre")).equals(nombre)) {
	    		JSONObject jsonNuevo = new JSONObject();
	    		
	    		if(datoModificado.equals("nombre")) {
	    			compresionNombre = new CompresionHuffman(valorAntiguo);
		    		agregarListaDeArbolesHuffman(compresionNombre);
	    		}else{
	    			compresionNombre = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("nombre"))));
	    		}
	    		jsonNuevo.accumulate("nombre", (compresionNombre).arbol.resultado);
	    		
	    		
	    		if(datoModificado.equals("autor")) {
	    			compresionAutor = new CompresionHuffman(valorAntiguo);
		    		agregarListaDeArbolesHuffman(compresionAutor);
	    		}else{
	    			compresionAutor = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("autor"))));
	    		}
	    		jsonNuevo.accumulate("autor", (compresionAutor).arbol.resultado);
	    		
	    		
	    		if(datoModificado.equals("anio")) {
	    			compresionAnio = new CompresionHuffman(valorAntiguo);
		    		agregarListaDeArbolesHuffman(compresionAnio);
	    		}else{
	    			compresionAnio = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("anio"))));
	    		}
	    		jsonNuevo.accumulate("anio", (compresionAnio).arbol.resultado);
	    		
	    		
	    		if(datoModificado.equals("tamano")) {
	    			compresionTamano = new CompresionHuffman(valorAntiguo);
		    		agregarListaDeArbolesHuffman(compresionTamano);
	    		}else{
	    			compresionTamano = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("tamano"))));
	    		}
	    		jsonNuevo.accumulate("tamano", (compresionTamano).arbol.resultado);
	    		
	    		
	    		
	    		if(datoModificado.equals("descripcion")) {
	    			compresionDescripcion = new CompresionHuffman(valorAntiguo);
		    		agregarListaDeArbolesHuffman(compresionDescripcion);
	    		}else{
	    			compresionDescripcion = new CompresionHuffman((encontrarDecriptListaArbolesHuffman((String) json.get("descripcion"))));
	    		}
	    		jsonNuevo.accumulate("descripcion", (compresionTamano).arbol.resultado);
	    		
	    			    		
	    		
	    		listaLineas.add(jsonNuevo.toString());
	    	}else {
	    		listaLineas.add(linea);
	    	}
	    }
	    
	    
	    
	    br.close();
	    File archivo = new File("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
	    archivo.delete();
	    
	    try {
			FileWriter archivoCrear = new FileWriter("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
	    for(int i=0; i<listaLineas.size(); i++) {
	    	 try(FileWriter fw2 = new FileWriter("MetadataDB/" + nombreGaleria +"/BaseDeMetadata.json", true);
	         	    BufferedWriter bw2 = new BufferedWriter(fw2);
	         	    PrintWriter out = new PrintWriter(bw2))
	         	    {
	         	    	out.println(listaLineas.get(i));
	         	} catch (IOException e) {
					e.printStackTrace();
	         	}
	    }
	    
	}
	
	
	
	
	
	/**
	 * Metodo que se encarga de hacer el commit
	 */
	public void commit() {
		try {
			FileWriter archivoCrear = new FileWriter("RecuperacionPorCommit.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que se encarga de hacer el rollback
	 * @throws JSONException
	 * @throws IOException
	 */
	public void rollback() throws JSONException, IOException {
		ArrayList<JSONObject> ordenesDesordenadas = new ArrayList<>();
		ArrayList<JSONObject> ordenes = new ArrayList<>();

		String linea;
	    FileReader fw = new FileReader("RecuperacionPorCommit.json");
	    BufferedReader br = new BufferedReader(fw);
	    	    
	    while((linea = br.readLine())!=null) {
	    	JSONObject json = new JSONObject(linea);
	    	ordenesDesordenadas.add(json);
	    }
	    
	    for(int i=0; i<ordenesDesordenadas.size()-1; i++) {
	    	ordenes.add(ordenesDesordenadas.get(ordenesDesordenadas.size()-i));
	    }
	    
	    for(int i=0; i<ordenes.size()-1;i++) {
	    	if(ordenes.get(i).getInt("ID")==1) {
	    		eliminarRollback(encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("nombre")), 
	    				encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("galeria")));
	    	}else if(ordenes.get(i).getInt("ID")==3) {
	    		agregarRollback(encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("nombre")), 
	    				encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("autor")), 
	    				encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("anio")),
	    				encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("tamano")),
	    				encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("descripcion")),
	    				encontrarDecriptListaArbolesHuffman(ordenes.get(i).getString("galeria")));
	    	}
	    }
	    
	    
	    
	    
	    
	    br.close();
	    commit();
	}
	
	ArrayList<CompresionHuffman> arbolesParaDecript;
	ImageController RAID5;
	
}
