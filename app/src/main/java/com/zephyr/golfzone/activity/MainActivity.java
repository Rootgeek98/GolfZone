package com.zephyr.golfzone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zephyr.golfzone.R;
import com.zephyr.golfzone.helper.SQLiteHandler;
import com.zephyr.golfzone.helper.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnDashboard;

    private SQLiteHandler db;
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

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String firstname = user.get("firstname");
        String lastname = user.get("lastname");
        String email = user.get("email");

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
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}