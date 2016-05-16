package com.technologyminds.ferozproductsorderbookingsystem;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.wearable.DataItemAsset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.Category;
import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Helper;
import Utils.Instruction;
import Utils.Item;
import Utils.Order;
import Utils.Product;
import Utils.Product_Flavour;
import Utils.SqlDB;
import Utils.Store;
import Utils.User;

public class DashboardActivity extends AppCompatActivity {

    TextView txtBookAnOrder,txtRegisterStore,txtPastOrder,txtMyAccount,txtNotification,txtContactUs;
    LinearLayout menuNewOrder,menuRegisterOrder,menuPastOrder,menuMyAccount,menuNotification,menuContactUs;
    ImageView ivPic;

    SqlDB db;

    //JSONParser parser;
    private Activity activity;

    private static final int request = 1;
    boolean canGetLocation = false;
    private ProgressDialog pDialog;
    GPSTracker gps;
    Context context;
    ConnectionDetector conn;
    String category="";
    int lastIndex=-1;
    int lastOtherIndex=-1;
    int totalQuantity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeControls();
        db = new SqlDB(this);

        activity = this;
        context=this;
        conn = new ConnectionDetector(this);
        gps = new GPSTracker(context);

        Constant.loginUser = db.getLoginedUser();
        startService(new Intent(this, LocationService.class));

        if (!checkPermission()) {
            requestPermission();
        } else {
            //Toast.makeText(getApplicationContext(), "Please Already Granted.", Toast.LENGTH_LONG).show();
            if(conn.isConnectingToInternet()){
                if(checkPermission()){
                    if(gps.canGetLocation()){
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        if(conn.isConnectingToInternet()){
                            if(Constant.loginUser.getIs_login()==1){
                                updateLocation(String.valueOf(Constant.loginUser.getId()),
                                        String.valueOf(latitude),
                                        String.valueOf(longitude));
                            }
                            else{
                                Log.i("Logout","Location Can not Send");
                            }
                        }
                    }
                }
                if(db.getAllOfflineStore().size()>0 || db.getAllOfflineOrders().size()>0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
                    alertDialog.setTitle("Metro Sync Alert...");
                    alertDialog.setMessage("Click OK to Push Data on Server");
                    //alertDialog.setIcon(R.drawable.attension);


                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    if(db.getAllOfflineStore().size()>0){
                                        lastIndex = db.getAllOfflineStore().size()-1;
                                        Log.i("Offline Store List Size",db.getAllOfflineStore().size()+"");
                                        Log.i("Last Index",lastIndex+"");
                                        for(int i=0;i<db.getAllOfflineStore().size();i++){
                                            Store offlineStore = db.getAllOfflineStore().get(i);
                                            List<Category> categoryList = db.getAllCategory();

                                            for(int j=0;j<categoryList.size();j++){
                                                if(categoryList.get(j).getId()==offlineStore.getCategoryId()){
                                                    category = categoryList.get(j).getName();
                                                }
                                            }
                                            registerStore(offlineStore,
                                                    category,i);
                                        }
                                    }else{
                                        if(db.getAllOfflineOrders().size()>0){
                                            List<Order> offlineOrderItem = db.getAllOfflineOrders();
                                            lastOtherIndex = offlineOrderItem.size()-1;
                                            Log.i("Last Other Index",lastOtherIndex+"");
                                            for(int j=0;j<offlineOrderItem.size();j++){
                                                Order order = offlineOrderItem.get(j);
                                                Store storeOrder = db.getStore(order.getStoreName());
                                                String itemListJson = composeJSONOfOrderItem(order.getOrderId());
                                                Log.i("Item List Json",itemListJson);
                                                putOfflineOrder(String.valueOf(storeOrder.getStoreId()),
                                                        String.valueOf(Constant.loginUser.getId()),
                                                        String.valueOf(order.getOrderAmount()),
                                                        order.getDeliveryDate(),
                                                        order.getInstructionID(),
                                                        String.valueOf(order.getOrderDiscount()),
                                                        String.valueOf(order.getOrderPerCartonDiscount()),
                                                        String.valueOf(totalQuantity),
                                                        itemListJson,j);
                                            }
                                        }
                                    }
                                }
                            });

                    alertDialog.show();
                }
            }
        }

        Log.i("User id", Constant.loginUser.getId() + "");
        Log.i("name", Constant.loginUser.getName() + "");
        Log.i("address", Constant.loginUser.getAddress() + "");
        Log.i("contact_num", Constant.loginUser.getContact() + "");
        Log.i("cnic", Constant.loginUser.getCnic() + "");
        Log.i("user_name", Constant.loginUser.getUser_name() + "");
        Log.i("password", Constant.loginUser.getPassword() + "");
        Log.i("latitude", Constant.loginUser.getLatitude() + "");
        Log.i("longitude", Constant.loginUser.getLongitude() + "");
        Log.i("gcm_id", Constant.loginUser.getGcm_id() + "");

        List<Category> categoryList = db.getAllCategory();

        Log.i("Category Count", categoryList.size() + "");
        Log.i("Category 1 Name", categoryList.get(0).getName() + "");
        Log.i("Category 2 Name", categoryList.get(1).getName() + "");
        Log.i("Category 3 Name", categoryList.get(2).getName() + "");

        List<Instruction> instructionsList = db.getAllInstructions();
        Log.i("Instruction Count", instructionsList.size() + "");

        for(int i=0;i<instructionsList.size();i++){
            Log.i("Instruction "+i+1+" Name", instructionsList.get(i).getName()+"");
        }


    }

    public void userDialog(final Context context){
        CharSequence[] items = {"Logout","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Constant.loginUser.getName());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Constant.loginUser = db.getLoginedUser();
                    db.deleteAllRecords("user");
                    Constant.loginUser.setIs_login(0);
                    db.addUser(Constant.loginUser);
                    startActivity(new Intent(
                            context,
                            MainActivity.class));
                    finish();
                }
                else{
                    if(item==1){
                        dialog.dismiss();
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wmlp.y= 25;   //x position
        //y position

        dialog.show();
    }

    private void initializeControls(){
        menuNewOrder = (LinearLayout) findViewById(R.id.menu_order);
        menuRegisterOrder = (LinearLayout) findViewById(R.id.menu_register);
        menuPastOrder = (LinearLayout) findViewById(R.id.menu_past_order);
        menuMyAccount = (LinearLayout) findViewById(R.id.menu_my_account);
        menuNotification = (LinearLayout) findViewById(R.id.menu_notification);
        menuContactUs = (LinearLayout) findViewById(R.id.menu_contact_us);

        txtBookAnOrder = (TextView) findViewById(R.id.txt_book_an_order);
        txtRegisterStore = (TextView) findViewById(R.id.txt_register_store);
        txtPastOrder = (TextView) findViewById(R.id.txt_past_order);
        txtMyAccount = (TextView) findViewById(R.id.txt_my_account);
        txtNotification = (TextView) findViewById(R.id.txt_notification);
        txtContactUs = (TextView) findViewById(R.id.txt_contact_us);

        ivPic = (ImageView) findViewById(R.id.ivPic);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        menuNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, BookAnOrderActivity.class);
                startActivity(i);
            }
        });

        menuRegisterOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, RegisterStoreActivity.class);
                startActivity(i);
            }
        });

        menuMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, MyTargetActivity.class);
                startActivity(i);
            }
        });

        menuContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, ContactUsActivity.class);
                startActivity(i);
            }
        });

        menuNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(i);
            }
        });

        menuPastOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, PastOrderActivity.class);
                startActivity(i);
            }
        });

        ivPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                userDialog(context);
            }
        });
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(DashboardActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(DashboardActivity.this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},request);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.canGetLocation = true;
                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {
                    this.canGetLocation = false;
                    //Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }


    private void registerStore(final Store store, final String category,final int index) {
        // Tag used to cancel the request
        String tag_string_req = "req_register_offline_store";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_REGISTER_STORE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "Response: " + response.toString());
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

                        Log.i("On Volley Request index", index + "");
                        Log.i("On Volley Request Last index",lastIndex+"");
                        if(lastIndex==index){
                            db.deleteAllRecords("offline_store");

                            if(conn.isConnectingToInternet()){
//                                List<Order> offlineOrderItem = db.getAllOfflineOrders();
//                                lastOtherIndex = offlineOrderItem.size()-1;
//                                for(int j=0;j<offlineOrderItem.size();j++){
//                                    Order order = offlineOrderItem.get(j);
//                                    Store storeOrder = db.getStore(order.getStoreName());
//                                    putOfflineOrder(String.valueOf(storeOrder.getStoreId()),
//                                            String.valueOf(Constant.loginUser.getId()),
//                                            String.valueOf(order.getOrderAmount()),
//                                            order.getDeliveryDate(),
//                                            order.getInstructionID(),
//                                            composeJSONOfOrderItem(order.getOrderId()),j);
//                                }
                                if(db.getAllOfflineOrders().size()>0) {
                                    List<Order> offlineOrderItem = db.getAllOfflineOrders();
                                    lastOtherIndex = offlineOrderItem.size() - 1;
                                    Log.i("Last Other Index", lastOtherIndex + "");
                                    for (int j = 0; j < offlineOrderItem.size(); j++) {
                                        Order order = offlineOrderItem.get(j);
                                        Store storeOrder = db.getStore(order.getStoreName());
                                        String itemListJson = composeJSONOfOrderItem(order.getOrderId());
                                        Log.i("Item List Json", itemListJson);
                                        putOfflineOrder(String.valueOf(storeOrder.getStoreId()),
                                                String.valueOf(Constant.loginUser.getId()),
                                                String.valueOf(order.getOrderAmount()),
                                                order.getDeliveryDate(),
                                                order.getInstructionID(),
                                                String.valueOf(order.getOrderDiscount()),
                                                String.valueOf(order.getOrderPerCartonDiscount()),
                                                String.valueOf(totalQuantity),
                                                itemListJson, j);
                                    }
                                }
                            }
                        }
//                        Helper.showToast(DashboardActivity.this, "Store Registered Successfully");
//                        Intent intent = new Intent(DashboardActivity.this,
//                                DashboardActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();






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

    private void putOfflineOrder(final String store_id, final String user_id,final String totalPrice,
                          final String order_delivery_date,
                          final String instruction_id, final String discount, final String discount_per_carton,
                                 final String totalQuantity, final String order_item, final int index) {
        // Tag used to cancel the request
        String tag_string_req = "req_put_offline_orders";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_PLACE_ORDERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Order Response", "Order Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                            if(lastOtherIndex==index){
                                db.deleteAllRecords("offline_orders");
                                db.deleteAllRecords("offline_order_product");
                                Helper.showToast(DashboardActivity.this,"Your Order Has Been Pushed To Server Successfully");
                            }
//                        Constant.itemList = new ArrayList<>();
//                        Constant.StoreName="";
//                        Constant.StoreCode="";
//                        Constant.StoreNumber="";
//                        Constant.DeliverDate="";
//                        Constant.SelectedInstruction="";
//                        Constant.SelectedInstructionID=-1;

//                        Intent i = new Intent(DashboardActivity.this,DashboardActivity.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
//                        finish();
                    }
                    else{

                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
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
                params.put("user_id", user_id);
                params.put("store_id", store_id);
                params.put("status_id", "1");
                params.put("total_order", totalPrice);
                params.put("due_date", order_delivery_date);
                params.put("instruction_id", instruction_id);
                params.put("discount", discount);
                params.put("per_carton_discount", discount_per_carton);
                params.put("total_quantity", totalQuantity);
                params.put("order_items", order_item);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public String composeJSONOfOrderItem(String orderID) {
        //List<Product> listOfProducts = db.getAllProduct();

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        List<Item> offlineOrderItem = db.getOfflineOrderItemList(orderID);
        Log.i("Offline Order Item Size",offlineOrderItem.size()+"");
        for(int i=0;i<offlineOrderItem.size();i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("product_id", String.valueOf(offlineOrderItem.get(i).getItemId()));
            map.put("price", String.valueOf(offlineOrderItem.get(i).getPrice()));
            map.put("qty", String.valueOf(offlineOrderItem.get(i).getQuantity()));
            map.put("discount", String.valueOf(offlineOrderItem.get(i).getDiscount_per_product()));
            map.put("flavour_id", String.valueOf(offlineOrderItem.get(i).getFlavourId()));
            totalQuantity += offlineOrderItem.get(i).getQuantity();
            wordList.add(map);
//            String productId="";
//            Log.i("Quantity"+i,String.valueOf(offlineOrderItem.get(i).getQuantity()));
//            if(!String.valueOf(offlineOrderItem.get(i).getQuantity()).contentEquals("0")){
//                Product_Flavour product_flavour = db.getFlavour(offlineOrderItem.get(i).getFlavour());
//                for(int k=0;k<listOfProducts.size();k++){
//                    if(listOfProducts.get(k).getProductName().contentEquals(offlineOrderItem.get(i).getItemName())){
//                        productId = String.valueOf(listOfProducts.get(k).getProductId());
//                    }
//                }
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("product_id", productId);
//                map.put("price", String.valueOf(offlineOrderItem.get(i).getPrice()));
//                map.put("qty", String.valueOf(offlineOrderItem.get(i).getQuantity()));
//                map.put("flavour_id", String.valueOf(product_flavour.getFlavourId()));
//                wordList.add(map);
//            }
        }
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    static class GPSTracker extends Service implements LocationListener {

        private final Context mContext;
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        boolean canGetLocation = false;

        Location location;
        double latitude;
        double longitude;

        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                } else {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            //location.setAccuracy(1);
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            return latitude;
        }

        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            return longitude;
        }

        public boolean canGetLocation() {
            return this.canGetLocation;
        }


        @Override
        public void onLocationChanged(Location location) {
            Log.i("Location Changed Call", location.getLatitude() + "==" + location.getLongitude());
            //Toast.makeText(mContext, "Your Location is - \nLat: " + location.getLatitude() + "\nLong: " + location.getLongitude(), Toast.LENGTH_LONG).show();
            ConnectionDetector conn = new ConnectionDetector(mContext);
            SqlDB db = new SqlDB(mContext);
            if(conn.isConnectingToInternet()){
                Constant.loginUser = db.getLoginedUser();
                if(Constant.loginUser.getIs_login()==1){
                    updateLocation(String.valueOf(Constant.loginUser.getId()),
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()));
                }
                else{
                    Log.i("Logout", "Location Can not Send");
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

    }

    static void updateLocation(final String userId, final String latitude, final String longitude) {
        // Tag used to cancel the request
        String tag_string_req = "req_update_location";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_UPDATE_LOCATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(DashboardActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Location Response", "Response: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
                params.put("latitude",latitude);
                params.put("longitude", longitude);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
