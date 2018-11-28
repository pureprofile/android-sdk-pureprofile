## Overview
Pureprofile is a survey platform that delivers surveys instead of ads through mobile apps. The Pureprofile iOS SDK is an easy to use library for developers who want to integrate Purerprofile's surveying platform into their Android apps.

### Requirements
Minimum sdk version is 16.
```
minSdkVersion 16
```

# android-sdk-pureprofile

#### Quick guide of Android SDK Pureprofile
1. Register as a Pureprofile partner, create a panel and copy panel key
2. Download Pureprofile SDK aar file and import to your project
3. Import Pureprofile SDK dependencies
4. Import Pureprofile SDK classes
5. Add permissions to AndroidManifest.xml
6. Call Pureprofile SDK initialization function in onCreate() of your Activity to activate SDK
7. Implement **PaymentListener** in your sdk activity to process payments received from surveys.

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
    implementation 'com.android.support:design:28.0.0'
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
import com.pureprofile.sdk.events.PaymentEvent;
import com.pureprofile.sdk.ui.helpers.SdkActivity;
import com.pureprofile.sdk.ui.listeners.PaymentListener;
```

#### 5. Add permissions to AndroidManifest.xml
You should also add the following lines in your AndroidManifest.xml
```
<uses-permission android:name="android.permission.INTERNET"/>
```
Pureprofile uses these permissions to get and send survey requests and responses to Pureprofile.

#### 6. Call Pureprofile SDK initialization function in onCreate() of your Activity to activate SDK
After you link your project to all dependencies you can easily initialize the SDK. Your activity must extend the **com.pureprofile.sdk.ui.helpers.SdkActivity**. Once you added all dependencies then you can call Pureprofile SDK init() in onCreate() ( just after super.onCreate(savedInstanceState) ) passing the authentication token you received from the login process and you are ready to go. To start the sdk just call run() passing the activity context. If you require to test the sdk in test mode simply set the test environment by calling setTestEnv() after you init the sdk. Below is a sample:
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SdkApp.getInstance().init(this, Token.getToken(this));
        SdkApp.getInstance().registerPaymentListener(this);
        SdkApp.getInstance().setTestEnv(this, !Cache.getStoredEnvKey(this));
        SdkApp.getInstance().run(this);
    }
```

#### 7. Implement PaymentListener in your sdk activity to process payments received from surveys
Register your activity to implement the **PaymentListener** and listen for payment events. Simply call **registerPaymentListener()** and override **onProcessPayment()** that returns a com.pureprofile.sdk.events.PaymentEvent object with the payment details (date of payment, payment unique key, payment). Below is a sample:
```
    public class PaymentEvent {
        public String date;
        public String key;
        public Float payment;
    }
    
    @Override
    public void onProcessPayment(PaymentEvent event) {
        // Get payment event and process your payment
        Toast.makeText(this, "Added payment: " + String.valueOf(event.payment),
                Toast.LENGTH_SHORT).show();
    }
```
The transactions API can also be used for quering Pureprofile about a transaction. The payment key (as provided in the PaymentEvent will have to be passed as a parameter to the end-point:
```
GET https://staging-ah-api.pureprofile.com/api/v1/pp-au/transactions/<payment-uuid> HTTP/1.1

HTTP/1.1 200 OK

{
    "status": "ok",
    "data": {
        "uuid": "payment-uuid",
        "value": 1.05,
        "createdAt": "2018-11-14T23:33:36+11:00",
        "campaignUuid": "campaign-uuid"
    },
    "ppToken": "pureprofile-token"
}
```
