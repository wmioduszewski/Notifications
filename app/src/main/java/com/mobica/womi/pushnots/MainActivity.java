package com.mobica.womi.pushnots;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ARGUMENT_ID = "passedArgument";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        scheduleAlarm();
    }

    private NotificationModel getNotificationInfo() {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setTitle("Taki sobie tytuł");
        notificationModel.setIsLongContent(false);
        notificationModel.setLargeImageId(R.drawable.monkey);
        notificationModel.setSmallImageId(R.drawable.panda);
        return notificationModel;
    }

    private void scheduleAlarm() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        alarmIntent.putExtra(ARGUMENT_ID, getNotificationInfo());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Long time = new GregorianCalendar().getTimeInMillis() + 1000 * 2;

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        Toast.makeText(this, "Notification send to appearance", Toast.LENGTH_LONG).show();
    }

}
