package com.ione.iseller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        SessionManager sessionManager = new SessionManager(this);
        Intent intent;
        if (sessionManager.isLoggedIn()) {
            intent = new Intent(this, MainScreen.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
    }
}
