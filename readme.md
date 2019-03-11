
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
* 需要文件读写权限

#### 1.1 导入依赖
```
    implementation 'com.bigbai.mlog:mlog:1.0.0'
```

#### 1.2 基础使用

 log模块是对原Log进行加工处理，简化log拼写和日志保存。简要使用如下:

* 简易模式，省略TAG，默认TAG为“测试”，可通过 LOG.TAG="TAG"修改
 ``` java
LOG.i("str");
 ```

* 一般模式
 ``` java
LOG.i("TAG","str");
 ```

* 日志保存到本地
``` java
LOG.isSave = true;              // 开启日志保存
LOG.LogPath = "log.log";        // 自定义日志路径（包括文件名）
String logStr = LOG.getLog();   // 获取日志文件
LOG.clearLog();                 // 清除日志文件
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
