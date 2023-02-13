package com.pureprofile.sampleapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.pureprofile.sdk.SdkApp;

import java.io.UnsupportedEncodingException;

import static com.pureprofile.sampleapp.services.AuthService.*;

public class MainActivity extends AppCompatActivity {

    protected String USER_KEY = "b2852072-c0b6-4ebe-8e99-72875e4cf610";
    protected String PANEL_SECRET = "d0b2981c-3dfb-4855-8523-d94caad8da28";
    protected String PANEL_KEY = "e598da88-6749-4394-a2cd-662be94e9bec";

    private TextView mBadgeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        mBadgeText = findViewById(R.id.badge_text);
        loginRequest();
        fab.setOnClickListener(v -> {
            createToast(this.getString(R.string.panel_login));
            if (Token.getToken(this) != null) {
                startSdk();
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
        params.addProperty("userKey", USER_KEY);

        GsonRequest<Login> request = new GsonRequest<>(
                Request.Method.POST, LOGIN_SERVICE, Login.class, null, params,
                response -> {
                    String token = response.ppToken;
                    if (token != null) {
                        Token.setToken(this, token);
                        SdkApp.getInstance().init(this, Token.getToken(this));
                        SdkApp.getInstance().setEnv(this, "dev");
                        SdkApp.getInstance().getBadgeValues(this, badge -> {
                            mBadgeText.setText(String.valueOf(badge.total));
                            mBadgeText.setVisibility(View.VISIBLE);
                        });
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
            Log.d("Launch error", e.getMessage());
        }
    }
}
