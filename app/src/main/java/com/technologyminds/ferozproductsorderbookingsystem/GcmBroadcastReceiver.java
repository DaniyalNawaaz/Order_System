package com.technologyminds.ferozproductsorderbookingsystem;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Created by Daniyal Nawaz on 2/26/2016.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            ComponentName comp = new ComponentName(context.getPackageName(),
                    GcmIntentService.class.getName());
            Toast.makeText(context, "Broadcast Recieve", Toast.LENGTH_SHORT).show();
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }
        catch(Exception exp){
            Toast.makeText(context,"Broadcast Exception",Toast.LENGTH_SHORT).show();
        }
    }
}
