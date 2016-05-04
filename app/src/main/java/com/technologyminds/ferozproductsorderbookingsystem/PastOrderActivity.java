package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.PastOrderStatisticAdapter;
import Utils.Category;
import Utils.ConnectionDetector;
import Utils.Constant;
import Utils.Item;
import Utils.Order;
import Utils.SqlDB;

public class PastOrderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    List<Order> orderList;
    PastOrderStatisticAdapter adapter;
    ExpandableHeightGridView listView;

    Button btnBack;
    ProgressDialog pDialog;

    ConnectionDetector conn;
    SqlDB db;
    List<Order> offline,online;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_order);

        initializeControls();

        conn = new ConnectionDetector(this);
        db = new SqlDB(this);

        if(conn.isConnectingToInternet()){
            getAllPastOrders(String.valueOf(Constant.loginUser.getId()));
        }
        else{
            orderList = new ArrayList<>();
            online = db.getAllOrders();
            Collections.sort(online, Order.StuNameComparator);
            Collections.sort(online, Order.OrderIdComparator1);

            offline = db.getAllOfflineOrders();
            Collections.sort(offline,Order.OrderIdComparator1);

            for(int i=0;i<offline.size();i++){
                orderList.add(offline.get(i));
            }

            for(int i=0;i<online.size();i++){
                orderList.add(online.get(i));
            }
            Log.i("Order Size",orderList.size()+"");

            if(orderList.size()>0){

                adapter = new PastOrderStatisticAdapter(this,orderList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Order selectedOrder = Constant.filterList.get(position);

                        Log.i("Order ID", selectedOrder.getOrderId());
                        Log.i("Store Name", selectedOrder.getStoreName());
                        Log.i("Due Date", selectedOrder.getDeliveryDate());
                        Log.i("Total Price", selectedOrder.getOrderAmount() + "");

                        Constant.OrderId = selectedOrder.getOrderId();
                        Constant.StoreName = selectedOrder.getStoreName();
                        Constant.DeliverDate = selectedOrder.getDeliveryDate();
                        Constant.DiscountPrice = selectedOrder.getOrderDiscount();
                        Constant.GRANDTOTAL = String.valueOf(selectedOrder.getOrderAmount());
                        Constant.TOTALPRICE = String.valueOf(selectedOrder.getOrderAmount()+selectedOrder.getOrderDiscount());
                        Constant.OrderDate = selectedOrder.getOrderDate();

                        for(int j=0;j<offline.size();j++){
                            if(offline.get(j).getOrderId().contentEquals(selectedOrder.getOrderId())){
                                List<Item> itemList = db.getOfflineOrderItems(Constant.OrderId);
                                Log.i("Item Size", itemList.size() + "");
                                if (itemList.size() > 0) {
                                    Constant.itemList = itemList;
                                    Intent i = new Intent(PastOrderActivity.this, PastOrderDetailActivity.class);
                                    startActivity(i);
                                }
                            }
                        }

                        List<Item> itemList = db.getOrderItems(Constant.OrderId);
                        Log.i("Item Size", itemList.size() + "");
                        if (itemList.size() > 0) {
                            Constant.itemList = itemList;
                            Intent i = new Intent(PastOrderActivity.this, PastOrderDetailActivity.class);
                            startActivity(i);
                        }
                    }
                });
            }
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PastOrderActivity.this, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void getAllPastOrders(final String userId){
        String tag_string_req = "req_all_past_order";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_ORDERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "GET PAST ORDERS Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){

                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);
                        db.deleteAllRecords("orders");
                        db.deleteAllRecords("order_product");
                        orderList = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj =jsonArray.getJSONObject(i);
                            Order order = new Order();
                            order.setOrderId(obj.getString("id"));
                            order.setStoreName(obj.getString("store_name"));
                            if(!obj.getString("discount").contentEquals("null")){
                                order.setOrderDiscount(Double.parseDouble(obj.getString("discount")));
                            }
                            else{
                                order.setOrderDiscount(0.00);
                            }

                            order.setOrderPerCartonDiscount(Double.parseDouble(obj.getString("per_carton_discount")));

                            String orderDate = obj.getString("order_date");
                            String dueDate = obj.getString("due_date");
                            order.setOrderDate(orderDate.substring(8, 10) + "-" + orderDate.substring(5, 7) + "-" + orderDate.substring(0, 4));
                            order.setDeliveryDate(dueDate.substring(8, 10) + "-" + dueDate.substring(5, 7) + "-" + dueDate.substring(0, 4));

                            //Log.i("Order Date", order.getOrderDate().substring(8,10) + "-" + order.getOrderDate().substring(5, 7) + "-" + order.getOrderDate().substring(0, 4));
                            //order.set
                            order.setOrderAmount(Double.parseDouble(obj.getString("total_order")));
                            db.addOrder(order, Constant.loginUser.getId());
                            //db.addOrder(order,Constant.loginUser.getId());
                            orderList.add(order);

                            JSONArray itemArray = obj.getJSONArray("items");

                            for(int j=0;j<itemArray.length();j++){
                                JSONObject itemObj = itemArray.getJSONObject(j);
                                Item item = new Item();
                                item.setOrderId(Integer.parseInt(order.getOrderId()));
                                item.setItemName(itemObj.getString("product_name"));
                                item.setQuantity(Integer.parseInt(itemObj.getString("qty")));
                                item.setPrice(Double.parseDouble(itemObj.getString("price")));
                                if(!itemObj.getString("discount_per_product").contentEquals("null")){
                                    item.setDiscount_per_product(Double.parseDouble(itemObj.getString("discount_per_product")));
                                }
                                else{
                                    item.setDiscount_per_product(0);
                                }

                                item.setFlavour(itemObj.getString("flavour_name"));

                                db.addOrderProduct(item);
                            }

                        }

                        Collections.sort(orderList, Order.StuNameComparator);
                        Collections.sort(orderList,Order.OrderIdComparator);
                        adapter = new PastOrderStatisticAdapter(PastOrderActivity.this,orderList);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Order selectedOrder = Constant.filterList.get(position);

                                Log.i("Order ID", selectedOrder.getOrderId());
                                Log.i("Store Name", selectedOrder.getStoreName());
                                Log.i("Due Date", selectedOrder.getDeliveryDate());
                                Log.i("Total Price", selectedOrder.getOrderAmount() + "");

                                Constant.OrderId = selectedOrder.getOrderId();
                                Constant.StoreName = selectedOrder.getStoreName();
                                Constant.DeliverDate = selectedOrder.getDeliveryDate();
                                Constant.DiscountPrice = selectedOrder.getOrderDiscount();
                                Constant.GRANDTOTAL = String.valueOf(selectedOrder.getOrderAmount());
                                Constant.TOTALPRICE = String.valueOf(selectedOrder.getOrderAmount()+selectedOrder.getOrderDiscount());
                                Constant.OrderDate = selectedOrder.getOrderDate();

                                List<Item> itemList = db.getOrderItems(Constant.OrderId);
                                Log.i("Item Size", itemList.size() + "");
                                if (itemList.size() > 0) {
                                    Constant.itemList = itemList;
                                    Intent i = new Intent(PastOrderActivity.this, PastOrderDetailActivity.class);
                                    startActivity(i);
                                }
//                                if(conn.isConnectingToInternet()){
//                                    //getPastOrderDetail(Constant.OrderId);
//                                }
//                                else{
//
//                                }
                            }
                        });
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject obj =jsonArray.getJSONObject(i);
//                            Category category = new Category();
//                            category.setId(Integer.parseInt(obj.getString("id")));
//                            category.setName(obj.getString("name"));
//                            db.addCategory(category);
//                        }

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
                Log.e("GET PAST ORDERS ERROR", "GET PAST ORDERS Error: " + error.getMessage());
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

    List<Item> listOfItems;
    private void getPastOrderDetail(final String orderId){
        String tag_string_req = "req_all_past_order_detail";

        pDialog.setMessage("Please wait ...");
        showDialog();

        Log.i("Run Before", tag_string_req);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constant.POST_GET_ALL_PAST_ORDER_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", "GET PAST ORDERS DETAILS Response: " + response.toString());
                hideDialog();

                try {
                    if(response.contentEquals("No Record Available")){

                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj =jsonArray.getJSONObject(i);
                            Constant.OrderId = obj.getString("id");
                            Constant.OrderDate = obj.getString("order_date");
                            listOfItems = new ArrayList<>();
                            JSONArray itemArray = obj.getJSONArray("items");

                            for(int j=0;j<itemArray.length();j++){
                                JSONObject obj1 = itemArray.getJSONObject(j);
                                String ProductName = obj1.getString("product_name")+"\n"+obj1.getString("flavour_name");
                                int Qty = Integer.parseInt(obj1.getString("qty"));
                                double Price = Double.parseDouble(obj1.getString("price"));
                                Log.i("Product Name",ProductName);
                                Log.i("Qty",Qty+"");
                                Log.i("Price",Price+"");
                                //item.setOrderId();
                                //db.addOrderProduct();
                                listOfItems.add(new Item(ProductName, Qty, Price));
                            }
                            Constant.itemList = listOfItems;
                            Log.i("List Size",Constant.itemList.size()+"");
                        }


                        Intent i = new Intent(PastOrderActivity.this,PastOrderDetailActivity.class);
                        startActivity(i);
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject obj =jsonArray.getJSONObject(i);
//                            Category category = new Category();
//                            category.setId(Integer.parseInt(obj.getString("id")));
//                            category.setName(obj.getString("name"));
//                            db.addCategory(category);
//                        }

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
                Log.e("GET PAST ORDERS ERROR", "GET PAST ORDERS Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", orderId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

//    private void getAllPastOrders(final String userId){
//        String tag_string_req = "req_all_past_order";
//
//        pDialog.setMessage("Please wait ...");
//        showDialog();
//
//        Log.i("Run Before", tag_string_req);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                Constant.POST_GET_ALL_PAST_ORDERS, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("Response", "GET PAST ORDERS Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    if(response.contentEquals("No Record Available")){
//
//                    }
//                    else{
//                        JSONArray jsonArray = new JSONArray(response);
//                        orderList = new ArrayList<>();
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject obj =jsonArray.getJSONObject(i);
//                            Order order = new Order();
//                            order.setOrderId(obj.getString("id"));
//                            order.setStoreName(obj.getString("store_name"));
//                            order.setDeliveryDate(obj.getString("due_date"));
//                            order.setOrderDate(obj.getString("order_date"));
//                            //order.set
//                            order.setOrderAmount(Double.parseDouble(obj.getString("total_order")));
//                            //db.addOrder(order,Constant.loginUser.getId());
//                            orderList.add(order);
//                        }
//
//                        adapter = new PastOrderStatisticAdapter(PastOrderActivity.this,orderList);
//                        listView.setAdapter(adapter);
//
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                if(conn.isConnectingToInternet()){
//                                    Order selectedOrder = orderList.get(position);
//                                    Log.i("Order ID",selectedOrder.getOrderId());
//                                    Log.i("Store Name",selectedOrder.getStoreName());
//                                    Log.i("Due Date",selectedOrder.getDeliveryDate());
//                                    Log.i("Total Price",selectedOrder.getOrderAmount()+"");
//
//                                    Constant.OrderId = selectedOrder.getOrderId();
//                                    Constant.StoreName = selectedOrder.getStoreName();
//                                    Constant.DeliverDate = selectedOrder.getDeliveryDate();
//                                    Constant.TOTALPRICE = String.valueOf(selectedOrder.getOrderAmount());
//
//                                    getPastOrderDetail(Constant.OrderId);
//                                }
//                            }
//                        });
////                        for(int i=0;i<jsonArray.length();i++){
////                            JSONObject obj =jsonArray.getJSONObject(i);
////                            Category category = new Category();
////                            category.setId(Integer.parseInt(obj.getString("id")));
////                            category.setName(obj.getString("name"));
////                            db.addCategory(category);
////                        }
//
////                        Intent intent = new Intent(MainActivity.this,
////                                DashboardActivity.class);
////                        startActivity(intent);
////                        finish();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("GET PAST ORDERS ERROR", "GET PAST ORDERS Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", userId);
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }
//
//    List<Item> listOfItems;
//    private void getPastOrderDetail(final String orderId){
//        String tag_string_req = "req_all_past_order_detail";
//
//        pDialog.setMessage("Please wait ...");
//        showDialog();
//
//        Log.i("Run Before", tag_string_req);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                Constant.POST_GET_ALL_PAST_ORDER_DETAILS, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("Response", "GET PAST ORDERS DETAILS Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    if(response.contentEquals("No Record Available")){
//
//                    }
//                    else{
//                        JSONArray jsonArray = new JSONArray(response);
//
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject obj =jsonArray.getJSONObject(i);
//                            Constant.OrderId = obj.getString("id");
//                            Constant.OrderDate = obj.getString("order_date");
//                            listOfItems = new ArrayList<>();
//                            JSONArray itemArray = obj.getJSONArray("items");
//
//                            for(int j=0;j<itemArray.length();j++){
//                                JSONObject obj1 = itemArray.getJSONObject(j);
//                                String ProductName = obj1.getString("product_name")+"\n"+obj1.getString("flavour_name");
//                                int Qty = Integer.parseInt(obj1.getString("qty"));
//                                double Price = Double.parseDouble(obj1.getString("price"));
//                                Log.i("Product Name",ProductName);
//                                Log.i("Qty",Qty+"");
//                                Log.i("Price",Price+"");
//                                //item.setOrderId();
//                                //db.addOrderProduct();
//                                listOfItems.add(new Item(ProductName, Qty, Price));
//                            }
//                            Constant.itemList = listOfItems;
//                            Log.i("List Size",Constant.itemList.size()+"");
//                        }
//
//
//                        Intent i = new Intent(PastOrderActivity.this,PastOrderDetailActivity.class);
//                        startActivity(i);
////                        for(int i=0;i<jsonArray.length();i++){
////                            JSONObject obj =jsonArray.getJSONObject(i);
////                            Category category = new Category();
////                            category.setId(Integer.parseInt(obj.getString("id")));
////                            category.setName(obj.getString("name"));
////                            db.addCategory(category);
////                        }
//
////                        Intent intent = new Intent(MainActivity.this,
////                                DashboardActivity.class);
////                        startActivity(intent);
////                        finish();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("GET PAST ORDERS ERROR", "GET PAST ORDERS Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("order_id", orderId);
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void initializeControls(){
        btnBack = (Button) findViewById(R.id.btn_back);
        listView = (ExpandableHeightGridView) findViewById(R.id.list_past_orders);
        listView.setExpanded(true);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }
}
