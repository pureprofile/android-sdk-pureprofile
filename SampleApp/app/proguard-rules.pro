# Pureprofile sdk classes
-dontwarn com.pureprofile.sdk.**
-keep class com.pureprofile.sdk.** { *; }

# Your application classes that will be serialized/deserialized over Gson for sdk login
-keep class com.pureprofile.sampleapp.model.** { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*