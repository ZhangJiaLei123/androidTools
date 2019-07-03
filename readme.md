
## 个人常用工具集，收集并整理于互联网，仅供学习，请勿商用

## 一 项目导入
* 1 在 项目根build中添加
 ``` 
 allprojects {
     repositories {
         google()
         jcenter()
        。。。
         maven {
            url "https://raw.githubusercontent.com/ZhangJiaLei123/androidTools/master"
         }
        。。。
     }
 }
 ```

![导入步骤1](https://github.com/ZhangJiaLei123/Picture-bed/blob/master/%E5%AF%BC%E5%85%A5%E6%AD%A5%E9%AA%A41.png?raw=true)

* 2 在需要导入依赖的build中，添加
```
    implementation 'com.bigbai.anunit:anunit:1.0.0'
```
![导入步骤2](https://github.com/ZhangJiaLei123/Picture-bed/blob/master/%E5%AF%BC%E5%85%A5%E6%AD%A5%E9%AA%A42.png?raw=true)

## 二 模块
### 1、日志模块

* 基于java的 System.out.println() 的打印方式，兼容普通的java项目。
* 目前有4个输出等级：INFO、DEBUG、WARNING、ERROR，对应的调用方式为 LOG.i();LOG.d();LOG.w();LOG.e();
* 可打印异常抛出，调用方式为: LOG.(String TAG, Obj...args);arg可以是 String 或 Exception

#### 1.1 导入依赖
```
    implementation 'com.bigbai.mDenug:mDenug:1.0.3'
```

#### 简单使用

``` java
LOG.isLog = true; // 开启日志打印，默认为true
LOG.isInfo = false; // 关闭INFO信息的打印和保存到文件
LOG.isSave = true; // 开启日志文件保存到本地（需要设置文件路径）
File file = LOG.getInstance(); // 获取默认日志文件 ：/data/data/包名/cache/AppInfo/Bxlt/Log.log
LOG.setLogFile(File); // 设置日志保存文件
LOG.MAXSIZE = 16; // 日志文件最大容量，单位为Mb，
LOG.clearLog();                 // 清除日志文件

```

Android 专用
``` java
LOG.showLogDialog(Content); // 显示日志对话框，并进行简单操作
LOG.setLogView(View);   // 同步打印日志到UI视图（TextView和EditText）
```



### 2、网络模块

* 请添加网络权限

包含常用网络处理工具，如ping工具，网络开关检查工具，下载工具，url工具。更多工具，在后期添加。

#### 2.1 导入依赖
```
    implementation 'com.bigbai.mnetwork:mnetwork:1.0.0'
```

#### 2.2 ping工具的简单实用

``` java
new Thread(new Runnable() {
@Override
public void run() {
         PingNetEntity pingNetEntity=new PingNetEntity(purecameraip,3,5,new StringBuffer());
         pingNetEntity=PingNet.ping(pingNetEntity);
         if (pingNetEntity.isResult()){
             Log.i("Ping测试",pingNetEntity.getIp());
             Log.i("Ping测试","time="+pingNetEntity.getPingTime());
         }
         else{
          //UserControl.ui.Error("IP不存在");
         }
    }
}).start();

```
