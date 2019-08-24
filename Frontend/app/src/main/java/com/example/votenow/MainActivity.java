package com.example.votenow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        next=findViewById(R.id.imageButton);
        check();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }


    public void check(){
        if(checkConnection()){
            go2next();
        }
        else{

            new AlertDialog.Builder(this)
                    .setTitle("Network")
                    .setMessage("Please Turn on your Data or Wifi. \nYou can't Vote Without Internet.")
                    .setPositiveButton("Okay",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            check();
                        }
                    })
                    .create().show();
                    
        }
    }

    private void go2next() {

        next.setVisibility(View.GONE);

        Thread thread = new Thread(){

            @Override
            public void run() {
                try{
                    sleep(2000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }finally {
                    Intent mainIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(mainIntent);
                    finish();

                }
            }
        };
        thread.start();
    }

    private boolean checkConnection() {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        return false;

    }

}
