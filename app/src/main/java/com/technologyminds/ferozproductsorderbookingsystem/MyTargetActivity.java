package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Helper;
import Utils.SqlDB;
import Utils.Target;
import Utils.User;

public class MyTargetActivity extends AppCompatActivity {
    Button btnBack;
    TextView txtTotalTarget,txtTotalAchieved,txtTotalRemaining,txtShowValueType;
    Context context;
    Activity activity;

    SqlDB db;
    ConnectionDetector conn;

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_target);

        context = this;
        activity = this;

        initializeControls();

        conn = new ConnectionDetector(this);

        db = new SqlDB(this);
        Constant.loginUser = db.getLoginedUser();

        if(conn.isConnectingToInternet()){
            getTarget(Constant.loginUser);
        }
        else{
            if(db.getUserTargetCount()==1){
                Target target = db.getUserTarget();
                if(target.getTotalTarget()>target.getTotalAchieved()){
                    txtTotalTarget.setText(target.getTotalTarget()+"");
                    txtTotalAchieved.setText(target.getTotalAchieved()+"");
                    int remaining = target.getTotalTarget()-target.getTotalAchieved();
                    txtTotalRemaining.setText(remaining+"");
                }
                else{
                    txtTotalTarget.setText(target.getTotalTarget()+"");
                    txtTotalAchieved.setText(target.getTotalAchieved()+"");
                    int remaining = target.getTotalAchieved()-target.getTotalTarget();
                    txtShowValueType.setText("Extra Sold");
                    txtTotalRemaining.setText(remaining+"");
                }
            }
            else{
                txtTotalTarget.setText("0");
                txtTotalAchieved.setText("0");
                txtTotalRemaining.setText("0");
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyTargetActivity.this, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    private void initializeControls(){
        btnBack = (Button) findViewById(R.id.btn_back);

        txtTotalTarget = (TextView) findViewById(R.id.txt_total_target);
        txtTotalAchieved = (TextView) findViewById(R.id.txt_total_achieve);
        txtTotalRemaining = (TextView) findViewById(R.id.txt_total_remaining);
        txtShowValueType = (TextView) findViewById(R.id.txt_show_value);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

    private void getTarget(final User user) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_targets";

        pDialog.setMessage("Getting Your Target ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_MY_TARGET+user.getId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject obj = new JSONObject(response);
                    Log.i("Status", obj.getString("error"));
                    boolean status = Boolean.parseBoolean(obj.getString("error"));
                    Log.i("Error", status+"-");
                    if(!status){
                        JSONObject objUser = obj.getJSONObject("user");
                        int userId =    Integer.parseInt(objUser.getString("id"));
                        int totalTarget=0,totalAchieve=0,totalRemaining=0;
                        if(!objUser.getString("total_target").contentEquals("null")){
                            totalTarget =  Integer.parseInt(objUser.getString("total_target"));
                            if(!objUser.getString("achieve_target").contentEquals("null")){
                                totalAchieve = Integer.parseInt(objUser.getString("achieve_target"));
                                if(totalTarget>totalAchieve){
                                    totalRemaining = totalTarget - totalAchieve;
                                }
                                else{
                                    txtShowValueType.setText("Extra Sold");
                                    totalRemaining = totalAchieve - totalTarget;
                                }
                            }
                            else{
                                totalAchieve=0;
                            }

                        }
                        Target target = new Target();

                        target.setUserId(Constant.loginUser.getId());
                        target.setTotalTarget(totalTarget);
                        target.setTotalAchieved(totalAchieve);

                        db.deleteAllRecords("user_target");

                        db.addTarget(target);

                        txtTotalTarget.setText(totalTarget+"");
                        txtTotalAchieved.setText(totalAchieve+"");
                        txtTotalRemaining.setText(totalRemaining+"");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Password Response", "Response: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(user.getId()));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
