package com.example.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_information extends AppCompatActivity {


    Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_information);
        selectedImage = Uri.parse("android.resource://com.example.project/" + R.drawable.dp);
    }


    public void uploadImg(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 1);
        }
    }

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

                ImageView img = findViewById(R.id.user_image);
                img.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            }
        }

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

    private StorageReference mStorageRef;
    FirebaseAuth mAuth;
    TextView name;
    FirebaseUser user;
    Context context;
    Button b;
    public void save(View v){
        b = findViewById(R.id.save_profile);
        b.setEnabled(false);
        b.setClickable(false);
        mAuth = FirebaseAuth.getInstance();
        context = this;
        name = findViewById(R.id.c_name);
        user = mAuth.getCurrentUser();
        if(user==null)
            return;
        if(selectedImage!=null) {
            mStorageRef = FirebaseStorage.getInstance().getReference("profilePics/" + user.getUid() + ".jpg");
            mStorageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    String imageuri = uri.getResult().toString();
                    UserProfileChangeRequest user_update =new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).setPhotoUri(Uri.parse(imageuri)).build();
                    user.updateProfile(user_update);
                    Toast.makeText(context, "image saved", Toast.LENGTH_LONG).show();
                    Intent obj = new Intent(getApplicationContext(), done_creating_account.class);
                    finish();
                    mAuth.getCurrentUser().sendEmailVerification();
                    startActivity(obj);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "image saved "+e.toString(), Toast.LENGTH_LONG).show();
                    b.setEnabled(true);
                    b.setClickable(true);
                }
            });
        }


    }

}
