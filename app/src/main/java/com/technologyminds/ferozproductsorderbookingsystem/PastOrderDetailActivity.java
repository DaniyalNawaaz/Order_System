package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Adapters.OrderStatisticAdapter;
import Utils.Constant;
import Utils.Item;

public class PastOrderDetailActivity extends AppCompatActivity {
    ExpandableHeightGridView itemGrid;
    OrderStatisticAdapter adapter;
    List<Item> listOfItems;

    Button btnBack;
    TextView txtSubTotalAmmount,txtTotalAmmount,txtTotalDiscount;
    TextView txtStoreName,txtOrderCode,txtOrderDate,txtDeliverDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_order_detail);

        initializeControls();
    }

    private void initializeControls(){
        itemGrid = (ExpandableHeightGridView) findViewById(R.id.list_order_statistic);
        itemGrid.setExpanded(true);
        btnBack = (Button) findViewById(R.id.btn_back);

        txtStoreName = (TextView) findViewById(R.id.txt_order_store_name);
        txtOrderCode = (TextView) findViewById(R.id.txt_order_code);
        txtOrderDate = (TextView) findViewById(R.id.txt_order_date);
        txtDeliverDate = (TextView) findViewById(R.id.txt_order_delivery_date);

        txtOrderDate.setText(Constant.OrderDate);

        txtStoreName.setText(Constant.StoreName);
        txtOrderCode.setText(Constant.OrderId);
        txtDeliverDate.setText(Constant.DeliverDate);
        txtOrderDate.setText(Constant.OrderDate);

        txtSubTotalAmmount = (TextView) findViewById(R.id.txt_sub_total_ammount);
        txtTotalAmmount = (TextView) findViewById(R.id.txt_total_ammount);
        txtTotalDiscount = (TextView) findViewById(R.id.txt_total_discount);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.itemList = new ArrayList<Item>();
                Constant.OrderDate = "";
                Constant.DeliverDate = "";
                Constant.StoreName = "";
                Constant.StoreCode = "";
                Constant.StoreNumber = "";
                Constant.SelectedInstruction = "";
                Constant.OrderId = "";
                Constant.TOTALPRICE = "";
                Intent i = new Intent(PastOrderDetailActivity.this, PastOrderActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        listOfItems = Constant.itemList;

        adapter = new OrderStatisticAdapter(this,listOfItems);
        itemGrid.setAdapter(adapter);

        txtSubTotalAmmount.setText(Constant.TOTALPRICE);
        txtTotalAmmount.setText(Constant.GRANDTOTAL);
        txtTotalDiscount.setText(String.valueOf(Constant.DiscountPrice));
    }
}
