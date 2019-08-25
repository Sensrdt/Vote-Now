package com.example.votenow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateVote extends AppCompatActivity {

    ArrayList<EditText> candidates = null;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String id;
    EditText editText;
    String VOTE_ID;
    String orgURL,candidateURL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate);
        candidates = new ArrayList<>();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait While We Log In");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Login In");
        progressDialog.setCanceledOnTouchOutside(false);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

         id=sharedPreferences.getString("id","null");


        editText=findViewById(R.id.inputText);
        String URL= getResources().getString(R.string.URL);
        orgURL=URL+id+"/org_register";
        candidateURL=URL+id+"id/updateCandidate";


        ((Button)findViewById(R.id.addNew)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean err = false;
                for(EditText er: candidates)
                    if(er.getText().toString().trim().equals("")) {
                        er.setError("Cannot be empty");
                        err = true;
                    }
                if(err) return;
                EditText et = new EditText(CreateVote.this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                et.setLayoutParams(p);
                et.setHint("Name");
                et.setSingleLine();
                et.setId(candidates.size() + 1);
                ((LinearLayout)findViewById(R.id.candidateLayout)).addView(et);
                candidates.add(et);


            }
        });

        ((ImageButton)findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalString="";
                for(EditText et: candidates) {
                    if (et.getText().equals("")) {
                    }
                    finalString += et.getText() + ",";
                }
                Toast.makeText(CreateVote.this,finalString,Toast.LENGTH_LONG).show();
                volleyOrg();
            }
        });
    }

    private void volleyOrg() {
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();

        //String url = "http://192.168.31.183:5555/register";
        String url =getResources().getString(R.string.URL)+"register" ;
        try {
            jsonObject.accumulate("id",id );
            jsonObject.accumulate("orgName",editText.getText().toString());

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, orgURL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("Done")){
                                VOTE_ID=response.getString("ID");
                                Toast.makeText(CreateVote.this,VOTE_ID,Toast.LENGTH_LONG).show();
                                volleyCandidates();
                            }

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

    private void volleyCandidates() {
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();

        //String url = "http://192.168.31.183:5555/register";
        //String url =getResources().getString(R.string.URL)+"register" ;
        try {
            jsonObject.accumulate("id",id );
            jsonObject.accumulate("candidates",candidates);
            jsonObject.accumulate("orgName",editText.getText().toString());

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, candidateURL
                ,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("Done")){
                                Toast.makeText(CreateVote.this,"Vote Created",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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






    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateVote.this,HomeScreen.class);
        startActivity(intent);
    }
}
