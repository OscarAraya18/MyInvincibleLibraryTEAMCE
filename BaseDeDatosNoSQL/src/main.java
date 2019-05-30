import java.io.IOException;

import org.json.JSONException;

public class main {

	public static void main(String[] args) throws JSONException, IOException {
		BaseDeMetadata baseMetadata = new BaseDeMetadata();
		baseMetadata.agregarMetadata("Montaña", "Oscar Araya Garbanzo", "5 de mayo del 2019", "600x400", "Fotografía de una montaña");
		//baseMetadata.agregarMetadata("Playa", "Agustin Venegas Vega", "20 de abril de 2008", "700x200", "Foto tomada en la playa de Jacó");
		//baseMetadata.eliminarMetadata("Montaña");
		//baseMetadata.agregarMetadata("Montaña", "Oscar Araya Garbanzo", "5 de mayo del 2019", "600x400", "Fotografía de una montaña");
		//baseMetadata.modificarMetadata("Montaña","Perro", "Oscar Araya Garbanzo", "5 de mayo del 2019", "600x400", "Fotografía de una montaña");
		
		
		baseMetadata.rollback();
		
		

	}
}
