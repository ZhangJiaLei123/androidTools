package com.bigbai.anunit.mFileUnit;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GetSdcardPath {
	
	//private static String [] FilterKEY = {"emulated","knox-emulated","privatemode","legacy"};
	private static String  FilterKEYString = "emulated,knox-emulated,privatemode,legacy";
	/**
	 * 获取外置默认SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		String cmd = "cat /proc/mounts";
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		String result = null;  //http://iwan.qq.com/index.htm
		try {
			Process p = run.exec(cmd);// 启动另一个进程来执行命令
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				// LOG.i("CommonUtil:getSDCardPath", lineStr);
				if (lineStr.contains("sdcard")
						&& lineStr.contains(".android_secure")) {
					String[] strArray = lineStr.split(" ");
					if (strArray != null && strArray.length >= 5) {
						result = strArray[1].replace("/.android_secure", "");
						return result;
					}
				}
				// 检查命令是否执行失败。
				if (p.waitFor() != 0 && p.exitValue() == 1) {
					// p.exitValue()==0表示正常结束，1：非正常结束
					// LOG.e("CommonUtil:getSDCardPath", "命令执行失败!");
				}
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			// LOG.e("CommonUtil:getSDCardPath", e.toString());

			return Environment.getExternalStorageDirectory().getPath();
		}
		if (TextUtils.isEmpty(result)) {
			result = Environment.getExternalStorageDirectory().getPath();
		}
		if(getMemorySize(result) == 0)
		{
			for (Map.Entry<String, String> entry : getSDList().entrySet()) {
					result = entry.getValue();
			}
		}
		return result;
	}

	/**
	 * 获取外置SD卡路径List
	 * 
	 * @return
	 */
	public static Map<String, String> getSDList() {
		Map<String, String> respone = new HashMap<String, String>();
		String cmd = "cat /proc/mounts";
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		try {
			Process p = run.exec(cmd);// 启动另一个进程来执行命令
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				// LOG.i("CommonUtil:getSDCardPath", lineStr);
				if (lineStr.contains("sdcard")
						&& !lineStr.contains(".android_secure")) {
					String[] strArray = lineStr.split(" ");
					if (strArray != null && strArray.length >= 5) {
						String value = strArray[1];
						String key = value
								.substring(value.lastIndexOf("/") + 1);
						if (!respone.containsKey(key)) {
							if(!FilterKEYString.contains(key))
							{
								if(key.equals("0")&& value.contains("storage"))
								{
									key = "  d" + key;
								}
								
								respone.put(key, value);
								
							}
						}
					}
				}
				// 检查命令是否执行失败。
				if (p.waitFor() != 0 && p.exitValue() == 1) {
					// p.exitValue()==0表示正常结束，1：非正常结束
					// LOG.e("CommonUtil:getSDCardPath", "命令执行失败!");
				}
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			// LOG.e("CommonUtil:getSDCardPath", e.toString());
			respone = null;
		}

		return respone;
	}
	
	 /** 
     * 获取扩展存储路径，TF卡、U盘 
     */  
    public static Map<String, String> getExternalStorageDirectory(){
    	
    	Map<String, String> respone = new HashMap<String, String>();
        String dir = new String();  
        try {  
            Runtime runtime = Runtime.getRuntime();  
            Process proc = runtime.exec("mount");  
            InputStream is = proc.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            String line;  
            BufferedReader br = new BufferedReader(isr);  
            while ((line = br.readLine()) != null) {  
                if (line.contains("secure")) continue;  
                if (line.contains("asec")) continue;  
                  
                if (line.contains("fat")) {  
                    String columns[] = line.split(" ");  
                    if (columns != null && columns.length > 1) {  
                        //dir = dir.concat(columns[1] + "\n");
                    	respone=getMap(respone, columns[1]);
                    }  
                } else if (line.contains("fuse")) {  
                    String columns[] = line.split(" ");  
                    if (columns != null && columns.length > 1) {  
                        //dir = dir.concat(columns[1] + "\n");
                    	respone=getMap(respone, columns[1]);
                    }  
                }  
            }  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        //return dir;  
        return respone;
    } 
    
    public static Map<String, String> getMap(Map<String, String> respone,String path) {
		
    	if (!TextUtils.isEmpty(path)) {
    		int index=path.lastIndexOf("/");
    		if (index!=-1) {
    			respone.put(path.substring(index+1), path);
			}
			
		}
    	return respone;
    	
	}
    //获取指定路径的内存大小
    private static long getMemorySize(String mPath) {
		File path = new File(mPath);
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
    
    /**
     * 获取选择的SD卡文件的大小
     * @return
     */
    public static long getAvailableExternalSDMemorySize() {
		File path = new File(getSDCardPath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}



	/**
	 * 获取路径
	 * @param context     getApplicationContext()
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(final Context context,Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		if(null!=cursor&&cursor.moveToFirst()){
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
			cursor.close();
		}
		return res;
	}

	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{split[1]};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
								String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}


}
