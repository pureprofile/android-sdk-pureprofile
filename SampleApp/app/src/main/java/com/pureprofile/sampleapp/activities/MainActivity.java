package com.pureprofile.sampleapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.NetworkResponse;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pureprofile.sampleapp.R;
import com.pureprofile.sampleapp.model.Error;
import com.pureprofile.sampleapp.model.Login;
import com.pureprofile.sampleapp.model.Token;
import com.pureprofile.sampleapp.services.ApiManager;
import com.pureprofile.sampleapp.services.GsonRequest;

import java.io.UnsupportedEncodingException;

import static com.pureprofile.sampleapp.services.AuthService.*;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    /* Enter email or user identifier or both
       replace with your panel key and secret **/

    protected String EMAIL = "REPLACE_WITH_USER_EMAIL";
    protected String USER_KEY = "REPLACE_WITH_USER_KEY";
    protected String PANEL_KEY = "REPLACE_WITH_PANEL_KEY";

    protected String PANEL_SECRET = "REPLACE_WITH_PANEL_SECRET";

    private Button oldSdkButton;
    private Button newSdkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        oldSdkButton = findViewById(R.id.old_sdk_button);
        newSdkButton = findViewById(R.id.new_sdk_button);
        setSupportActionBar(toolbar);

        loginRequest();

        oldSdkButton.setOnClickListener(v -> {
            if (Token.getToken(this) != null) {
                startSdk();
            }
        });

        newSdkButton.setOnClickListener(v -> {
            if (Token.getToken(this) != null) {
                startNewSdk();
            }
        });
    }

    private void createToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Make a POST login request and return a Token to provide to the SDK
     * so it can communicate with Pureprofile's servers.
     *
     * This Token has to be obtained for every user in a secure, server to server communication.
     * We recommend this service to be called from server after the authenticity of client
     * has been verified. Calling this service from client application would be insecure as
     * secret key would be compromised and attacker could potentially use this key to obtain
     * ppToken for any number of users.
     *
     * panelKey - key which belongs to the panel you're trying to login user for
     * panelSecret - secret key assigned to panel (never reveal this to client app)
     * userKey - unique identifier that does never change for a user
     * email - optional email of the user
     */
    private void loginRequest() {
        JsonObject params = new JsonObject();
        params.addProperty("panelKey", PANEL_KEY);
        params.addProperty("panelSecret", PANEL_SECRET);
        params.addProperty("email", EMAIL);
        params.addProperty("acceptedTermsConditions", true);

        GsonRequest<Login> request = new GsonRequest<>(
                Request.Method.POST, LOGIN_SERVICE, Login.class, null, params,
                response -> {
                    String token = response.ppToken;
                    if (token != null) {
                        Token.setToken(this, token);
                        oldSdkButton.setEnabled(true);
                        newSdkButton.setEnabled(true);
                    }
                },
                (error -> {
                    NetworkResponse response = error.networkResponse;
                    if (response != null && error.networkResponse.statusCode == 403) {
                        try {
                            String json = new String(error.networkResponse.data, getString(R.string.charset));
                            Gson gson = new Gson();
                            Error r = gson.fromJson(json, Error.class);
                            if (r.data.code.equalsIgnoreCase("panel_membership_limit_reached")) {
                                createToast(r.message);
                            } else {
                                createToast(getResources().getString(R.string.generic_error));
                            }
                        } catch (UnsupportedEncodingException e) {
                            createToast(getResources().getString(R.string.generic_error));
                        }
                    } else {
                        createToast(getResources().getString(R.string.generic_error));
                    }
                }));

        ApiManager.getInstance(this).addToRequestQueue(request, "login request");
    }

    private void startSdk() {
        try {
            Intent intent = new Intent(MainActivity.this, SDKActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Timber.tag("Launch error").d(e);
        }
    }

    private void startNewSdk() {
        try {
            Intent intent = new Intent(MainActivity.this, JetSdkActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Timber.tag("Launch error").d(e);
        }
    }
}
