package com.lemzeeyyy.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText userName, emailSignUp, passwordSignup;
    private Button signUpBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private ProgressBar signUpProgress;
    //create firebase connection
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.user_name);
        emailSignUp = findViewById(R.id.email_signup);
        passwordSignup = findViewById(R.id.password_signup);
        signUpBtn = findViewById(R.id.sign_up_account);
        signUpProgress = findViewById(R.id.sign_up_progress);
        authStateListener = firebaseAuth -> {
            currentUser = firebaseAuth.getCurrentUser();
            if(currentUser!=null){
                //user is logged in
            }else{
                //user isnt logged in
            }
        };

        signUpBtn.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(emailSignUp.getText().toString())
            && !TextUtils.isEmpty(passwordSignup.getText().toString())
            && !TextUtils.isEmpty(userName.getText().toString())){

                String email = emailSignUp.getText().toString().trim();
                String password = passwordSignup.getText().toString().trim();
                String username = userName.getText().toString().trim();

                createUserEmailAccount(email, password, username);
            }else
                Toast.makeText(this, "Empty Field not allowed", Toast.LENGTH_SHORT).show();
        });
    }
    private void createUserEmailAccount(String email, String password, String username){
        if(!TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(password)
        && !TextUtils.isEmpty(username)){
            signUpProgress.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            currentUser = firebaseAuth.getCurrentUser();
                            String currentUserId = currentUser.getUid();
                            Map<String, String> userObject = new HashMap<>();
                            userObject.put("userid",currentUserId);
                            userObject.put("username",username);
                            // save to firestore
                            collectionReference.add(userObject)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(task.getResult().exists()){
                                                                    signUpProgress.setVisibility(View.INVISIBLE);
                                                                    String name = task.getResult()
                                                                            .getString("username");

                                                                    Intent intent = new Intent(SignUpActivity.this,
                                                                            PostJournalActivity.class);
                                                                    intent.putExtra("username",name);
                                                                    intent.putExtra("userid", currentUserId);
                                                                    startActivity(intent);

                                                                }else{
                                                                    signUpProgress.setVisibility(View.INVISIBLE);

                                                                }
                                                            }
                                                        });
                                            }

                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}