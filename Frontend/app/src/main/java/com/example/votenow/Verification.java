package com.example.votenow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;

import de.hdodenhof.circleimageview.CircleImageView;

public class Verification extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private static final int MY_UPLOAD_PERMISSION_CODE = 0;
    private static final int MY_CAMERA_REQUEST_CODE = 0;
    public Button faceButton,idButton;
    ImageView imageView;
    Bitmap bitmap;
    CircleImageView circleImageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    boolean faceBool=false,idBool=false;
    ImageButton next;

    String isVefify="";

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        faceButton=findViewById(R.id.inputFace);
        idButton=findViewById(R.id.inputId);
        imageView=findViewById(R.id.imageView);
        next=findViewById(R.id.next);
        circleImageView=findViewById(R.id.circleView);
        circleImageView.setVisibility(View.INVISIBLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        try {
            isVefify = getIntent().getExtras().getString("vDone", "none");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Toast.makeText(this,isVefify,Toast.LENGTH_SHORT).show();

        //JSONArray can=getIntent().getExtras().getParcelable("Candidates");

        if(isVefify.equals("yes")){
                Toast.makeText(this,"Face Veriification Done",Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(Verification.this,VoteNow.class);
                intent.putExtra("can",(Parcelable)can);*/



            }else{
                Toast.makeText(this,"Face Veriification Not Done",Toast.LENGTH_SHORT).show();
            }

        idButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(Verification.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Verification.this,
                            Manifest.permission.CAMERA)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(Verification.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_UPLOAD_PERMISSION_CODE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(cameraIntent,MY_UPLOAD_PERMISSION_CODE);
                    }catch (Exception e){
                        Log.e("FACE",e.getMessage());                    }

                }

            }



        });

        faceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(Verification.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Verification.this,
                            Manifest.permission.CAMERA)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(Verification.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                      }
                } else {
                    // Permission has already been granted
                    Intent cameraIntent = new Intent(Verification.this,GooglyEyesActivity.class);
                    try {
                        startActivity(cameraIntent);
                    }catch (Exception e){
                        Log.e("FACE",e.getMessage());                    }

                }

            }



        });


    }

    public void check() {

        if (faceBool == false && idBool == true && isVefify.equals("yes")) {
            Toast.makeText(this, "Verification Done", Toast.LENGTH_LONG).show();
        } else if (idBool==false){
            idButton.setError("Id Not Uploaded");
        }
        else {
            Toast.makeText(this, "Verification Done", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!=MY_UPLOAD_PERMISSION_CODE) return;
        try {
            bitmap=(Bitmap)data.getExtras().get("data");
            circleImageView.setImageBitmap(bitmap);
            circleImageView.setVisibility(View.VISIBLE);
            faceButton.setHint("Change Face?");
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(Verification.this,GooglyEyesActivity.class);
                    startActivity(cameraIntent);
                    //startActivityForResult(takePictureIntent, MY_CAMERA_REQUEST_CODE);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Verification.this,HomeScreen.class));
        finish();
    }
}
