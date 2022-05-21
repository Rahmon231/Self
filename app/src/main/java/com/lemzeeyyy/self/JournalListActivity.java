package com.lemzeeyyy.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class JournalListActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                //take users to add journal
                if(user!=null && firebaseAuth!=null){
                    startActivity(new Intent(JournalListActivity.this,
                            PostJournalActivity.class));
                    //finish();
                }
                break;
            case R.id.action_sign_out:
                //sign user out
                if(user!=null && firebaseAuth!=null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(JournalListActivity.this,
                            MainActivity.class));
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}