import java.io.IOException;

import org.json.JSONException;

public class main {

	public static void main(String[] args) throws JSONException, IOException {
		BaseDeMetadata baseMetadata = new BaseDeMetadata();
		baseMetadata.agregarMetadata("Monta�a", "Oscar Araya Garbanzo", "5 de mayo del 2019", "600x400", "Fotograf�a de una monta�a");
		//baseMetadata.agregarMetadata("Playa", "Agustin Venegas Vega", "20 de abril de 2008", "700x200", "Foto tomada en la playa de Jac�");
		//baseMetadata.eliminarMetadata("Monta�a");
		//baseMetadata.agregarMetadata("Monta�a", "Oscar Araya Garbanzo", "5 de mayo del 2019", "600x400", "Fotograf�a de una monta�a");
		//baseMetadata.modificarMetadata("Monta�a","Perro", "Oscar Araya Garbanzo", "5 de mayo del 2019", "600x400", "Fotograf�a de una monta�a");
		
		
		baseMetadata.rollback();
		
		

	}
}
