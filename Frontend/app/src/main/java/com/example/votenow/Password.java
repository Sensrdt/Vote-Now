package com.example.votenow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.BufferedWriter;
import java.io.IOError;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.http.Url;


public class Password extends AppCompatActivity {
    EditText input;
    ImageButton next;
    EditText name, password, c_password;
    TextView phone;
    ProgressDialog progressDialog;

    String getPhnNo, getName, getPassword, getCPassword;
    SharedPreferences sharedPreferences;

    SharedPreferences.Editor meditor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
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


        getPhnNo = getIntent().getStringExtra("phnNo");
        phone.setText(getPhnNo);





        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                check();

            }
        });


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

    private void volley() {
        progressDialog.show();
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();

        //String url = "http://192.168.31.183:5555/register";
        String url =getResources().getString(R.string.URL)+"register" ;
        try {
            jsonObject.accumulate("name", getName);
            jsonObject.accumulate("phn",getPhnNo);
            jsonObject.accumulate("passw", md5(getPassword));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),response.getString("name"), Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(Password.this,Login.class);
                            meditor.putString("number", getPhnNo);
                            meditor.putString("password",getPassword);
                            meditor.commit();
                            startActivity(intent);
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Response error",Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsonObjectRequest);
       // Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
    }

    private void check() {

        getName = name.getText().toString();
        getPassword = password.getText().toString();
        getCPassword = c_password.getText().toString();

        if (getName.isEmpty()) {
            name.setError("Enter Name");
        } else if (getPassword.isEmpty()) {
            password.setError("Enter Password");
        } else if (getPassword.length() < 4) {
            password.setError("Password must be of 4 letters");
        } else if (getCPassword.isEmpty()) {
            c_password.setError("Enter Confirm Password");
        } else if (!(getCPassword.equals(getPassword))) {
            Toast.makeText(this, "Password did not match with Confirm Password", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            volley();


        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Password.this,Phone.class);
        startActivity(intent);
        finish();
    }
}