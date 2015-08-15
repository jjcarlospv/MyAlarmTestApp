package pe.area51.myalarmtestapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by alumno on 8/15/15.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "AlarmBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()){

            case MainActivity.ACTION_ALARM_FIRED:
                Log.d(TAG,"Alarm fired!!!");
                Toast.makeText(context,"Alarm Fired",Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d(TAG,"Intent with not action");
                break;

        }
    }
}
