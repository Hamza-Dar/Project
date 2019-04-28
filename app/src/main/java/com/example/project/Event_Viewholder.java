package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Event_Viewholder  extends RecyclerView.ViewHolder{
    TextView Name, desc;
    ImageView img;
    View v;
    String Event_key;
    public Event_Viewholder(@NonNull View itemView) {
        super(itemView);

        Name = itemView.findViewById(R.id.row_name);
        desc = itemView.findViewById(R.id.event_desc_row);
        img = itemView.findViewById(R.id.event_image_row);
        v = itemView;
        Event_key =null;
    }

    public void set_post(post p, String post_key, Context c){
        Name.setText(p.getUserName());
        desc.setText(p.getDesc());
        Event_key = post_key;
        if(p.getImage_uri()!=null) {
            Picasso.get().load(p.image_uri).into(img);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Event_key!=null){
                    Intent obj =  new Intent(c, View_post.class);
                    obj.putExtra("post_key", Event_key);
                    c.startActivity(obj);
                }
            }
        });
    }
}
