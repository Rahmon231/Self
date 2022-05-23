package com.lemzeeyyy.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalApi;

public class MainActivity extends AppCompatActivity {
    private Button getStartedBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStartedBtn = findViewById(R.id.startBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser!=null){
                    currentUser = firebaseAuth.getCurrentUser();
                    String currentUserId = currentUser.getUid();
                    collectionReference.whereEqualTo("userid",currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if(error != null){
                                        return;
                                    }
                                    String name;
                                    if(!value.isEmpty()){
                                        for (QueryDocumentSnapshot snapshot :
                                                value) {
                                            JournalApi journalApi = JournalApi.getInstance();
                                            journalApi.setUserId(snapshot.getString("userid"));
                                            journalApi.setUserName(snapshot.getString("username"));
                                            startActivity(new Intent(MainActivity.this,
                                                    JournalListActivity.class));
                                            finish();
                                        }
                                    }
                                }
                            });
                }else{

                }

            }
        };
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}