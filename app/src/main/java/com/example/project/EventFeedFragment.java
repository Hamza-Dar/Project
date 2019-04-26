package com.example.project;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class EventFeedFragment extends Fragment implements RecyclerView.OnItemTouchListener {
    private static final String TAG = "HomeFragment";

    GestureDetector gestureDetector;
    RecyclerView rv;

    FirebaseAuth mAuth;
    FirebaseUser C_user;



    //private ArrayList<post_details> items= new ArrayList<>();
    Post_adapter adapter;
    Context con= getActivity();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_event_feed_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();
        C_user = mAuth.getCurrentUser();

        rv = view.findViewById(R.id.rcvEvent);
        LinearLayoutManager layout = new LinearLayoutManager(con);
        layout.setReverseLayout(true);
        layout.setStackFromEnd(true);
        rv.setLayoutManager(layout);

        DatabaseReference dref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/posts");
        FirebaseRecyclerAdapter<post, post_viewholder> firebaseadapter = new FirebaseRecyclerAdapter<post, post_viewholder>( post.class,
                R.layout.post_view,
                post_viewholder.class,
                dref
        ) {
            @Override
            protected void populateViewHolder(post_viewholder viewHolder, post model, int position) {

                viewHolder.set_post(model, getRef(position).getKey(), getActivity().getApplication());
            }
        };


        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(firebaseadapter);


        /*NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        TextView name = hView.findViewById(R.id.user_name);
        TextView email = hView.findViewById(R.id.user_email);
        ImageView img = hView.findViewById(R.id.user_image);
        name.setText(C_user.getDisplayName());
        email.setText(C_user.getEmail());
        if(C_user.getPhotoUrl()!=null) {
            String image_url = C_user.getPhotoUrl().toString();
            Toast.makeText(con, image_url, Toast.LENGTH_LONG).show();
            Picasso.get().load(image_url).into(img);
        }
*/
        return view;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }
    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}