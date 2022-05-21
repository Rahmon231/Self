package com.lemzeeyyy.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lemzeeyyy.self.model.Journal;

import java.util.Date;
import java.util.Map;

import util.JournalApi;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 1;
    private TextView userName, dateTextView;
    private EditText titleEditText, descriptionEdit;
    private ImageView addPhoto,backgroundImage;
    private Button postSavebtn;
    private ProgressBar postProgress;
    private String currentUserName;
    private String currentUserId;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference storageReference ;
    private CollectionReference reference = firestore.collection("Journals");
    private Uri imageUri;

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
        backgroundImage = findViewById(R.id.backgroundImage);
        postSavebtn = findViewById(R.id.post_save_journal_btn);
        postProgress = findViewById(R.id.post_progressBar);
        postProgress.setVisibility(View.INVISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        addPhoto.setOnClickListener(this);
        postSavebtn.setOnClickListener(this);
        if(JournalApi.getInstance() !=null){
            currentUserId = JournalApi.getInstance().getUserId();
            currentUserName =JournalApi.getInstance().getUserName();
            userName.setText(currentUserName);
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                }
            };
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.post_image_btn:
                //get image from gallery
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
                break;
            case R.id.post_save_journal_btn:
                saveJournal();
                break;
        }
    }

    private void saveJournal() {
         String title = titleEditText.getText().toString().trim();
         String description = descriptionEdit.getText().toString().trim();
        postProgress.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title)
        && !TextUtils.isEmpty(description)
        && imageUri != null
        ){
             StorageReference imagePath = storageReference
                     .child("journal_image")
                     .child("my_image_"+ Timestamp.now().getSeconds());
            imagePath.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imagePath.getDownloadUrl().addOnSuccessListener(uri -> {
                         /*
                Create a journal object
                invoke collectionReference
                add an instance of journal
                 */
                            String imageUrl = uri.toString();
                            Journal journal = new Journal();
                            journal.setTitle(title);
                            journal.setDescription(description);
                            journal.setImageUri(imageUrl);
                            journal.setTimeAdded(new Timestamp(new Date()));
                            journal.setUserName(currentUserName);
                            journal.setUserId(currentUserId);
                            reference.add(journal)
                                    .addOnSuccessListener((DocumentReference documentReference) -> {
                                        postProgress.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(PostJournalActivity.this,
                                                JournalListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Log.d("jornalnotadded", "onFailure: " + e.getMessage()));
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("downloadurlfailure", "onFailure: "+e.getMessage());
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        postProgress.setVisibility(View.INVISIBLE);
                        Log.d("failuremessage", "onFailure: "+e.getMessage());
                    });

        }else{
            postProgress.setVisibility(View.INVISIBLE);
            Log.d("widgets", "saveJournal: widget error");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if(data!=null){
                imageUri = data.getData();
                backgroundImage.setImageURI(imageUri);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}