## Overview
Pureprofile is a survey platform that delivers surveys through the web and mobile apps. The Pureprofile Android SDK is an easy to use library for developers who want to integrate Purerprofile's surveying platform into their Android apps.

### Requirements
Minimum sdk version is 23.
```
minSdkVersion 23
```

# android-sdk-pureprofile

#### Quick guide of Android SDK Pureprofile
1. Register as a Pureprofile partner, create a panel and copy panel key
2. Download Pureprofile SDK aar file and import to your project
3. Pureprofile SDK dependencies
4. Import Pureprofile SDK classes
5. Add permissions to AndroidManifest.xml
6. Use Login API to obtain a login token to initialise the SDK
7. Call Pureprofile SDK initialisation function in onCreate() of your Activity to activate SDK
8. Implement ```PaymentListener``` in your sdk activity to process payments received from surveys.

> Requirements: Pureprofile Android SDK works with Android 16 (4.1) and above.

## Steps detail

#### 1. Register as a Pureprofile partner, create a panel and copy panel key
[Contact Pureprofile](mailto:product@pureprofile.com). Create a new panel with your account manager and copy the ```panelKey``` in order to use later on, when initialising Pureprofile SDK within your code.

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
        maven { url "https://android-sdk.pureprofile.com/artifactory/libs-release-local" }
    }
}
```

Retrieve Pureprofile through maven() by adding the following line in your project build.gradle (not the top level one, the one under 'app') in dependencies section:

```
dependencies {
  implementation 'com.pureprofile.sdk:droid-sdk:2.0.30'
}
```

#### Proguard rules
You need to add rules proguard-rules.pro when generating apk when ``minifyEnabled true``.
```
# Pureprofile sdk classes
-dontwarn com.pureprofile.sdk.**
-keep class com.pureprofile.sdk.** { *; }

##-------- Begin: proguard configuration if using Gson for your application classes when logging to Pureprofile ----------
# Your application classes that will be serialized/deserialized over Gson for sdk login
-keep class com.pureprofile.sampleapp.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
```

#### 3. Pureprofile SDK dependencies
The sdk uses the following dependencies:
```
dependencies {
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.recyclerview:recyclerview:1.0.0'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'com.google.android.gms:play-services-ads-identifier:17.0.0'
    implementation 'com.android.volley:volley:1.1.1'
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'com.facebook.fresco:fresco:1.11.0'
    implementation 'com.facebook.fresco:animated-gif:1.11.0'
}
```
Also you'll need to add YouTube library to your ``libs`` folder of your current project. 
1. Download YouTube library from [Google](https://developers.google.com/youtube/android/player/downloads/)
2. Paste it in libs folder inside app folder of your project

#### 4. Import Pureprofile SDK classes
Import Pureprofile classes with the following lines at the top of your Activity’s class file:
```
import com.pureprofile.sdk.SdkApp;
import com.pureprofile.sdk.events.PaymentEvent;
import com.pureprofile.sdk.ui.listeners.PaymentListener;
```

#### 5. Add permissions to AndroidManifest.xml
Add the following permissions in your AndroidManifest.xml.
```
<uses-permission android:name="android.permission.INTERNET"/>
```
Pureprofile uses these permissions to receive and send survey requests and responses to Pureprofile.

#### 6. Add landscape mode support in AndroidManifest.xml
Add the following to the activity used for the sdk in your AndroidManifest.xml.
```
<activity android:name=".activities.SDKActivity"
    android:configChanges="orientation|screenSize"/>
```
This will allow landscape mode for surveys that require it.

#### 7. Use Login API and obtain a login token to initialise the SDK
The first step before accessing the Pureprofile SDK is to obtain a login token from Pureprofile. You can do that by calling Pureprofile's [login API](https://pp-auth-api.pureprofile.com/api-docs/#/panel/post_api_v1_panel_login) where you have to pass the following parameters in the POST call:

| Property name | Type          | Mandatory | Description 
|---------------|---------------|:---------:|-------------
| panelKey      | String(UUID)  | Yes       | key which belongs to the panel you're trying to login user for
| panelSecret   | String(UUID)  | Yes       | secret key assigned to panel (never reveal this to client app)
| userKey       | String        | Yes       | unique identifier that does never change for a user
| email or emailHash | String | Yes       | user's email or hashed email

Response body:

| Property name | Type          | Description 
|---------------|---------------|-------------
| ppToken       | String(UUID)  | Token provided to SDK so it can communicate with Pureprofile's servers


The values of `panelKey` and `panelSecret` are provided by Pureprofile and are used to identify you as Pureprofile's partner. [Get in touch with us](mailto:product@pureprofile.com) to find out how to obtain the panel keys. 

The ```userKey``` is used for uniquely identifying each one of your users. It is recommended that a UUID is used as ```userKey``` value and that this UUID never changes so that we can always identify your users in our systems in order to offer them better targeted surveys with maximum yield. There is no restriction though as to the type of user key that is used which means that your user's email or phone number or any other identifier is also accepted. Beware though that in this case if the user identifier ever changes, for example when your user changes his/her email, the next time the user with the changed identifier is logged in to Pureprofile, a new Pureprofile user will be created which means that all targeting information we hold for the said user will no longer be usable and will have to be recreated. The ```email``` key can be used to match a ```userKey``` with an email.

For testing and evaluation purposes Pureprofile provides a public partner account which can be used for running the sample app or for integrating the SDK with your app for evaluation purposes. A full example of how to log in a user, as well as the public partner keys can be found in the source code of the sample app. Bare in mind though that storing sensitive data (such as the panel key and secret) in the source files is not considered good practice and we therefore strongly suggest to employ a secure, server to server communication for obtaining the ppToken from Pureprofile. In this case the login service is called from your server after the authenticity of the client has been verified. See the diagram below for a depiction on how to login via an intermediate secure service.

![alt text](https://devtools.pureprofile.com/surveys/ios/assets/server2server_login.png)

#### Membership limit reached
As part of the login process it is possible to encounter the 'membership limit reached' error case which is triggered when the number of your users that have already used at least once the SDK has reached the limit that Pureprofile can accept at the time. The error case is signified with HTTP error code 403 and the body of the response contains error code panel_membership_limit_reached as it can be seen in the example below. An example of how to handle the error in your application can be found in the source code of the sample app. The membership limit is configurable and when you get in touch with Pureprofile you can discuss and set it according to your membership requirements.
```
 {
  "statusCode": 403,
  "error": "Forbidden",
  "message": "We're unable to register you for the panel at this time as the limit of panel members has been reached.",
  "data": {
    "code": "panel_membership_limit_reached"
  }
}
```

#### 8. Call Pureprofile SDK initialisation functions in onCreate() of your Activity to activate SDK
After you link your project to all dependencies you can easily initialise the SDK. Once you added all dependencies then you can call Pureprofile SDK init() in onCreate() ( just after super.onCreate(savedInstanceState) ) passing the authentication token you received from the login process and you are ready to go. To start the sdk just call run() passing the activity context and a flag true if you want to display the SDK splash screen. If you require to test the sdk in development mode simply set the environment by calling setEnv(this, "dev") after you init the sdk. For production set the env to "prod" Below is a sample:
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SdkApp.getInstance().init(this, Token.getToken(this));
        SdkApp.getInstance().registerPaymentListener(this);
        SdkApp.getInstance().setEnv(this, "prod");
        SdkApp.getInstance().run(this, true);
    }
```

#### 9. Implement PaymentListener in your sdk activity to process payments received from surveys
Register your activity to implement the ```PaymentListener``` and listen for payment events. Simply call ```registerPaymentListener()``` and override ```onProcessPayment()``` that returns a com.pureprofile.sdk.events.PaymentEvent object with the payment details (date of payment, payment unique key, payment). Below is a sample:
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
The transactions API can also be used for querying Pureprofile about a transaction. The payment key (as provided in the PaymentEvent will have to be passed as a parameter to the end-point:
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

#### 10. Count of available/paid surveys
In order to obtain the total number of available surveys and total number of paid surveys you can use the following API call after you initialize the SDK:
```
SdkApp.getInstance().init(this, Token.getToken(this));
SdkApp.getInstance().getBadgeValues(this, new BadgeListener() {
    @Override
    public void onSuccess(Badge badge) {
        int total = badge.total;
        int paid = badge.paid;
    }
});
```
Through the callback you will receive two integers, one for all available surveys and one for the count of paid surveys.

## SDK (Jetpack Compose)
A new SDK in Jetpack Compose is now available. This is a beta release of the survey platform.

### Requirements
Minimum sdk version is 23.
```
minSdkVersion 23
```

### Set up Compose
To start using the Compose SDK you need to add some build configurations to your project. Add the following definition 
to your app's ```build.gradle``` file:

```
android {
    buildFeatures {
        compose true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
}
```

```
allprojects {
    repositories {
        maven { url "https://android-sdk.pureprofile.com/artifactory/libs-release-local-jet" }
    }
}
```

Retrieve Pureprofile Compose SDK through maven() by adding the following line in your project build.gradle:

```
dependencies {
  implementation 'com.pureprofile.jet.sdk:jet-sdk:1.0.54'
}
```

### Add permissions to AndroidManifest.xml
Add the following permissions in your AndroidManifest.xml.
```
<uses-permission android:name="android.permission.INTERNET"/>
```
Pureprofile uses these permissions to receive and send survey requests and responses to Pureprofile.

### Add landscape mode support in AndroidManifest.xml
Add the following to the activity used for the sdk in your AndroidManifest.xml.
```
<activity android:name=".activities.yourActivity"
    android:configChanges="orientation|screenSize"/>
```
This will allow landscape mode for surveys that require it.

### Import Compose Pureprofile SDK class
Import Pureprofile client class adding the following line at the top of your Activity’s class file:
```
import com.pureprofile.jet.sdk.PureprofileClient
```

### Call Pureprofile Compose SDK initialisation functions in onCreate() of your Activity to start SDK
Below is a sample to start the SDK. The ```onPayment``` event is fired when a successful payment is 
received returning a PaymentEvent with the payment date, payment key and amount.
```
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContent {
            val token = Token.getToken(this)
            token?.let {
                PureprofileClient(
                    token = it,
                    onPayment = { event ->
                        Timber.d("Payment received: ${event.payment}")
                    }
                )
            }
        }
    }
```







