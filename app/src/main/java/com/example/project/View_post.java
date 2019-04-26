package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class View_post extends Activity {
    TextToSpeech t1;

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    SharePhotoContent content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        String post_key = getIntent().getStringExtra("post_key");
        DatabaseReference DB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/posts").child(post_key);
        TextView Username = findViewById(R.id.view_username);
        TextView desc = findViewById(R.id.view_desc);
        ImageView img = findViewById(R.id.view_image);
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String UN =(String) dataSnapshot.child("userName").getValue();
                String Desc =(String) dataSnapshot.child("desc").getValue();
                String Image =(String) dataSnapshot.child("image_uri").getValue();
                Username.setText(UN);
                desc.setText(Desc);
                if(Image!=null && !Image.equals("no_imag")) {
                    Picasso.get().load(Image).into(img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            set_share_button();
                            ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
                            shareButton.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {


                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        shareDialog = new ShareDialog(this);

        callbackManager = CallbackManager.Factory.create();



        set_recyclerView();

    }

    void set_share_button(){
        ImageView img = findViewById(R.id.view_image);
        img.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        TextView desc = findViewById(R.id.view_desc);
        content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);
    }

    void set_recyclerView(){
        RecyclerView rv = findViewById(R.id.comment_rv);
        String post_key = getIntent().getStringExtra("post_key");
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
        FirebaseRecyclerAdapter<String, String_viewHolder>  firebaseadapter = new FirebaseRecyclerAdapter<String, String_viewHolder>(
                String.class,
                R.layout.comment_row,
                String_viewHolder.class,
                dref
        ) {
            @Override
            protected void populateViewHolder(String_viewHolder viewHolder, String model, int position) {
                viewHolder.setvalue(model);
            }
        };
        rv.setAdapter(firebaseadapter);

    }


    public static class String_viewHolder extends RecyclerView.ViewHolder{
        View v;
        public String_viewHolder(@NonNull View itemView) {
            super(itemView);
            v =itemView;
        }
        void setvalue(String cmnt){
            TextView cmnt1 = v.findViewById(R.id.comnt_text);
            cmnt1.setText(cmnt);
        }
    }

    public void text_to_speech(View v)
    {
        TextView Username = findViewById(R.id.view_username);
        TextView desc = findViewById(R.id.view_desc);
        String toSpeak = Username.getText().toString()+ " Posted:  "+ desc.getText().toString();
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }


    public void comment(View v){
        TextView cmnt = findViewById(R.id.comment);
        String post_key = getIntent().getStringExtra("post_key");
        DatabaseReference CmntRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key).push();
        String c = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        c = c + ": " + cmnt.getText().toString();
        CmntRef.setValue(c);

        cmnt.setText("");
    }


    public  void share(View v){
        if (ShareDialog.canShow(ShareLinkContent.class)) {


            ImageView img = findViewById(R.id.view_image);
            img.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            TextView desc = findViewById(R.id.view_desc);
            content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
            shareButton.setShareContent(content);
            shareDialog.show(content);
        }
    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
