package com.bigbai.anunit.mView;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * 定义一个相机预览类
 * private Camera mCamera;
 * private CameraPreview mPreview;
 *
 *
 *相机初始化
 void CameraUnitInit(){
 mCamera=getCameraInstance();
 // 创建预览类，并与Camera关联，最后添加到界面布局中
 Camera.Parameters param = mCamera.getParameters();
 param.setPictureSize(640, 360);//如果不设置会按照系统默认配置最低160x120分辨率
 mCamera.setParameters(param);

 mPreview=new CameraPreview(this,this, mCamera);
 FrameLayout preview=(FrameLayout)findViewById(R.id.camera_preview);
 preview.addView(mPreview);
 }
 *
 *打开一个Camera
 public static Camera getCameraInstance(){
 Camera c=null;
 try{
 c=Camera.open();
 }catch(Exception e){
 //  Log.d( "打开Camera失败失败");
 }
 return c;
 }
 //相机图片回调
 private PictureCallback mPicture = new PictureCallback() {
@Override
public void onPictureTaken(byte[] data, Camera camera) {
Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
show_view.setImageBitmap(bitmap);
convertToBlack(bitmap); // 图片操作
}
};

 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "main";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // 通过SurfaceView获得SurfaceHolder
        mHolder = getHolder();
        // 为SurfaceHolder指定回调
        mHolder.addCallback(this);
        // 设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到界面 在Android3.0之后弃用
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraPreview(Activity activity, Context context, Camera camera) {
        super(context);
        mCamera = camera;

        setCameraDisplayOrienation(activity,camera);

        // 通过SurfaceView获得SurfaceHolder
        mHolder = getHolder();
        // 为SurfaceHolder指定回调
        mHolder.addCallback(this);
        // 设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到界面 在Android3.0之后弃用
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // 当Surface被创建之后，开始Camera的预览
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "预览失败");
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Surface发生改变的时候将被调用，第一次显示到界面的时候也会被调用
        if (mHolder.getSurface() == null){
            // 如果Surface为空，不继续操作
            return;
        }

        // 停止Camera的预览
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            Log.d(TAG, "当Surface改变后，停止预览出错");
        }

        // 在预览前可以指定Camera的各项参数

        // 重新开始预览
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "预览Camera出错");
        }
    }

    public static void setCameraDisplayOrienation(Activity activity,Camera mCamera){
        Camera.CameraInfo info = new Camera.CameraInfo();
        info.orientation = 90;

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation){
            case Surface.ROTATION_0 : degrees = 0;break;
            case Surface.ROTATION_90 : degrees = 90;break;
            case Surface.ROTATION_180 : degrees = 180;break;
            case Surface.ROTATION_270 : degrees = 270;break;
        }

        int result ;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        }
        else{
            result = (info.orientation - degrees + 360) % 360;
        }

        mCamera.setDisplayOrientation(result);
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    public int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


}