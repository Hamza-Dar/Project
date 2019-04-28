package com.example.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

public class EVENT extends AppCompatActivity {

    RecyclerView rv;
    TextView name,desc;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        rv = findViewById(R.id.rv_event_v);
        name = findViewById(R.id.name_event_v);
        desc = findViewById(R.id.desc_event_v);
        img = findViewById(R.id.img_Event_v);


    }
}
