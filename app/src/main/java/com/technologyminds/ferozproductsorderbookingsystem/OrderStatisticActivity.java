package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.ProgressDialog;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.OrderStatisticAdapter;
import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Helper;
import Utils.Instruction;
import Utils.Item;
import Utils.Order;
import Utils.Product;
import Utils.Product_Flavour;
import Utils.SqlDB;
import Utils.Target;
import Utils.User;

public class OrderStatisticActivity extends AppCompatActivity {
    ExpandableHeightGridView itemGrid;
    OrderStatisticAdapter adapter;
    List<Item> listOfItems;

    Button btnBack,btnConfirm;
    TextView txtSubTotalAmmount,txtTotalAmmount,txtTotalDiscount;
    TextView txtStoreName,txtStoreCode,txtOrderDate,txtDeliverDate;

    private ProgressDialog pDialog;
    ConnectionDetector conn;
    SqlDB db;
    double totalDiscount = 0.00;
    double totalProductWiseDiscount = 0.00;
    double totalAmount = 0.00;
    int totalQuantity=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_statistic);

        initializeControls();
        db = new SqlDB(this);
        conn = new ConnectionDetector(this);

        Log.i("User id", Constant.loginUser.getId() + "");
        Log.i("name", Constant.loginUser.getName() + "");
        Log.i("address", Constant.loginUser.getAddress() + "");
        Log.i("contact_num", Constant.loginUser.getContact() + "");
        Log.i("cnic", Constant.loginUser.getCnic() + "");
        Log.i("user_name", Constant.loginUser.getUser_name() + "");
        Log.i("password", Constant.loginUser.getPassword() + "");
        Log.i("latitude", Constant.loginUser.getLatitude() + "");
        Log.i("longitude", Constant.loginUser.getLongitude() + "");
        Log.i("gcm_id", Constant.loginUser.getGcm_id()+"");

        listOfItems = new ArrayList<>();

        for(int i=0;i<Constant.itemList.size();i++){
            Log.i("Quantity" + i, String.valueOf(Constant.itemList.get(i).getQuantity()));
            if(!String.valueOf(Constant.itemList.get(i).getQuantity()).contentEquals("0")){
                double discount=0;
                for(int j=0;j<Constant.itemDiscountList.size();j++){
                    if(Constant.itemDiscountList.get(j).getItemName().contentEquals(Constant.itemList.get(i).getItemName())){
                        Constant.itemList.get(i).setDiscount_per_product(Constant.itemDiscountList.get(j).getDiscount_per_product());
                        discount=Constant.itemDiscountList.get(j).getDiscount_per_product();
                        totalProductWiseDiscount=totalProductWiseDiscount+(Constant.itemList.get(i).getQuantity()*discount);
                        Log.i("Discount",discount+"-");
                        Log.i("QTY",Constant.itemDiscountList.get(j).getQuantity()+"-");
                        Log.i("Total Discount Amount",totalProductWiseDiscount+"-");
                    }
                }
                listOfItems.add(new Item(Constant.itemList.get(i).getItemName()+"\n"+Constant.itemList.get(i).getFlavour(),
                        Constant.itemList.get(i).getQuantity(), Constant.itemList.get(i).getPrice(),discount));
            }
        }

        adapter = new OrderStatisticAdapter(this,listOfItems);
        itemGrid.setAdapter(adapter);

        final double totalPrice = getTotalPrice(listOfItems);
        txtSubTotalAmmount.setText(String.valueOf(totalAmount));
        //txtTotalDiscount.setText(String.valueOf(totalDiscount));
        txtTotalDiscount.setText(String.valueOf(totalProductWiseDiscount));
        //txtTotalAmmount.setText(String.valueOf(totalPrice));
        txtTotalAmmount.setText(String.valueOf(totalAmount-totalProductWiseDiscount));

        //Log.i("Item JSON", composeJSONOfOrderItem());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.IS_FROM_BOOK_ORDER=false;
                Constant.IS_FROM_ORDER_STATISTIC=true;
                Intent i = new Intent(OrderStatisticActivity.this,DiscountAcitivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(OrderStatisticActivity.this,"Your Order has been Submitted Successfully",Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(OrderStatisticActivity.this,DashboardActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                finish();
                Constant.IS_FROM_BOOK_ORDER=false;
                Constant.IS_FROM_ORDER_STATISTIC=false;
                List<Instruction> instructionList = db.getAllInstructions();
                int instructionId=0;
                for(int i=0;i<instructionList.size();i++){
                    if(instructionList.get(i).getName().contentEquals(Constant.SelectedInstruction)){
                        instructionId = instructionList.get(i).getId();
                        Log.i("Instruction ID",""+instructionId);
                        Log.i("Instruction Name",""+Constant.SelectedInstruction);
                    }
                }
                if(instructionId>0){
                    if(conn.isConnectingToInternet()){
                        String jsonOrderList = composeJSONOfOrderItem();
                        Log.i("Total Quantity","-"+totalQuantity);
                        putOrder(Constant.StoreCode, String.valueOf(Constant.loginUser.getId()),
                                txtTotalAmmount.getText().toString(),
                                Constant.DeliverDate, String.valueOf(instructionId),
                                "0",
                                String.valueOf(totalProductWiseDiscount),
                                String.valueOf(totalQuantity), jsonOrderList);
                    }
                    else{
                        Order order = new Order();
                        order.setOrderAmount(Double.parseDouble(txtTotalAmmount.getText().toString()));
                        order.setStoreName(txtStoreName.getText().toString());
                        order.setOrderDate(txtOrderDate.getText().toString());
                        order.setDeliveryDate(Constant.DeliverDate);
                        order.setOrderDiscount(totalProductWiseDiscount);
                        order.setOrderPerCartonDiscount(0);

                        long orderId = db.addOfflineOrder(order, Constant.loginUser.getId(), String.valueOf(instructionId));
                        Log.i("Offline Order ID",orderId+"");
                        List<Product> listOfProducts = db.getAllProduct();

                        for(int i=0;i<Constant.itemList.size();i++){
                            String productId="";
                            Log.i("Quantity"+i,String.valueOf(Constant.itemList.get(i).getQuantity()));
                            if(!String.valueOf(Constant.itemList.get(i).getQuantity()).contentEquals("0")){
                                Product_Flavour product_flavour = db.getFlavour(Constant.itemList.get(i).getFlavour());
                                for(int k=0;k<listOfProducts.size();k++){
                                    if(listOfProducts.get(k).getProductName().contentEquals(Constant.itemList.get(i).getItemName())){
                                        productId = String.valueOf(listOfProducts.get(k).getProductId());
                                    }
                                }
                                Item item = new Item();
                                item.setOrderId(Integer.parseInt(String.valueOf(orderId)));
                                item.setItemId(Integer.parseInt(productId));
                                item.setItemName(Constant.itemList.get(i).getItemName());
                                item.setFlavourId(product_flavour.getFlavourId());
                                item.setFlavour(Constant.itemList.get(i).getFlavour());
                                item.setPrice(Constant.itemList.get(i).getPrice());
                                item.setQuantity(Constant.itemList.get(i).getQuantity());
                                item.setDiscount_per_product(Constant.itemList.get(i).getDiscount_per_product());
                                totalQuantity += Constant.itemList.get(i).getQuantity();
                                db.addOfflineOrderProduct(item);
                            }
                        }
                        Target oldTarget = db.getUserTarget();

                        db.deleteAllRecords("user_target");
                        int newValue = oldTarget.getTotalAchieved()+totalQuantity;
                        oldTarget.setTotalAchieved(newValue);

                        db.addTarget(oldTarget);

                        Constant.itemList = new ArrayList<>();
                        Constant.itemDiscountList = new ArrayList<>();
                        Constant.StoreName="";
                        Constant.StoreCode="";
                        Constant.StoreNumber="";
                        Constant.DeliverDate="";
                        Constant.SelectedInstruction="";
                        Constant.DiscountPrice=0.00;
                        Constant.SelectedInstructionID=-1;
                        Helper.showToast(OrderStatisticActivity.this,"Your Order has been Successfully Placed");
                        Intent i = new Intent(OrderStatisticActivity.this,DashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Log.i("Instruction Error","Instruction Not Found");
                }


            }
        });
    }

    private void initializeControls(){
        itemGrid = (ExpandableHeightGridView) findViewById(R.id.list_order_statistic);
        itemGrid.setExpanded(true);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        txtStoreName = (TextView) findViewById(R.id.txt_order_store_name);
        txtStoreCode = (TextView) findViewById(R.id.txt_order_store_code);
        txtOrderDate = (TextView) findViewById(R.id.txt_order_date);
        txtDeliverDate = (TextView) findViewById(R.id.txt_order_delivery_date);

        final Calendar c = Calendar.getInstance();
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(String.valueOf(month).length()==1){
            txtOrderDate.setText(new StringBuilder()
                    .append(day).append("-").append("0")
                    .append(month + 1).append("-").append(year));
        }
        else{
            txtOrderDate.setText(new StringBuilder()
                    .append(day).append("-")
                    .append(month + 1).append("-").append(year));
        }
//        txtOrderDate.setText(new StringBuilder().append(day).append("-")
//                .append(month + 1).append("-").append(year));

        txtStoreName.setText(Constant.StoreName);
        txtStoreCode.setText(Constant.StoreCode);
        txtDeliverDate.setText(Constant.DeliverDate);

        txtSubTotalAmmount = (TextView) findViewById(R.id.txt_sub_total_ammount);
        txtTotalAmmount = (TextView) findViewById(R.id.txt_total_ammount);
        txtTotalDiscount = (TextView) findViewById(R.id.txt_total_discount);
    }

    protected double getTotalPrice(List<Item> listOfItems){
        double price = 0.0;

        for(int i=0;i<listOfItems.size();i++){
            Item item = listOfItems.get(i);
            totalAmount = totalAmount + (item.getQuantity()*item.getPrice());
            double discount = item.getPrice() - Constant.DiscountPrice;
            totalDiscount = totalDiscount + item.getQuantity() * Constant.DiscountPrice;
            price = price + (item.getQuantity()* discount);
        }
        return price;
    }

    public String composeJSONOfOrderItem() {
        List<Product> listOfProducts = db.getAllProduct();

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();

        for(int i=0;i<Constant.itemList.size();i++){
            String productId="";
            Log.i("Quantity"+i,String.valueOf(Constant.itemList.get(i).getQuantity()));
            if(!String.valueOf(Constant.itemList.get(i).getQuantity()).contentEquals("0")){
                Product_Flavour product_flavour = db.getFlavour(Constant.itemList.get(i).getFlavour());
                for(int k=0;k<listOfProducts.size();k++){
                    if(listOfProducts.get(k).getProductName().contentEquals(Constant.itemList.get(i).getItemName())){
                        productId = String.valueOf(listOfProducts.get(k).getProductId());
                    }
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("product_id", productId);
                map.put("price", String.valueOf(Constant.itemList.get(i).getPrice()));
                map.put("qty", String.valueOf(Constant.itemList.get(i).getQuantity()));
                map.put("discount", String.valueOf(Constant.itemList.get(i).getDiscount_per_product()));
                map.put("flavour_id", String.valueOf(product_flavour.getFlavourId()));
                totalQuantity += Constant.itemList.get(i).getQuantity();
                wordList.add(map);
//                listOfItems.add(new Item(Constant.itemList.get(i).getItemName()+"\n"+Constant.itemList.get(i).getFlavour(),
//                        Constant.itemList.get(i).getQuantity(), Constant.itemList.get(i).getPrice()));
            }
        }
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    private void putOrder(final String store_id, final String user_id,final String totalPrice,
                          final String order_delivery_date,
                            final String instruction_id, final String perCarton, final String discount,
                          final String totalQuantiy, final String order_item) {
        // Tag used to cancel the request
        String tag_string_req = "req_put_orders";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.i("Order Items",order_item);
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

                        Constant.itemList = new ArrayList<>();
                        Constant.StoreName="";
                        Constant.StoreCode="";
                        Constant.StoreNumber="";
                        Constant.DeliverDate="";
                        Constant.SelectedInstruction="";
                        Constant.SelectedInstructionID=-1;
                        Helper.showToast(OrderStatisticActivity.this,"Your Order has been Successfully Placed");
                        Intent i = new Intent(OrderStatisticActivity.this,DashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
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
                params.put("per_carton_discount", perCarton);
                params.put("discount", discount);
                params.put("total_quantity", totalQuantiy);
                params.put("order_items", order_item);

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
