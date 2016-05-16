package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.GridViewAdapter;
import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Helper;
import Utils.Instruction;
import Utils.Item;
import Utils.Product;
import Utils.Product_Flavour;
import Utils.SqlDB;
import Utils.Store;
import Utils.Zone;

public class BookAnOrderActivity extends AppCompatActivity {
    ExpandableHeightGridView itemGrid;
    GridViewAdapter adapter;
    List<Item> listOfItems;

    List<Product> listOfProducts;
    List<Product_Flavour> listOfFlavours;

    SqlDB db;

    Button btnBack,btnNext,btnPickDate,btnPickOrderDate;
    AutoCompleteTextView txtStoreNames;
    AutoCompleteTextView txtStoreCode;
    AutoCompleteTextView txtStoreNum;
    TextView txtShowDate,txtShowOrderDate;
    Spinner spinnerInstructions;
    //EditText edDiscountField;

    List<Store> storeList;
    List<Instruction> instructionList;
    List<String> allInstructions;
    String[] storeNames; // = {"Abdullah Store","Ali General Store","Ayaz Shop","Cheze Point","Jamal General Store","Zaib Store"};
    String[] storeCodes; // = {"6545546","4534634","0926765","3435556","5524526","2344456"};
    String[] storeNumbers;

    private int year;
    private int month;
    private int day;

    private int selYear;
    private int selMonth;
    private int selDay;

    private ProgressDialog pDialog;

    int listSize=-1;
    int currentIndex=-1;

    static final int DATE_PICKER_ID = 1111;
    static final int DATE_PICKER_ORDER_ID = 1112;

    ConnectionDetector conn;

    boolean canPlaceOrder = false;
    boolean isValidStore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_an_order);

        initializeControls();
        db = new SqlDB(this);
        conn = new ConnectionDetector(this);
        Constant.loginUser = db.getLoginedUser();
        if(Constant.itemList.size()>0){
            initializeData();
        }
        else{
            if(conn.isConnectingToInternet()){
                //db.deleteAllRecords("store");
                List<Store> storeList = db.getLastStore();
                Log.i("Last Store ID",storeList.get(0).getStoreId()+"-");
                List<Zone> zoneList = db.getAllZone();
                listSize = zoneList.size()-1;
                for(int i=0;i<zoneList.size();i++){
                    getAllStores(String.valueOf(zoneList.get(i).getId()),i,storeList.get(0).getStoreId());
                }
                //getAllStores(String.valueOf(Constant.loginUser.getZone_id()));
            }
            else{
                initializeData();
            }
        }

        //Toast.makeText(this,listOfItems.size()+"",Toast.LENGTH_SHORT).show();
    }

    private void initializeControls(){
        itemGrid = (ExpandableHeightGridView) findViewById(R.id.list_order_item);
        itemGrid.setExpanded(true);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnNext = (Button) findViewById(R.id.btn_next);
        txtStoreNames = (AutoCompleteTextView) findViewById(R.id.store_name);
        txtStoreCode = (AutoCompleteTextView) findViewById(R.id.store_code);
        txtStoreNum = (AutoCompleteTextView) findViewById(R.id.store_num);

        btnPickDate = (Button) findViewById(R.id.btn_pick_date);
        txtShowDate = (TextView) findViewById(R.id.tvDate);

        btnPickOrderDate = (Button) findViewById(R.id.btn_pick_order_date);
        txtShowOrderDate = (TextView) findViewById(R.id.tvOrderDate);

        spinnerInstructions = (Spinner) findViewById(R.id.spinner_instruction);
        //edDiscountField = (EditText) findViewById(R.id.ed_discount_field);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        if(String.valueOf(month).length()==1){
            txtShowDate.setText(new StringBuilder()
                    .append(day).append("-").append("0")
                    .append(month + 1).append("-").append(year));

            txtShowOrderDate.setText(new StringBuilder()
                    .append(day).append("-").append("0")
                    .append(month + 1).append("-").append(year));
        }
        else{
            txtShowDate.setText(new StringBuilder()
                    .append(day).append("-")
                    .append(month + 1).append("-").append(year));

            txtShowOrderDate.setText(new StringBuilder()
                    .append(day).append("-")
                    .append(month + 1).append("-").append(year));
        }

    }

    private void initializeData(){
        listOfProducts = db.getAllProduct();
        listOfFlavours = db.getAllFlavours();
        listOfItems = new ArrayList<>();
        storeList = db.getAllStore();

        List<Store> offlineList = db.getAllOfflineStore();
        if(offlineList.size()>0){
            for(int i=0;i<offlineList.size();i++){
                storeList.add(offlineList.get(i));
            }
        }

        Log.i("Store Count",storeList.size()+"");
        instructionList = db.getAllInstructions();
        Log.i("Instruction Count", instructionList.size() + "");
        if(storeList.size()>0 && instructionList.size()>0){
            storeNames = new String[storeList.size()];
            storeCodes = new String[storeList.size()];
            storeNumbers = new String[storeList.size()];

            for(int j=0;j<storeList.size();j++){
                storeNames[j] = storeList.get(j).getStoreName();
                storeCodes[j] = String.valueOf(storeList.get(j).getStoreId());
                storeNumbers[j] = storeList.get(j).getContact();
            }
            allInstructions = new ArrayList<>();
            allInstructions.add("Select Instruction");
            for(int k=0;k<instructionList.size();k++){
                allInstructions.add(instructionList.get(k).getName());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allInstructions);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerInstructions.setAdapter(dataAdapter);

            if(Constant.SelectedInstructionID>-1){
                spinnerInstructions.setSelection(Constant.SelectedInstructionID);
            }


            if(Constant.itemList.size()>0){
                listOfItems = Constant.itemList;
                Constant.itemList=new ArrayList<>();
            }
            else{
                for(int i =0;i<listOfProducts.size();i++){
                    Product prod = listOfProducts.get(i);

                    for(int j=0;j<listOfFlavours.size();j++){
                        Product_Flavour flv = listOfFlavours.get(j);
                        if(prod.getProductId()== flv.getProductId()){
                            listOfItems.add(new Item(flv.getProductId(),
                                    flv.getImagePath(),flv.getFlavourName(),0,prod.getProductName(),prod.getPrice()+flv.getPrice()));
                        }
                    }
                }
            }
//        listOfItems.add(new Item(101,R.drawable.metro10lg,"Ketchup",0,"Metro Cartoon",5.00));
//        listOfItems.add(new Item(102,R.drawable.metro12lg,"Cheese",0,"Metro Cartoon",5.00));
//        listOfItems.add(new Item(103,R.drawable.metro8lg,"Chicken",0,"Metro Aloo Fries",10.00));
//        listOfItems.add(new Item(104,R.drawable.metro14lg,"Tomato",0,"Metro Aloo Fries",10.00));
//        listOfItems.add(new Item(105,R.drawable.metro2lg,"Kali Mirch",0,"Metro Network",15.00));
//        listOfItems.add(new Item(106,R.drawable.metro3lg,"Chattpta",0,"Metro Network",15.00));

            adapter = new GridViewAdapter(BookAnOrderActivity.this,listOfItems);
            itemGrid.setAdapter(adapter);

            ArrayAdapter<String> adapt = new ArrayAdapter<String>
                    (BookAnOrderActivity.this,android.R.layout.select_dialog_item,storeNames);
            txtStoreNames.setThreshold(1);//will start working from first character
            txtStoreNames.setAdapter(adapt);//setting the adapter data into the AutoCompleteTextView
            txtStoreNames.setTextColor(Color.BLUE);

            if(!Constant.StoreName.isEmpty()){
                txtStoreNames.setText(Constant.StoreName);
                txtStoreCode.setText(Constant.StoreCode);
                txtStoreNum.setText(Constant.StoreNumber);
                txtShowOrderDate.setText(Constant.OrderDate);
                txtShowDate.setText(Constant.DeliverDate);
                //edDiscountField.setText(String.valueOf(Constant.DiscountPrice));
            }

            txtStoreNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedStoreName = parent.getItemAtPosition(position).toString();
                    Toast.makeText(BookAnOrderActivity.this, selectedStoreName + "\n" + position, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < storeNames.length; i++) {
                        if (selectedStoreName.contentEquals(storeNames[i])) {
                            //String selectedStoreCode = storeCodes[i];
                            txtStoreCode.setText(storeCodes[i]);
                            txtStoreNum.setText(storeNumbers[i]);
                        }
                    }
                }
            });

            ArrayAdapter<String> adapt1 = new ArrayAdapter<String>
                    (BookAnOrderActivity.this,android.R.layout.select_dialog_item,storeCodes);
            txtStoreCode.setThreshold(1);//will start working from first character
            txtStoreCode.setAdapter(adapt1);//setting the adapter data into the AutoCompleteTextView
            txtStoreCode.setTextColor(Color.BLUE);

            txtStoreCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedStoreName = parent.getItemAtPosition(position).toString();
                    Toast.makeText(BookAnOrderActivity.this, selectedStoreName + "\n" + position, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < storeCodes.length; i++) {
                        if (selectedStoreName.contentEquals(storeCodes[i])) {
//                            String selectedStoreCode = storeNames[i];
                            txtStoreNames.setText(storeNames[i]);
                            txtStoreNum.setText(storeNumbers[i]);
                        }
                    }
                }
            });

            ArrayAdapter<String> adapt2 = new ArrayAdapter<String>
                    (BookAnOrderActivity.this,android.R.layout.select_dialog_item,storeNumbers);
            txtStoreNum.setThreshold(1);//will start working from first character
            txtStoreNum.setAdapter(adapt2);//setting the adapter data into the AutoCompleteTextView
            txtStoreNum.setTextColor(Color.BLUE);

            txtStoreNum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedStoreName = parent.getItemAtPosition(position).toString();
                    Toast.makeText(BookAnOrderActivity.this, selectedStoreName + "\n" + position, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < storeNumbers.length; i++) {
                        if (selectedStoreName.contentEquals(storeNumbers[i])) {
//                            String selectedStoreCode = storeNames[i];
                            txtStoreNames.setText(storeNames[i]);
                            txtStoreCode.setText(storeCodes[i]);
                        }
                    }
                }
            });

            btnPickDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DATE_PICKER_ID);
                }
            });

            btnPickOrderDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DATE_PICKER_ORDER_ID);
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.itemList = new ArrayList<>();
                    Constant.itemDiscountList = new ArrayList<>();
                    Constant.StoreName="";
                    Constant.StoreCode="";
                    Constant.StoreNumber="";
                    Constant.DeliverDate="";
                    Constant.SelectedInstruction="";
                    Constant.SelectedInstructionID=-1;
                    Intent i = new Intent(BookAnOrderActivity.this,DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!txtStoreNames.getText().toString().isEmpty()
                            //&& !txtStoreCode.getText().toString().isEmpty()
                            //&& !txtStoreNames.getText().toString().isEmpty()
                            && !spinnerInstructions.getSelectedItem().toString().isEmpty()){
                        if(!spinnerInstructions.getSelectedItem().toString().contentEquals("Select Instruction")){
                            Log.i("List Size",Constant.itemList.size()+"");
                            for(int i=0;i< Constant.itemList.size();i++){
                                if(Constant.itemList.get(i).getQuantity()>0){
                                    canPlaceOrder = true;
                                }
                                Log.i("i "+i+1,Constant.itemList.get(i).getImage_path()+"");
                                Log.i("i " + i + 1, Constant.itemList.get(i).getFlavour() + "");
                                Log.i("i "+i+1,Constant.itemList.get(i).getQuantity()+"");
                                Log.i("i "+i+1,Constant.itemList.get(i).getItemId()+"");
                                Log.i("i " + i + 1, Constant.itemList.get(i).getItemName() + "");
                                Log.i("i "+i+1,Constant.itemList.get(i).getPrice()+"");
                            }
                            Log.i("-------------------","----------------------------");
//                            String deliveryDate = txtShowDate.getText().toString();
//
//                            deliveryDate = (new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day)).toString();
//
//                            Log.i("Date",deliveryDate);


                            if(canPlaceOrder){
                                Constant.StoreName = txtStoreNames.getText().toString();
                                Constant.StoreCode = txtStoreCode.getText().toString();
                                Constant.StoreNumber = txtStoreNum.getText().toString();
                                Constant.OrderDate = txtShowOrderDate.getText().toString();
                                Constant.DeliverDate = txtShowDate.getText().toString();
                                Constant.SelectedInstruction = spinnerInstructions.getSelectedItem().toString();
                                Constant.SelectedInstructionID = spinnerInstructions.getSelectedItemPosition();
                                //if(!edDiscountField.getText().toString().isEmpty()){
                                    Constant.DiscountPrice = 0;//Double.parseDouble(edDiscountField.getText().toString());
                                    Log.i("Discount Price",Constant.DiscountPrice+"-");
                                //}
                                Log.i("Order Date",Constant.OrderDate);
                                Log.i("Delivery Date",Constant.DeliverDate);
                                for(int i=0;i<storeList.size();i++){
                                    if(storeList.get(i).getStoreName().contentEquals(Constant.StoreName)
                                            && storeList.get(i).getStoreId()==Integer.parseInt(Constant.StoreCode)
                                            && storeList.get(i).getContact().contentEquals(Constant.StoreNumber)){
                                        isValidStore = true;
                                    }
                                }

                                if(isValidStore){
                                    Constant.IS_FROM_BOOK_ORDER=true;
                                    Constant.IS_FROM_ORDER_STATISTIC=false;
                                    Intent i = new Intent(BookAnOrderActivity.this,DiscountAcitivity.class);
                                    startActivity(i);
                                }
                                else{
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookAnOrderActivity.this);
                                    alertDialog.setTitle("Store Not Exist ...");
                                    alertDialog.setMessage("Do you want to Add it?");

                                    alertDialog.setNegativeButton("NO",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,  int which) {
                                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                }
                                            });
                                    alertDialog.setPositiveButton("YES",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int which) {
                                                    // Write your code here to execute after dialog
                                                    Intent i = new Intent(BookAnOrderActivity.this,RegisterStoreActivity.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });

                                    alertDialog.show();
                                    //Helper.showToast(BookAnOrderActivity.this,"You Have Selected Invalid Store");
                                }


                            }
                            else{
                                Helper.showToast(BookAnOrderActivity.this,"You have to Select Atleast One Product For One Item For Order");
                            }

                        }
                        else{
                            Toast.makeText(BookAnOrderActivity.this,"Please Select Instruction",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(BookAnOrderActivity.this,"Please Enter All Fields",Toast.LENGTH_SHORT).show();
                    }

//                    Intent i = new Intent(BookAnOrderActivity.this,OrderStatisticActivity.class);
//                    startActivity(i);
//                Intent i = new Intent(BookAnOrderActivity.this,DashboardActivity.class);
//                startActivity(i);
                }
            });
        }
        else{
            Toast.makeText(BookAnOrderActivity.this,"Store not found in your zone",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void getAllStores(final String zoneID,int index, final int lastStoreId){
        String tag_string_req = "req_all_stores";
        pDialog.setMessage("Please wait ...");
        showDialog();

        currentIndex=index;

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_LATEST_STORES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "GET ALL STORES Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("Store Not Available")){
                        if(currentIndex ==listSize){
                            initializeData();
                        }
                        //Toast.makeText(BookAnOrderActivity.this,"Not New Store Found in your zone",Toast.LENGTH_SHORT).show();
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
//
//                            store.setCarryName(obj.getString("store_have"));
//                            db.addStore(store);
//                        }
//
//                        Log.i("I Value", currentIndex + "");
//                        if(currentIndex==listSize){
//                            initializeData();
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
                params.put("store_id", String.valueOf(lastStoreId));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public class storeParsingInBackground extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BookAnOrderActivity.this);
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
            //super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.i("I Value", currentIndex + "");
            if(currentIndex==listSize){
                initializeData();
            }
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month,day);
            case DATE_PICKER_ORDER_ID:
                return new DatePickerDialog(this, pickerListener1, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            selYear  = selectedYear;
            selMonth = selectedMonth;
            selDay   = selectedDay;

//            if(selYear>=year){
//                if(selYear==year){
//                    if(selMonth>=month){
//                        if(selMonth==month){
//                            if(selDay>day){
//                                if(String.valueOf(selMonth).length()==1){
//                                    txtShowDate.setText(new StringBuilder().append(selDay)
//                                            .append("-").append("0").append(selMonth + 1).append("-").append(selYear)
//                                            .append(" "));
//                                }
//                                else{
//                                    txtShowDate.setText(new StringBuilder().append(selDay)
//                                            .append("-").append(selMonth+1).append("-").append(selYear)
//                                            .append(" "));
//                                }
//                                txtShowDate.requestFocus();
//                            }
//                            else{
//                                Helper.showAlertDialog(BookAnOrderActivity.this,"Wrong Date Selection","Invalid Order Due Date");
//                                txtShowDate.setText(new StringBuilder()
//                                        .append(day).append("-")
//                                        .append(month + 1).append("-").append(year));
//                            }
//                        }
//                        else{
//                            if(String.valueOf(selMonth).length()==1){
//                                txtShowDate.setText(new StringBuilder().append(selDay)
//                                        .append("-").append("0").append(selMonth+1).append("-").append(selYear)
//                                        .append(" "));
//                            }
//                            else{
//                                txtShowDate.setText(new StringBuilder().append(selDay)
//                                        .append("-").append(selMonth+1).append("-").append(selYear)
//                                        .append(" "));
//                            }
//                            txtShowDate.requestFocus();
//                        }
//                    }
//                    else{
//                        Helper.showAlertDialog(BookAnOrderActivity.this,"Wrong Date Selection","Invalid Order Due Date");
//                        txtShowDate.setText(new StringBuilder()
//                                .append(day).append("-")
//                                .append(month + 1).append("-").append(year));
//                    }
//                }
//                else{
//                    if(String.valueOf(selMonth).length()==1){
//                        txtShowDate.setText(new StringBuilder().append(selDay)
//                                .append("-").append("0").append(selMonth+1).append("-").append(selYear)
//                                .append(" "));
//                    }
//                    else{
//                        txtShowDate.setText(new StringBuilder().append(selDay)
//                                .append("-").append(selMonth+1).append("-").append(selYear)
//                                .append(" "));
//                    }
//                    txtShowDate.requestFocus();
//                }
//            }
//            else{
//                Helper.showAlertDialog(BookAnOrderActivity.this,"Wrong Date Selection","Invalid Order Due Date");
//                txtShowDate.setText(new StringBuilder()
//                        .append(day).append("-")
//                        .append(month + 1).append("-").append(year));
//            }

            txtShowDate.setText(new StringBuilder().append(selDay)
                    .append("-").append("0").append(selMonth + 1).append("-").append(selYear)
                    .append(" "));
        }
    };

    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            selYear  = selectedYear;
            selMonth = selectedMonth;
            selDay   = selectedDay;

//            if(selYear>=year){
//                if(selYear==year){
//                    if(selMonth>=month){
//                        if(selMonth==month){
//                            if(selDay>day){
//                                if(String.valueOf(selMonth).length()==1){
//                                    txtShowDate.setText(new StringBuilder().append(selDay)
//                                            .append("-").append("0").append(selMonth + 1).append("-").append(selYear)
//                                            .append(" "));
//                                }
//                                else{
//                                    txtShowDate.setText(new StringBuilder().append(selDay)
//                                            .append("-").append(selMonth+1).append("-").append(selYear)
//                                            .append(" "));
//                                }
//                                txtShowDate.requestFocus();
//                            }
//                            else{
//                                Helper.showAlertDialog(BookAnOrderActivity.this,"Wrong Date Selection","Invalid Order Due Date");
//                                txtShowDate.setText(new StringBuilder()
//                                        .append(day).append("-")
//                                        .append(month + 1).append("-").append(year));
//                            }
//                        }
//                        else{
//                            if(String.valueOf(selMonth).length()==1){
//                                txtShowDate.setText(new StringBuilder().append(selDay)
//                                        .append("-").append("0").append(selMonth+1).append("-").append(selYear)
//                                        .append(" "));
//                            }
//                            else{
//                                txtShowDate.setText(new StringBuilder().append(selDay)
//                                        .append("-").append(selMonth+1).append("-").append(selYear)
//                                        .append(" "));
//                            }
//                            txtShowDate.requestFocus();
//                        }
//                    }
//                    else{
//                        Helper.showAlertDialog(BookAnOrderActivity.this,"Wrong Date Selection","Invalid Order Due Date");
//                        txtShowDate.setText(new StringBuilder()
//                                .append(day).append("-")
//                                .append(month + 1).append("-").append(year));
//                    }
//                }
//                else{
//                    if(String.valueOf(selMonth).length()==1){
//                        txtShowDate.setText(new StringBuilder().append(selDay)
//                                .append("-").append("0").append(selMonth+1).append("-").append(selYear)
//                                .append(" "));
//                    }
//                    else{
//                        txtShowDate.setText(new StringBuilder().append(selDay)
//                                .append("-").append(selMonth+1).append("-").append(selYear)
//                                .append(" "));
//                    }
//                    txtShowDate.requestFocus();
//                }
//            }
//            else{
//                Helper.showAlertDialog(BookAnOrderActivity.this,"Wrong Date Selection","Invalid Order Due Date");
//                txtShowDate.setText(new StringBuilder()
//                        .append(day).append("-")
//                        .append(month + 1).append("-").append(year));
//            }

            txtShowOrderDate.setText(new StringBuilder().append(selDay)
                    .append("-").append("0").append(selMonth + 1).append("-").append(selYear)
                    .append(" "));
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Constant.itemList = new ArrayList<>();
        Constant.itemDiscountList = new ArrayList<>();
        Constant.StoreName="";
        Constant.StoreCode="";
        Constant.StoreNumber="";
        Constant.DeliverDate="";
        Constant.SelectedInstruction="";
        Constant.SelectedInstructionID=-1;
    }
}
