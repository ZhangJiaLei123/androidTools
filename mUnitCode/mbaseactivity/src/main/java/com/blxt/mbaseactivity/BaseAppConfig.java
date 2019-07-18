package com.blxt.mbaseactivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.blxt.xmltool.EConfigBase;
import com.blxt.xmltool.Element;
import com.blxt.xmltool.ElementTool;

import java.io.File;

/**
 * App配置文件管理
 * @author: Zhang
 * @date: 2019/7/3 - 15:22
 * @note Created by com.blxt.mbaseactivity.
 */
public abstract class BaseAppConfig {

    Context context;
    /** 首次运行标记 */
    public static boolean isFirstRun = false;
    /** 配置文件名 */
    private final String configFileName = "bconfig.xml";
    private final String assetsFile = "config.xml";

    /** /data/data/包名/files/AppInfo/xxx.xml */
    public File configFile = null;

    /** */
    public Element rootS = null;

    public MAppinfo mAppinfo = new MAppinfo();

    public BaseAppConfig(Context context){
        this.context = context;
        String configPath = context.getFilesDir().getPath() + "/AppInfo/" + configFileName;
        configFile = new File(configPath);

        initConfig();

        Element element = mAppinfo.make();
        rootS.upSubElement(element); // 更新本机信息

        // 每次重新保存
        FileUtils.Write.save(new File(configPath), rootS.toString().trim());
    }

    public void initConfig(){
        // 获取配置文件
        if(!configFile.exists()){ // 配置文件不存在就从本地拷贝一个过去
            if(!configFile.getParentFile().exists()){
                configFile.getParentFile().mkdirs();
            }
            if(!copyConfig()){
                Log.i("创建配置文件失败", configFile.getPath());
            }
            else{
                Log.i("创建配置文件", configFile.getPath());
            }
        }
        else {
            // 否则就比较版本，
            String cLoack = getConfigContent();
            rootS = new Element(cLoack, 0, 0);
            float vSd = Float.parseFloat(rootS.getKeyvalue("version"));

            String sAa = FileUtils.Read.getStr4Assets(context, assetsFile);
            Element elementAa = new Element(sAa, 0, 0);

            float vAa = Float.parseFloat(elementAa.getKeyvalue("version"));

            if(vAa > vSd){ // 如果程序内配置版本大于sd卡，就覆盖
                rootS = elementAa;
                Log.i("配置config.xml", "更新");
                upConfig(rootS, elementAa);
            }
        }

        // 重新获取Element
        if(rootS == null){
            String sAa = FileUtils.Read.getStr4Assets(context, assetsFile);
            rootS = new Element(sAa, 0, 0);
        }


        // 处理 首次运行问题
        for(Element element : rootS.getSubElement()){
            if(getFirstRun(element)){

            }
        }
        // 置首次运行为false
        if(isFirstRun){
            ElementTool.setElementKeyValueByLable(rootS, "FirstRun", "value", "false");
        }

    }

    /**
     * 获取是否是首次运行
     * @param element
     * @return
     */
    public boolean getFirstRun(Element element){
        if(element.lable.equals("FirstRun")){
            isFirstRun = element.getKeyvalue("value", "false").equals("true");
            return true;
        }
        return false;
    }


    /**
     * 复制配置文件到本地
     * @return
     */
    private boolean copyConfig(){
        if(!FileUtils.copyFileFromAsstes(context,
                assetsFile,
                configFile
        )) {
            Log.e("AppConfig", "复制配置文件失败");
            return false;
        }
        return true;
    }

    /**
     * 更新配置，并保存
     * @param oldE
     * @param newE
     */
    private void upConfig(Element oldE, Element newE){
        // TODO 目前只做简单替换，后面有空就做容差覆盖（没有的就加入，有的就保留）
        Log.d("更新配置", "旧版本");
        saveConfigContent();
    }

    /**
     * 保存配置到本地
     * @return
     */
    public boolean saveConfigContent(Context context){
        return FileUtils.Write.save(configFile, rootS.toString());
    }

    /**
     * 保存配置到本地
     * @return
     */
    public boolean saveConfigContent(String str){
        return FileUtils.Write.save(configFile, str);
    }

    /**
     * 保存配置到本地
     * @return
     */
    public boolean saveConfigContent(){
        return FileUtils.Write.save(configFile, rootS.toString().trim());
    }

    /**
     * 获取本地配置xml文本
     * @return
     */
    public String getConfigContent(){
        return FileUtils.Read.getStrByLine(configFile);
    }

    /**
     * app配置
     */
    public class MAppinfo implements EConfigBase {

        /** 包名 */
        private String pakgeName = context.getPackageName();
        /** 软件名称 */
        private String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        /** 软件版本号 */
        private String appVerCode = getAppVersionCode(context);
        /** 软件版本名称 */
        private String appVerName = getAppVersionName(context);
        /** 设备MAC地址 */
        private String macStr = null;
        /** 设备IP地址 */
        private String hostIpStr = null;
        /** 设备电话号码 */
        private String phoneNumber = null;
        /** 设备名字 */
        private String devName = null;
        /** 设备唯一ID */
        private String devId = null;
        /** 设备厂商 */
        private String manufacturer = null;

        @Override
        public boolean analysis(Element element) {
            if(element == null){
                return false;
            }
            if(element.lable.equals(this.getClass().getName())){

                return true;
            }
            return false;
        }


        @Override
        public Element make(){
            Element element = new Element();
            element.lable = this.getClass().getName();
            element.addDatas("pakgeName", pakgeName);
            element.addDatas("appName", appName);
            element.addDatas("appVerCode", appVerCode);
            element.addDatas("appVerName", appVerName);
            element.addDatas("hostIpStr", hostIpStr);
            element.addDatas("phoneNumber", phoneNumber);
            element.addDatas("devName", devName);
            element.addDatas("devId", devId);
            element.addDatas("manufacturer", manufacturer);

            return element;
        }

    }

    /**
     * 返回当前程序版本号
     */
    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode + "";
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


}
