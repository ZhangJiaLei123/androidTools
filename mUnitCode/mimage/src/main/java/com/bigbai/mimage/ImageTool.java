package com.bigbai.mimage;

import android.os.Environment;

import java.io.File;

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
}
