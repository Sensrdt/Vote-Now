package com.example.votenow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class StartPage extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    LinearLayout logout,acnt,createVote;
    ImageButton next;
    EditText vCode;
    private String getvCode,name,id,phoneNumber,getPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor meditor;

    ProgressDialog progressDialog;


    boolean aBoolean=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        next=findViewById(R.id.next);
        logout=findViewById(R.id.logoutLayout);
        acnt=findViewById(R.id.acntLayout);
        createVote=findViewById(R.id.createVoteLayout);
        vCode=findViewById(R.id.inputText);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        meditor = sharedPreferences.edit();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait While We Log In");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Login In");
        progressDialog.setCanceledOnTouchOutside(false);

        Intent intent=getIntent();

        name=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
        phoneNumber=intent.getStringExtra("phone");
        getPassword=sharedPreferences.getString("password","null");

        Toast.makeText(this,name+" "+id,Toast.LENGTH_SHORT).show();


        invisibleActionbar();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aBoolean)
                    visibleActionbar();
                else
                    invisibleActionbar();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        acnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSettings();
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntent();
            }
        });
        createVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this, CreateVote.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void accountSettings() {
        Intent intent = new Intent(StartPage.this,AccountSettings.class);
        startActivity(intent);
        intent.putExtra("name",name);
        intent.putExtra("password",getPassword);
        intent.putExtra("phone",phoneNumber);
        startActivity(intent);
    }

    private void nextIntent() {
        getvCode=vCode.getText().toString();
        if(getvCode.isEmpty()){
            vCode.setError("Vote Code is Empty");
        }
        else{
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
                jsonObject.accumulate("voteCode",getvCode);
                jsonObject.accumulate("Id", id);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("Done")) {
                                    String voteName=response.getString("");
                                    JSONArray candidateName=response.getJSONArray("");
                                    Intent intent=new Intent(StartPage.this,VoteNow.class);
                                    intent.putExtra("Name",voteName);
                                    intent.putExtra("Candidates", (Parcelable) candidateName);

                                }
                            }catch (Exception e){

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "You can't vote for this", Toast.LENGTH_LONG).show();
                        }
                    });

            queue.add(jsonObjectRequest);
            // Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
        }




    private void logout() {
        Intent intent = new Intent(StartPage.this,Login.class);
        meditor.putString("number", "null");
        meditor.putString("password","null");
        meditor.commit();
        startActivity(intent);
    }

    private void visibleActionbar() {
        aBoolean=false;
        logout.setVisibility(View.VISIBLE);
        acnt.setVisibility(View.VISIBLE);
        createVote.setVisibility(View.VISIBLE);
        next.setVisibility(View.INVISIBLE);

    }

    private void invisibleActionbar() {
        aBoolean=true;
        logout.setVisibility(View.INVISIBLE);
        acnt.setVisibility(View.INVISIBLE);
        createVote.setVisibility(View.INVISIBLE);
        next.setVisibility(View.VISIBLE);
    }
}