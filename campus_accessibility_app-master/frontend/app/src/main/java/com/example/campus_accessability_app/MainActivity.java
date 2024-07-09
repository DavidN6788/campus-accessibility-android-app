package com.example.campus_accessability_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    public BackendAPI backendapi;
    private static Context appContext;

    private final boolean AUTO_LOGON = true; // can be set to false for demo/testing purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = this;
        backendapi = new BackendAPI(getApplicationContext());

        if (AUTO_LOGON) {
            attemptAutoLogon(); // if user has logged in before
        }

        // Create the opening page
        setContentView(R.layout.activity_main);
        Button signUp = findViewById(R.id.buttonOne);
        signUpButtonActivity(signUp);
        Button logIn = findViewById(R.id.button);
        loginButtonActivity(logIn);

        // LEAVE FOR: TESTING PURELY THE MAP
//        setContentView(R.layout.activity_maps); // MAPS CLASS
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.test_map);
//        assert mapFragment != null;
//        mapFragment.getMapAsync(new MapsActivity());

        // LEAVE FOR: TESTING (JUMPING STRAIGHT TO) HOME SCREEN - AFTER LOGIN
//        setContentView(R.layout.activity_home);
//        Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
//        startActivity(homeActivityIntent);
    }

    private void loginSuccess() {
        backendapi.updateBuildingInformation();
        backendapi.fetchReports();
        // Load Up Home screen
        Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeActivityIntent);
        finish(); // kill activity instance
    }

    private void attemptAutoLogon() {
        backendapi.isLoggedIn(new Callback() {
            // TODO: Handle errors better
            @Override
            public void onFailure(Call call, IOException e) { }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.getBoolean("success")) {
                        loginSuccess();
                        return;
                    }
                } catch (Exception e) {
                    // TODO: Handle errors better
                    Log.e("myapp", Log.getStackTraceString(e));
                }
            }
        });
    }

    protected static Context getContext() {
        return appContext;
    }

    /**
     * This functions takes the user to a sign up activity via the sign up button
     * @param signUp the sign in button in Main Activity
     */
    private void signUpButtonActivity(Button signUp){
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(activity2Intent);
            }
        });
    }

    /**
     * This function logs in the username and passwords via the log in button
     * then loads up the map once they are inputted.
     * @param logIn The log In button in Main Activity
     */
    // TODO: Fix extracting text bug
    private void loginButtonActivity(Button logIn){
        EditText userNameEditText = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText passwordEditText = (EditText) findViewById(R.id.editTextTextPassword);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameEditText.getText().toString(); // Holds what is inside the username text field
                String password = passwordEditText.getText().toString(); // Holds what is inside the password text field
                // When username or password is not inputted
                if (userName.matches("") || password.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please fill in username or password", Toast.LENGTH_LONG).show();
                } else {
                    backendapi.login(userName, password, new Callback() {
                        // TODO: Handle errors better
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.e("myapp", Log.getStackTraceString(e));
                                    Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            try {
                                JSONObject result = new JSONObject(response.body().string());
                                if (result.getBoolean("success")) {
                                    backendapi.saveAndSetToken(result.getString("token"));
                                    loginSuccess();
                                    return;
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                Toast.makeText(getApplicationContext(), result.getString("error"), Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                // TODO: Handle errors better
                                                Log.e("myapp", Log.getStackTraceString(e));
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                // TODO: Handle errors better
                                Log.e("myapp", Log.getStackTraceString(e));
                            }
                        }
                    });
                }
            }
        });
    }
}