package com.bigbai.mimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @brief：对文件的简述
 * @author: Zhang
 * @date: 2019/6/20 - 15:52
 * @note Created by com.bigbai.mimage.
 */
public class ImageTool {

    /**
     * SD根路径
     */
    public static String SDPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * 获取SD路径下的指定文件夹下的图片文件数组
     * @param folderPath
     * @return
     */
    public static File[] getImages(String folderPath) {
        folderPath = folderPath;

        File file01 = new File(folderPath);

        String[] files01 = file01.list();
        int imageFileNums = 0;
        for (int i = 0; i < files01.length; i++) {
            File file02 = new File(folderPath + "/" + files01[i]);
            if (!file02.isDirectory()) {
                if (isImageFile(file02.getName())) {
                    imageFileNums++;
                }
            }
        }
        File[] files02 = new File[imageFileNums];
        int j = 0;
        for (int i = 0; i < files01.length; i++) {
            File file02 = new File(folderPath + "/" + files01[i]);
            if (!file02.isDirectory()) {
                if (isImageFile(file02.getName())) {
                    files02[j] = file02;
                    j++;
                }
            }
        }
        return files02;
    }

    /**
     * 判断是否是图片文件
     * @param fileName
     * @return
     */
    private static boolean isImageFile(String fileName) {
        String fileEnd = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
        if (fileEnd.equalsIgnoreCase("jpg")) {
            return true;
        } else if (fileEnd.equalsIgnoreCase("png")) {
            return true;
        } else if (fileEnd.equalsIgnoreCase("bmp")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从文件获取Bitmap
     * @param imageFile
     * @return
     */
    public Bitmap getBitmap(File imageFile){
        return BitmapFactory.decodeFile(imageFile.getPath());
    }

    /**
     * 保存图片 Bitmap
     * @param imageFile
     * @param mBitmap
     */
    public  boolean saveBitmap(File imageFile, Bitmap mBitmap) {
        if(imageFile.exists()){
            imageFile.delete();
        }
        else{
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * 保存图片 byte[]
     * @param buffer
     * @param fileImage
     * @return
     */
    public static boolean saveImage(byte[] buffer, File fileImage)
    {
        boolean writeSucc = false;
        fileImage = createFile(fileImage);

        if(fileImage == null){
            return false;
        }

        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream( fileImage );
            out.write(buffer);
            writeSucc = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {out.close();} catch (IOException e) {e.printStackTrace();}
        }

        return writeSucc;
    }

    /**
     * 创建文件
     * @param file
     * @return
     */
    public static File createFile(File file){
        if (file.isDirectory()) {
            return null;
        }
        if (!file.exists()) {
            try {

                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }

                file = new File(file.getParentFile(),file.getName());

                file.createNewFile();

                if (!file.exists()){
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        return file;
    }

}
