package com.technologyminds.ferozproductsorderbookingsystem;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.Category;
import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Helper;
import Utils.Instruction;
import Utils.Notification;
import Utils.Product;
import Utils.Product_Flavour;
import Utils.SqlDB;
import Utils.Store;
import Utils.User;
import Utils.Zone;


public class MainActivity extends AppCompatActivity {

    EditText edUsername,edPassword;
    Button btnLogin;
    SqlDB db;
    ConnectionDetector conn;

    private ProgressDialog pDialog;
    int lastIndex=-1;
    Context context;
    private Activity activity;

    private static final int request = 1;
    boolean canGetLocation = false;
    boolean isConnectToInternet = false;

    GoogleCloudMessaging gcmObj;
    String gcmid;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    int Role = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        activity = this;
        initializeControls();

        conn = new ConnectionDetector(this);

        db = new SqlDB(this);

        if(db.getUserCount()==1){
            Constant.loginUser = db.getLoginedUser();
            if(Constant.loginUser.getIs_login()==1){
                Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                startActivity(i);
                finish();
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                isConnectToInternet = conn.isConnectingToInternet();
                if(!username.isEmpty() && !password.isEmpty()){
                    if(isConnectToInternet){
                        if(checkPlayServices()){
                            db.deleteAllRecords("user");
                            db.deleteAllRecords("zone");
                            db.deleteAllRecords("store");
                            db.deleteAllRecords("category");
                            db.deleteAllRecords("product");
                            db.deleteAllRecords("product_flavour");
                            db.deleteAllRecords("instruction");
                            db.deleteAllRecords("news_notification");
                            checkLogin(username, password);
//                            if(userLogin!=null){
//                                insertGcmID(userLogin);
//                            }
                        }
                    }
                    else{

                        if(db.getUserCount()==1){
                            Constant.loginUser = db.getLoginedUser();
                            db.deleteAllRecords("user");
                            Constant.loginUser.setIs_login(1);
                            db.addUser(Constant.loginUser);
                            Constant.loginUser = db.getLoginedUser();
                            if(Constant.loginUser.getIs_login()==1){
                                Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{

                            }

                        }
                        else{
                            Helper.showAlertDialog(MainActivity.this, "No Internet Connection",
                                    "You don't have internet connection.");
                        }
                    }

                }
                else{
                    Helper.showToast(MainActivity.this,"Please Enter the Credentials");
                }
            }
        });
    }

    private boolean checkPlayServices(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Log.i("Check Play Service", "This device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }

    private void initializeControls(){
        edUsername = (EditText) findViewById(R.id.ed_username);
        edPassword = (EditText) findViewById(R.id.ed_password);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //edUsername.setText("Login");
        //edPassword.setText("Login");
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Login Response", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String uid = jObj.getString("id");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String address = user.getString("address");
                        String contact_num = user.getString("contact_num");
                        String cnic = user.getString("cnic");
                        int userRole = Integer.parseInt(user.getString("user_role"));
                        //String zone_id = user.getString("zone_id");
                        //String zone_name = user.getString("zone_name");
                        String user_name = user.getString("user_name");
                        String password = user.getString("password");
                        String latitude = user.getString("latitude");
                        String longiutde = user.getString("longitude");
                        String gcm_id = user.getString("gcm_id");

                        if(gcm_id.contentEquals("null")){
                            Constant.loginUser = new User(Integer.parseInt(uid), name,address, contact_num, cnic,userRole,
                                    user_name,password, Double.parseDouble(latitude), Double.parseDouble(longiutde),gcm_id,1);
                            Role = Constant.loginUser.getUser_role();
                            new getGcmRegistration().execute();
                        }
                        else{
                            Constant.loginUser = new User(Integer.parseInt(uid), name,address, contact_num, cnic,userRole,
                                    user_name,password, Double.parseDouble(latitude), Double.parseDouble(longiutde),gcm_id,1);
                            db.addUser(Constant.loginUser);
                            Constant.loginUser = db.getLoginedUser();
                            Role = Constant.loginUser.getUser_role();
                            Log.i("Caliing", "Calling");
                            getAllZones(String.valueOf(Constant.loginUser.getId()));
                            //insertGcmID(Constant.loginUser);
                            //getAllCategory("A121");
//                            Constant.loginUser = new User(Integer.parseInt(uid), name,address, contact_num, cnic,
//                                    Integer.parseInt(zone_id),zone_name, user_name,password, Double.parseDouble(latitude), Double.parseDouble(longiutde),gcm_id);
//                            db.addUser(Constant.loginUser);
//                            Intent intent = new Intent(MainActivity.this,
//                                    DashboardActivity.class);
//                            startActivity(intent);
//                            finish();
                        }



//                        User us = new User(Integer.parseInt(uid), name,address, contact_num, cnic,
//                                   Integer.parseInt(zone_id), user_name,password, Double.parseDouble(latitude), Double.parseDouble(longiutde),gcm_id);




                        //db.addUser(us);
                        //Constant.loginUser = us;//db.getLoginedUser();

                        Log.i("User Data", Constant.loginUser.getGcm_id() + " --- " + Constant.loginUser.getName());
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
                Log.e("Login Error", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

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

    public class getGcmRegistration extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String msg="";
            try {
                if (gcmObj == null) {
                    gcmObj = GoogleCloudMessaging
                            .getInstance(getApplicationContext());
                }
                gcmid = gcmObj
                        .register(Constant.GOOGLE_PROJ_ID);
                //Helper.showToast(MainActivity.this,gcm_id);
                Log.i("GCM ID ",gcmid);
            }catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                Log.i("Error GCM",msg);
            }
            if(gcmid!=null) {
                Constant.loginUser.setGcm_id(gcmid);
                Log.i("Login User", Constant.loginUser.getName() + " ---- " + Constant.loginUser.getGcm_id());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(Constant.loginUser.getGcm_id()!=null){
                Log.i("Successful","Yahoo");
                insertGcmID(Constant.loginUser);
            }
        }
    }

    private void insertGcmID(final User user){
        String tag_string_req = "req_gcm_update";

        pDialog.setMessage("Please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_INSERT_GCM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("INSERT GCM Response", "INSERT GCM Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        db.addUser(user);
                        Constant.loginUser = db.getLoginedUser();
                        Log.i("Caliing", "Calling");
                        getAllZones(String.valueOf(Constant.loginUser.getId()));

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
                Log.e("GCM Insertion Error", "GCM Insertion Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(user.getId()));
                params.put("gcm_id", user.getGcm_id());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getAllZones(final String userId){
        String tag_string_req = "req_all_zones";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_ZONE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "GET ALL ZONE Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){

                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj =jsonArray.getJSONObject(i);
                            Zone zone = new Zone();
                            zone.setId(Integer.parseInt(obj.getString("id")));
                            zone.setZoneName(obj.getString("name"));
                            zone.setUserId(Integer.parseInt(obj.getString("userid")));
                            db.addZone(zone);
                        }

                        getAllCategory("A128");

//                        Intent intent = new Intent(MainActivity.this,
//                                DashboardActivity.class);
//                        startActivity(intent);
//                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GET ALL ZONE ERROR", "GET ALL ZONE Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getAllCategory(final String key){
        String tag_string_req = "req_all_category";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "GET ALL CATEGORY Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){
                        
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj =jsonArray.getJSONObject(i);
                            Category category = new Category();
                            category.setId(Integer.parseInt(obj.getString("id")));
                            category.setName(obj.getString("name"));
                            db.addCategory(category);
                        }

                        getAllInstruction("A129");

//                        Intent intent = new Intent(MainActivity.this,
//                                DashboardActivity.class);
//                        startActivity(intent);
//                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GET ALL CATEGORY ERROR", "GET ALL CATEGORY Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", key);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getAllInstruction(final String key){
        String tag_string_req = "req_all_instruction";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_INSTRUCTIONS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "GET ALL INSTRUCTION Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){

                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj =jsonArray.getJSONObject(i);
                            Instruction instruction = new Instruction();
                            instruction.setId(Integer.parseInt(obj.getString("id")));
                            instruction.setName(obj.getString("name"));
                            db.addInstruction(instruction);
                        }
                        getAllNotification("A122",String.valueOf(Constant.loginUser.getId()));
                        //getAllNotification("A122");

//                        Intent intent = new Intent(MainActivity.this,
//                                DashboardActivity.class);
//                        startActivity(intent);
//                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "GET ALL INSTRUCTION Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", key);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getAllNotification(final String key,final String userId){
        String tag_string_req = "req_all_notification";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before",tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_USER_NOTIFICATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "Get All Notication Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){
                        getAllProduct("A202");
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

                        getAllProduct("A202");
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

    List<Zone> zoneList;

    private void getAllProduct(final String key){
        String tag_string_req = "req_all_products";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before",tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_PRODUCT_WITH_FLAVOUR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "Get All Product Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){

                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj =jsonArray.getJSONObject(i);
                            Product product = new Product();
                            product.setProductId(Integer.parseInt(obj.getString("id")));
                            product.setProductName(obj.getString("name"));
                            Log.i("User Role",Role+"-");
                            if(Role!=-1){
                                if(Role==3){
                                    product.setPrice(Double.parseDouble(obj.getString("price")));
                                }
                                else{
                                    if(Role==4){
                                        product.setPrice(Double.parseDouble(obj.getString("price_2")));
                                    }
                                }
                            }

                            Log.i("Price Set",product.getPrice()+"-");


                            JSONArray jsonFlavourArray = obj.getJSONArray("flavours");

                            for(int j=0;j<jsonFlavourArray.length();j++){
                                JSONObject flv =jsonFlavourArray.getJSONObject(j);
                                Product_Flavour product_flavour = new Product_Flavour();
                                product_flavour.setProductId(Integer.parseInt(flv.getString("product_id")));
                                product_flavour.setFlavourId(Integer.parseInt(flv.getString("flavour_id")));
                                product_flavour.setFlavourName(flv.getString("flavour_name"));
                                product_flavour.setImagePath(flv.getString("image_path"));
                                product_flavour.setPrice(0.00);//Double.parseDouble(flv.getString("sub_price")));

                                db.addProductFlavour(product_flavour);
                            }

                            db.addProduct(product);
                        }

                        zoneList = db.getAllZone();
                        lastIndex = zoneList.size()-1;
                        currentIndex=0;
                        //for(int i=0;i<zoneList.size();i++){
                            getAllStores(String.valueOf(zoneList.get(currentIndex).getId()),currentIndex);
                        //}

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
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void getAllStores(final String zoneID, final int index){
        String tag_string_req = "req_all_stores";

        pDialog.setMessage("Please wait ...");
        showDialog();
        currentIndex = index;

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_STORES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "GET ALL STORES Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("Store Not Available")){
//                        Intent intent = new Intent(MainActivity.this,
//                                DashboardActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                    else{
                        new storeParsingInBackground().execute(response);
//                        JSONArray jsonArray = new JSONArray(response);
//
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject obj =jsonArray.getJSONObject(i);
//                            Store store = new Store();
//                            store.setStoreId(Integer.parseInt(obj.getString("store_id")));
//                            store.setStoreName(obj.getString("store_name"));
//                            store.setStoreAddress(obj.getString("address"));
//                            store.setContact(obj.getString("contact_num"));
//                            store.setContactPerson(obj.getString("contact_person"));
//                            store.setZoneId(Integer.parseInt(obj.getString("zone_id")));
//                            if(!obj.getString("category_id").contentEquals("null")){
//                                store.setCategoryId(Integer.parseInt(obj.getString("category_id")));
//                            }
//                            else{
//                                store.setCategoryId(-1);
//                            }
//                            store.setCarryName(obj.getString("store_have"));
//                            db.addStore(store);
//                        }
//
//                        if(index==lastIndex){
//                            Intent intent = new Intent(MainActivity.this,
//                                    DashboardActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GET ALL STORES ERROR", "GET ALL STORES Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("zone_id", zoneID);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    int currentIndex=-1;
    public class storeParsingInBackground extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String msg="";
            try {
                String response = params[0];

                JSONArray jsonArray = new JSONArray(response);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject obj =jsonArray.getJSONObject(i);
                    Store store = new Store();
                    store.setStoreId(Integer.parseInt(obj.getString("store_id")));
                    store.setStoreName(obj.getString("store_name"));
                    store.setStoreAddress(obj.getString("address"));
                    store.setContact(obj.getString("contact_num"));
                    store.setContactPerson(obj.getString("contact_person"));
                    store.setZoneId(Integer.parseInt(obj.getString("zone_id")));
                    if(!obj.getString("category_id").contentEquals("null")){
                        store.setCategoryId(Integer.parseInt(obj.getString("category_id")));
                    }
                    else{
                        store.setCategoryId(-1);
                    }
                    store.setCarryName(obj.getString("store_have"));
                    db.addStore(store);
                }



            }catch (JSONException ex) {
                msg = "Error :" + ex.getMessage();
                Log.i("Error GCM",msg);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.i("Current Index",currentIndex+"--");
            Log.i("Last Index", lastIndex + "--");
            if(currentIndex==lastIndex){
                Intent intent = new Intent(MainActivity.this,
                        DashboardActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                if(currentIndex<lastIndex){
                    currentIndex++;
                    getAllStores(String.valueOf(zoneList.get(currentIndex).getId()),currentIndex);
                }
            }
        }
    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }



    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(MainActivity.this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},request);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.canGetLocation = true;

                } else {
                    this.canGetLocation = false;
                }
                break;
        }
    }

}
