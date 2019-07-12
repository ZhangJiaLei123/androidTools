package com.blxt.mtool;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @brief：从Res中获取资源
 * @author: Zhang
 * @date: 2019/6/15 - 15:47
 * @note Created by com.blxt.securitybox.tools.
 */
public class Tool {

    /**
     * 从res-drawable中获取图片
     * @param context
     * @param resourcesId
     * @return
     */
    public static Drawable getDrawable(Context context, int resourcesId){
        Drawable drawable =
                ContextCompat.getDrawable(context.getApplicationContext()
                        , resourcesId);
        return drawable;
    }


    /** byte序列转换为16进制字符串,为 01 09 .. .. FE 格式，全大写 */
    public static String ToHexStr(byte[] src) {
        return ToHexStr(src, false);
    }

    /** byte序列转换为16进制字符串,为 01 09 .. .. FE 格式，全大写 */
    public static String ToHexStr(byte[] src, boolean isSp) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);

            if(isSp){ // 追加空格
                stringBuilder.append(" ");
            }

        }
        // 转大写
        return stringBuilder.toString().toUpperCase();
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
     * int 转 字符串
     * @param datas
     * @return
     */
    public static String int2Str(int[] datas){
        return int2Str(datas, null, false);
    }

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

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
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
        for( ; i > 1 && bytes[i - 1] == 0; i--);
        byte[] bytes_new =  new byte[i];
        return bytes_new;
    }


    /**
     * 字节数组转换成整数
     * 关键技术：ByteArrayInputStream和DataInputStream
     * @param byteArray
     * 需要转换的字节数组
     * @return
     */
    public static int getInt(byte[] byteArray) {
        int n = 0;
        try {
            ByteArrayInputStream byteInput = new ByteArrayInputStream(byteArray);
            DataInputStream dataInput = new DataInputStream(byteInput);
            n = dataInput.readInt();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return n;
    }

    public static final int MODEL_B = 0;
    public static final int MODEL_KB = 1;
    public static final int MODEL_MB = 2;
    public static final int MODEL_GB = 3;
    public static final int MODEL_TB = 4;
    /**
     * 格式化内存单位
     *
     * @param size 大小
     * @return
     */
    public static String getFormatSize(double size, int model) {
        double kiloByte = size / 1024;

        if ((kiloByte < 1 && model == -1) || model == MODEL_B) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if ((megaByte < 1 && model == -1) || model == MODEL_KB) {
            BigDecimal result1 = BigDecimal.valueOf(kiloByte);
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if ((gigaByte < 1 && model == -1) || model == MODEL_MB) {
            BigDecimal result2 = BigDecimal.valueOf(megaByte);
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if ((teraBytes < 1 && model == -1) || model == MODEL_GB ) {
            BigDecimal result3 = BigDecimal.valueOf(gigaByte);
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = BigDecimal.valueOf(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 格式化内存单位
     *
     * @param size 大小
     * @return
     */
    public static String getFormatSize(double size) {
        return getFormatSize(size, -1);
    }

}
