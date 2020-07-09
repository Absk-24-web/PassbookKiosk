package Testing;

import sun.nio.cs.StandardCharsets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Testing {
     public static int  iLen =-1;
//    public static void main(String[] args) {

//        String main = "F030810128A080000000000004000034";
//
//        int count = 0;
//            int length = main.length() / 2;
//        byte[] output = new byte[length];
//        for (int i = 0; i < output.length; i++) {
//            count++;
////            String s = String.valueOf((Integer.parseInt(main.substring(i*2,i*2+2), 16)));
////                output[i] = Byte.parseByte(s,2);
//
//            byte in = (byte) Integer.parseInt(main.substring(i*2,i*2+2), 16);
//                output[i] = in;
//
//
//            System.out.println( output[i]);
//        }
//        System.out.println("count-:"+count);

//        byte [] byRequest = {22,40,50,13,};
//        byte [] bRequest = new byte[byRequest.length];
//        System.arraycopy(byRequest,0,bRequest,0,byRequest.length);
//        for(int i=0; i< bRequest.length ; i++) {
//            System.out.print(bRequest[i] +" ");
//        }
//            int a = 2;
//        String s = "0121010200278961100000000000000001234562019112912112911068888882911191742120002072649" +
//                "00010000000068+0000000000000000+0000000000000000+0000000000000000356              356003SWT003KSK071Y17" +
//                "00000030202343300170000003020234330000000000000000000000000000000000011291119 2019001 ";
//        String st = s.substring(0,a);
//        boolean b =isNumber("01");
//        System.out.println(b);
//    }
//
//    private static boolean isNumber(String str){
//        try{
//            iLen = Integer.parseInt(str);
//            return true;
//        }
//        catch (Exception e){
//            return false;
//        }
//    }
public static void main(String[] args) {

//    File file = new File("C:\\Users\\df-dev16\\Desktop\\Auth_Req.dat");
//    FileInputStream fin = null;
//    try {
//        // create FileInputStream object
//        fin = new FileInputStream(file);
//
//        byte[] fileContent = new byte[(int)file.length()];
//
//        // Reads up to certain bytes of data from this input stream into an array of bytes.
//        try {
//            fin.read(fileContent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //create string from byte array
//        String s = new String(fileContent);
//        System.out.println("File content: " + Arrays.toString(s.getBytes()));
//        System.out.println("File content: " + s);
//    }
//    catch (FileNotFoundException e) {
//        System.out.println("File not found" + e);
//    } finally {
//        // close the streams using close method
//        try {
//            if (fin != null) {
//                fin.close();
//            }
//        }
//        catch (IOException ioe) {
//            System.out.println("Error while closing stream: " + ioe);
//        }
//    }

//    Path path = Paths.get("C:\\Users\\df-dev16\\Desktop\\Auth_Req.dat");
//    try {
//        byte[] data = Files.readAllBytes(path);
//        System.out.println("File content: " + Arrays.toString(data));
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//    File file = new File("C:\\Users\\df-dev16\\Desktop\\Auth_Req.dat");
////init array with file length
//    byte[] bytesArray = new byte[(int) file.length()];
//
//    FileInputStream fis = null;
//    try {
//        fis = new FileInputStream(file);
//        fis.read(bytesArray); //read file into bytes[]
//        System.out.println(Arrays.toString(bytesArray));
//
//        fis.close();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }


        int[] source = { 1, 2, 3, 4, 5, 6, 7 };
        int[] destination = new int[source.length];
        System.arraycopy(source, 2, destination, 0, source.length-2);
        System.out.println(Arrays.toString(destination));


}

}
