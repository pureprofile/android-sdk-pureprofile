package com.pureprofile.sampleapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pureprofile.sampleapp.R;
import com.pureprofile.sdk.SdkApp;
import com.pureprofile.sampleapp.model.Token;
import com.pureprofile.sdk.events.PaymentEvent;
import com.pureprofile.sdk.ui.listeners.PaymentListener;

public class SDKActivity extends AppCompatActivity implements PaymentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SdkApp.getInstance().init(this, Token.getToken(this));
        SdkApp.getInstance().registerPaymentListener(this);
        SdkApp.getInstance().setTestEnv(this, false);
        SdkApp.getInstance().hasNavigation(false);
        SdkApp.getInstance().run(this, true);
    }

    @Override
    public void onProcessPayment(PaymentEvent event) {
        // Handle payments received from Pureprofile paid surveys.
        Log.i("payment", String.format(
                getResources().getString(
                        R.string.payment_info), event.key, event.payment, event.date));
    }
}
