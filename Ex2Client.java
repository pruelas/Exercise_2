
import java.io.*;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.zip.CRC32;

public final class Ex2Client {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("codebank.xyz", 38102)) {
            System.out.println("Connected to server.");
            
            //Initialize an input stream and an output stream to communicate with server
            InputStream is = socket.getInputStream();

            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os, true, "UTF-8");

            byte[] b = new byte[200];

            int j = 0;
            int counter = 0;
            int byteCounter = 0;
            int k;
            System.out.println("Received bytes: ");
            System.out.print("  ");

            //Reads the bytes from the server and prints them out to the screen as their
            //corresponding hexadecimal value
            while((k = is.read()) != -1){
            b[j] = (byte)k;
          
            System.out.print(Integer.toHexString( (int) (b[j])).toUpperCase());
            
            counter++;

            if(counter == 20 && j < 199 ){
            System.out.println();
            System.out.print("  ");
            counter = 0;
            }

            byteCounter++;
            j++;
            if(j == 200)
             break;
            }

            //Combines byte pieces received from server into single bytes and represents them
            //as hexadecimal
            String[] arr = new String[100];
            int h1 = 0;
            int h2 = 1;
            String b1;
            for(int p = 0; p < 100; p++){

               b1 = Integer.toHexString((int)(b[h1]));
               b1 += Integer.toHexString((int)(b[h2]));
             
               arr[p] = b1;
               b1 = null;
               h1 += 2;
               h2 += 2;  
            }

            //Will hold merged bytes 
            byte[] fbyte = new byte[100];
            
            //Finds the decimal value for each of the bytes recevied
            for(int p = 0; p < 100; p++){
            fbyte[p] = (byte) Integer.parseInt(arr[p], 16);
            }
           
            //Create CRC32 object to calculate the CRC32 code of the message
            CRC32 cr = new CRC32();
            cr.update(fbyte);
            long value = cr.getValue();
            System.out.println("\nGenerated CRC32: " + Long.toHexString((long)(value)).toUpperCase());
            String CRC = Long.toHexString((long)(value));
            byte[] c = new byte[4];

            //Turns the CRC32 code into a four bytes
            if(CRC.length() == 8){ 
            c[0] = (byte) Integer.parseInt(CRC.substring(0,2), 16);
            c[1] = (byte) Integer.parseInt(CRC.substring(2,4), 16);
            c[2] = (byte) Integer.parseInt(CRC.substring(4,6), 16);
            c[3] = (byte) Integer.parseInt(CRC.substring(6,8), 16);
            }else{
            //Appends a 0 to the CRC code before splitting the code into 4 parts
            String y = "0" + CRC;
            c[0] = (byte) Integer.parseInt(y.substring(0,2), 16);
            c[1] = (byte) Integer.parseInt(y.substring(2,4), 16);
            c[2] = (byte) Integer.parseInt(y.substring(4,6), 16);
            c[3] = (byte) Integer.parseInt(y.substring(6,8), 16);

            }
            //Send CRC32 code to server
            out.write(c);

            //Read response from server
            int res = (int)is.read();

            //Determines whether or not the server constructed the same CRC32 code
            if(res == 1)
            System.out.println("Response good.");
            else
            System.out.println("Response bad.");

            System.out.println("Disconnected from server.");
           
      }
    }
    
}















