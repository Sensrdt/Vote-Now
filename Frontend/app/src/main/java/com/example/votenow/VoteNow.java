package com.example.votenow;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;

public class VoteNow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_now);

        JSONArray can=getIntent().getExtras().getParcelable("can");
        Toast.makeText(this,can.toString(),Toast.LENGTH_LONG).show();
    }
}
