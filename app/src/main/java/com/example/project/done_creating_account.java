package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class done_creating_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_creating_account);
    }

    public void done(View v){
        finish();
        Intent obj = new Intent(getApplicationContext(), Main_Post.class);
        startActivity(obj);
    }

}
