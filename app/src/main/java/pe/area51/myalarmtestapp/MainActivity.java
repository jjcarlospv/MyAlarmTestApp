package pe.area51.myalarmtestapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_ALARM_FIRED = "pe.area51.myalarmtestapp.ALARM_FIRED";
    private TextView main_activity_status;
    private AlarmManager alarmManager;
    private AlarmBroadcastReceiver alarmBroadcastReceiver;

    private class AlarmBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            setAlarmStatusTextView(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_activity_status = (TextView)findViewById(R.id.main_activity_status);
        setAlarmStatusTextView(isAlarmSet());

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmBroadcastReceiver = new AlarmBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ALARM_FIRED);
        registerReceiver(alarmBroadcastReceiver,intentFilter);

    }

    private MenuItem menuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.set_alarm:

                if(isAlarmSet()){
                    stopAlarm();
                    item.setTitle(R.string.alarm_programar_alarma);
                    setAlarmStatusTextView(false);
                }
                else{
                    setAlarm(5);
                    item.setTitle(R.string.alarm_cancelar_alarma);
                    setAlarmStatusTextView(true);
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void setAlarm(final int timeInSeconds){

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,getAlarmIntent(),PendingIntent.FLAG_CANCEL_CURRENT);
        final long realtimeTriggerAtMillis = SystemClock.elapsedRealtime()+ timeInSeconds*1000;

        /* Podemos poner al targetSDk a una version menor al API 19(Kitkat),
        sin embargo vamos a mantener el targetSDK a la última versión posible
        y vamos a preguntar qué versión estamos ejecutando  para seleccionar el método adecuado. Esto se realizará
        con la finalidad que la aplicación se ejecute en modo compatibilidad en las versiones nuevas
        */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, realtimeTriggerAtMillis,pendingIntent);
        }
        else{
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, realtimeTriggerAtMillis, pendingIntent);

        }

    }

    private Intent getAlarmIntent(){
        return new Intent(ACTION_ALARM_FIRED);
    }

    public void stopAlarm(){

        alarmManager.cancel(PendingIntent.getBroadcast(this,0,getIntent(),PendingIntent.FLAG_CANCEL_CURRENT));
        setAlarmStatusTextView(false);
    }

    public boolean isAlarmSet(){

        return PendingIntent.getBroadcast(this,0,getIntent(),PendingIntent.FLAG_NO_CREATE)!= null;
    }

    private void setAlarmStatusTextView(final boolean status){
        if(isAlarmSet())
        {
            main_activity_status.setText(getResources().getString(R.string.Status,getResources().getString(R.string.Status_programmed)));

        }
        else{
            main_activity_status.setText(getResources().getString(R.string.Status,getResources().getString(R.string.Status_canceled)));

        }
    }
}
