package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class post_viewholder extends RecyclerView.ViewHolder{
    TextView Name, desc, n_likes, n_comnt;
    ImageView img;
    ToggleButton like;
    View v;
    String postkey;
    public post_viewholder(@NonNull View itemView) {
        super(itemView);
        Name = itemView.findViewById(R.id.usrname);
        desc = itemView.findViewById(R.id.desc);
        n_comnt = itemView.findViewById(R.id.n_cmnt);
        n_likes = itemView.findViewById(R.id.n_likes);
        img = itemView.findViewById(R.id.image_row);
        like = itemView.findViewById(R.id.button_like);
        v = itemView;
        postkey=null;
    }

    public void set_values(post_details p){
        Name.setText(p.username);
        desc.setText(p.desc);
        n_likes.setText(p.no_likes + " Likes");
        n_comnt.setText(p.no_comnts + " Comments");
    }
    public void set_post(post p, String post_key, Context c){
        Name.setText(p.getUserName());
        desc.setText(p.getDesc());
        postkey = post_key;
        if(p.getImage_uri()!=null) {
            Picasso.get().load(p.image_uri).into(img);
        }
        DatabaseReference likeref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/likes");
        likeref.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n_likes.setText(String.valueOf((int)dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n_comnt.setText(String.valueOf((int)dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postkey!=null){
                    Intent obj =  new Intent(c, View_post.class);
                    obj.putExtra("post_key", postkey);
                    c.startActivity(obj);
                }
            }
        });
    }
}
