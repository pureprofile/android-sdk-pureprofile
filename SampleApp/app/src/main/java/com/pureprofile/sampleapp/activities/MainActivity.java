package com.pureprofile.sampleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.JsonObject;
import com.pureprofile.sampleapp.R;
import com.pureprofile.sampleapp.model.Login;
import com.pureprofile.sampleapp.model.Token;
import com.pureprofile.sampleapp.services.ApiManager;
import com.pureprofile.sampleapp.services.GsonRequest;

import static com.pureprofile.sampleapp.services.AuthService.*;

public class MainActivity extends AppCompatActivity {

    protected String USER_EMAIL = "sdk_user@pureprofile.com";
    protected String USER_KEY = "1acf5f79-1b82-456f-aa85-b1e7c58fca88";
    protected String PANEL_SECRET = "c0e6b322-f654-4583-8202-3136504e7843";
    protected String PANEL_KEY = "f986e3ac-32c9-42e9-944d-9801dbe28d97";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            createToast(this.getString(R.string.panel_login));
            loginRequest();
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
        params.addProperty("email", USER_EMAIL);

        GsonRequest<Login> request = new GsonRequest<>(
                Request.Method.POST, LOGIN_SERVICE, Login.class, null, params,
                response -> {
                    String token = response.ppToken;
                    if (token != null) {
                        Token.setToken(this, token);
                        startSdk();
                    }
                },
                (error -> Log.e("error", error.toString())));

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
