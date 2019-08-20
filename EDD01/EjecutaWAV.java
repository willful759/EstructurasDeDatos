/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author IvanPineda
 */
public class EjecutaWAV {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if(args.length < 1){
            throw new IllegalArgumentException("No filename provided");
        }
        
        try{
            FileReader fileReader = new FileReader(args[0]);
            BufferedReader buffReader = new BufferedReader(fileReader);
            String name = buffReader.readLine();
            int frecuency = Integer.parseInt(buffReader.readLine());
            int harmonic =  Integer.parseInt(buffReader.readLine());
            int duration = Integer.parseInt(buffReader.readLine());

            GeneraWAV.escribe(name,duration,frecuency,harmonic);
            buffReader.close();
            fileReader.close();

        }catch(FileNotFoundException e) {
            throw new FileNotFoundException("file" + args[0] + "could not be found\n" + e.getMessage());
        }catch(IOException e){
            throw new IOException("A problem ocurred while reading the file\n" + e.getMessage());
        }
    }

}
