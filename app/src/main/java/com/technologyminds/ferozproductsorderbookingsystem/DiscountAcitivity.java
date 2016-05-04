package com.technologyminds.ferozproductsorderbookingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Adapters.DiscountAdapter;
import Utils.Constant;
import Utils.Item;

public class DiscountAcitivity extends AppCompatActivity {

    Button btnOk,btnBack;
    ExpandableHeightGridView listDiscount;

    DiscountAdapter adapter;
    List<Item> listOfItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_acitivity);

        initializeControls();


        if(Constant.IS_FROM_BOOK_ORDER) {
            listOfItems = new ArrayList<>();

            for (int i = 0; i < Constant.itemList.size(); i++) {
                Log.i("Quantity" + i, String.valueOf(Constant.itemList.get(i).getQuantity()));
                if (!String.valueOf(Constant.itemList.get(i).getQuantity()).contentEquals("0")) {
                    Item item = Constant.itemList.get(i);
                    boolean isNotRepeat = true;
                    for (int j = 0; j < listOfItems.size(); j++) {
                        if (listOfItems.get(j).getItemName().contentEquals(item.getItemName())) {
                            isNotRepeat = false;
                            break;
                        }
                    }

                    if (isNotRepeat)
                        listOfItems.add(item);
//                listOfItems.add(new Item(Constant.itemList.get(i).getItemName()+"\n"+Constant.itemList.get(i).getFlavour(),
//                        Constant.itemList.get(i).getQuantity(), Constant.itemList.get(i).getPrice()));
                }
            }

            Constant.itemDiscountList = new ArrayList<>();
            adapter = new DiscountAdapter(this,listOfItems);
            listDiscount.setAdapter(adapter);
        }
        else{

            if(Constant.IS_FROM_ORDER_STATISTIC){
                if(Constant.itemDiscountList.size()>0){
                    listOfItems = Constant.itemDiscountList;
                    Constant.itemDiscountList = new ArrayList<>();
                    adapter = new DiscountAdapter(this,listOfItems);
                    listDiscount.setAdapter(adapter);

//                    Constant.itemDiscountList = new ArrayList<>();
//                    adapter = new DiscountAdapter(this,listOfItems);
//                    listDiscount.setAdapter(adapter);
                }
            }

        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.IS_FROM_BOOK_ORDER=false;
                Constant.IS_FROM_ORDER_STATISTIC=false;
                Intent i = new Intent(DiscountAcitivity.this,OrderStatisticActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.IS_FROM_BOOK_ORDER=false;
                Constant.IS_FROM_ORDER_STATISTIC=false;
                Intent i = new Intent(DiscountAcitivity.this, BookAnOrderActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    private void initializeControls(){
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnBack = (Button) findViewById(R.id.btn_back);

        listDiscount = (ExpandableHeightGridView) findViewById(R.id.list_discount);
        listDiscount.setExpanded(true);
        listOfItems = new ArrayList<>();
    }
}
