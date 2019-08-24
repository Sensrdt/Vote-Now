package com.example.votenow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    private Button VN, AC, CV, LO;
    String name, id, phoneNumber, getPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor meditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        VN = findViewById(R.id.voteNow);
        AC = findViewById(R.id.acntSettings);
        CV = findViewById(R.id.createVote);
        LO = findViewById(R.id.logout);
        Intent intent=getIntent();

        name=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
        phoneNumber=intent.getStringExtra("phone");
        getPassword=sharedPreferences.getString("password","null");

        CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeScreen.this,AccountSettings
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
}
