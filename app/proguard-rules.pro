# Правила для LoudnessCodecController
-keep class android.media.LoudnessCodecController { void <init>(); }
-keep class android.media.LoudnessCodecController$OnLoudnessCodecUpdateListener { void <init>(); }
-dontwarn android.media.LoudnessCodecController$OnLoudnessCodecUpdateListener
-dontwarn android.media.LoudnessCodecController

# Правила для Google Play Services
-keep public class com.google.vending.licensing.ILicensingService { void <init>(); }
-keep public class com.android.vending.licensing.ILicensingService { void <init>(); }
-keep public class com.google.android.vending.licensing.ILicensingService { void <init>(); }
-keep public class com.google.android.gms.common.internal.ReflectedParcelable { void <init>(); }
-keep,allowshrinking class * implements com.google.android.gms.common.internal.ReflectedParcelable { void <init>(); }

# Правила для аннотаций
-keep @interface android.support.annotation.Keep { void <init>(); }
-keep @androidx.annotation.Keep class * { void <init>(); }
-keep class android.support.annotation.Keep { void <init>(); }

# Дополнительные правила
-keep @interface com.google.android.gms.common.annotation.KeepName { void <init>(); }
-keep,allowshrinking @com.google.android.gms.common.annotation.KeepName class * { void <init>(); }
-keep @interface com.google.android.gms.common.util.DynamiteApi { void <init>(); }
-keep,allowshrinking public class androidx.webkit.WebViewClientCompat { void <init>(); }
-keep class * extends androidx.work.Worker { void <init>(); }
-keep class * extends androidx.work.InputMerger { void <init>(); }
-keep class androidx.work.WorkerParameters { void <init>(); }
-keep !interface * implements androidx.lifecycle.LifecycleObserver { void <init>(); }
-keep,allowshrinking class * extends androidx.startup.Initializer { void <init>(); }
-keep class * implements androidx.versionedparcelable.VersionedParcelable { void <init>(); }
-keep public class androidx.versionedparcelable.ParcelImpl { void <init>(); }
-keep class * extends androidx.room.RoomDatabase { void <init>(); }

# Правила для вашего приложения
-keep class com.yuliitezary.gpt.MainActivity { *; }

# Сохранение аттрибутов
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature

# Правила для WebView
-keep class android.webkit.** { *; }
-keep class * extends android.webkit.WebChromeClient { *; }
-keep class * extends android.webkit.WebViewClient { *; }

# Правила для JavaScript интерфейсов
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Общие правила
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Правила для сохранения конструкторов
-keepclassmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}