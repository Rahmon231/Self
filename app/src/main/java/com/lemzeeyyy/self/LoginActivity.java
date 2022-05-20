package com.lemzeeyyy.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalApi;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login, signup;
    private EditText password;
    private AutoCompleteTextView email;
    private ProgressBar loginProgress;
    //Firebase connection
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CollectionReference collectionReference = firestore.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginProgress = findViewById(R.id.login_progress);
        login = findViewById(R.id.log_in_btn);
        signup = findViewById(R.id.sign_up_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(this::onClick);
        signup.setOnClickListener(this::onClick);
        
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_in_btn:
                //go to login page
                loginEmailAndPassword(email.getText().toString().trim(),
                        password.getText().toString().trim());

                break;
            case R.id.sign_up_btn:
                //go to signup page
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;
        }

    }

    private void loginEmailAndPassword(String email, String password) {
        if(!TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String currentUserId = user.getUid();
                            collectionReference.whereEqualTo("userid",currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error!=null){
                                                return;
                                            }
                                            if(!value.isEmpty()){
                                                for (QueryDocumentSnapshot queryDoc :
                                                        value) {
                                                    JournalApi journalApi = JournalApi.getInstance();
                                                    journalApi.setUserName(queryDoc.getString("username"));
                                                    journalApi.setUserId(queryDoc.getString("userid"));
                                                    startActivity(new Intent(LoginActivity.this,
                                                            PostJournalActivity.class));

                                                }
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
            

        }else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        }

    }
}