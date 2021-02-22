# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn **
-ignorewarnings


-keep public class * implements com.ixuea.android.downloader.db.DownloadDBController


#amplitude
-keep class com.google.android.gms.ads.** { *; }
    -dontwarn okio.**

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**


-dontwarn com.google.auto.value.AutoValue
-dontwarn com.google.auto.value.AutoValue$Builder


##  ________________________________________________________________________________________________________________

-dontwarn org.**
-dontwarn com.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn sun.**



##  ________________________________________________________________________________________________________________

-keep class android.** { *; }
-keep class org.** { *; }
-keep class java.** { *; }
-keep class javax.** { *; }
-keep class sun.** { *; }
-keep class de.mindpipe.** { *; }
-keep class com.j256.** { *; }




    ##  ________________________________________________________________________________________________________________
    ## Preserve line numbers in the obfuscated stack traces.
    -keepattributes LineNumberTable
    -keepattributes SourceFile


    -keepattributes Signature
    -keepattributes *Annotation*
    -keepattributes InnerClasses,EnclosingMethod


    # Basic ProGuard rules for Firebase Android SDK 2.0.0+
    -keep class com.firebase.** { *; }

    -keepnames class com.fasterxml.jackson.** { *; }
    -keepnames class javax.servlet.** { *; }
    -keepnames class org.ietf.jgss.** { *; }

    -keep class com.firebase.** { *; }
    -keepnames class com.fasterxml.jackson.** { *; }
    -keepnames class javax.servlet.** { *; }
    -keepnames class org.ietf.jgss.** { *; }
    -dontwarn org.w3c.dom.**
    -dontwarn org.joda.time.**
    -dontwarn org.shaded.apache.**
    -dontwarn org.ietf.jgss.**
    -dontwarn com.firebase.**
    -dontnote com.firebase.client.core.GaePlatform

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn **
-ignorewarnings


-keep public class * implements com.ixuea.android.downloader.db.DownloadDBController


#amplitude
-keep class com.google.android.gms.ads.** { *; }
    -dontwarn okio.**

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**


-dontwarn com.google.auto.value.AutoValue
-dontwarn com.google.auto.value.AutoValue$Builder


##  ________________________________________________________________________________________________________________

-dontwarn org.**
-dontwarn com.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn sun.**



##  ________________________________________________________________________________________________________________

-keep class android.** { *; }
-keep class org.** { *; }
-keep class java.** { *; }
-keep class javax.** { *; }
-keep class sun.** { *; }
-keep class de.mindpipe.** { *; }
-keep class com.j256.** { *; }




    ##  ________________________________________________________________________________________________________________
    ## Preserve line numbers in the obfuscated stack traces.
    -keepattributes LineNumberTable
    -keepattributes SourceFile


    -keepattributes Signature
    -keepattributes *Annotation*
    -keepattributes InnerClasses,EnclosingMethod


    # Basic ProGuard rules for Firebase Android SDK 2.0.0+
    -keep class com.firebase.** { *; }

    -keepnames class com.fasterxml.jackson.** { *; }
    -keepnames class javax.servlet.** { *; }
    -keepnames class org.ietf.jgss.** { *; }

    -keep class com.firebase.** { *; }
    -keepnames class com.fasterxml.jackson.** { *; }
    -keepnames class javax.servlet.** { *; }
    -keepnames class org.ietf.jgss.** { *; }
    -dontwarn org.w3c.dom.**
    -dontwarn org.joda.time.**
    -dontwarn org.shaded.apache.**
    -dontwarn org.ietf.jgss.**
    -dontwarn com.firebase.**
    -dontnote com.firebase.client.core.GaePlatform


##  ________________________________________________________________________________________________________________

-dontwarn org.**
-dontwarn com.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn sun.**



##  ________________________________________________________________________________________________________________

-keep class android.** { *; }
-keep class org.** { *; }
-keep class java.** { *; }
-keep class javax.** { *; }
-keep class sun.** { *; }
-keep class de.mindpipe.** { *; }
-keep class com.j256.** { *; }


##  ________________________________________________________________________________________________________________
## Preserve line numbers in the obfuscated stack traces.
-keepattributes LineNumberTable
-keepattributes SourceFile

-dontwarn com.google.**



## solucion porblema serializado de model en firefase o algo asi
-keep class com.google.firebase.example.fireeats.model.** { *; }

-keepclassmembers class com.lamesa.netfilms.model** { *; }


-keep public class * extends android.view.View{*;}

-keep public class * implements com.kk.taurus.playerbase.player.IPlayer{*;}








