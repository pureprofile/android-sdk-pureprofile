package com.pureprofile.sampleapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.pureprofile.sampleapp.R;
import com.pureprofile.sdk.SdkApp;
import com.pureprofile.sampleapp.model.Token;
import com.pureprofile.sdk.events.PaymentEvent;
import com.pureprofile.sdk.ui.helpers.SdkActivity;
import com.pureprofile.sdk.ui.listeners.PaymentListener;

public class SDKActivity extends SdkActivity implements PaymentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SdkApp.getInstance().run(this, Token.getToken(this));
        SdkApp.getInstance().registerPaymentListener(this);
    }

    @Override
    public void onProcessPayment(PaymentEvent event) {
        // Handle payments received from Pureprofile paid surveys.
        Toast.makeText(this, String.format(getResources().getString(
                R.string.payment_info), event.key, event.payment, event.date),
                Toast.LENGTH_SHORT)
                .show();
    }
}
