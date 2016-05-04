package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import Utils.ConnectionDetector;
import Utils.SqlDB;
import Utils.User;

public class LocationService extends Service {
    DashboardActivity.GPSTracker gps;
    Handler handler;
    Runnable runnable = null;
    SqlDB db;
    ConnectionDetector conn;
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Location Service", "Sending New Location");

        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                gps = new DashboardActivity.GPSTracker(LocationService.this);
                db = new SqlDB(LocationService.this);
                conn = new ConnectionDetector(LocationService.this);

                User loginUser = db.getLoginedUser();
                db.close();
                if(gps!=null){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    if(conn.isConnectingToInternet()){
                        Log.i("Call From Location Service",latitude+"--"+longitude);
                        //Toast.makeText(LocationService.this,latitude+"\n"+longitude,Toast.LENGTH_SHORT).show();
                        DashboardActivity.updateLocation(
                                String.valueOf(loginUser.getId()),
                                String.valueOf(latitude),
                                String.valueOf(longitude));
                    }
                }
                handler.postDelayed(runnable, 100000);

            }
        };
        handler.postDelayed(runnable, 100000);
    }
}
