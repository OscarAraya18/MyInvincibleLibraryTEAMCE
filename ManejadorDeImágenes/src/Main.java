import Ficheros.Archivo;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        ImageController imageController = new ImageController();

        try {
            imageController.addImage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
