package com.example.mstringunit;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class Converter {

    /**
     * int 转 字符串
     * @param datas
     * @return
     */
    public static String int2Str(int[] datas){
        return int2Str(datas, null, false);
    }

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }
    /**
     * byte 转 字符串
     * @param bytes
     * @return
     */
    public static String byte2Str(byte[] bytes){
        char[] a = getChars(bytes);
//        for (int i = 0; i < a.length; i++){
//            a[i] = (char)(a[i] ^ 'z');
//        }
        return new String(a);
    }

    /**
     * char[] 转 byte[]
     * @param chars
     * @return
     */
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        byte[] bytes =  bb.array();

        int i = bytes.length;
        for( ; i > 1 && bytes[i - 1] == 0; i--){};
        byte[] bytes_new =  new byte[i];
        System.arraycopy(bytes, 0,  bytes_new, 0, i);
        return bytes_new;
    }


    /***
     * 按指定格式输出int数组
     * @param datas
     * @param format
     * @param fal
     * @return
     */
    public static String int2Str(int[] datas,String format, boolean fal){
        String s = "";

        if(datas == null)
        {
            return "null";
        }
        for (int i = 0; i < datas.length; i++){
            if(format == null){
                s += datas[i];
            }
            else{
                s += String.format(format, datas[i]);
            }

            if(fal){
                s += " ";
            }

        }
        return s;
    }

    /** byte序列转换为16进制字符串,为 01 09 .. .. FE 格式，全大写 */
    public static String HexToString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
            stringBuilder.append(" ");
        }
        // 转大写
        return stringBuilder.toString().toUpperCase();
    }


    /** 从string中得到short数据数组 */
    public static short[] ToShorts(String licString) {
        if (licString == null || licString.equals("")) {
            return null;
        }
        licString = licString.toUpperCase();
        int length = licString.length();
        char[] hexChars = licString.toCharArray();
        short[] date = new short[length];
        for (int i = 0; i < length; i++) {
            date[i] = (short) hexChars[i];
        }
        return date;
    }
}

