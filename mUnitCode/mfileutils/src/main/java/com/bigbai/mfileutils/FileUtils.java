package com.bigbai.mfileutils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/** 
 * 文件操作工具包
 * 包含安卓路径操作工具
 * @author zjl
 * @version 1.0
 */
public class FileUtils 
{

	/**
	 * 路径
	 */
	public static class PATH{
		/**
		 * SD根路径
		 */
		public static String SDPath = Environment.getExternalStorageDirectory().getPath();

		/**
		 * /data/data/包名/files
		 * @param context
		 * @return
		 */
		public static String getAppFilesPath(Context context){
			return context.getFilesDir().getPath();
		}

		/**
		 *  /data/data/包名/cache
		 * @param context
		 * @return
		 */
		public static String getAppCachePath(Context context){
			return context.getCacheDir().getPath();
		}
	}

	private static int length;
	static String TAG = "文件操作";


	/***
	 * 写文本和写byte
	 */
	public static class Write{
		/***
		 * 写入byte[]
		 * @param file
		 * @param datas
		 * @return
		 */
		public static boolean save(@NonNull File file, @NonNull byte[] datas){
			file = MFile.createFile(file);
			if(file == null){
				return false;
			}

			OutputStream out = null;
			try {
				out = new FileOutputStream(file);
				out.write(datas);
				out.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				return false;
			} finally {
				CloseableClose(out);
			}

		}

		/**
		 * 文件写入，文件流
		 * @param file
		 * @param filecontent
		 * @return
		 */
		public  static boolean save(File file, String filecontent) {
			return save(file, filecontent.getBytes());
		}

		public static boolean add(@NonNull File fileName, @NonNull byte[] datas) {
			if (!fileName.isFile()) {
				return false;
			}

			OutputStream out = null;
			try {
				if(!fileName.exists()){
					fileName.createNewFile();
				}
				out = new FileOutputStream(fileName, true);
				for (int i = 0; i < datas.length; i++) {
					out.write(datas[i]);
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				return false;
			} finally {
				CloseableClose(out);
			}
		}

		/**
		 * 追加
		 * Add String.
		 *
		 * @param fileName the file
		 * @param content  the content
		 */
		public static boolean add(@NonNull File fileName, @NonNull String content) {
			return add(fileName, content.getBytes());
		}

	}

	/**
	 * 读文本和读byte
	 */
	public static class Read{

		/**
		 * 读取字节
		 * MappedByteBuffer 可以在处理大文件时，提升性能
		 * @param filename
		 * @return
		 */
		public static byte[] getBytesFormAccess(String filename){
			FileChannel fc = null;
			byte[] result = null;
			try {
				fc = new RandomAccessFile(filename, "r").getChannel();
				MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
						fc.size()).load();
				result = new byte[(int) fc.size()];
				if (byteBuffer.remaining() > 0) {
					// System.out.println("remain");
					byteBuffer.get(result, 0, byteBuffer.remaining());
				}
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return result;
		}

		/**
		 * 读取文件的字节数组
		 * 每次读取1024字节
		 * String imgStr = new BASE64Encoder().encode(fileByte);
		 * @param file
		 * @return
		 * @throws IOException
		 */
		public static byte[] getBytes(File file) throws IOException {
			File f = file;
			if (!f.exists()) {
				throw new FileNotFoundException("file not exists");
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(f));
				int buf_size = 1024;
				byte[] buffer = new byte[buf_size];
				int len = 0;
				while (-1 != (len = in.read(buffer, 0, buf_size))) {
					bos.write(buffer, 0, len);
				}
				return bos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos.close();
			}
		}

		/**
		 * 读取文本文件
		 * @param context
		 * @param file
		 * @return
		 */
		public static String getStr(Context context, File file)
		{
			try
			{
				FileInputStream in = context.openFileInput(file.getPath());
				return readInStream(in);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return "";
		}

		/**
		 * 从sd卡更目录开始读
		 * @param strFilePath
		 * @return
		 */
		public static String readText4SD(String strFilePath)
		{
			String path = strFilePath;
			String content = ""; //文件内容字符串
			//打开文件
			try {
				path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + path;
			} catch (IOException e) {
				e.printStackTrace();
			}
			File file = new File(path);
			//如果path是传递过来的参数，可以做一个非目录的判断
			if (file.isDirectory())
			{
				Log.d(TAG, "The File doesn't not exist.");
			}
			else
			{
				try {
					InputStream instream = new FileInputStream(file);
					if (instream != null)
					{
						InputStreamReader inputreader = new InputStreamReader(instream);
						BufferedReader buffreader = new BufferedReader(inputreader);
						String line;
						//分行读取
						while (( line = buffreader.readLine()) != null) {
							content += line + "\n";
						}
						instream.close();
					}
				}
				catch (java.io.FileNotFoundException e)
				{
					Log.d(TAG,  "The File doesn't not exist.");
				}
				catch (IOException e)
				{
					Log.d(TAG,  "error:" + e.getMessage());
				}
			}
			return content;
		}

		/**
		 * 按行读文本
		 * @param file
		 * @return
		 */
		public static String getStrByLine(File file)
		{
			String content = ""; //文件内容字符串
			//如果path是传递过来的参数，可以做一个非目录的判断
			if (file.isDirectory())
			{
				Log.d(TAG, "The File doesn't not exist.");
				return null;
			}
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null)
				{
					InputStreamReader inputreader = new InputStreamReader(instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					//分行读取
					while (( line = buffreader.readLine()) != null) {
						content += line + "\n";
					}
					instream.close();
				}
			}
			catch (java.io.FileNotFoundException e)
			{
				Log.d(TAG,  "The File doesn't not exist.");
				return null;
			}
			catch (IOException e)
			{
				Log.d(TAG,  "error:" + e.getMessage());
				return null;
			}

			return content;
		}

		/**
		 * Asset中的文件属于只读文件
		 * 获取 Asset中的文件InputStream，
		 * @Worring 用完记得close;
		 * @param context
		 * @param fileName		文件名&相对路径
		 * @return
		 */
		public static InputStream getFile4Assets(@NonNull Context context,@NonNull String fileName){
			//创建解压目标目录 
			InputStream input = null;
			try {
				//打开资源文件 
				input = context.getAssets().open(fileName);
			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
			return input;
		}

		/**
		 * 从 Assets 读取字符串
		 * @param context
		 * @param fileName
		 * @return
		 */
		public static String getStr4Assets(Context context,@NonNull String fileName) {
			String strRes = "";
			InputStreamReader in = null;
			try {
				in = new InputStreamReader(context.getAssets().open(fileName));
				BufferedReader bufferedReader = new BufferedReader(in);
				String line = "";
				while ((line = bufferedReader.readLine()) != null){
					strRes += line + "\n";
				}
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return strRes;
		}

		/**
		 * 从文件流获取String
		 * @param inStream
		 * @return
		 */
		private static String readInStream(FileInputStream inStream)
		{
			try
			{
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length = -1;
				while((length = inStream.read(buffer)) != -1 )
				{
					outStream.write(buffer, 0, length);
				}

				outStream.close();
				inStream.close();
				return outStream.toString();
			}
			catch (IOException e)
			{
				Log.d(TAG,  e.getMessage());
			}
			return null;
		}


		/**
		 * 按行读取
		 * Read file by lines string.
		 *
		 * @param file the file
		 * @return the string
		 */
		public static String readStrByLines(@NonNull File file) {
			BufferedReader reader = null;
			StringBuilder builder = new StringBuilder();
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString;
				while ((tempString = reader.readLine()) != null) {
					builder.append(tempString);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
			} finally {
				CloseableClose(reader);
			}

			return builder.toString();
		}


	}


	/**
	 * 文件操作
	 */
	public static class MFile{


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

		public static File createFile( String folderPath, String fileName )
		{
			File destDir = new File(folderPath);
			if (!destDir.exists())
			{
				destDir.mkdirs();
			}
			return new File(folderPath,  fileName + fileName );
		}

		/**
		 * 根据文件绝对路径获取文件名
		 * @param filePath
		 * @return
		 */
		public static String getFileName(String filePath )
		{
			if( filePath.isEmpty() )	{
				return "";
			}
			return filePath.substring( filePath.lastIndexOf( File.separator )+1 );
		}

		/**
		 * 根据文件的绝对路径获取文件名但不包含扩展名
		 * @param filePath
		 * @return
		 */
		public static String getFileNameNoFormat( String filePath){
			if(filePath.isEmpty()){
				return "";
			}
			int point = filePath.lastIndexOf('.');
			return filePath.substring(filePath.lastIndexOf(File.separator)+1,point);
		}

		/**
		 * 获取文件扩展名
		 * @param fileName
		 * @return
		 */
		public static String getFileFormat( String fileName )
		{
			if( fileName.isEmpty() )	{
				return "";
			}

			int point = fileName.lastIndexOf( '.' );
			return fileName.substring( point+1 );
		}

		/**
		 * 获取文件大小
		 * @param filePath
		 * @return
		 */
		public static long getFileSize( String filePath )
		{
			long size = 0;

			File file = new File( filePath );
			if(file!=null && file.exists())
			{
				size = file.length();
			}
			return size;
		}

		/**
		 * 获取文件大小
		 * @param size 字节
		 * @return
		 */
		public static String getFileSize(long size)
		{
			if (size <= 0)	{
				return "0";
			}
			java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
			float temp = (float)size / 1024;
			if (temp >= 1024)
			{
				return df.format(temp / 1024) + "M";
			}
			else
			{
				return df.format(temp) + "K";
			}
		}

		/**
		 * 字节Long 转换文件大小
		 * @param fileS
		 * @return B/KB/MB/GB
		 */
		public static String getFileSizeStr(long fileS) {
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			String fileSizeString = "";
			if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "B";
			} else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "KB";
			} else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "MB";
			} else {
				fileSizeString = df.format((double) fileS / 1073741824) + "G";
			}
			return fileSizeString;
		}


	}


	/**
	 * 文件夹操作
	 */
	public static class MFolder{

		/**
		 * 获取目录文件大小
		 * @param dir
		 * @return
		 */
		public static long getDirSize(File dir) {
			if (dir == null) {
				return 0;
			}
			if (!dir.isDirectory()) {
				return 0;
			}
			long dirSize = 0;
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					dirSize += file.length();
				} else if (file.isDirectory()) {
					dirSize += file.length();
					dirSize += getDirSize(file); //递归调用继续统计
				}
			}
			return dirSize;
		}

		/**
		 * 获取目录文件个数
		 * @return
		 */
		public long getFileList(File dir){
			long count = 0;
			File[] files = dir.listFiles();
			count = files.length;
			for (File file : files) {
				if (file.isDirectory()) {
					count = count + getFileList(file);//递归
					count--;
				}
			}
			return count;
		}

		/**
		 * 新建目录
		 * @param directoryName
		 * @return
		 */
		public static boolean createDirectoryAtSd(String directoryName) {
			boolean status;
			if (!directoryName.equals("")) {
				File path = Environment.getExternalStorageDirectory();
				File newPath = new File(path.toString() + directoryName);
				status = newPath.mkdir();
				status = true;
			} else {
				status = false;
			}
			return status;
		}

		/**
		 * 新建目录
		 * @param directoryName
		 * @return
		 */
		public static boolean createDirectory(String directoryName) {
			boolean status;
			if (!directoryName.equals("")) {
				File newPath = new File( directoryName);
				status = newPath.mkdir();
				status = true;
			} else {
				status = false;
			}
			return status;
		}


		/**
		 * 删除目录(包括：目录里的所有文件)
		 * @param fileName
		 * @return
		 */
		public static boolean deleteDirectory(String fileName) {
			boolean status;
			SecurityManager checker = new SecurityManager();

			if (!fileName.equals("")) {

				File path = Environment.getExternalStorageDirectory();
				File newPath = new File(path.toString() + fileName);
				checker.checkDelete(newPath.toString());
				if (newPath.isDirectory()) {
					String[] listfile = newPath.list();
					// delete all files within the specified directory and then
					// delete the directory
					try {
						for (int i = 0; i < listfile.length; i++) {
							File deletedFile = new File(newPath.toString() + "/"
									+ listfile[i].toString());
							deletedFile.delete();
						}
						newPath.delete();
						Log.d(TAG, "DirectoryManager"+ fileName);
						status = true;
					} catch (Exception e) {
						e.printStackTrace();
						status = false;
					}

				} else {
					status = false;
				}
			} else {
				status = false;
			}
			return status;
		}

		/**
		 * 删除文件
		 * @param fileName 绝对路径
		 * @return
		 */
		public static boolean deleteFile(String fileName) {
			boolean status;
			SecurityManager checker = new SecurityManager();

			if (!fileName.equals("")) {

				File newPath = new File(fileName);
				checker.checkDelete(newPath.toString());
				if (newPath.isFile()) {
					try {
						Log.d(TAG,  "删除文件" + fileName);
						newPath.delete();
						status = true;
					} catch (SecurityException se) {
						se.printStackTrace();
						status = false;
					}
				} else {
					status = false;
				}
			} else {
				status = false;
			}
			return status;
		}

		/**
		 * 递归删除文件夹
		 *
		 * @param dir the dir
		 * @return the boolean
		 */
		public static boolean deleteDir(@NonNull File dir) {
			if (dir != null && dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
			if (dir == null) {
				return false;
			}
			return dir.delete();
		}

	}

	public static byte[] toBytes(InputStream in) throws IOException 
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    int ch;
	    while ((ch = in.read()) != -1)
	    {
	    	out.write(ch);
	    }
	    byte buffer[]=out.toByteArray();
	    out.close();
	    return buffer;
	}


	/**
	 * 检查文件是否存在
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;

	}
	
	/**
	 * 计算SD卡的剩余空间
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * 检查是否安装SD卡
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else {
			status = false;
		}
		return status;
	}


	/**
	 * 获取Asset目录下的文件
	 *
	 * @param context  the mContext
	 * @param fileName the file name
	 * @return file
	 */
	public static File getCacheFile(@NonNull Context context, @NonNull String fileName) {
		File savedir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			savedir = new File(context.getExternalCacheDir(), fileName);
		}

		if (savedir == null) {
			savedir = new File(context.getCacheDir(), fileName);
		}

		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		return savedir;
	}


	/**
	 * Gets root path.
	 *
	 * @param context the context
	 * @return the root path
	 * @description 获取存储路径(如果有内存卡，这是内存卡根目录，如果没有内存卡，则是软件的包file目录)
	 */
	public static String getRootFolder(@NonNull Context context) {
		String rootPath = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			rootPath = context.getFilesDir().getAbsolutePath();
		}
		return rootPath;
	}


	/**
	 * 从Assets文件夹复制文件到SD卡
	 * @param context
	 * @param sourceFileName        源文件路径
	 * @param targetFile            目标文件
	 * @return
	 */
	public static boolean copyFileFromAsstes(Context context, String sourceFileName, File targetFile ){
		//创建解压目标目录 
		InputStream input = null;
		OutputStream output = null;
		try {
			//如果目标目录不存在，则创建 
			File fp = targetFile.getParentFile();
			if(!fp.exists()){
				fp.mkdir();
			}
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			//打开资源文件 
			input = context.getAssets().open(sourceFileName);
			output = new FileOutputStream(targetFile);
			int temp;
			while ((temp = input.read()) != (-1)) {
				output.write(temp);
			}
			input.close();
			output.close();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally {
			CloseableClose(input);
			CloseableClose(output);
		}
		return true;
	}

	public static boolean copyFolder(@NonNull String oldPath, @NonNull String newPath) {
		return copyFolder(new File(oldPath), new File(newPath));
	}

	/**
	 * 获取文件夹大小
	 *
	 * @param file the file
	 * @return the folder size
	 * @throws Exception the exception
	 */
	public static long getFolderSize(@NonNull File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 复制文件
	 * Copy file boolean.
	 *
	 * @param sourceFile the source file
	 * @param targetFile the target file
	 * @return the boolean
	 */
	private static boolean copyFile(@NonNull File sourceFile, @NonNull File targetFile) {
		if (!sourceFile.exists() || targetFile.exists()) {
			//原始文件不存在，目标文件已经存在
			return false;
		}
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(sourceFile);
			output = new FileOutputStream(targetFile);
			int temp;
			while ((temp = input.read()) != (-1)) {
				output.write(temp);
			}
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
		} finally {
			CloseableClose(input);
			CloseableClose(output);
		}
		return true;
	}

	/**
	 * 复制整个文件夹
	 * Copy folder.
	 *
	 * @param oldFile the old path
	 * @param newPath the new path
	 */
	public static boolean copyFolder(@NonNull File oldFile, @NonNull File newPath) {
		if (oldFile.isFile())//如果是文件，直接复制
		{
			return copyFile(oldFile, new File(newPath, oldFile.getName()));
		}
		try {//文件夹
			newPath.mkdirs(); //如果文件夹不存在 则建立新文件夹
			File[] temps = oldFile.listFiles();
			File temp;
			boolean flag = true;
			length = temps.length;
			for (int i = 0; i < length; i++) {
				temp = temps[i];
				//文件夹里面
				if (temp.isFile()) {
					File path = new File(newPath, oldFile.getName());
					path.mkdirs();
					File file = new File(path, temp.getName());
					flag = copyFile(temp, file);
				} else if (temp.isDirectory()) {//如果是子文件夹
					flag = copyFolder(temp, new File(newPath + File.separator + oldFile.getName()));
				}

				if (!flag) {
					break;
				}
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 移动文件到指定目录
	 *
	 * @param oldPath String  如：/test/abc.md
	 * @param newPath String  如：/abc.md
	 */
	public static boolean moveFile(@NonNull String oldPath, @NonNull String newPath) {
		return moveFile(new File(oldPath), new File(newPath));
	}

	public static boolean moveFile(@NonNull File oldPath, @NonNull File newPath) {
		if (!oldPath.isFile()) {
			return false;
		}
		//如果是文件夹，这创建文件
		if (newPath.isDirectory()) {
			newPath = new File(newPath, oldPath.getName());
		}
		try {
			return oldPath.renameTo(newPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移动文件到指定目录
	 *
	 * @param oldPath String
	 * @param newPath String
	 */
	public static boolean moveFolder(@NonNull String oldPath, @NonNull String newPath) {
		return moveFolder(new File(oldPath), new File(newPath));
	}

	/**
	 * 移动文件夹
	 * Move folder.
	 *
	 * @param oldFile the old path
	 * @param newPath the new path
	 */
	public static boolean moveFolder(@NonNull File oldFile, File newPath) {
		return copyFolder(oldFile, newPath) && deleteFile(oldFile);
	}

	/**
	 * 删除文件
	 * Delete file boolean.
	 *
	 * @param file the file
	 * @return the boolean
	 */
	public static boolean deleteFile(File file) {
		return MFolder.deleteDir(file);
	}

	public static void CloseableClose(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}
}