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


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class ImageController extends JPanel {

    private BufferedImage imagenPrincipal;
    private int width, height;
    private Image imagePart1, imagePart2, imagePart3;
    private Archivo archivo = new Archivo();
    private int nombre = 1;
    private int contParidad = 4;

    private byte[] byteArray1, byteArray2, byteArray3, parityArray, completeArray, newArray;

    private ArrayList<Image> imagenPartes = new ArrayList();
    ArrayList<Integer> numbers = new ArrayList<>();
    ArrayList<Byte> completeImage = new ArrayList<>();

    public ImageController() {

    }
    public void convertImagefromBytes(ArrayList<Integer> arrayList) throws IOException {

        completeArray = new byte[0];
        for(int i=3; i< arrayList.size(); i++){
            completeArray = addByte(completeArray, arrayList.get(i).byteValue());
        }
        bytesToimage(1,3,2);
        //deleteImage("aux.png");

    }

    public void loadImage(String ruta) throws IOException {

        imagenPrincipal = ImageIO.read(new File("img.png"));

        width = imagenPrincipal.getWidth(null);
        height = imagenPrincipal.getHeight(null);

        //imageToByteArray(4, "png", "img.png");
        imagePart1 = createImage(new FilteredImageSource(imagenPrincipal.getSource(),
                new CropImageFilter(0, 0, width , height / 3)));
        imagePart2 = createImage(new FilteredImageSource(imagenPrincipal.getSource(),
                new CropImageFilter(0, height/3, width, height/3)));
        imagePart3 = createImage(new FilteredImageSource(imagenPrincipal.getSource(),
                new CropImageFilter(0, 2*(height/3), width, height/3)));

        saveImages();

    }

    public void saveImages() throws IOException {

        switch (contParidad) {
            case 1: {
                savePic(1, imagePart1, "png" , "DISCO2/" + nombre + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO3/" + nombre + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO4/" + nombre + "-3.png");

                break;
            }
            case 2: {
                savePic(1, imagePart1, "png" , "DISCO1/" + nombre + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO3/" + nombre + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO4/" + nombre + "-3.png");
                break;
            }
            case 3: {
                savePic(1, imagePart1, "png" , "DISCO1/" + nombre + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO2/" + nombre + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO4/" + nombre + "-3.png");
                break;
            }
            case 4:{
                savePic(1, imagePart1, "png" , "DISCO1/" + nombre + "-1.png");
                savePic(2, imagePart2, "png" , "DISCO2/" + nombre + "-2.png");
                savePic(3, imagePart3, "png" , "DISCO3/" + nombre + "-3.png");
                break;
            }
        }

        generateParity();
        deleteImage("jaja.png");
        nombre++;

    }

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

    public void imageToByteArray(int id, String type, String dst) throws IOException {

        BufferedImage imageAux = ImageIO.read(new File(dst));

        // write it to byte array in-memory (jpg format)
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ImageIO.write(imageAux, type, b);

        if(id == 1){
            // do whatever with the array...
            byteArray1 = b.toByteArray();

            // convert it to a String with 0s and 1s
            /*StringBuilder sb = new StringBuilder();
            for (byte by : byteArray1){
                sb.append(Integer.toBinaryString(by & 0xFF));
            }*/

        }
        else if(id== 2){
            // do whatever with the array...
            byteArray2 = b.toByteArray();

            // convert it to a String with 0s and 1s
            /*StringBuilder sb = new StringBuilder();
            for (byte by : byteArray2) {
                sb.append(Integer.toBinaryString(by & 0xFF));

            }*/

        }
        else if(id ==3 ) {
            byteArray3 = b.toByteArray();

            // convert it to a String with 0s and 1s
            /*StringBuilder sb = new StringBuilder();
            for (byte by : byteArray3)
                sb.append(Integer.toBinaryString(by & 0xFF));*/

        }
    }

    public void bytesToimage(int id, int name, int piece) throws IOException {

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

            ImageIO.write(bImageFromConvert, "png", new File(
                    "completa.png"));
        }
    }

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

    private int randomPlus(){
        int n = (int)(Math.random() * 3 + 1);

        if(numbers.contains(n)){
            return randomPlus();
        }
        numbers.add(n);
        return  n;
    }


    public void addImage(int name) throws IOException {

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

        BufferedImage uno= ImageIO.read(new File(ruta1));
        BufferedImage dos = ImageIO.read(new File(ruta2));
        BufferedImage tres = ImageIO.read(new File(ruta3));

        int width = uno.getWidth();
        int height = uno.getHeight() + dos.getHeight() + tres.getHeight();

        BufferedImage biResultado = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); //suponiendo tamaño de imagen  y ARGB que soporta trasparencia

        //se crea la nueva imagen juntando los trozos
        Graphics g = biResultado.getGraphics();
        g.drawImage(uno, 0, 0, null);
        g.drawImage(dos, 0, height/3, null);
        g.drawImage(tres,0,2*(height/3), null);

        //se guarda en la direccion especificada
        ImageIO.write(biResultado, "png", new File(name+".png"));
    }

    public void generateParity(){

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
            archivo.escribir(nombre, contParidad, tam1, tam2, tam3, parityArray);
        }
        else{
            contParidad= 4;
            archivo.escribir(nombre, contParidad, tam1, tam2, tam3, parityArray);
        }
        contParidad--;
    }


    public void recuperarImg(int name, int id) throws IOException {
        System.out.println("RECUPERANDO...");
        parityArray = new byte[0];

        String ruta1;
        String ruta2;
        String ruta3;

        String rutaParidad = searchFile("paridad"+name+".txt",1);
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

    private void deleteImage(String ruta) throws IOException {
        File imagen = new File(ruta);

        if(imagen.exists()){
            imagen.delete();
        }
    }


    public String searchFile(String nombre, int numDisco) {

        if(numDisco<=4){
            // Aquí la carpeta donde queremos buscar
            String path = "DISCO"+numDisco+"/";
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
                return searchFile(nombre, numDisco += 1);
            }
            else {
                files = path + files;
                //System.out.println(files);
                return files;
            }
        }
        else{
            return "";
        }

    }

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
}
