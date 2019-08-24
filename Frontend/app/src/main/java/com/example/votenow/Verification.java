package com.example.votenow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class Verification extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    public Button faceButton,idButton;
    ImageView imageView;
    Bitmap bitmap;
    CircleImageView circleImageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    boolean faceBool=false,idBool=false;
    ImageButton next;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        faceButton=findViewById(R.id.inputFace);
        idButton=findViewById(R.id.inputFace);
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

        faceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,456);
            }
        });


    }

    public void check(){
        if(faceBool==true && idBool==true){
            Toast.makeText(this,"Vote Done",Toast.LENGTH_LONG).show();
        }else if(faceBool==false){
            faceButton.setError("Face Not Uploaded");
        }else{
            idButton.setError("Id Not Uploaded");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!=456) return;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST:
                if(permissions.length>0 && permissions[0].equals(PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(Verification.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(Verification.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
