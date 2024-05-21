package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload_page extends AppCompatActivity {
    FloatingActionButton uploadButton;
    ImageView Uploadimage;
    EditText Uploadcaption;
    ProgressBar progressBar;
    Uri ImageUri;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Image");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        uploadButton = findViewById(R.id.uploadbutton);
        Uploadcaption = findViewById(R.id.uploadcaption);
        Uploadimage = findViewById(R.id.uploadimage);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        ActivityResultLauncher<Intent>activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK)
                        {
                            Intent data = result.getData();
                            ImageUri = data.getData();
                            Uploadimage.setImageURI(ImageUri);
                        }
                        else {
                            Toast.makeText(Upload_page.this, "No Image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageUri != null){
                    uploadToFirebase(ImageUri);
                }
                else {
                    Toast.makeText(Upload_page.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void uploadToFirebase(Uri uri){
       String caption = Uploadcaption.getText().toString();
       final StorageReference imageReference = storageReference.child(System.currentTimeMillis() +"." +getFilExtension(uri));

       imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               imageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull Task<Uri> task) {
                       DataClass dataClass = new DataClass(uri.toString(), caption);
                       String key = databaseReference.push().getKey();
                       databaseReference.child(key).setValue(dataClass);
                       progressBar.setVisibility(View.INVISIBLE);
                       Toast.makeText(Upload_page.this, "Uploaded", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(Upload_page.this,Photo_page.class);
                       startActivity(intent);
                       finish();
                   }
               });
           }
       }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
               progressBar.setVisibility(View.INVISIBLE);
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressBar.setVisibility(View.INVISIBLE);
               Toast.makeText(Upload_page.this, "Failed", Toast.LENGTH_SHORT).show();
           }
       });
    }
    private String getFilExtension(Uri fileuri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(contentResolver.getType(fileuri));
    }
}