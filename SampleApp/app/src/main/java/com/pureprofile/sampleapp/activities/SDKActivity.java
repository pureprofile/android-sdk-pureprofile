package com.pureprofile.sampleapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.pureprofile.sampleapp.R;
import com.pureprofile.sdk.SdkApp;
import com.pureprofile.sampleapp.model.Token;
import com.pureprofile.sdk.ui.helpers.SdkActivity;
import com.pureprofile.sdk.ui.listeners.PaymentListener;

public class SDKActivity extends SdkActivity implements PaymentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = SdkApp.init(this, Token.getToken(this));
        mInstance.registerPaymentListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mInstance.destroy();
    }

    @Override
    public void onProcessPayment(String date, String key, Float payment) {
//        Handle payments received from Pureprofile paid surveys.
        Toast.makeText(this, String.format(getResources().getString(
                R.string.payment_info), key, payment, date), Toast.LENGTH_SHORT)
                .show();
    }
}
