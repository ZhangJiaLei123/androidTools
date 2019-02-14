package com.bigbai.anunit.NetWork;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载线程
 *
 * String  sdCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
 *  DownloadRunnable runnable;//下载任务
 *     DownloadTaskInfo info;//任务信息
 *     private void downloadFile(final String url){
 *
 *         //实例化任务信息对象
 *         info = new DownloadTaskInfo("aa.apk"
 *                 , sdCardRoot + "/Download/"
 *                 , url);
 *
 *         //创建下载任务
 *         runnable = new DownloadRunnable(info);
 *         //开始下载任务
 *         new Thread(runnable).start();
 *         //开始Handler循环
 *         handler.sendEmptyMessageDelayed(1, 200);
 *
 *     }
 * // 回调
 *     @SuppressLint("HandlerLeak")
 *     private Handler handler = new Handler(){
 *         @Override
 *         public void handleMessage(final Message msg) {
 *             if(msg != null)
 *             {
 *                 //使用Handler制造一个200毫秒为周期的循环
 *                 handler.sendEmptyMessageDelayed(1, 200);
 *                 //计算下载进度
 *                 int l = (int) ((float) info.getCompletedLen() / (float) info.getContentLen() * 100);
 *                 //设置进度条进度
 *                 Log.i("正在下载");
 *                 if (l>=100) {//当进度>=100时，取消Handler循环
 *                     handler.removeCallbacksAndMessages(null);
 *                     Log.i("下载完成");
 *                 }
 *                 return;
 *             }
 *     }
 */
public class DownloadRunnable  implements Runnable {

    private DownloadTaskInfo info;//下载信息JavaBean
    private boolean isStop;//是否暂停

    /**
     * 构造器
     * @param info 任务信息
     */
    public DownloadRunnable(DownloadTaskInfo info) {
        this.info = info;
    }

    /**
     * 停止下载
     */
    public void stop() {
        isStop = true;
    }

    /**
     * Runnable的run方法，进行文件下载
     */
    @Override
    public void run() {
        HttpURLConnection conn;//http连接对象
        BufferedInputStream bis;//缓冲输入流，从服务器获取
        RandomAccessFile raf;//随机读写器，用于写入文件，实现断点续传
        int len = 0;//每次读取的数组长度
        byte[] buffer = new byte[1024 * 8];//流读写的缓冲区
        try {
            //通过文件路径和文件名实例化File
            File file = new File(info.getPath() + info.getName());
            //实例化RandomAccessFile，rwd模式
            raf = new RandomAccessFile(file, "rwd");
            conn = (HttpURLConnection) new URL(info.getUrl()).openConnection();
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setConnectTimeout(120000);//连接超时时间
            conn.setReadTimeout(120000);//读取超时时间
            conn.setRequestMethod("GET");//请求类型为GET
            if (info.getContentLen() == 0) {//如果文件长度为0，说明是新任务需要从头下载
                //获取文件长度
                String lstr = conn.getHeaderField("content-length");
                if (conn.getHeaderField("content-length")!= null) {
                    info.setContentLen(Long.parseLong(lstr));
                }
                else
                    Log.i("content-length = " + lstr);

            } else {//否则设置请求属性，请求制定范围的文件流
                conn.setRequestProperty("Range", "bytes=" + info.getCompletedLen() + "-" + info.getContentLen());
            }
            raf.seek(info.getCompletedLen());//移动RandomAccessFile写入位置，从上次完成的位置开始
            conn.connect();//连接
            bis = new BufferedInputStream(conn.getInputStream());//获取输入流并且包装为缓冲流
            //从流读取字节数组到缓冲区
            while (!isStop && -1 != (len = bis.read(buffer))) {
                //把字节数组写入到文件
                raf.write(buffer, 0, len);
                //更新任务信息中的完成的文件长度属性
                info.setCompletedLen(info.getCompletedLen() + len);
            }
            if (len == -1) {//如果读取到文件末尾则下载完成
                Log.i("DownloadRunnable", "下载完了");
                bis.close();
                raf.close();
            } else {//否则下载系手动停止
                Log.i("DownloadRunnable", "下载停止了");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(e.toString());
        }
    }



}
