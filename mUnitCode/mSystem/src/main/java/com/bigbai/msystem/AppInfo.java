package com.bigbai.msystem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

/**
 * 软件信息
 */
public class AppInfo {

    // 获取版本号
    public static int getVersionCode(Context context){
       // String version;
        int versionCode = -1;

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = null;
            info = manager.getPackageInfo(context.getPackageName(), 0);
         //   version = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = -1;
        }
        return versionCode;
    }

    /**
     * 安装 apk
     * @param context
     * @param filePath
     */
    public static void install(Context context, String filePath) {
        Log.i("开始执行安装: ", filePath);
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w( "安装 apk","版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    context
                    , "com.bigbai.mdview.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w("安装 apk","正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
