package com.mamindeveloper.dailylist.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mamindeveloper.dailylist.Repositories.AuthRepository;
import com.mamindeveloper.dailylist.Main.MainActivity;
import com.mamindeveloper.dailylist.Services.NetworkService;
import com.mamindeveloper.dailylist.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordRepeatView;
    private View progressBarView;
    private View loginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBarView = findViewById(R.id.progress_bar);
        loginView = findViewById(R.id.progress_bar);
        nameView = findViewById(R.id.name);
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        passwordRepeatView = (EditText) findViewById(R.id.password_repeat);

        passwordRepeatView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attemptRegister() {
        nameView.setError(null);
        emailView.setError(null);
        passwordView.setError(null);
        passwordRepeatView.setError(null);

        String name = nameView.getText().toString();
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordRepeat = passwordRepeatView.getText().toString();

        if (!isNameValid(name)) {
            nameView.setError(getString(R.string.error_invalid_name));
            nameView.requestFocus();
            return;
        }
        if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            emailView.requestFocus();
            return;
        }
        if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.requestFocus();
            return;
        }
        if (!password.equals(passwordRepeat)) {
            passwordRepeatView.setError(getString(R.string.error_invalid_password_repeat));
            passwordRepeatView.requestFocus();
            return;
        }

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        emailView.clearFocus();
        passwordView.clearFocus();

        progressBarView.setVisibility(View.VISIBLE);

        NetworkService
                .getInstance()
                .getAuthApi()
                .signUp(new RegisterBody(name, email, password))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                        RegisterResponse registerResponse = response.body();

                        progressBarView.setVisibility(View.GONE);

                        AuthRepository.getInstance().setUser(registerResponse.getUser());
                        AuthRepository.getInstance().setToken(registerResponse.getToken());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                        progressBarView.setVisibility(View.GONE);

                        passwordView.setError(getString(R.string.error_incorrect_password));
                        passwordView.requestFocus();
                    }
                });
    }

    private boolean isNameValid(String password) {
        return !TextUtils.isEmpty(password);
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 4;
    }
}
