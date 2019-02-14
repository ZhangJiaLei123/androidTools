package com.bigbai.anunit.mImageUnit;

import android.graphics.Bitmap;

/**
 * 图像裁剪
 * */
public class CutImage {
    /**
     * 自定义裁剪，根据第一个像素点(左上角)X和Y轴坐标和需要的宽高来裁剪
     * @param srcBitmap
     * @param firstPixelX
     * @param firstPixelY
     * @param needWidth
     * @param needHeight
     * @return
     */
    public static Bitmap cropBitmapCustom(Bitmap srcBitmap, int firstPixelX, int firstPixelY, int needWidth, int needHeight) {
        if(firstPixelX + needWidth > srcBitmap.getWidth()){
            needWidth = srcBitmap.getWidth() - firstPixelX;
        }

        if(firstPixelY + needHeight > srcBitmap.getHeight()){
            needHeight = srcBitmap.getHeight() - firstPixelY;
        }
        /**裁剪关键步骤*/
        Bitmap cropBitmap = Bitmap.createBitmap(srcBitmap, firstPixelX, firstPixelY, needWidth, needHeight);
        return cropBitmap;
    }
}
