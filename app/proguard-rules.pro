# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Sinyuk/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#如果在没有混淆的版本运行正常，在混淆后的版本的代码运行错误，
#并提示Uncaught TypeError: Object [object Object] has no method，
#那就是你没有做混淆例外处理。 在混淆文件加入类似这样的代码

-keepattributes *Annotation*
-keepattributes JavascriptInterface
-keep class com.example.javajsinteractiondemo$JsInteration {
    *;
}

#searchView
-keep class br.com.mauker.MsvAuthority
-keepclassmembers class br.com.mauker.** { *; }