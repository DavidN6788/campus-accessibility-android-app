package com.example.campus_accessability_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    public BackendAPI backendapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Button creation
        Button confirmButton = findViewById(R.id.button2);
        confirmButtonActivity(confirmButton);

        backendapi = new BackendAPI(getApplicationContext());
    }

    /**
     * This method confirms that the users has successfully signed up
     * with all fields filled out
     * @param signUp the confirm button in the sign up activity
     */
    // TODO: Fix extracting text bug
    private void confirmButtonActivity(Button signUp){
        EditText userNameTextField = findViewById(R.id.editTextTextPersonName3);
        EditText passwordTextField = findViewById(R.id.editTextTextPersonName8);
        EditText reTypeTextField = findViewById(R.id.editTextTextPersonName4);

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = userNameTextField.getText().toString();
                String password = passwordTextField.getText().toString();
                String reType = reTypeTextField.getText().toString();
                Integer accessibilityLevel = getAccessibilityLevel();

                if (username.matches("") || password.matches("") || reType.matches("") || accessibilityLevel == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter required fields", Toast.LENGTH_LONG).show();
                    return;
                } else if (!password.equals(reType)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                backendapi.signUp(username, password, accessibilityLevel, new Callback() {
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
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Signup successful, please login", Toast.LENGTH_LONG).show();
                                    }
                                });
                                // Go back to Login page once signed up
                                Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(activity2Intent);
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
        });
    }

    public int getAccessibilityLevel() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        if (radioButtonId == -1) {
            return 0;
        } else {
            String result = findViewById(android.R.id.content).getRootView().getResources().getResourceName(radioButtonId);
            return Integer.parseInt(result.split("radioButton")[1]);
        }
    }

}