
个人常用工具集，收集并整理于互联网，仅供学习，请勿商用

1.项目导入
 1.1 在 项目根build中添加
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

 1.2 在需要导入依赖的build中，添加
```
    implementation 'com.bigbai.anunit:anunit:1.0.0'
```
![导入步骤2](https://github.com/ZhangJiaLei123/Picture-bed/blob/master/%E5%AF%BC%E5%85%A5%E6%AD%A5%E9%AA%A42.png?raw=true)
