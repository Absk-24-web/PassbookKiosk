package Testing;

import java.util.Arrays;

public class Testing {
    public static int iLen = -1;
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

//        int[] source = { 1, 2, 3, 4, 5, 6, 7 };
//        int[] destination = new int[source.length];
//        System.arraycopy(source, 2, destination, 0, source.length-2);
//        System.out.println(Arrays.toString(destination));

        //left pading
//    String result = String.format("%9s", "str").replace(' ','0');
        //right pading
//    int a = 5;
//    String result = String.format("%"+a+"s", "str");
//    System.out.println(result);


//            String value = "00000264555.00";
//            // Use custom trimEnd and trimStart methods.
//            System.out.println("[" + trimEnd(value) + "]");
//            System.out.println("[" + trimStart(value) + "]");
//            System.out.println("[" + removeLeadingZeroes(value) + "]");
//
//}
//    public static String trimEnd(String value) {
//    // Use replaceFirst to remove trailing spaces.
//    return value.replaceFirst("\\s+$", "");
//}
//
//    public static String trimStart(String value) {
//        // Remove leading spaces.
//        return value.replaceFirst("^\\s+", "");
//    }
//
//    public static String removeLeadingZeroes(String str) {
//        String strPattern = "^0+(?!$)";
//        str = str.replaceAll(strPattern, "");
//        return str;
//    }
        System.out.println(Arrays.toString(ConvertHexToAscii("F030810128A080000000000004000034")));
//        System.out.println((Arrays.toString(hexToAscii("F030810128A080000000000004000034"))));

    }


    public static byte[] ConvertHexToAscii(String strHex)
    {
        // TODO: 13-07-2020  return fixed
        byte[] Asc = new byte[strHex.length() / 2];
        for (int i = 0; i < Asc.length; i++) {
            Asc[i] = (byte) Integer.parseInt(strHex.substring(i*2, i*2+2), 16);
        }
        return Asc;
    }
}
