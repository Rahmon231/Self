package com.lemzeeyyy.self;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class PostJournalActivity extends AppCompatActivity {
    private TextView userName, dateTextView;
    private EditText titleEditText, descriptionEdit;
    private ImageView addPhoto;
    private Button postSavebtn;
    private ProgressBar postProgress;
    private String currentUserName;
    private String currentUserId;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = firestore.collection("Journal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);
//        Bundle bundle = getIntent().getExtras();
//        if(bundle!=null){
//            String username = bundle.getString("username");
//            String currentUserId = bundle.getString("userid");
//        }
        userName = findViewById(R.id.post_username);
        dateTextView = findViewById(R.id.post_date_textview);
        titleEditText = findViewById(R.id.post_title_et);
        descriptionEdit = findViewById(R.id.post_description_et);
        addPhoto = findViewById(R.id.post_image_btn);
        postSavebtn = findViewById(R.id.post_save_journal_btn);
        postProgress = findViewById(R.id.post_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}