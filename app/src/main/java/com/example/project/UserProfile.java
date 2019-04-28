package com.example.project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    String uid, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        RecyclerView rv = findViewById(R.id.user_posts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        uid = getIntent().getStringExtra("UID");
        name = getIntent().getStringExtra("name");
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("posts");
        FirebaseRecyclerAdapter<String, post_viewholder> firebaseadapter = new FirebaseRecyclerAdapter<String, post_viewholder>(
                String.class,
                R.layout.post_view,
                post_viewholder.class,
                dref
        ) {
            @Override
            protected void populateViewHolder(post_viewholder viewHolder, String model, int position) {
                viewHolder.set_values(model, getApplicationContext());
            }
        };
        rv.setAdapter(firebaseadapter);

        DatabaseReference imgref =FirebaseDatabase.getInstance().getReference().child("User_Info").child(uid);
        imgref.child("img_URI").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String path = (String) dataSnapshot.getValue();
                ImageView dp = findViewById(R.id.user_profile_img);
                Picasso.get().load(path).into(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;


        TextView nameV = findViewById(R.id.user_profile_name);
        nameV.setText(name);
    }



}
