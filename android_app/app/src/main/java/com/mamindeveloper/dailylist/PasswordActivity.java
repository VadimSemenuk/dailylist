package com.mamindeveloper.dailylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mamindeveloper.dailylist.Main.MainActivity;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        findViewById(R.id.in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in();
            }
        });
    }

    private void in() {
        String input = ((EditText) findViewById(R.id.password)).getText().toString();
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String password = prefs.getString("passwordOnEnter", null);

        if (input.equals(password)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
