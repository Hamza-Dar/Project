package com.example.project;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class PopularFeedFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.activity_popular_feed_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();
        C_user = mAuth.getCurrentUser();

        rv = view.findViewById(R.id.rcvPopular);
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

        return view;
    }
}