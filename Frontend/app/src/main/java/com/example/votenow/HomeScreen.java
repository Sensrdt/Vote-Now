package com.example.votenow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    private Button VN, AC, CV, LO,viewVote;
    String name, id, phoneNumber, getPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor meditor;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        VN = findViewById(R.id.voteNow);
        AC = findViewById(R.id.acntSettings);
        CV = findViewById(R.id.createVote);
        LO = findViewById(R.id.logout);
        viewVote=findViewById(R.id.viewVote);
        alertDialog=new AlertDialog.Builder(HomeScreen.this).create();
        Intent intent = getIntent();

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        meditor = sharedPreferences.edit();

        name=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
        phoneNumber=intent.getStringExtra("phone");
        getPassword=sharedPreferences.getString("password","null");

        CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeScreen.this,CreateVote
                        .class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();

            }
        });
        AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSettings();

            }
        });


        VN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeScreen.this,StartPage.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                intent.putExtra("phone",phoneNumber);
                startActivity(intent);
                finish();
            }
        });
        LO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        viewVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //voteGoingOn();
                voteNotStarted();
                //voteEnd();
            }
        });
    }

    private void voteEnd() {
        alertDialog.setTitle("Vote Has Ended");
        alertDialog.setMessage("Do You Want To View The Result?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"View Result",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"No Result",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private void voteGoingOn(){
        alertDialog.setTitle("Vote is Going On");
        alertDialog.setMessage("Do You Want To Stop the Vote?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Stop Vote", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"Stop Vote",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"nkj",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void voteNotStarted(){
        alertDialog.setTitle("Vote Has Not Yet Started");
        alertDialog.setMessage("Do You Want Start The Vote or Cancel the Vote");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start Vote", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"Start",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel Vote", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"Cancel",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }


    private void logout() {
        Intent intent = new Intent(HomeScreen.this,Login.class);
        meditor.putString("number", "null");
        meditor.putString("password","null");
        meditor.commit();
        startActivity(intent);
    }

    private void accountSettings() {
        Intent intent = new Intent(HomeScreen.this,AccountSettings.class);
        startActivity(intent);
        intent.putExtra("name",name);
        intent.putExtra("password",getPassword);
        intent.putExtra("phone",phoneNumber);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
    }

