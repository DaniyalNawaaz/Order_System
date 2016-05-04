package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Utils.Helper;

public class ContactUsActivity extends AppCompatActivity {

    Button btnBack;
    TextView txtPhoneCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        initializeControls();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactUsActivity.this, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        txtPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= txtPhoneCall.getText().toString().replaceAll("-", "");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContactUsActivity.this);
                alertDialog.setTitle("Confirm Box...");
                alertDialog.setMessage("Are you sure you want to dial call on " + phone_no + "?");
                        //alertDialog.setIcon(R.drawable.ic_launcher);

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,  int which) {
                            }
                        });
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    // Write your code here to execute after dialog
                                    String phone_no= txtPhoneCall.getText().toString().replaceAll("-", "");
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + phone_no));
                                    //callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(callIntent);
                                }
                                catch (Exception exp){
                                    Helper.showToast(ContactUsActivity.this,"Your Device does not Support Calling Function");
                                }

                            }
                        });

                alertDialog.show();
//                String phone_no= txtPhoneCall.getText().toString().replaceAll("-", "");

//                try{
//                    startActivity(in);
//                }
//
//                catch (android.content.ActivityNotFoundException ex){
//                    Toast.makeText(getApplicationContext(),"Call Dial-Up Service Failed",Toast.LENGTH_SHORT).show();
//                }
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:"+phone_no));
//                //callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(callIntent);
            }
        });
    }

    private void initializeControls(){
        btnBack = (Button) findViewById(R.id.btn_back);
        txtPhoneCall = (TextView) findViewById(R.id.txt_phone_no);
    }
}
