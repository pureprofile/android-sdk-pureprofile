package com.pureprofile.sampleapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

    protected String PANEL_USER_EMAIL = "gs+49@freee.gr";
    protected String PANEL_USER_KEY = "1acf5f79-1b82-456f-aa85-b1e7c58fca88";
    protected String PANEL_SECRET = "d0b2981c-3dfb-4855-8523-d94caad8da28";
    protected String PANEL_KEY = "e598da88-6749-4394-a2cd-662be94e9bec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            createToast(this.getString(R.string.panel_login));
            loginRequest(this, PANEL_KEY);
        });
    }

    private void createToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Login to sdk using demo user.
     */
    private void loginRequest(Context context, String panelKey) {
        JsonObject params = new JsonObject();
        params.addProperty("panelKey", panelKey);
        params.addProperty("panelSecret", PANEL_SECRET);
        params.addProperty("userKey", PANEL_USER_KEY);
        params.addProperty("email", PANEL_USER_EMAIL);
        String url = SERVICE_ENDPOINT + LOGIN;

        GsonRequest<Login> request = new GsonRequest<>(Request.Method.POST, url, Login.class, null, params,
                response -> {
                    String token = response.ppToken;
                    if (token != null) {
                        Token.setToken(context, token);
                        startSdk();
                    }
                },
                (error -> Log.e("error", error.toString())));

        ApiManager.getInstance(context).addToRequestQueue(request, "login request");
    }

    private void startSdk() {
        try {
            Intent intent = new Intent(MainActivity.this, SDKActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.d("splash error", e.getMessage());
        }
    }
}
