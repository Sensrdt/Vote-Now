package com.example.votenow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateVote extends AppCompatActivity {

    ArrayList<EditText> candidates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vote);
        candidates = new ArrayList<>();


    }

    protected void addEdit(View view) {
        boolean err = false;
        for(EditText er: candidates)
            if(er.getText().toString().trim().equals("")) {
                er.setError("Cannot be empty");
                err = true;
            }
        if(err) return;
        EditText et = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et.setHint("Name");
        et.setSingleLine();
        et.setId(candidates.size() + 1);
        ((LinearLayout)findViewById(R.id.candidateLayout)).addView(et);
        candidates.add(et);
    }

    protected void sendData(View view) {
        String finalString="";
        boolean err=false;
        for(EditText et: candidates) {
            if(et.getText().equals("")) {
                err = true;
                et.setError("Cannot be empty");
            }
            finalString += et.getText()+",";
        }
        if(!err)
            Toast.makeText(this,finalString,Toast.LENGTH_LONG).show();
    }
}
