package com.ejsfbu.app_main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ejsfbu.app_main.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// app will automatically load to this screen, unless user already logged in
// upon login app will navigate to goals list fragment in main activity

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivities";

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.bLogin)
    Button bLogin;
    @BindView(R.id.bSignUp)
    Button bSignUp;
    @BindView(R.id.bParentSignUp)
    Button bParentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @OnClick(R.id.bLogin)
    public void clickLogin() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        login(username, password);
    }

    @OnClick(R.id.bSignUp)
    public void clickSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("isParent", false);
        startActivity(intent);
    }

    @OnClick(R.id.bParentSignUp)
    public void clickParentSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("isParent", true);
        startActivity(intent);
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Login Success");

                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Login Failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
