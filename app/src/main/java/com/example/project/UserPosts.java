package com.example.project;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;


public class UserPosts extends Fragment implements RecyclerView.OnItemTouchListener, SensorEventListener {
    private static final String TAG = "UserPosts";

    GestureDetector gestureDetector;
    RecyclerView rv;
    LinearLayoutManager layout;


    FirebaseAuth mAuth;
    FirebaseUser C_user;
    FirebaseRecyclerAdapter<post, post_viewholder> firebaseadapter;


    private static final float SHAKE_THRESHOLD = 1.25f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;
    int lastpos;
    private SensorManager mSensorMgr;
    boolean like = false;


    //private ArrayList<post_details> items= new ArrayList<>();
    Context con= getActivity();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_posts,container,false);

        mAuth = FirebaseAuth.getInstance();
        C_user = mAuth.getCurrentUser();
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        rv = view.findViewById(R.id.rcvUserPosts);
        layout = new LinearLayoutManager(con);
        layout.setReverseLayout(true);
        layout.setStackFromEnd(true);
        rv.setLayoutManager(layout);

        DatabaseReference likeref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/likes");
        DatabaseReference dref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/posts");
        firebaseadapter = new FirebaseRecyclerAdapter<post, post_viewholder>( post.class,
                R.layout.post_view,
                post_viewholder.class,
                dref
        ) {
            @Override
            protected void populateViewHolder(post_viewholder viewHolder, post model, int position) {

                viewHolder.set_post(model, getRef(position).getKey(), getActivity().getApplication());
                likeref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(getRef(position).getKey()).hasChild(mAuth.getCurrentUser().getUid())) {
                            viewHolder.like.setChecked(true);
                        } else {
                            viewHolder.like.setChecked(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                viewHolder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like = true;
                        likeref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (like) {
                                    if (dataSnapshot.child(getRef(position).getKey()).hasChild(mAuth.getCurrentUser().getUid())) {
                                        UserRef.child(model.getUID()).child("likers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                        likeref.child(getRef(position).getKey()).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        like=false;
                                    } else {
                                        UserRef.child(model.getUID()).child("likers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("liked");
                                        likeref.child(getRef(position).getKey()).child(mAuth.getCurrentUser().getUid()).setValue("Random");
                                        like=false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                lastpos = (int) dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        con = getActivity();
        mSensorMgr = (SensorManager) con.getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorMgr.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }


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


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    //       Toast.makeText(con, "Chor do hilaana -.-", Toast.LENGTH_SHORT).show();
                    firebaseadapter.notifyDataSetChanged();

                    layout.scrollToPosition(lastpos-1);
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

}