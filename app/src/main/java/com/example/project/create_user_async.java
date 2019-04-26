package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class create_user_async extends AsyncTask {

    user u;
    Activity activity;
    create_user_async(user u, Activity act){
        this.u= u;
        activity= act;
    }
    DatabaseReference myRef;
    FirebaseDatabase database;
    @Override
    protected Object doInBackground(Object[] objects) {
        FirebaseApp.initializeApp(activity);
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference("users");

        myRef.setValue(u.email, u);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Intent i = new Intent(activity ,MainActivity.class);
        activity.startActivity(i);
    }
}
