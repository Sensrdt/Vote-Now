package com.example.votenow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AccountSettings extends AppCompatActivity {

    EditText input;
    ImageButton next;
    EditText name, password, c_password;
    TextView phone;
    ProgressDialog progressDialog;

    String getPhnNo, getName, getPassword, getCPassword,getPasswordFromLocal;
    SharedPreferences sharedPreferences;

    SharedPreferences.Editor meditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        next = findViewById(R.id.next);
        name = findViewById(R.id.inputName);
        password = findViewById(R.id.inputPassword);
        c_password = findViewById(R.id.inputConfirmPassword);
        phone = findViewById(R.id.inputPhone);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Please Wait While We Register You");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        meditor = sharedPreferences.edit();

        getName=getIntent().getStringExtra("name");
        getPhnNo=getIntent().getStringExtra("phone");
        getPasswordFromLocal=getIntent().getStringExtra("password");

        name.setText(getName);
        phone.setText(getPhnNo);
        password.setText(getPasswordFromLocal);
        c_password.setText(getPasswordFromLocal);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccountSettings.this,HomeScreen.class);
        startActivity(intent);
    }
}