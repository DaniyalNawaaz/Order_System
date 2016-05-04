package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import Utils.Constant;


/**
 * Created by Daniyal Nawaz on 2/26/2016.
 */
public class GcmIntentService extends IntentService {

    public static final int notifyID = 9001;
    NotificationCompat.Builder mNotifyBuilder;
    NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            Bundle extras = intent.getExtras();
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            //Toast.makeText(getApplicationContext(), "" + extras.get(AppConfig.MSG_KEY), Toast.LENGTH_SHORT).show();
            String messageType = gcm.getMessageType(intent);
            Log.i("Message","-"+extras.get(Constant.MSG_KEY));
            if (!extras.isEmpty()) {
                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                        .equals(messageType)) {
                    sendNotification("Send error: " + extras.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                        .equals(messageType)) {
                    sendNotification("Deleted messages on server: "
                            + extras.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                        .equals(messageType)) {
                    if(extras.get(Constant.MSG_KEY)!=null){
                        sendNotification("Message: "
                                + extras.get(Constant.MSG_KEY));
                    }
                }
            }
            GcmBroadcastReceiver.completeWakefulIntent(intent);

        }
        catch (Exception exp){
            Toast.makeText(getApplicationContext(),"Application have error\n"+exp.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    private void sendNotification(String msg) {
        Intent resultIntent = new Intent(this, NotificationActivity.class);
        resultIntent.putExtra("msg", msg);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);



        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this);
        mNotifyBuilder.setContentTitle("Metro Alert News")
                .setContentText(msg)
                .setSmallIcon(R.drawable.notification);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(msg);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}
