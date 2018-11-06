package com.pureprofile.sampleapp.activities;

import android.os.Bundle;

import com.pureprofile.sdk.SdkApp;
import com.pureprofile.sampleapp.model.Token;
import com.pureprofile.sdk.ui.helpers.SdkActivity;

public class SDKActivity extends SdkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = SdkApp.init(this, Token.getToken(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mInstance.destroy();
    }
}
