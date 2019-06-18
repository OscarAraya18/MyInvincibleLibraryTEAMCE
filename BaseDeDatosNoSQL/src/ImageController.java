import Ficheros.Archivo;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * Clase encargada del manejo de imagenes dentro del RAID5
 */
public class ImageController extends JPanel {

    /**
     * atributos
     */
    private BufferedImage imagenPrincipal;
    private int width, height;
    private Image imagePart1, imagePart2, imagePart3;
    private Archivo archivo = new Archivo();
    private int nombre = 1;
    private int contParidad = 4;
    private byte[] byteArray1, byteArray2, byteArray3, parityArray, completeArray;
    public byte[] newArray;
    private ArrayList<Image> imagenPartes = new ArrayList();
    ArrayList<Integer> numbers = new ArrayList<>();
    ArrayList<Byte> completeImage = new ArrayList<>();

    /**
     * Constructor de la  clase ImageController
     */
    public ImageController() {
    	vaciarCarpetas();
    }

    /**
     * Metodo encargado de crear una imagen a partir de un array de bytes
     * @param arrayList Lista que contiene todos los bytes de una imagen para construirla
     * @throws IOException
     */
    public void convertImagefromBytes(ArrayList<Integer> arrayList, String name) throws IOException {

        completeArray = new byte[0];
        for(int i=0; i< arrayList.size(); i++){
        
            completeArray = addByte(completeArray, arrayList.get(i).byteValue());
        }
        bytesToimage(5,name,2);
        //deleteImage("aux.png");

    }

    /**
     * Metodo para cargar una imagen y partirla en 3 partes del mismo tamaño fisico
     * @param ruta ruta donde se encuentra la imagen que se quiere partir
     * @throws IOException
     */
    public void loadImage(String ruta, String name) throws IOException {

        imagenPrincipal = ImageIO.read(new File(ruta));

        width = imagenPrincipal.getWidth(null);
        height = imagenPrincipal.getHeight(null);

        //imageToByteArray(4, "png", "img.png");
        imagePart1 = createImage(new FilteredImageSource(imagenPrincipal.getSource(),
                new CropImageFilter(0, 0, width , height / 3)));
        imagePart2 = createImage(new FilteredImageSource(imagenPrincipal.getSource(),
                new CropImageFilter(0, height/3, width, height/3)));
        imagePart3 = createImage(new FilteredImageSource(imagenPrincipal.getSource(),
                new CropImageFilter(0, 2*(height/3), width, height/3)));

        saveImages(name);
        deleteImage(ruta);

    }

    /**
     * Metodo para guardar las partes de la imagen donde corresponda, segun el RAID5
     * @throws IOException
     */
    public void saveImages(String name) throws IOException {
    	File disco1 = new File("DISCO1");
    	File disco2 = new File("DISCO2");
    	File disco3 = new File("DISCO3");
    	File disco4 = new File("DISCO4");
    	
    	disco1.mkdir();
    	disco2.mkdir();
    	disco3.mkdir();
    	disco4.mkdir();
    	
    	
    	System.out.println("Contador paridad "+ contParidad);

    	if(contParidad<1) {
    		contParidad=4;
    	}
        switch (contParidad) {
            case 1: {
                savePic(1, imagePart1, "png" , "DISCO2/" + name + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO3/" + name + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO4/" + name + "-3.png");

                break;
            }
            case 2: {
                savePic(1, imagePart1, "png" , "DISCO1/" + name + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO3/" + name + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO4/" + name + "-3.png");
                break;
            }
            case 3: {
                savePic(1, imagePart1, "png" , "DISCO1/" + name + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO2/" + name + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO4/" + name + "-3.png");
                break;
            }
            case 4:{
                savePic(1, imagePart1, "png" , "DISCO1/" + name + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO2/" + name + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO3/" + name + "-3.png");
                break;
            }
        }
        

        generateParity(name);
        nombre++;

    }

    /**
     * Metodo auxiliar para guardar cada parte de la imagen
     * @param id nos indica cual es la parte a  guardar (1, 2, 3)
     * @param image el objeto tipo image es la imagen a guardar en si
     * @param type nos indica el tipo de imagen que estamos trabajando (png)
     * @param dst indica el destino a donde queremos que se guarde la imagen
     */
    public void savePic(int id, Image image, String type, String dst){
        int w = image.getWidth(this);
        int h = image.getHeight(this);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        try {
            g.drawImage(image, 0, 0, null);
            ImageIO.write(bi, type, new File(dst));
            imageToByteArray(id, type, dst);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para convertir una imagen en un arreglo de bytes
     * @param id indica cual es la parte a  guardar (1, 2, 3)
     * @param type nos indica el tipo de imagen que estamos trabajando (png)
     * @param dst indica el destino a donde queremos que se guarde la imagen
     * @throws IOException
     */
    public void imageToByteArray(int id, String type, String dst) throws IOException {

        BufferedImage imageAux = ImageIO.read(new File(dst));

        // write it to byte array in-memory (png format)
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ImageIO.write(imageAux, type, b);

        if(id == 1){
            byteArray1 = b.toByteArray();

            //c.ejecutarConexion("");

            // convert it to a String with 0s and 1s
            /*StringBuilder sb = new StringBuilder();
            for (byte by : byteArray1){
                sb.append(Integer.toBinaryString(by & 0xFF));
            }*/
        }
        else if(id== 2){
            byteArray2 = b.toByteArray();
        }
        else if(id ==3 ) {
            byteArray3 = b.toByteArray();
        }
        else {
        	newArray = b.toByteArray();
        }
    }

    /**
     * Metodo para pasar de un arreglo de bytes a una parte de la imagen que se guarda en el RAID5
     * @param id indica que hacer a la funcion dependiendo del numero que le entre
     * @param name indica el nombre con el que se quiere que se guarde la imagen
     * @param piece  indica cual es la parte a  guardar (1, 2, 3)
     * @throws IOException
     */
    public void bytesToimage(int id, String name, int piece) throws IOException {

        if(id==1){
            InputStream in = new ByteArrayInputStream(byteArray1);
            BufferedImage bImageFromConvert = ImageIO.read(in);

            ImageIO.write(bImageFromConvert, "png", new File(
                    "DISCO1/new-imagen1.png"));
        }
        else if(id==2){
            InputStream in = new ByteArrayInputStream(byteArray2);
            BufferedImage bImageFromConvert = ImageIO.read(in);

            ImageIO.write(bImageFromConvert, "png", new File(
                    "DISCO2/new-imagen2.png"));
        }
        else if(id == 3){
            InputStream in = new ByteArrayInputStream(byteArray3);
            BufferedImage bImageFromConvert = ImageIO.read(in);

            ImageIO.write(bImageFromConvert, "png", new File(
                    "DISCO3/new-imagen3.png"));
        }
        else if(id ==4){
            InputStream in = new ByteArrayInputStream(newArray);
            BufferedImage bImageFromConvert = ImageIO.read(in);

            String path = rutaDiscoMasVacio() + name + "-" + piece + ".png";
            ImageIO.write(bImageFromConvert, "png", new File(
                    path));
        }
        else {
            InputStream in = new ByteArrayInputStream(completeArray);
            BufferedImage bImageFromConvert = ImageIO.read(in);
            
            ////////////////////////////
            
            ImageIO.write(bImageFromConvert, "png", new File(name+".png"));
			
            ////////////////////////////

            loadImage(name+".png",name);
        }
    }

    /**
     * Metodo utilizado para agregar bytes a un arreglo de bytes
     * lo que hace es crear un nuevo arreglo con un tamano mas grande al anterior
     * @param series El arreglo de bytes anterior o al que se le quiere agregar el elemento
     * @param newInt el byte a agregar a este arreglo
     * @return nuevo arreglo con el nuevo alemento agregado junto con los que tenia
     */
    public static byte[] addByte(byte [] series, byte newInt){
        //create a new array with extra index
        byte[] newSeries = new byte[series.length + 1];

        //copy the integers from series to newSeries
        for (int i = 0; i < series.length; i++){
            newSeries[i] = series[i];
        }
        //add the new integer to the last index
        newSeries[newSeries.length - 1] = newInt;

        return newSeries;
    }

    /**
     * Metodo para juntar las tres partes de la imagen y generar una completa a partir de estos
     * @param name el nombre con el que se va a guardar la imagen generada
     * @throws IOException
     */
    public void addImage(String name, int id) throws IOException {

        String ruta1 = searchFile(name+"-1.png", 1);
        String ruta2 = searchFile(name+"-2.png", 1);
        String ruta3 = searchFile(name+"-3.png", 1);

        if(ruta1 == ""){
            recuperarImg(name,1);
            ruta1 = searchFile(name+"-1.png", 1);
        }
        else if(ruta2 == ""){
            recuperarImg(name,2);
            ruta2 = searchFile(name+"-2.png", 1);
        }
        else if(ruta3 == ""){
            recuperarImg(name,3);
            ruta3 = searchFile(name+"-3.png", 1);
        }

        if(!(ruta1.equals("")) && !(ruta2.equals("")) && !(ruta3.equals(""))){
            generateParity(name);
        }

        BufferedImage uno = ImageIO.read(new File(ruta1));
        BufferedImage dos = ImageIO.read(new File(ruta2));
        BufferedImage tres = ImageIO.read(new File(ruta3));

        int width = uno.getWidth();
        int height = uno.getHeight() + dos.getHeight() + tres.getHeight();

        BufferedImage biResultado = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            //se crea la nueva imagen juntando los trozos
        Graphics g = biResultado.getGraphics();
        g.drawImage(uno, 0, 0, null);
        g.drawImage(dos, 0, height / 3, null);
        g.drawImage(tres, 0, 2 * (height / 3), null);

        if(id == 1) {
            //se guarda en la direccion especificada
            ImageIO.write(biResultado, "png", new File(name + ".png"));
            
            imageToByteArray(4,"png",name+".png");
            
            deleteImage(name+".png");
           
        }
        else{
            //se guarda en la direccion especificada
            ImageIO.write(biResultado, "png", new File("Papelera/" + name + ".png"));
        }
    }

    /**
     * Metodo que genera la paridad a partir de la conversion a bytes
     * de cada una de las imagen y aplicando el xor respectivo, generando
     * los bytes de paridad, los cuales son guardados en un archivo .txt
     */
    public void generateParity(String name){
        String ruta1 = searchFile(name+"-1.png", 1);
        String ruta2 = searchFile(name+"-2.png", 1);
        String ruta3 = searchFile(name+"-3.png", 1);

        searchFile("DISCO"+contParidad+"/paridad"+nombre+".txt", 1);

        try {
            imageToByteArray(1,"png", ruta1);
            imageToByteArray(2,"png", ruta2);
            imageToByteArray(3,"png", ruta3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int min = Math.min(Math.min(byteArray1.length, byteArray2.length), byteArray3.length);
        int middle = middleOfThree(byteArray1.length, byteArray2.length, byteArray3.length);
        int max = Math.max(Math.max(byteArray1.length, byteArray2.length), byteArray3.length);

        int tam1 = byteArray1.length;
        int tam2 = byteArray2.length;
        int tam3 = byteArray3.length;

        int cont = 0;
        parityArray = new byte[0];
        for(int i = cont; i< min; i++){
            byte xor = (byte)(byteArray1[i] ^ byteArray2[i] ^ byteArray3[i]);
            parityArray = addByte(parityArray, xor);
            cont++;
        }
        for(int i = cont; i< middle; i++){
            if(min == byteArray1.length){
                byte xor = (byte)(byteArray2[i] ^ byteArray3[i]);
                parityArray = addByte(parityArray, xor);
                cont++;
            }
            else if(min == byteArray2.length){
                byte xor = (byte)(byteArray1[i] ^ byteArray3[i]);
                parityArray = addByte(parityArray, xor);
                cont++;
            }
            else{
                byte xor = (byte)(byteArray1[i] ^ byteArray2[i]);
                parityArray = addByte(parityArray, xor);
                cont++;
            }
        }
        for(int i = cont; i< max; i++){
            if(max == byteArray1.length){
                byte xor = byteArray1[i];
                parityArray = addByte(parityArray, xor);
            }
            else if(max == byteArray2.length){
                byte xor = byteArray2[i];
                parityArray = addByte(parityArray, xor);
            }
            else{
                byte xor = byteArray3[i];
                parityArray = addByte(parityArray, xor);
            }
        }
        if(contParidad >=1) {
            archivo.escribir(name, contParidad, tam1, tam2, tam3, parityArray);
        }
        else{
            contParidad = 4;
            archivo.escribir(name, contParidad, tam1, tam2, tam3, parityArray);
        }
        contParidad--;
    }

    /**
     * Metodo que recupera una imagen a partir de la paridad y la tecnica del xor
     * lo que hace es trabajar con casos dependiendo de cual sea la parte a reconstruir
     * @param name el nombre de la imagen original
     * @param id se refiere a la parte que quiero recuperar (1,2,3)
     * @throws IOException
     */
    public void recuperarImg(String name, int id) throws IOException {
        System.out.println("RECUPERANDO...");
        parityArray = new byte[0];

        String ruta1;
        String ruta2;
        String ruta3;

        String rutaParidad = searchFile("paridad"+name+".txt",1);
        if(rutaParidad == ""){
            System.out.println("NO hay paridad, se esta creando");
            generateParity(name);
        }

        ArrayList<Integer> lectura = archivo.leer(rutaParidad);

        switch (id){
            case 1:  {
                ruta2 = searchFile(name+"-2.png", 1);
                ruta3 = searchFile(name+"-3.png", 1);

                imageToByteArray(2,"png", ruta2);
                imageToByteArray(3,"png", ruta3);
                break;
            }
            case 2:{
                ruta1 = searchFile(name+"-1.png", 1);
                ruta3 = searchFile(name+"-3.png", 1);

                imageToByteArray(1,"png", ruta1);
                imageToByteArray(3,"png", ruta3);
                break;
            }
            case 3:{
                ruta1 = searchFile(name+"-1.png", 1);
                ruta2 = searchFile(name+"-2.png", 1);

                imageToByteArray(1,"png", ruta1);
                imageToByteArray(2,"png", ruta2);
                break;
            }
        }

        for(int i=3; i< lectura.size(); i++){
            parityArray = addByte(parityArray, lectura.get(i).byteValue());
        }

        newArray = new byte[0];

        int tam1 = lectura.get(0);
        int tam2 = lectura.get(1);
        int tam3 = lectura.get(2);

        for(int i = 0; i< parityArray.length; i++){
            if(id == 1){

                if((byteArray2.length > i) && (byteArray3.length > i ) && (i < tam1) ) {
                    byte xor = (byte) (byteArray2[i] ^ byteArray3[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray2.length <= i) && (byteArray3.length > i) && (i < tam1) ) {
                    byte xor = (byte) (byteArray3[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray2.length > i) && (byteArray3.length <= i) && (i < tam1) ) {
                    byte xor = (byte) (byteArray2[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray2.length <= i) && (byteArray3.length <= i) && (i < tam1) ) {
                    byte xor = parityArray[i];
                    newArray = addByte(newArray, xor);
                }
                else {
                    break;
                }
            }
            else  if(id == 2){
                if((byteArray1.length > i) && (byteArray3.length > i ) && (i < tam2) ) {
                    byte xor = (byte) (byteArray1[i] ^ byteArray3[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray1.length <= i) && (byteArray3.length > i) && (i < tam2) ) {
                    byte xor = (byte) (byteArray3[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray1.length > i) && (byteArray3.length <= i) && (i < tam2) ) {
                    byte xor = (byte) (byteArray1[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray1.length <= i) && (byteArray3.length <= i) && (i < tam2) ) {
                    byte xor = parityArray[i];
                    newArray = addByte(newArray, xor);
                }
                else {
                    break;
                }
            }
            else {
                if((byteArray1.length > i) && (byteArray2.length > i ) && (i < tam3) ) {
                    byte xor = (byte) (byteArray1[i] ^ byteArray2[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray1.length <= i) && (byteArray2.length > i) && (i < tam3) ) {
                    byte xor = (byte) (byteArray2[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray1.length > i) && (byteArray2.length <= i) && (i < tam3) ) {
                    byte xor = (byte) (byteArray1[i] ^ parityArray[i]);
                    newArray = addByte(newArray, xor);
                }
                else if((byteArray1.length <= i) && (byteArray2.length <= i) && (i < tam3) ) {
                    byte xor = parityArray[i];
                    newArray = addByte(newArray, xor);
                }
                else {
                    break;
                }
            }
        }
        bytesToimage(4, name, id);

    }

    /**
     * Metodo que busca al entero del medio entre 3 numeros
     * @param a numero a comparar
     * @param b numero a comparar
     * @param c numero a comparar
     * @return el numero que corresponde al del medio
     */
    public int middleOfThree(int a, int b, int c)
    {
        // Checking for b
        if ((a < b && b < c) || (c < b && b < a))
            return b;

            // Checking for a
        else if ((b < a && a < c) || (c < a && a < b))
            return a;

        else
            return c;
    }

    /**
     * Metodo para eliminar una imagen
     * @param ruta ruta donde se encuantra la imagen que se quiere eliminar
     * @throws IOException
     */
    private void deleteImage(String ruta) throws IOException {
        File imagen = new File(ruta);

        if(imagen.exists()){
            imagen.delete();
        }
    }

    /**
     * Metodo para buscar un archivo dentro de los 4 discos del RAID5
     * @param nombre nombre del archivo a buscar
     * @param numDisco numero de disco donde se quiere comenzar a buscar
     * @return el nombre de la ruta si encuantra el archivo, un "" en caso contrario
     */
    public String searchFile(String nombre, int numDisco) {

        if(numDisco<=4){
            // Aquí la carpeta donde queremos buscar
            String path = "DISCO"+numDisco+"/";
            String files = "";
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            if(!folder.exists()){
                folder.mkdir();
                System.out.println("Se esta creando el fichero");
                listOfFiles = folder.listFiles();
            }


            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    files = listOfFiles[i].getName();
                    if(files.equals(nombre)){
                        break;
                    }
                }
            }
            if(!files.equals(nombre)){
                return searchFile(nombre, numDisco += 1);
            }
            else {
                files = path + files;
                return files;
            }
        }
        else{
            return "";
        }

    }

    /**
     * Busca el disco que este mas vacio
     * @return la ruta del disco mas vacio
     */
    private String rutaDiscoMasVacio() {
        int smallDisk;
        int tamDisco1;
        int tamDisco2;
        int tamDisco3;
        int tamDisco4;

        String disco1 = "DISCO1/";
        String disco2 = "DISCO2/";
        String disco3 = "DISCO3/";
        String disco4 = "DISCO4/";
        File folder = new File(disco1);
        File[] listOfFiles = folder.listFiles();
        tamDisco1 = listOfFiles.length;

        folder = new File(disco2);
        listOfFiles = folder.listFiles();
        tamDisco2 = listOfFiles.length;

        folder = new File(disco3);
        listOfFiles = folder.listFiles();
        tamDisco3 = listOfFiles.length;

        folder = new File(disco4);
        listOfFiles = folder.listFiles();
        tamDisco4 = listOfFiles.length;

        smallDisk = Math.min(Math.min(tamDisco1, tamDisco2), Math.min(tamDisco3, tamDisco4));

        if(tamDisco1 == smallDisk){
            return  disco1;
        }
        else if(tamDisco2 == smallDisk){
            return disco2;
        }
        else if(tamDisco3 == smallDisk) {
            return disco3;
        }
        else{
            return  disco4;
        }

    }

    public void deleteImageFromRaid(String name){
        try {
            addImage(name,2);
            String ruta1 = searchFile(name+"-1.png", 1);
            String ruta2 = searchFile(name+"-2.png", 1);
            String ruta3 = searchFile(name+"-3.png", 1);
            String rutaParidad = searchFile("paridad"+ name+".txt",1);

            System.out.println("RUTA PARIDAD ES :" + rutaParidad);

            deleteImage(ruta1);
            deleteImage(ruta2);
            deleteImage(ruta3);
            deleteImage(rutaParidad);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recuperarDePapelera(String name){
        String ruta = buscarEnPapelera(name + ".png");

        if(!ruta.equals("")){
            try {
                loadImage(ruta, name);
                deleteImage(ruta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String buscarEnPapelera(String nombre){
        String path = "Papelera/";
        String files = "";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if(files.equals(nombre)){
                    break;
                }
            }
        }
        if(!files.equals(nombre)){
            return "";
        }
        else {
            files = path + files;
            return files;
        }
    }
    
    private void vaciarCarpetas() {
    	String disco1 = "DISCO1";
        String disco2 = "DISCO2";
        String disco3 = "DISCO3";
        String disco4 = "DISCO4";
        
        File folder1 = new File(disco1);
        File folder2= new File(disco2);
        File folder3 = new File(disco3);
        File folder4 = new File(disco4);
        File papelera = new File("Papelera");
        
        if(!folder1.exists()) {
        	folder1.mkdir();
        }
        if(!folder2.exists()) {
        	folder2.mkdir();
        }
        if(!folder3.exists()) {
        	folder3.mkdir();
        }
        if(!folder4.exists()) {
        	folder4.mkdir();
        }
        if(!papelera.exists()) {
        	papelera.mkdir();
        }
    }

}
