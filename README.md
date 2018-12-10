## Overview
Pureprofile is a survey platform that delivers surveys through the web and mobile apps. The Pureprofile Android SDK is an easy to use library for developers who want to integrate Purerprofile's surveying platform into their Android apps.

### Requirements
Minimum sdk version is 16.
```
minSdkVersion 16
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

#### 3. Pureprofile SDK dependencies
The sdk uses the following dependencies:
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
Also you'll need to add YouTube library to your ``libs`` folder of your current project. 
1. Download YouTube library from [Google](https://developers.google.com/youtube/android/player/downloads/)
2. Paste it in libs folder inside app folder of your project

#### 4. Import Pureprofile SDK classes
Import Pureprofile classes with the following lines at the top of your Activity’s class file:
```
import com.pureprofile.sdk.SdkApp;
import com.pureprofile.sdk.events.PaymentEvent;
import com.pureprofile.sdk.ui.helpers.SdkActivity;
import com.pureprofile.sdk.ui.listeners.PaymentListener;
```

#### 5. Add permissions to AndroidManifest.xml
Add the following permissions in your AndroidManifest.xml.
```
<uses-permission android:name="android.permission.INTERNET"/>
```
Pureprofile uses these permissions to receive and send survey requests and responses to Pureprofile.

#### 6. Use Login API and obtain a login token to initialise the SDK
The first step before accessing the Pureprofile SDK is to obtain a login token from Pureprofile. You can do that by calling Pureprofile's login API where you have to pass the following parameters in the POST call:

Production service:

```
POST https://pp-auth-api.pureprofile.com/api/v1/panel/login
```

Service accepts and returns a JSON body, as specified:

| Property name | Type          | Mandatory | Description 
|---------------|---------------|:---------:|-------------
| panelKey      | String(UUID)  | Yes       | key which belongs to the panel you're trying to login user for
| panelSecret   | String(UUID)  | Yes       | secret key assigned to panel (never reveal this to client app)
| userKey       | String        | Yes       | unique identifier that does never change for a user
| email         | String(Email) |           | email that can be used to match user

Response body:

| Property name | Type          | Description 
|---------------|---------------|-------------
| ppToken       | String(UUID)  | Token provided to SDK so it can communicate with Pureprofile's servers


The values of panelKey and panelSecret are provided by Pureprofile and are used to identify you as Pureprofile's partner. [Get in touch with us](mailto:product@pureprofile.com) to find out how to obtain the panel keys. 

The ```userKey``` is used for uniquely identifying each one of your users. It is recommended that a UUID is used as ```userKey``` value and that this UUID never changes so that we can always identify your users in our systems in order to offer them better targeted surveys with maximum yield. There is no restriction though as to the type of user key that is used which means that your user's email or phone number or any other identifier is also accepted. Beware though that in this case if the user identifier ever changes, for example when your user changes his/her email, the next time the user with the changed identifier is logged in to Pureprofile, a new Pureprofile user will be created which means that all targeting information we hold for the said user will no longer be usable and will have to be recreated. The ```email``` key is optional and can be used to match a ```userKey``` with an email.

For testing and evaluation purposes Pureprofile provides a public partner account which can be used for running the sample app or for integrating the SDK with your app for evaluation purposes. A full example of how to log in a user, as well as the public partner keys can be found in the source code of the sample app. Bare in mind though that storing sensitive data (such as the panel key and secret) in the source files is not considered good practice and we therefore strongly suggest to employ a secure, server to server communication for obtaining the ppToken from Pureprofile. In this case the login service is called from your server after the authenticity of the client has been verified. See the diagram below for a depiction on how to login via an intermediate secure service.

![alt text](https://devtools.pureprofile.com/surveys/ios/assets/server2server_login.png)

#### 7. Call Pureprofile SDK initialisation functions in onCreate() of your Activity to activate SDK
After you link your project to all dependencies you can easily initialise the SDK. Your activity must extend the ```com.pureprofile.sdk.ui.helpers.SdkActivity```. Once you added all dependencies then you can call Pureprofile SDK init() in onCreate() ( just after super.onCreate(savedInstanceState) ) passing the authentication token you received from the login process and you are ready to go. To start the sdk just call run() passing the activity context. If you require to test the sdk in test mode simply set the test environment by calling setTestEnv() after you init the sdk. Below is a sample:
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SdkApp.getInstance().init(this, Token.getToken(this));
        SdkApp.getInstance().registerPaymentListener(this);
        SdkApp.getInstance().setTestEnv(this, false);
        SdkApp.getInstance().run(this);
    }
```

#### 8. Implement PaymentListener in your sdk activity to process payments received from surveys
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
