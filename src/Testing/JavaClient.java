package Testing;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class JavaClient {
        // initialize socket and input output streams
        private Socket socket = null;
        private OutputStream out = null;
        private InputStream in = null;
        public static int  iLen = -1;

        // constructor to put ip address and port
        public JavaClient(String address, int port) {
            // establish a connection
            System.gc();
            try {
                socket = new Socket(address, port);
                if (socket.isConnected()) {
                    System.out.println("Connected");
                }

                // sends output to the socket
                out = new DataOutputStream(socket.getOutputStream());
                //takes input from socket
                in = new DataInputStream(socket.getInputStream());
            } catch (UnknownHostException u) {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }


            try {
//                byte[] arr = {(byte)0x00, (byte)0x01, (byte)0x00, (byte)0x10, (byte)0x00, (byte)0x01,
//                        (byte)0x00, (byte)0x1F, (byte)0x60, (byte)0x1D, (byte)0xA1, (byte)0x09,
//                        (byte)0x06, (byte)0x07, (byte)0x60, (byte) 0x85, (byte)0x74, (byte)0x05,
//                        (byte) 0x08, (byte)0x01, (byte)0x01, (byte)0xBE, (byte)0x10, (byte)0x04,
//                        (byte)0x0E, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x06,
//                        (byte)0x5F, (byte)0x1F, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x18,
//                        (byte)0x1D, (byte)0xFF, (byte)0xFF};
               byte []arr = {};
                // sending data to server

                    out.write(1);
                    int res = in.read();
                    System.out.println("Recieved from server : " + res);

                    out.write(res+1);
                    res = in.read();
                    System.out.println("Recieved from server : " + res);

                    out.write(res+1);
                    res = in.read();
                    System.out.println("Recieved from server : " + res);

                //printing request to console
//                System.out.println("Sent to server : " + );

                // Receiving reply from server
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                baos.write(buffer, 0 , in.read(buffer));
//                byte[] result = baos.toByteArray();
//                String res = Arrays.toString(result);

                // printing reply to console


            } catch (IOException i) {
                System.out.println(i);
            }
            // }

            // close the connection
            try {
                // input.close();
                in.close();
                out.close();
                socket.close();
                System.gc();
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        public static void main(String args[]) {
            new JavaClient("172.16.4.101", 8080);
        }

//    private static boolean isNumber(String str){
//        try{
//            iLen = Integer.parseInt(str);
//            return true;
//        }
//        catch (Exception e){
//            return false;
//        }
//    }
}
