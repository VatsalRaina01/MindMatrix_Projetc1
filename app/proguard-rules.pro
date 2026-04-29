# ================================================
# Shishu-Sneh ProGuard / R8 Rules
# ================================================

# === General Android ===
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions

# === Kotlin ===
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Lazy { public protected *; }

# === Jetpack Compose ===
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# === Hilt / Dagger ===
-dontwarn dagger.**
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keepclassmembers,allowobfuscation class * {
    @dagger.* <methods>;
    @javax.inject.* <methods>;
    @dagger.hilt.* <methods>;
}
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# === Room (SQLite) ===
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-dontwarn androidx.room.paging.**

# === Retrofit ===
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# === OkHttp ===
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# === Moshi ===
-dontwarn com.squareup.moshi.**
-keep class com.squareup.moshi.** { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
# Keep generated Moshi adapters
-keep class **JsonAdapter { *; }
-keepnames @com.squareup.moshi.JsonClass class *

# === DataStore ===
-keep class androidx.datastore.** { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite { *; }

# === WorkManager ===
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.ListenableWorker { *; }

# === Domain Models (keep for serialization/Room) ===
-keep class com.shishusneh.domain.model.** { *; }
-keep class com.shishusneh.data.local.entity.** { *; }
-keep class com.shishusneh.data.remote.dto.** { *; }

# === Coroutines ===
-dontwarn kotlinx.coroutines.**
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# === Enum safety ===
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# === Parcelable ===
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# === R8 full mode compatibility ===
-dontwarn java.lang.invoke.StringConcatFactory
