package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.Category;
import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Helper;
import Utils.SqlDB;
import Utils.Store;
import Utils.User;
import Utils.Zone;

public class RegisterStoreActivity extends AppCompatActivity {
    Button btnBack,btnRegister;
    Spinner spinnerCategory,spinnerStoreCarry,spinnerZone;
    EditText edStoreName, edStoreAddress, edContact, edOwnerContact;
    //TextView edStoreRegion;

    Context context;
    Activity activity;

    SqlDB db;
    ConnectionDetector conn;
    boolean isConnectToInternet = false;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);

        context = this;
        activity = this;
        initializeControls();

        conn = new ConnectionDetector(this);

        db = new SqlDB(this);

        List<Category> categoryList = db.getAllCategory();
        String[] items = new String[categoryList.size()+1];
        items[0] = "Select Category";
        for(int i=0;i<categoryList.size();i++){
            items[i+1] = categoryList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        String[] carryItems = {"Select Option","Stand","Basket","None"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, carryItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStoreCarry.setAdapter(adapter1);

        if(db.getUserCount()==1){
            Constant.loginUser = db.getLoginedUser();

            List<Zone> zoneList = db.getAllZone();
            Log.i("Zone List Size",zoneList.size()+"");
            String[] zoneItems = new String[zoneList.size()+1];
            zoneItems[0] = "Select Zone";
            for(int i=0;i<zoneList.size();i++){
                zoneItems[i+1] = zoneList.get(i).getZoneName();
            }

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, zoneItems);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerZone.setAdapter(adapter2);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterStoreActivity.this,DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeName = edStoreName.getText().toString();
                String storeAddress = edStoreAddress.getText().toString();
                String storeRegion = spinnerZone.getSelectedItem().toString();
                String contact = edContact.getText().toString();
                String ownerContact = edOwnerContact.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String carry = spinnerStoreCarry.getSelectedItem().toString();

                if(!storeName.isEmpty() && !storeAddress.isEmpty() && !storeRegion.isEmpty()
                        && !contact.isEmpty() && !ownerContact.isEmpty() && !category.isEmpty() && !carry.isEmpty()){

                    if(!storeRegion.contentEquals("Select Zone") && !category.contentEquals("Select Category")
                            && !carry.contentEquals("Select Option")){
                        isConnectToInternet = conn.isConnectingToInternet();
                        int zoneId=-1;
                        List<Zone> zoneList = db.getAllZone();
                        for(int i=0;i<zoneList.size();i++){
                            if(zoneList.get(i).getZoneName().contentEquals(storeRegion)){
                                zoneId = zoneList.get(i).getId();
                                break;
                            }
                        }
                        Store store = new Store();
                        if(zoneId>-1) {

                            store.setStoreName(storeName);
                            store.setStoreAddress(storeAddress);
                            store.setContact(contact);
                            store.setContactPerson(ownerContact);
                            store.setCarryName(carry);
                            store.setZoneId(zoneId);
                        }
                        else{
                            Helper.showToast(RegisterStoreActivity.this, "Zone Not Found");
                        }
                        if(isConnectToInternet){
                            registerStore(store, category);
                        }
                        else{
                            List<Category> listCategory = db.getAllCategory();
                            for(int i=0;i<listCategory.size();i++){
                                if(listCategory.get(i).getName().contentEquals(category)){
                                    store.setCategoryId(listCategory.get(i).getId());
                                    db.addOfflineStore(store);
                                    Helper.showToast(RegisterStoreActivity.this, "Store Registered Successfully");
                                    break;
                                }
                            }


                            Intent intent = new Intent(RegisterStoreActivity.this,
                                    DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
//                        Helper.showAlertDialog(RegisterStoreActivity.this, "No Internet Connection",
//                                "You don't have internet connection.");
                        }
                    }
                    else{
                        Helper.showToast(RegisterStoreActivity.this, "Please fill the complete form");
                    }
                }
                else{
                    Helper.showToast(RegisterStoreActivity.this, "Please fill the complete form");
                }
            }
        });
    }

    private void initializeControls(){
        btnBack = (Button) findViewById(R.id.btn_back);
        btnRegister = (Button) findViewById(R.id.btn_register);

        edStoreName = (EditText) findViewById(R.id.store_name);
        edStoreAddress = (EditText) findViewById(R.id.store_address);
        //edStoreRegion = (TextView) findViewById(R.id.store_region);
        edContact = (EditText) findViewById(R.id.store_contact);
        edOwnerContact = (EditText) findViewById(R.id.store_owner_contact);

        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        spinnerStoreCarry = (Spinner) findViewById(R.id.spinner_store_carry);
        spinnerZone = (Spinner) findViewById(R.id.spinner_zones);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //spinnerCategory.setPrompt("Select Category");

    }

    private void registerStore(final Store store, final String category) {
        // Tag used to cancel the request
        String tag_string_req = "req_register_store";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_REGISTER_STORE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Login Response", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String id = jObj.getString("id");

                        JSONObject store = jObj.getJSONObject("store");
                        String name = store.getString("name");
                        String address = store.getString("address");
                        String contact_num = store.getString("contact_num");
                        String contact_person = store.getString("contact_person");
                        String zone_id = store.getString("zone_id");
                        String category_id = store.getString("category_id");
                        Log.i("Category ID",category_id);
                        String carryName = store.getString("carry_name");

                        Store registeredStore = new Store();
                        registeredStore.setStoreId(Integer.parseInt(id));
                        registeredStore.setStoreName(name);
                        registeredStore.setStoreAddress(address);
                        registeredStore.setContact(contact_num);
                        registeredStore.setContactPerson(contact_person);
                        registeredStore.setZoneId(Integer.parseInt(zone_id));
                        registeredStore.setCategoryId(Integer.parseInt(category_id));
                        Log.i("Category ID --- ", registeredStore.getCategoryId() + "");
                        registeredStore.setCarryName(carryName);

                        db.addStore(registeredStore);
                        Log.i("Store Count", db.getStoreCount() + "");
                        Helper.showToast(RegisterStoreActivity.this, "Store Registered Successfully");
                        Intent intent = new Intent(RegisterStoreActivity.this,
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
                Log.e("Login Error", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("storename", store.getStoreName());
                params.put("address", store.getStoreAddress());
                params.put("contact_num", store.getContact());
                params.put("contact_person", store.getContactPerson());
                params.put("zone_id", String.valueOf(store.getZoneId()));
                params.put("category", category);
                params.put("carry", store.getCarryName());

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
