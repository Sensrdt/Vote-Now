package com.example.votenow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class V_Code extends AppCompatActivity {

    EditText input;
    ImageButton next;
    ImageView imageView;
    String phnNo;

    private  String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        input = findViewById(R.id.inputText);
        next = findViewById(R.id.next);
        imageView = findViewById(R.id.imageView);

        input.setHint("Verification Code");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        imageView.setImageResource(R.drawable.codes);
        phnNo=getIntent().getStringExtra("phnNo");
        Toast.makeText(this,phnNo,Toast.LENGTH_SHORT).show();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Sending OTP");
        progressDialog.setMessage("Please Wait While We Send The OTP");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        sendCode("+91"+phnNo);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });


    }

    private void verifyCode() {
        String code = input.getText().toString();

        if (code.isEmpty()) {
            Toast.makeText(this, "Code Is Empty", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this,"k"+code,Toast.LENGTH_LONG).show();
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signWithPhoneAuthCredential(phoneAuthCredential);
        }
    }

    private void sendCode(String phnNo) {
        setUpVerificationCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phnNo,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks
        );
    }

    private void setUpVerificationCallback() {
        verificationCallbacks=
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        phoneVerificationId=s;
                        resendingToken=forceResendingToken;
                        progressDialog.dismiss();
                    }
                };
    }

    private void signWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Verification Done",Toast.LENGTH_LONG).show();
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(V_Code.this,Password.class);
                            intent.putExtra("phnNo",phnNo);
                            startActivity(intent);
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Wrong Code",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(V_Code.this,Phone.class);
        startActivity(intent);
        finish();
    }

}
