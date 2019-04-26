package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Locale;

public class make_post extends Activity {

    private StorageReference mStorageRef;
    private FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);
        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Feed", "Event", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_row, items);
//
        dropdown.setAdapter(adapter);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 1);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    public void add_image(View v){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 1);
        }

    }

    public void cancel(View v){
        finish();
    }

    ProgressDialog prog;
    public void post(View v){
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference UserRef = db.getReference().child("Users").child(UID).child("posts").push();
        DatabaseReference DBRef = db.getReference().child("posts").push();


        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        TextView desc = findViewById(R.id.Post_desc);

        prog = new ProgressDialog(this);
        prog.setMessage("Posting");
        if(selectedImage!=null) {
            prog.show();
            mStorageRef = FirebaseStorage.getInstance().getReference("postImages/" + DBRef.getKey() + ".jpg");
            mStorageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    prog.show();
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    String imageuri = uri.getResult().toString();
                    post p = new post(UID, desc.getText().toString(), imageuri, username);
                    DBRef.setValue(p);
                    prog.dismiss();
                    UserRef.setValue(DBRef.getKey());
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "image saved "+e.toString(), Toast.LENGTH_LONG).show();
                    prog.dismiss();
                }
            });
        }
        else
        {
            prog.show();
            post p = new post(UID, desc.getText().toString(), "no_image", username);
            DBRef.setValue(p);
            UserRef.setValue(DBRef.getKey());
            prog.dismiss();
            finish();
        }

    }

    Uri selectedImage;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data ==  null)
            return;
        if (requestCode == 1) {
            //TODO: action
            final Bundle extras = data.getExtras();
            if (extras != null) {
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView img = findViewById(R.id.image_post);
                img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                img.setVisibility(View.VISIBLE);

            }
        }
        else if( requestCode == 12345){
            if (resultCode == RESULT_OK && data!=null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                TextView desc = findViewById(R.id.Post_desc);
                String d = desc.getText().toString();
                d = d+ " "+ result.get(0);
                desc.setText(result.get(0));
            }
        }

    }


    public void speech_to_text(View v)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
        try {
            startActivityForResult(intent, 12345);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry your device not supported", Toast.LENGTH_SHORT).show();
        }
    }


}
