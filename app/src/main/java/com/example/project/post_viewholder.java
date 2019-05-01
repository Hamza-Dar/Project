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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class post_viewholder extends RecyclerView.ViewHolder {
    TextView Name, desc, n_likes, n_comnt;
    ImageView img;
    ToggleButton like;
    View v;
    String postkey;
    FirebaseAuth mAuth;
    public post_viewholder(@NonNull View itemView) {
        super(itemView);
        mAuth = FirebaseAuth.getInstance();
        Name = itemView.findViewById(R.id.usrname);
        desc = itemView.findViewById(R.id.desc);
        n_comnt = itemView.findViewById(R.id.n_cmnt);
        n_likes = itemView.findViewById(R.id.n_likes);
        img = itemView.findViewById(R.id.image_row);
        like = itemView.findViewById(R.id.button_like);
        v = itemView;
        postkey = null;
    }

    String UID;
    Boolean liked= false;
    public void set_values(String p, Context c) {
        String post_key = p;

        DatabaseReference DB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/posts").child(post_key);

        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String UN = (String) dataSnapshot.child("userName").getValue();
                UID = (String) dataSnapshot.child("uid").getValue();
                String Desc = (String) dataSnapshot.child("desc").getValue();
                String Image = (String) dataSnapshot.child("image_uri").getValue();
                Name.setText(UN);
                desc.setText(Desc);
                if (Image != null && !Image.equals("no_imag")) {
                    Picasso.get().load(Image).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference likeref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/likes");
        likeref.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n_likes.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n_comnt.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postkey != null) {
                    Intent obj = new Intent(c, View_post.class);
                    obj.putExtra("post_key", postkey);
                    c.startActivity(obj);
                }
            }
        });

        likeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                    like.setChecked(true);
                } else {
                    like.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        like.setOnClickListener(v -> {
            liked = true;
            likeref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (liked) {
                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                            UserRef.child(UID).child("likers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"|"+post_key).removeValue();
                            likeref.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                            liked=false;
                        } else {
                            UserRef.child(UID).child("likers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"|"+post_key).setValue("liked");
                            likeref.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random");
                            liked=false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });

    }

    public void set_post(post p, String post_key, Context c) {
        Name.setText(p.getUserName());
        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(c, UserProfilePage.class);
                obj.putExtra("UID", p.getUID());
                obj.putExtra("name", p.getUserName());
                c.startActivity( obj);
            }
        });
        desc.setText(p.getDesc());
        postkey = post_key;
        if (p.getImage_uri() != null) {
            Picasso.get().load(p.getImage_uri()).into(img);
        }
        DatabaseReference likeref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/likes");
        likeref.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n_likes.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n_comnt.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postkey != null) {
                    Intent obj = new Intent(c, View_post.class);
                    obj.putExtra("post_key", postkey);
                    c.startActivity(obj);
                }
            }
        });
    }
}
