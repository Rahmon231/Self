package com.lemzeeyyy.self;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login, signup;
    private EditText password;
    private AutoCompleteTextView email;
    private ProgressBar loginProgress;
    //Firebase connection
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginProgress = findViewById(R.id.login_progress);
        login = findViewById(R.id.log_in_btn);
        signup = findViewById(R.id.sign_up_btn);
        login.setOnClickListener(this::onClick);
        signup.setOnClickListener(this::onClick);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_in_btn:
                //go to login page
                break;
            case R.id.sign_up_btn:
                //go to signup page
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;
        }

    }
}