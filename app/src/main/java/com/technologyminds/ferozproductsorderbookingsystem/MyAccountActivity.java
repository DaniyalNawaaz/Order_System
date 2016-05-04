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
import android.widget.EditText;
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
import Utils.User;

public class MyAccountActivity extends AppCompatActivity {
    Button btnBack,btnOk;
    EditText edCurrentPassword, edNewPassword, edConfirmPassword;
    Context context;
    Activity activity;

    SqlDB db;
    ConnectionDetector conn;
    boolean isConnectToInternet = false;

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        context = this;
        activity = this;

        initializeControls();

        conn = new ConnectionDetector(this);

        db = new SqlDB(this);
        Constant.loginUser = db.getLoginedUser();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAccountActivity.this, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = edCurrentPassword.getText().toString();
                String newPassword = edNewPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();

                if(!currentPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()){
                    if(newPassword.contentEquals(confirmPassword)){
                        if(Constant.loginUser.getPassword().contentEquals(currentPassword)){
                            isConnectToInternet = conn.isConnectingToInternet();
                            if(isConnectToInternet){
                                changePassword(Constant.loginUser,newPassword);

                            }
                            else{
                                Helper.showAlertDialog(MyAccountActivity.this, "No Internet Connection",
                                        "You don't have internet connection.");
                            }
                        }
                        else{
                            Helper.showAlertDialog(MyAccountActivity.this, "Incorrect Old Password",
                                    "You enter incorrect old password.");
                        }

                    }
                    else{
                        Helper.showToast(MyAccountActivity.this,"New & Confirm Password fields must be Same");
                    }
                }
                else{
                    Helper.showToast(MyAccountActivity.this, "Please enter all fields");
                }

            }
        });
    }

    private void initializeControls(){
        btnBack = (Button) findViewById(R.id.btn_back);
        btnOk = (Button) findViewById(R.id.btn_ok);

        edCurrentPassword = (EditText) findViewById(R.id.current_password);
        edNewPassword = (EditText) findViewById(R.id.new_password);
        edConfirmPassword = (EditText) findViewById(R.id.confirm_password);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

    String changedPassword;

    private void changePassword(final User user, final String newPassword) {
        // Tag used to cancel the request
        String tag_string_req = "req_change_password";
        changedPassword = newPassword;

        pDialog.setMessage("Changing Password ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_MY_ACCOUNT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String status = jObj.getString("status");
                        User updatedUser = new User();
                        updatedUser.setId(Constant.loginUser.getId());
                        updatedUser.setName(Constant.loginUser.getName());
                        updatedUser.setAddress(Constant.loginUser.getAddress());
                        updatedUser.setContact(Constant.loginUser.getContact());
                        updatedUser.setCnic(Constant.loginUser.getCnic());
                        updatedUser.setUser_name(Constant.loginUser.getUser_name());
                        updatedUser.setPassword(changedPassword);
                        updatedUser.setLatitude(Constant.loginUser.getLatitude());
                        updatedUser.setLongitude(Constant.loginUser.getLongitude());
                        updatedUser.setGcm_id(Constant.loginUser.getGcm_id());

                        Log.i("User Id", updatedUser.getId() + "");
                        Log.i("User Password",updatedUser.getPassword()+"");

                        db.updateUserPassword(updatedUser);
                        Constant.loginUser = db.getLoginedUser();
                        Helper.showToast(MyAccountActivity.this,status);
                        Intent intent = new Intent(MyAccountActivity.this,
                                DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();



//                        User us = new User(Integer.parseInt(uid), name,address, contact_num, cnic,
//                                   Integer.parseInt(zone_id), user_name,password, Double.parseDouble(latitude), Double.parseDouble(longiutde),gcm_id);

                        //db.addUser(us);
                        //Constant.loginUser = us;//db.getLoginedUser();

                        //Log.i("User Data", Constant.loginUser.getGcm_id() + " --- " + Constant.loginUser.getName());
                        /*Intent intent = new Intent(MainActivity.this,
                                DashboardActivity.class);
                        startActivity(intent);
                        finish();*/
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("username", user.getUser_name());
                params.put("current_password", user.getPassword());
                params.put("new_password", newPassword);

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
