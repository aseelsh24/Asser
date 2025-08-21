# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to the default ProGuard rules.
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If you use reflection, otherwise this isn't needed.
#-keepattributes Signature

# For using GSON @Expose annotation
#-keepattributes *Annotation*

# For using GSON @SerializedName annotation
#-keep class com.google.gson.annotations.SerializedName
#-keep class com.google.gson.annotations.Expose

# Hilt
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowshrinking class * {
    @javax.inject.Inject <init>(...);
    @javax.inject.Inject <fields>;
}
-keep class * extends androidx.lifecycle.ViewModel
-keep class * extends android.app.Application
-keep class * implements androidx.viewbinding.ViewBinding
-keep @com.google.dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.Provides @interface *
-keep @javax.inject.Inject @interface *
-keep @javax.inject.Singleton @interface *
-keep @dagger.hilt.components.SingletonComponent @interface *
-keep @dagger.hilt.android.components.ActivityComponent @interface *
-keep @dagger.hilt.android.components.FragmentComponent @interface *
-keep @dagger.hilt.android.components.ViewModelComponent @interface *
-keep @dagger.hilt.android.scopes.ViewModelScoped @interface *
