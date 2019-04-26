package com.example.project;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Main_Post extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    LinearLayoutManager layout;
    RecyclerView r;
    FirebaseAuth mAuth;
    FirebaseUser C_user;
    FirebaseRecyclerAdapter<post, post_viewholder> firebaseadapter;
    private static final float SHAKE_THRESHOLD = 5.25f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;
    int lastpos;
    private SensorManager mSensorMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__post);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mAuth = FirebaseAuth.getInstance();
        C_user = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
           navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), make_post.class);
                startActivity(i);
            }
        });
/*
        r = findViewById(R.id.all_post);
        layout = new LinearLayoutManager(this);
        layout.setReverseLayout(true);
        layout.setStackFromEnd(true);
        r.setLayoutManager(layout);

        DatabaseReference dref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectsmd-4aa60.firebaseio.com/posts");
        firebaseadapter = new FirebaseRecyclerAdapter<post, post_viewholder>( post.class,
                R.layout.row_text,
                post_viewholder.class,
                dref
        ) {
            @Override
            protected void populateViewHolder(post_viewholder viewHolder, post model, int position) {

                viewHolder.set_post(model, getRef(position).getKey(), getApplication());
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

        r.setItemAnimator(new DefaultItemAnimator());
        r.setAdapter(firebaseadapter);

        */

        View hView =  navigationView.getHeaderView(0);

        TextView name = hView.findViewById(R.id.user_name);
        TextView email = hView.findViewById(R.id.user_email);
        ImageView img = hView.findViewById(R.id.user_image);
        name.setText(C_user.getDisplayName());
        email.setText(C_user.getEmail());
        if(C_user.getPhotoUrl()!=null) {
            String image_url = C_user.getPhotoUrl().toString();
            Toast.makeText(this, image_url, Toast.LENGTH_LONG).show();
            Picasso.get().load(image_url).into(img);
        }

//        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
//        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (accelerometer != null) {
//            mSensorMgr.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        }
    }


    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Join Post IT")
                .setMessage("Tetsing invite to POST IT")
                .build();
        startActivityForResult(intent, 314);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 314) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    //do something
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFeedFragment(), "Home");
        adapter.addFragment(new EventFeedFragment(), "Events");
        adapter.addFragment(new PopularFeedFragment(), "Popular");
        viewPager.setAdapter(adapter);
    }


    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_profile) {
            //startActivity( new Intent(this, make_post.class));
        } else if (id == R.id.campus_events) {

        } else if (id == R.id.liked_posts) {

        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity( new Intent(this, MainActivity.class));

        } else if (id == R.id.settings) {

        }
        else if (id == R.id.help){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            long curTime = System.currentTimeMillis();
//            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {
//
//                float x = event.values[0];
//                float y = event.values[1];
//                float z = event.values[2];
//
//                double acceleration = Math.sqrt(Math.pow(x, 2) +
//                        Math.pow(y, 2) +
//                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
//
//                if (acceleration > SHAKE_THRESHOLD) {
//                    mLastShakeTime = curTime;
//                    Toast.makeText(getApplicationContext(), "Chor do hilaana -.-", Toast.LENGTH_SHORT).show();
//                    firebaseadapter.notifyDataSetChanged();
//
//                    layout.scrollToPosition(lastpos-1);
//                }
//            }
//        }
//    }
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

}
