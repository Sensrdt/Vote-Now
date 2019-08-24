package com.example.votenow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Phone extends AppCompatActivity {
    EditText input;
    ImageButton next;
    ImageView imageView;
    String phoneNo;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        input = findViewById(R.id.inputText);
        next = findViewById(R.id.next);
        imageView = findViewById(R.id.imageView);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Checking Phone Number");
        progressDialog.setMessage("Please Wait While We Check Your Phone Number");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        input.setHint("Phone Number");
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        imageView.setImageResource(R.drawable.phone);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo = input.getText().toString();
                if (phoneNo.isEmpty()) {
                    input.setError("Enter the Phone Number");

                } else if (phoneNo.length() != 10) {
                    input.setError("Phone Number Must be of 10digits");
                } else {
                    volley();


                }
            }

        });
    }

    private void volley() {
        progressDialog.show();
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();

        //String url = "http://192.168.31.183:5555/register";
        String url = "http://chisel-trawler.glitch.me/register_check";
        try {
            jsonObject.accumulate("phn", phoneNo);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(Phone.this, V_Code.class);
                                intent.putExtra("phnNo", phoneNo);
                                startActivity(intent);






                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Phone Number Already Register",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Phone.this, Login.class);
                        intent.putExtra("phnNo", phoneNo);
                        startActivity(intent);
                    }
                });

        queue.add(jsonObjectRequest);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Phone.this,Login.class);
        startActivity(intent);
        finish();
    }
}
