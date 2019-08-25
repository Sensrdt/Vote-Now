package com.example.votenow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class HomeScreen extends AppCompatActivity {

    private Button VN, AC, CV, LO,viewVote;
    String name, id, phoneNumber, getPassword,orgName;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor meditor;
    AlertDialog alertDialog;

    String createVoteURL,URL,viewCreatedVote,startVoteUrl,endVoteUrl,resultURL;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        VN = findViewById(R.id.voteNow);
        AC = findViewById(R.id.acntSettings);
        CV = findViewById(R.id.createVote);
        LO = findViewById(R.id.logout);
        viewVote=findViewById(R.id.viewVote);
        alertDialog=new AlertDialog.Builder(HomeScreen.this).create();
        Intent intent = getIntent();

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        meditor = sharedPreferences.edit();

        name=intent.getStringExtra("name");
        id=sharedPreferences.getString("id","null");
        phoneNumber=intent.getStringExtra("phone");
        getPassword=sharedPreferences.getString("password","null");


        //URL
        URL=getResources().getString(R.string.URL);
        startVoteUrl=URL+"voteControl/startVote";
        endVoteUrl=URL+"voteControl/endVote";
        createVoteURL=URL+"admin/register";
        viewCreatedVote=URL+"voteControl/status";

        CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog=new ProgressDialog(HomeScreen.this);
                progressDialog.setMessage("Please Wait While We Made You as Admin");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Create Vote");
                progressDialog.setCanceledOnTouchOutside(false);

                volleyCreateVote();





            }
        });
        AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSettings();

            }
        });


        VN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeScreen.this,StartPage.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                intent.putExtra("phone",phoneNumber);
                startActivity(intent);
                finish();
            }
        });
        LO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        viewVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //voteGoingOn();
                //voteNotStarted();
                //voteEnd();
                viewCreatedVoteVolley();
            }
        });
    }

    private void viewCreatedVoteVolley() {
        //progressDialog.show();

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id",id);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, viewCreatedVote,
                jsonObject,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            //progressDialog.dismiss();
                            if(response.getBoolean("Done")==true){

                                orgName=response.getString("orgName");
                                String status=response.getString("status");
                                if (status.equals("N"))
                                    voteNotStarted();
                                else if(status.equals("o"))
                                    voteGoingOn();
                                else
                                    voteEnd();
                                //startActivity(new Intent(HomeScreen.this,CreateVote.class));
                            }


                        }
                        catch (Exception e){
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.networkResponse.data.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsonObjectRequest);
        // Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
    }

    private void voteEnd() {
        alertDialog.setTitle("Vote Has Ended");
        alertDialog.setMessage("Do You Want To View The Result?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"View Result",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"No Result",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private void voteGoingOn(){
        alertDialog.setTitle("Vote is Going On");
        alertDialog.setMessage("Do You Want To Stop the Vote?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Stop Vote", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"Stop Vote",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"nkj",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void voteNotStarted(){
        alertDialog.setTitle("Vote Has Not Yet Started");
        alertDialog.setMessage("Do You Want Start The Vote or Cancel the Vote");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start Vote", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVoteVolley();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel Vote", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"Cancel",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }

    private void startVoteVolley() {
        progressDialog.show();

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id",id);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, startVoteUrl,
                jsonObject,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            progressDialog.dismiss();
                            if(response.getBoolean("Done")){
                                Toast.makeText(HomeScreen.this,"Vote has stared",Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"No Vote under You",Toast.LENGTH_SHORT).show();
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

    private void volleyCreateVote() {
        progressDialog.show();

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id",id);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createVoteURL,
                jsonObject,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            progressDialog.dismiss();
                            if(response.getBoolean("Done")==true){
                                startActivity(new Intent(HomeScreen.this,CreateVote.class));
                            }


                            }
                            catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.networkResponse.data.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsonObjectRequest);
        // Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
    }


    private void logout() {
        Intent intent = new Intent(HomeScreen.this,Login.class);
        meditor.putString("number", "null");
        meditor.putString("password","null");
        meditor.commit();
        startActivity(intent);
    }

    private void accountSettings() {
        Intent intent = new Intent(HomeScreen.this,AccountSettings.class);
        startActivity(intent);
        intent.putExtra("name",name);
        intent.putExtra("password",getPassword);
        intent.putExtra("phone",phoneNumber);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
    }

