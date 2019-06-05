package Ficheros;

import java.io.*;
import java.util.ArrayList;

//Basado en: https://www.lawebdelprogramador.com/codigo/Java/3842-Leer-y-escribir-en-un-archivo-de-texto.html

public class Archivo {



    public static void escribir(int nombre, int n, int tam1, int tam2, int tam3, byte[] array)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("DISCO"+n+"/paridad"+nombre+".txt");
            pw = new PrintWriter(fichero);

            System.out.println("Escribiendo en el archivo.txt");
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    pw.println(tam1);
                    pw.println(tam2);
                    pw.println(tam3);
                }
                pw.println(array[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para
                // asegurarnos que se cierra el fichero.
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static ArrayList<Integer> leer(String ruta)
    {

        ArrayList<Integer> arrayList = new ArrayList<>();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (ruta);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            System.out.println("Leyendo el contendio del archivo.txt");
            int contador = 1;
            String linea;
            while((linea=br.readLine())!=null) {
                int num = Integer.parseInt(linea);
                arrayList.add(num);
                //newArray = addByte(newArray, (byte) num);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();

                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return arrayList;
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

}

