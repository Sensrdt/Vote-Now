package com.example.votenow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.prefs.Preferences;

public class Login extends AppCompatActivity {

    ImageButton next;
    EditText phn,password;
    TextView register;
    CheckBox checkBox;

    String getPhone,getPassword;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

        SharedPreferences.Editor meditor;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            phn=findViewById(R.id.inputPhone);
            password=findViewById(R.id.inputPassword);
            next=findViewById(R.id.next);
            register=findViewById(R.id.register);
            checkBox=findViewById(R.id.checkBox);

            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Please Wait While We Log In");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Login In");
        progressDialog.setCanceledOnTouchOutside(false);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        meditor = sharedPreferences.edit();


        checkLogin();



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Phone.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin() {
        getPhone=sharedPreferences.getString("number","null");
        getPassword=sharedPreferences.getString("password","null");
        if(getPhone.equals("null") || getPassword.equals("null")){
            return;
        }
        else{
            volley();
        }
    }

    public String md5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            return String.format("%0"+(magnitude.length << 1) + "x",bi);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    private void check() {

        getPhone=phn.getText().toString();
        getPassword=password.getText().toString();

        if (getPhone.isEmpty()) {
            phn.setError("Enter the Phone Number");
        } else if (getPhone.length() != 10) {
            phn.setError("Phone Number Must be of 10digits");
        }else if(getPassword.isEmpty()){
            password.setError("Enter Password");
        }else if(getPassword.length()<4){
            password.setError("Password must be of 4 letters");
        }else {
            getPhone=getPhone;
            volley();
        }
    }


    private void volley() {
        progressDialog.show();
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();

        String url =getResources().getString(R.string.URL)+"login" ;
        try {
            jsonObject.accumulate("phn",getPhone);
            jsonObject.accumulate("passw", md5(getPassword));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            progressDialog.dismiss();
                            if(response.getBoolean("Done")==true){
                                if(checkBox.isChecked()) {

                                    meditor.putString("number", getPhone);
                                    meditor.putString("password",getPassword);
                                    meditor.commit();
                                }

                            }
                            String name=response.getString("Name");
                            String id = response.getString("ID");
                            Intent intent = new Intent(Login.this,StartPage.class);
                            intent.putExtra("name",name);
                            intent.putExtra("id",id);
                            intent.putExtra("phone",getPhone);
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Login Fail",Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "LogIn Fail", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsonObjectRequest);
        // Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
}
