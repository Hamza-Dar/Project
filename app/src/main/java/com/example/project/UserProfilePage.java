package com.example.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfilePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String uid, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);

        FloatingActionButton fab = findViewById(R.id.fab2);


        RecyclerView rv = findViewById(R.id.rv_new_userprofile);
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

        Intent obj = getIntent();
        String path = obj.getStringExtra("url");
        if(path!=null) {
            CircleImageView dp = findViewById(R.id.circleImageView2);
            Picasso.get().load(path).into(dp);
        }

        TextView nameV = findViewById(R.id.Username_n_profile);
        nameV.setText(name);

        if(!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            Button edt = findViewById(R.id.EditProfile);
            edt.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), make_post.class);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
