package com.zephyr.golfzone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zephyr.golfzone.R;
import com.zephyr.golfzone.app.AppConfig;
import com.zephyr.golfzone.app.AppController;
import com.zephyr.golfzone.helper.SQLiteHandler;
import com.zephyr.golfzone.helper.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnDashboard;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFirstName = findViewById(R.id.firstname);
        txtLastName = findViewById(R.id.lastname);
        txtEmail = findViewById(R.id.email);
        btnLogout = findViewById(R.id.btnLogout);
        btnDashboard = findViewById(R.id.btnDashboard);

        /*
         * Check for login session.
         * If not logged in launch Login Activity
         */
        if (AppController.getInstance().getSessionManager().getUser() == null) {
            logoutUser();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);

        String firstname = AppController.getInstance().getSessionManager().getUser().getFirstname();
        String lastname = AppController.getInstance().getSessionManager().getUser().getLastname();
        String email = AppController.getInstance().getSessionManager().getUser().getEmail();

        // Displaying the user details on the screen
        txtFirstName.setText(firstname);
        txtLastName.setText(lastname);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        // session manager
        session = new SessionManager(getApplicationContext());
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.clear();
        session.setLogin(false);

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}