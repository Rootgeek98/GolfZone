package com.zephyr.golfzone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zephyr.golfzone.R;
import com.zephyr.golfzone.app.AppConfig;
import com.zephyr.golfzone.app.AppController;
import com.zephyr.golfzone.helper.SQLiteHandler;
import com.zephyr.golfzone.helper.SessionManager;
import com.zephyr.golfzone.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (AppController.getInstance().getSessionManager().getUser() != null) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String empty_fields = getString(R.string.empty_fields);

                // Check for empty data in the form
                if (email.isEmpty() || password.isEmpty()) {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            empty_fields, Toast.LENGTH_LONG)
                            .show();
                }else {
                    // login user
                    checkLogin(email, password);
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {

        String logging_in = getString(R.string.logging_in);

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage(logging_in);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    String wrong_credentials = getString(R.string.wrong_credentials);
                    JSONObject jObj = new JSONObject(response);

                    // Check for error node in json
                    if (jObj.getString("error").equals("false")) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        JSONObject userObj = jObj.getJSONObject("user");
                        User user = new User(userObj.getString("unique_user_id"),
                                userObj.getString("firstname"),
                                userObj.getString("lastname"),
                                userObj.getString("email"),
                                userObj.getString("created_at"));

                        // Storing user in shared preferences
                        AppController.getInstance().getSessionManager().storeUser(user);

                        String success_login = getString(R.string.success_login);

                        Toast.makeText(getApplicationContext(), success_login, Toast.LENGTH_LONG).show();

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getApplicationContext(), wrong_credentials, Toast.LENGTH_LONG).show();

                        String errorMsg = jObj.getString("error_message");
                        Log.e(TAG, errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //Log.e()
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}