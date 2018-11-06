# android-sdk-pureprofile

#### Quick guide of Android SDK Pureprofile
1. Register as a Pureprofile partner, create a panel and copy panel key
2. Download Pureprofile SDK aar file and import to your project
3. Import Pureprofile SDK dependencies
4. Import Pureprofile SDK classes
5. Add permissions to AndroidManifest.xml
6. Call Pureprofile SDK initialization function in onCreate() of your Activity to activate SDK

> Requirements: Pureprofile Android SDK works with Android 16 (4.1) and above.

## Steps detail

#### 1. Register as a Pureprofile partner, create a panel and copy panel key
[Contact Pureprofile](https://www.pureprofile.com). Create a new panel with your account manager and copy then the given **panel key** for this app in order to use later on, when initializing **Pureprofile SDK** within your code.

#### 2. Download Pureprofile SDK aar file and import to your project
Download Pureprofile Android SDK aar or reference it through maven().
#### Download Pureprofile SDK .aar file
Add Pureprofile SDK .aar file to your project libraries

If you are using Android Studio, right click on your project add select New Module. Then select Import .JAR or .AAR Package option and from the file browser locate Pureprofile aar file. Right click again on your project and in Module Dependencies tab choose to add Pureprofile module that you recently added, as a dependency.

#### Retrieve Pureprofile Android SDK through maven()
Add maven() repository to your app build.gradle (top level one):

```
allprojects {
    repositories {
        maven { url "http://localhost:8081/artifactory/libs-release-local" }
    }
}
```

Retrieve Pureprofile through maven() by adding the following line in your project build.gradle (not the top level one, the one under 'app') in dependencies section:

```
dependencies {
  implementation 'com.pureprofile.sdk:droid-sdk:1.0.21'
}
```

#### 3. Import Pureprofile SDK dependencies
If you are adding the library by downloading .aar file then you need to add the folowing dependencies to your project:
If you are using gradle you can easily add in your dependencies:
```
dependencies {
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation 'com.android.volley:volley:1.1.0'
    implementation 'com.google.code.gson:gson:2.8.2'
    implementation 'com.facebook.fresco:fresco:1.11.0'
    implementation 'com.facebook.fresco:animated-gif:1.11.0'
}
```
Also you'll need to add YouTube libfrary to your **libs** folder of your current project. 
1. Download YouTube library from [Google](https://developers.google.com/youtube/android/player/downloads/)
2. Paste it in libs folder inside app folder of project

#### 4. Import Pureprofile SDK classes
Import Pureprofile classes with the following lines at the top of your Activity’s class file:
```
import com.pureprofile.sdk.SdkApp;
import com.pureprofile.sdk.ui.helpers.SdkActivity;
```

#### 5. Add permissions to AndroidManifest.xml
You should also add the following lines in your AndroidManifest.xml
```
<uses-permission android:name="android.permission.INTERNET"/>
```
Pureprofile uses these permissions to get and send survey requests and responses to Pureprofile.

#### 6. Call Pureprofile SDK initialization function in onCreate() of your Activity to activate SDK
After you link your project to all dependencies you can easily initialize the SDK. Once you added all dependencies then you can call Pureprofile SDK init() in onCreate() ( just after super.onCreate(savedInstanceState) ) passing the authentication token you received from the login process and you are ready to go.
Below is a sample:
```
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = SdkApp.init(this, "token");
    }
```
After initializing the SDK you have to destroy the instance ( mInstance ) in onDestroy() ( just after super.onDestroy() ). 
Below is a sample:
```
@Override
    protected void onDestroy() {
        super.onDestroy();

        mInstance.destroy();
    }
```
