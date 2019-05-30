import java.util.*;


public class CompresionHuffman {
	public class ElementosCompresion{
		public ElementosCompresion(char caracter, int frecuencia) {
			this.caracter = caracter;
			this.frecuencia = frecuencia;
			hijoIzquierdo = null;
			hijoDerecho = null;
			padre = null;
			valorBinario = "";

		}
		
		char caracter;
		int frecuencia;
		String valorBinario;
		
		
		
		ElementosCompresion hijoIzquierdo;
		ElementosCompresion hijoDerecho;
		ElementosCompresion padre;
		
		int contadorArbol;
		

	}
	
	
	
	
	public class ArbolBinarioCompresion{
		public ArbolBinarioCompresion(ArrayList<ElementosCompresion> listaElementos, String datoOriginal) {
			this.listaElementos = listaElementos;
			this.datoOriginal = datoOriginal;
			pisos = 0;
			calculoArbol(listaElementos);
			this.listaRaiz = listaElementos;
			desencriptado = "";
		}
		
		public void calculoArbol(ArrayList<ElementosCompresion> listaEstadoActual) {
			ElementosCompresion padre = new ElementosCompresion(' ',0);
			while(listaEstadoActual.size()!=1) {
				pisos++;
				ArrayList<ElementosCompresion> listaNuevoEstado = new ArrayList<ElementosCompresion>();
				int contador=0;
				while(contador<listaEstadoActual.size()-1) {
					mostrarFrecuencia(listaEstadoActual);
					if(contador>0 && !(contador%2==0)) {
						padre = new ElementosCompresion('?',0);
						padre.contadorArbol = listaEstadoActual.get(contador-1).contadorArbol + listaEstadoActual.get(contador).contadorArbol;
						padre.hijoIzquierdo = listaEstadoActual.get(contador-1);
						padre.hijoDerecho = listaEstadoActual.get(contador);
						listaEstadoActual.get(contador-1).padre = padre;
						listaEstadoActual.get(contador).padre = padre;
						listaNuevoEstado.add(padre);
					}
					contador++;
				}
				if(listaEstadoActual.size()%2==0) {
					padre = new ElementosCompresion('?',0);
					padre.contadorArbol = listaEstadoActual.get(listaEstadoActual.size()-2).contadorArbol + 
							listaEstadoActual.get(listaEstadoActual.size()-1).contadorArbol;
					padre.hijoIzquierdo = listaEstadoActual.get(listaEstadoActual.size()-2);
					padre.hijoDerecho =  listaEstadoActual.get(listaEstadoActual.size()-1);
					listaEstadoActual.get(listaEstadoActual.size()-2).padre = padre;
					listaEstadoActual.get(listaEstadoActual.size()-1).padre = padre;
					listaNuevoEstado.add(padre);
				}else{
					padre = new ElementosCompresion('?',0);
					padre.contadorArbol = listaEstadoActual.get(listaEstadoActual.size()-1).contadorArbol;
					padre.hijoDerecho =  listaEstadoActual.get(listaEstadoActual.size()-1);
					listaEstadoActual.get(listaEstadoActual.size()-1).padre = padre;
					listaNuevoEstado.add(padre);
				}
				listaEstadoActual = listaNuevoEstado;
			}
			
			this.padre = padre;
			encriptarArbol();
				
		}
		
		
		public void encriptarArbol() {
			ElementosCompresion elementoAuxiliar;
			ElementosCompresion padre;
			for(int i=0; i<listaElementos.size(); i++) {
				elementoAuxiliar = listaElementos.get(i);
				padre = new ElementosCompresion(' ',0);
				String valorBinarioAuxiliar = "";
				while(elementoAuxiliar!=null) {
					if(elementoAuxiliar.padre!=null) {
						padre = elementoAuxiliar.padre;
						if(padre.hijoDerecho.equals(elementoAuxiliar)) {
							valorBinarioAuxiliar = ("1") + valorBinarioAuxiliar;
						}else {
							valorBinarioAuxiliar = ("0") + valorBinarioAuxiliar;
						}
					}
					elementoAuxiliar = elementoAuxiliar.padre;
				}
				listaElementos.get(i).valorBinario = valorBinarioAuxiliar;
			}
			
			String resultado = "";
			for(int j=0; j<datoOriginal.length(); j++) {
				for(int i=0; i<listaElementos.size(); i++) {
					if(datoOriginal.charAt(j)==listaElementos.get(i).caracter) {
						System.out.println("El caracter " + datoOriginal.charAt(j) + " tiene un valor binario de " + listaElementos.get(i).valorBinario);
						resultado = resultado + listaElementos.get(i).valorBinario;
					}
				}
			}
			
			this.resultado = resultado;
		}
		
		
		
		public void desencriptarArbol() {
			//System.out.println(resultado);
			String auxiliar = resultado;
			String temporal = resultado;
			for(int i=0; i<datoOriginal.length(); i++) {
				temporal = auxiliar;
				temporal = temporal.substring(0,pisos);
				
				for(int j=0; j<listaElementos.size(); j++) {
					if(temporal.equals(listaElementos.get(j).valorBinario)) {
						desencriptado = desencriptado + listaElementos.get(j).caracter;
					}
				}
				auxiliar = auxiliar.substring(pisos);
			}
			System.out.println("El string : "+ desencriptado + " se representa como : " + resultado);
		}
		
		
		
		ElementosCompresion padre;
		ArrayList<ElementosCompresion> listaElementos;
		ArrayList<ElementosCompresion> listaRaiz;
		String resultado;
		String desencriptado;
		String datoOriginal;
		int pisos;
		
		}
		
		
		

	
	
	
	
	public ArrayList<ElementosCompresion> ordenarListaPorFrecuencia(ArrayList<ElementosCompresion> listaElementos) { 
		ElementosCompresion elementoAuxiliar = new ElementosCompresion(' ',0);
		
		for(int i=0; i<listaElementos.size(); i++) {
			for(int j = 1; j<(listaElementos.size()-i); j++) {
				if(listaElementos.get(j-1).frecuencia>listaElementos.get(j).frecuencia) {
					elementoAuxiliar.caracter = listaElementos.get(j-1).caracter;
					elementoAuxiliar.frecuencia = listaElementos.get(j-1).frecuencia;
					listaElementos.get(j-1).caracter = listaElementos.get(j).caracter;
					listaElementos.get(j-1).frecuencia = listaElementos.get(j).frecuencia;
					listaElementos.get(j).caracter = elementoAuxiliar.caracter;
					listaElementos.get(j).frecuencia = elementoAuxiliar.frecuencia;
				}
			}
			listaElementos.get(i).contadorArbol = listaElementos.get(i).frecuencia;
;
		}
		return listaElementos;
	}
	
	
	
	
	public boolean buscarEnLista(ArrayList<ElementosCompresion> listaElementos, char caracterBuscado) {
		for(int i=0; i<listaElementos.size(); i++) {
			if(listaElementos.get(i).caracter == caracterBuscado) {
				listaElementos.get(i).frecuencia++;
				return true;
			}
		}
		return false;
	}
	
	
	public void mostrarFrecuencia(ArrayList<ElementosCompresion> listaElementos) {
		for(int i=0; i<listaElementos.size(); i++) {
			//System.out.println("El elemento " + listaElementos.get(i).caracter + " aparece " + listaElementos.get(i).frecuencia + " veces");
			if(listaElementos.get(i).hijoDerecho!=null) {
				//System.out.println("Su hijo derecho es " + listaElementos.get(i).hijoDerecho.caracter);
			}
			if(listaElementos.get(i).hijoIzquierdo!=null) {
				//System.out.println("Su hijo izquierdo es " + listaElementos.get(i).hijoIzquierdo.caracter);
			}
		}
	}
	
	
	public CompresionHuffman(String datoComprimir){
		ArrayList<ElementosCompresion> listaElementos = new ArrayList<ElementosCompresion>();
		ElementosCompresion elemento;
		for(int i=0; i<datoComprimir.length(); i++) {
			if(!buscarEnLista(listaElementos, datoComprimir.charAt(i))) {
				listaElementos.add(elemento = new ElementosCompresion(datoComprimir.charAt(i), 1));
			}
		}
		
		listaElementos = ordenarListaPorFrecuencia(listaElementos);
		mostrarFrecuencia(listaElementos);
		
		arbol = new ArbolBinarioCompresion(listaElementos, datoComprimir);
		
		
		arbol.desencriptarArbol();
		
		

	}
	
	ArbolBinarioCompresion arbol;
	
	
}
