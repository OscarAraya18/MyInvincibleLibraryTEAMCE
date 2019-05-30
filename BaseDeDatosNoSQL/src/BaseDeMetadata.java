import java.io.*;
import java.util.ArrayList;

import org.json.*;

public class BaseDeMetadata {
	
	
	public BaseDeMetadata() {
		File archivo = new File("BaseDeMetadata.json");
		File archivoCommit = new File("RecuperacionPorCommit.json");
		if(archivo.exists()) {
		    archivo.delete();
		}
		if(archivoCommit.exists()) {
		    archivoCommit.delete();
		}
		if(!archivo.exists()) {
			try {
				FileWriter archivoCrear = new FileWriter("BaseDeMetadata.json");
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	
	
	
	public String encontrarDecriptListaArbolesHuffman(String dato) {
		for(int i=0; i<arbolesParaDecript.size(); i++) {
			if(arbolesParaDecript.get(i).arbol.resultado.equals(dato)) {
				//System.out.println(arbolesParaDecript.get(i).arbol.datoOriginal);
				return(arbolesParaDecript.get(i).arbol.datoOriginal);
			}
		}
		return "";
	}
	
	
	
		
	
	
	public void agregarMetadata(String nombreFoto, String autor, String anioCreacion, String tamano, String descripcion) throws JSONException {
		CompresionHuffman compresionNombre;
		CompresionHuffman compresionAutor;
		CompresionHuffman compresionAnio;
		CompresionHuffman compresionTamano;
		CompresionHuffman compresionDescripcion;
		
		JSONObject json = new JSONObject();
		
		JSONObject jsonCommit = new JSONObject();
		jsonCommit.accumulate("ID", 1);
		
		compresionNombre = new CompresionHuffman(nombreFoto);
		json.accumulate("nombre", (compresionNombre).arbol.resultado);
		jsonCommit.accumulate("nombre", (compresionNombre).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionNombre);
		
		compresionAutor = new CompresionHuffman(autor);
		json.accumulate("autor", (compresionAutor).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionAutor);
		
		compresionAnio = new CompresionHuffman(anioCreacion);
		json.accumulate("a�oCreaci�n", (compresionAnio).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionAnio);
	
		compresionTamano = new CompresionHuffman(tamano);
		json.accumulate("tama�o", (compresionTamano).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionTamano);
		
		compresionDescripcion = new CompresionHuffman(descripcion);
		json.accumulate("descripci�n", (compresionDescripcion).arbol.resultado);
		agregarListaDeArbolesHuffman(compresionDescripcion);
		
        try(FileWriter fw = new FileWriter("BaseDeMetadata.json", true);
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
	
	
	
	public void modificarMetadata(String nombreFotoModificar, String nuevoNombre, String nuevoAutor, String nuevoAnioCreacion,
			String nuevoTamano, String nuevaDescripcion) throws IOException, JSONException {
		ArrayList<String> listaLineas = new ArrayList<String>();		
		String linea;
	    FileReader fw = new FileReader("BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    
		JSONObject jsonCommit = new JSONObject();
		jsonCommit.accumulate("ID", 2);

		
	    while((linea = br.readLine())!=null) {
    		JSONObject json = new JSONObject(linea);

   
    		CompresionHuffman compresionNombre;
    		CompresionHuffman compresionAutor;
    		CompresionHuffman compresionAnio;
    		CompresionHuffman compresionTamano;
    		CompresionHuffman compresionDescripcion;
    		
    		CompresionHuffman compresionNombreAntiguo;
    		CompresionHuffman compresionAutorAntiguo;
    		CompresionHuffman compresionAnioAntiguo;
    		CompresionHuffman compresionTamanoAntiguo;
    		CompresionHuffman compresionDescripcionAntiguo;
    		
    		
    		
	    	if(encontrarDecriptListaArbolesHuffman((String) json.get("nombre")).equals(nombreFotoModificar)) {
	    		JSONObject jsonNuevo = new JSONObject();
	    		
	    		
	    		compresionNombre = new CompresionHuffman(nuevoNombre);
	    		jsonNuevo.accumulate("nombre", (compresionNombre).arbol.resultado);
	    		jsonCommit.accumulate("nombre", (compresionNombre).arbol.resultado);
	    		agregarListaDeArbolesHuffman(compresionNombre);
	    		
	    		compresionNombreAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("nombre")));
	    		compresionAutorAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("autor")));
	    		compresionAnioAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("a�oCreaci�n")));
	    		compresionTamanoAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("tama�o")));
	    		compresionDescripcionAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("descripci�n")));

	    		jsonCommit.accumulate("nombreAntiguo", (compresionNombreAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("autorAntiguo", (compresionAutorAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("a�oCreaci�nAntiguo", (compresionAnioAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("tama�oAntiguo", (compresionTamanoAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("descripci�nAntiguo", (compresionDescripcionAntiguo).arbol.resultado);

	    		
	    		
	    		compresionAutor = new CompresionHuffman(nuevoAutor);
	    		jsonNuevo.accumulate("autor", (compresionAutor).arbol.resultado);
	    		agregarListaDeArbolesHuffman(compresionAutor);
	    		
	    		compresionAnio = new CompresionHuffman(nuevoAnioCreacion);
	    		jsonNuevo.accumulate("a�oCreaci�n", (compresionAnio).arbol.resultado);
	    		agregarListaDeArbolesHuffman(compresionAnio);
	    	
	    		compresionTamano = new CompresionHuffman(nuevoTamano);
	    		jsonNuevo.accumulate("tama�o", (compresionTamano).arbol.resultado);
	    		agregarListaDeArbolesHuffman(compresionTamano);
	    		
	    		compresionDescripcion = new CompresionHuffman(nuevaDescripcion);
	    		jsonNuevo.accumulate("descripci�n", (compresionDescripcion).arbol.resultado);
	    		agregarListaDeArbolesHuffman(compresionDescripcion);
	    		
	    		listaLineas.add(jsonNuevo.toString());
	    	}else {
	    		listaLineas.add(linea);
	    	}
	    }
	    
	    
	    
	    br.close();
	    File archivo = new File("BaseDeMetadata.json");
	    archivo.delete();
	    
	    try {
			FileWriter archivoCrear = new FileWriter("BaseDeMetadata.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
	    for(int i=0; i<listaLineas.size(); i++) {
	    	 try(FileWriter fw2 = new FileWriter("BaseDeMetadata.json", true);
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
	
	
	
	public void eliminarMetadata(String nombreFotoEliminar) throws IOException, JSONException {
		ArrayList<String> listaLineas = new ArrayList<String>();		
		String linea;
	    FileReader fw = new FileReader("BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    
	    JSONObject jsonCommit = new JSONObject();
		jsonCommit.accumulate("ID", 3);
	    
	    while((linea = br.readLine())!=null) {
	    	CompresionHuffman compresionNombreAntiguo;
    		CompresionHuffman compresionAutorAntiguo;
    		CompresionHuffman compresionAnioAntiguo;
    		CompresionHuffman compresionTamanoAntiguo;
    		CompresionHuffman compresionDescripcionAntiguo;
    		
    		JSONObject json = new JSONObject(linea);
    		
	    	if(encontrarDecriptListaArbolesHuffman((String)json.get("nombre")).equals(nombreFotoEliminar)) {
	    		compresionNombreAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("nombre")));
	    		compresionAutorAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("autor")));
	    		compresionAnioAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("a�oCreaci�n")));
	    		compresionTamanoAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("tama�o")));
	    		compresionDescripcionAntiguo = new CompresionHuffman(encontrarDecriptListaArbolesHuffman((String) json.get("descripci�n")));

	    		jsonCommit.accumulate("nombreAntiguo", (compresionNombreAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("autorAntiguo", (compresionAutorAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("a�oCreaci�nAntiguo", (compresionAnioAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("tama�oAntiguo", (compresionTamanoAntiguo).arbol.resultado);
	    		jsonCommit.accumulate("descripci�nAntiguo", (compresionDescripcionAntiguo).arbol.resultado);
	    	}else{
	    		listaLineas.add(linea);
	    	}
	    }
	    br.close();
	    File archivo = new File("BaseDeMetadata.json");
	    archivo.delete();
	    
	    try {
			FileWriter archivoCrear = new FileWriter("BaseDeMetadata.json");
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
	    	 try(FileWriter fw2 = new FileWriter("BaseDeMetadata.json", true);
	         	    BufferedWriter bw2 = new BufferedWriter(fw2);
	         	    PrintWriter out = new PrintWriter(bw2))
	         	    {
	         	    	out.println(listaLineas.get(i));
	         	} catch (IOException e) {
					e.printStackTrace();
	         	}
	    }
	    
	}
	
	
	public void mostrarMetadata(String nombreFotoMostrar) throws JSONException, IOException {
		String linea;
	    FileReader fw = new FileReader("BaseDeMetadata.json");
	    BufferedReader br = new BufferedReader(fw);
	    
	    while((linea = br.readLine())!=null) {
    		JSONObject json = new JSONObject(linea);
	    	if(encontrarDecriptListaArbolesHuffman((String)json.get("nombre")).equals(nombreFotoMostrar)) {
	    		String nombre = encontrarDecriptListaArbolesHuffman((String) json.get("nombre"));
	    		String autor = encontrarDecriptListaArbolesHuffman((String) json.get("autor"));
	    		String anioCreacion = encontrarDecriptListaArbolesHuffman((String) json.get("a�oCreaci�n"));
	    		String tamano = encontrarDecriptListaArbolesHuffman((String) json.get("tama�o"));
	    		String descripcion = encontrarDecriptListaArbolesHuffman((String) json.get("descripci�n"));
	    		
	    	    System.out.println("El nombre de la foto es: " + nombre);
	    	    System.out.println("El autor de la foto es: " + autor);
	    	    System.out.println("El a�o de creaci�n de la foto es: " + anioCreacion);
	    	    System.out.println("El tama�o de la foto es: " + tamano);
	    	    System.out.println("La descripci�n de la foto es: " + descripcion);
	    		
	    	}
	    }
	    br.close();
	}
	
	public void commit() {
		File archivoCommit = new File("RecuperacionPorCommit.json");
		archivoCommit.delete();
		
		 try {
				FileWriter archivoCrear = new FileWriter("RecuperarPorCommit.json");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	public void rollback() throws JSONException, IOException {
		String linea;
	    FileReader fw = new FileReader("RecuperacionPorCommit.json");
	    BufferedReader br = new BufferedReader(fw);
	    
	    int contador = 1;
	    
	    while((linea = br.readLine())!=null) {
    		JSONObject json = new JSONObject(linea);
	    	int ID = json.getInt("ID");
	    	String nombre = "";
	    	
	    	if(ID!=3) {
		    	nombre = encontrarDecriptListaArbolesHuffman((String) json.get("nombre"));
	    	}
	    	
	    	
	    	if(ID == 1) {
	    		System.out.println("RECUPERANDO ESTADO PREVIO ANTES DE CREAR. LEYENDO LINEA NUMERO " + contador
	    				+ ". Se debe eliminar la imagen de nombre " + nombre);
	    	}else {
	    	    String nombreAntiguo = encontrarDecriptListaArbolesHuffman((String) json.get("nombreAntiguo"));
		    	String autorAntiguo = encontrarDecriptListaArbolesHuffman((String) json.get("autorAntiguo"));
		    	String anioCreacionAntiguo = encontrarDecriptListaArbolesHuffman((String) json.get("a�oCreaci�nAntiguo"));
		    	String tamanoAntiguo = encontrarDecriptListaArbolesHuffman((String) json.get("tama�oAntiguo"));
		    	String descripcionAntiguo = encontrarDecriptListaArbolesHuffman((String) json.get("descripci�nAntiguo"));
		    	
		    	if(ID == 2) {
		    		System.out.println("RECUPERANDO ESTADO PREVIO ANTES DE MODIFICAR. LEYENDO LINEA NUMERO " + contador
		    				+ ". Se debe modificar la imagen de nombre " + nombre + " para ponerle los datos:");
		    		System.out.println("Nombre previo: " + nombreAntiguo);
		    	}else {
		    		System.out.println("RECUPERANDO ESTADO PREVIO ANTES DE BORRAR. LEYENDO LINEA NUMERO " + contador
		    				+ ". Se debe recuperar de la papalera a la imagen de nombre " + nombreAntiguo + " con los datos:");
		    	}
	    		System.out.println("Autor previo: " + autorAntiguo);
	    		System.out.println("A�o de creaci�n previo: " + anioCreacionAntiguo);
	    		System.out.println("Tama�o previo: " + tamanoAntiguo);
	    		System.out.println("Descripci�n previa: " + descripcionAntiguo);
	    	}
	    	
	    	contador++;
	    	   
	    }
	    br.close();
	}
	
	
	ArrayList<CompresionHuffman> arbolesParaDecript;
	
	
	
}
