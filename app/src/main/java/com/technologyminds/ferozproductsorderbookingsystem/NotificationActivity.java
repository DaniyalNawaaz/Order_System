package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.NotificationAdapter;
import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Helper;
import Utils.Notification;
import Utils.SqlDB;

public class NotificationActivity extends AppCompatActivity {
    ListView listNotification;
    List<Notification> notifList;
    NotificationAdapter adapter;
    Button btnBack;
    Context context;

    ConnectionDetector conn;
    private ProgressDialog pDialog;

    SqlDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        context = this;
        initializeControls();
        db = new SqlDB(this);
        conn = new ConnectionDetector(this);

        if(conn.isConnectingToInternet()){
            db.deleteAllRecords("news_notification");
            getAllNotification("A102",String.valueOf(Constant.loginUser.getId()));
        }
        else{
            int count = db.getNotificationCount();
            if(count>0){
                notifList = db.getAllNotification();
                adapter = new NotificationAdapter(this,notifList);
                listNotification.setAdapter(adapter);
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                // Setting Dialog Title
                alertDialog.setTitle("Metro Notification");

                // Setting Dialog Message
                alertDialog.setMessage("There is no notifications for you");

                // Setting alert dialog icon
                //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
                //Helper.showAlertDialog(context,"Metro App Alert","There is no notifications for you");
            }
        }

//        int count = db.getNotificationCount();
//        if(count>0){
//            notifList = db.getAllNotification();
//            adapter = new NotificationAdapter(this,notifList);
//            listNotification.setAdapter(adapter);
//        }
//        else{
//            notifList = new ArrayList<>();
//            notifList.add(new Notification("Your Order 1 has been submitted","3/18/2016","3:27pm"));
//            notifList.add(new Notification("Your Order 2 has been submitted","3/18/2016","3:27pm"));
//            notifList.add(new Notification("Your Order 3 has been submitted","3/18/2016","3:27pm"));
//            notifList.add(new Notification("Your Order 4 has been submitted","3/18/2016","3:27pm"));
//            notifList.add(new Notification("Your Order 5 has been submitted","3/18/2016","3:27pm"));
//            notifList.add(new Notification("Your Order 6 has been submitted","3/18/2016","3:27pm"));
//            notifList.add(new Notification("Your Order 7 has been submitted","3/18/2016","3:27pm"));
//            notifList.add(new Notification("Your Order 8 has been submitted","3/18/2016","3:27pm"));
//
//            adapter = new NotificationAdapter(this,notifList);
//            listNotification.setAdapter(adapter);
//        }



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotificationActivity.this, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

    }

    private void initializeControls(){
        listNotification = (ListView) findViewById(R.id.list_notification);
        btnBack = (Button) findViewById(R.id.btn_back);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        listNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification selectedNotification = notifList.get(position);
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                // Setting Dialog Title
                alertDialog.setTitle("Metro Notification");

                // Setting Dialog Message
                alertDialog.setMessage(selectedNotification.getMessage());

                // Setting alert dialog icon
                //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });
    }

    public static void showAlertDialogForNotification(final Context context, String title, String message) {

    }

    private void getAllNotification(final String key, final String userId){
        String tag_string_req = "req_all_notification";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_USER_NOTIFICATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "Get All Notication Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){
                        Helper.showAlertDialog(context,"Metro App Alert","There is no notifications for you");
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj =jsonArray.getJSONObject(i);
                            Notification notification = new Notification();
                            notification.setMessage(obj.getString("message"));
                            notification.setDate(obj.getString("date"));
                            notification.setTime(obj.getString("time"));
                            db.addNotification(notification);
                        }


                        int count = db.getNotificationCount();
                        if(count>0){
                            notifList = db.getAllNotification();
                            adapter = new NotificationAdapter(context,notifList);
                            listNotification.setAdapter(adapter);
                        }
                        else{
                            Helper.showAlertDialog(context,"Metro App Alert","There is no notifications for you");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Notification Error", "Notification Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", key);
                params.put("user_id", userId);
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
