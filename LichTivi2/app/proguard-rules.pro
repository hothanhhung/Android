# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\hung\Soft\Android\android-sdk/tools/proguard/proguard-android.txt
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

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep class * extends android.app.Activity
-keep class * extends android.app.Application
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.app.backup.BackupAgentHelper
-keep class * extends android.preference.Preference
-keep class com.android.vending.licensing.ILicensingService
-keepclassmembers  class com.hth.utils.ResponseJson{
    private <fields>;
    public <fields>;
    protected <fields>;
}
-keepclassmembers class com.hth.utils.SearchProgramItem{
                                                           private <fields>;
                                                           public <fields>;
                                                           protected <fields>;
                                                       }
-keepclassmembers class com.hth.utils.ScheduleItem{
                                                      private <fields>;
                                                      public <fields>;
                                                      protected <fields>;
                                                  }
-keepclassmembers class com.hth.utils.AlarmItem{
                                                   private <fields>;
                                                   public <fields>;
                                                   protected <fields>;
                                               }
-keep class com.hth.utils.ScheduleAsyncTask
-keep class com.squareup.okhttp.**
-dontwarn com.squareup.okhttp.**

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}


-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------