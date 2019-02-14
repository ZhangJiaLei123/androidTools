
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

@import "./DocImg/导入步骤1.png"

 1.2 在需要导入依赖的build中，添加
```
    implementation 'com.bigbai.anunit:anunit:1.0.0'
```
@import "./DocImg/导入步骤2.png"