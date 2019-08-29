/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author IvanPineda
 */
public class GeneraWAV {

    private static int FORMAT = 16;
    private static short PCM = 1;
    private static short CHANNELS = 1;
    private static short BYTES_SAMPLE = 2;
    private static short BITS_SAMPLE = 16;
    
    private static byte[] intToBytes(int val){
        byte[] res = new byte[4];
        for(int i = 0; i < 4; ++i)
            res[i] = (byte)(0xFF & (val >> (3 - i)*8));
        return res;
    }
    /**
     *
     * @param nombre
     * @param iTiempo
     * @param iFrecuenciaMuestreo
     * @param iArmonico
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void escribe(
            String nombre
            ,int iTiempo
            ,int iFrecuenciaMuestreo
            ,int iArmonico
    ) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        //input validation
        if(nombre == null){
            throw new NullPointerException("Name was null");
        }
        
        String sFileExtension = nombre.split("\\.")[1];
        if(!sFileExtension.equals("wav")){
            throw new IllegalArgumentException("Unusuported/Invalid file format: " + sFileExtension);
        }

        if(iFrecuenciaMuestreo != 44100 && iFrecuenciaMuestreo != 22050 && iFrecuenciaMuestreo != 11025 ){
            throw new IllegalArgumentException("Invalid sample rate: " + iFrecuenciaMuestreo);
        }

        if(iTiempo < 0){
            throw new  IllegalArgumentException("Invalid time duration: " + iTiempo);
        }
        if(iArmonico < 0){
            throw new  IllegalArgumentException("Invalid Harmonic: " + iArmonico);
        }

        //wait that's illegal
        try{
            OutputStream wavFile = new FileOutputStream(nombre,false);

            //time*samplefrecuency*bytes per sample
            int dataSize = iTiempo*iFrecuenciaMuestreo*BYTES_SAMPLE;

            //descriptor
            wavFile.write("RIFF".getBytes());
            //total number of data bytes + bytes of the header (36)
            wavFile.write(intToBytes(Integer.reverseBytes(dataSize + 36)));

            //format chunk
            wavFile.write("WAVEfmt ".getBytes());
            //Format = 16
            wavFile.write(intToBytes(Integer.reverseBytes(FORMAT)));
            // pcm and channels
            wavFile.write(intToBytes(Integer.reverseBytes(PCM | CHANNELS << 16)));
            //Sample Frecuency in Samples per second
            wavFile.write(intToBytes(Integer.reverseBytes(iFrecuenciaMuestreo)));
            //Frecuency in bytes per second
            wavFile.write(intToBytes(Integer.reverseBytes(iFrecuenciaMuestreo*BYTES_SAMPLE)));
            // Bytes per sample and bits per sample
            wavFile.write(intToBytes(Integer.reverseBytes(BYTES_SAMPLE |(BITS_SAMPLE << 16))));

            //data chunk
            wavFile.write("data".getBytes());
            //total size of the data chunk
            wavFile.write(intToBytes(Integer.reverseBytes(dataSize)));

            //write sine wave
            short sample;
            byte[] data = new byte[dataSize];
            for(int i = 0; i < dataSize; i += BYTES_SAMPLE){
                sample =
                        (short)(
                        (Short.MAX_VALUE)*Math.sin(2.0*Math.PI*iArmonico*((double)i/((double)BYTES_SAMPLE*(double)iFrecuenciaMuestreo)))
                        );
                data[i] = (byte)(sample);
                data[i + 1] = (byte)(sample >> 8 & 0xFF);
            }
            wavFile.write(data);

            //close stream
            wavFile.close();
        } catch(IOException e){
            throw new IOException("couldn't write to the file!\n" + e.getMessage());
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IOException, IOException{
        escribe("test.wav",300,44100,420);
    }
}
