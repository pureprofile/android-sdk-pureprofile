package com.pureprofile.sampleapp.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.pureprofile.jet.sdk.PureprofileClient
import com.pureprofile.sampleapp.model.Token
import timber.log.Timber

/**
 * Created by George Sylaios on 23/2/23.
 */
class JetSdkActivity : ComponentActivity() {


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
}