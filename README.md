# YueFTshare
share to facebook and twitter

### 为何封装一个分享库？
做分享至Facebook 和Twitter功能时参考了官方文档，发现文档不是非常符合国内开发者的阅读习惯，可能导致兜圈子，所以简单封装了一下，方便大家使用。

这个Demo里是把一个RecycleView生成了一张图片然后分享至Facebook 和twitter平台。


### Facebook 前置工作
1. 申请一个Facebook开发者账号，创建一个应用，如下图：


图中的“应用编号”就是Facebook key

DashBoard地址 ： https://developers.facebook.com/apps/538061523610354/dashboard/

2. 生成key hash (就是中文文档中提到的散列值)并添加。
根据keystore文件，使用以下命令生成key hash 

```
keytool -exportcert -alias <RELEASE_KEY_ALIAS> -keystore <RELEASE_KEY_PATH> | openssl sha1 -binary | openssl base64
```
例如我生成的key hash 长这样,注意这只是示例。

```
KC7XXXXXCouE71R0sXEPpH+EQ/Y=
```
然后添加key hash 至“开发者设置”
![image](http://note.youdao.com/yws/res/19158/1248896AF3624999B3E9FE5DA2FF53F4)

别忘了保存。

以上，Facebook 配置完成。

### App必要的配置？
1. 初始化。Twitter SDK需要初始化，所以在Application类的onCreate 方法下添加

```
  Twitter.initialize(this);
```
2. 权限

```
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
STORAGE权限是为了保存生成的图片。

3. res下添加xml文件夹，Manifest中添加FileProvide。
Android 7之后不允许直接分享file:///文件，所以需要通过FileProvider.

xml文件夹下添加provider_paths.xml，内容如下：

```
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="external_files" path="."/>
</paths>
```
Manifest文件中添加<provider../>标签如下：

```
 <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.lee.mytzcdemo.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths"/>
        </provider>
```
4. Manifest文件中添加Facebook 的ApplicationId，FacebookContentProvider等，参考Demo

以上，配置完成。

### 使用封装的库有哪些步骤？

1. 初始化ShareFTdelegate

```
delegate = new ShareFTdelegate(this, new ShareFTdelegate.ShareListener() {
            @Override
            public void shareSuc() {
                
            }

            @Override
            public void shareFail() {
               
            }
        });
```
2. 分享至Facebook 

```
delegate.shareToFaceBook(recView);
```
recView是RecycleView,用来生成一张图片。

3.分享至Twitter

```
 delegate.shareToTitter(recView);
```
recView是RecycleView,用来生成一张图片。
