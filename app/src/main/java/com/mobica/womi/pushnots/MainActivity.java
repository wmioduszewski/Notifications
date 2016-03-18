package com.mobica.womi.pushnots;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.mobica.womi.pushnots.model.CompleteNotificationInfo;
import com.mobica.womi.pushnots.model.DelayModel;
import com.mobica.womi.pushnots.model.NotificationModel;
import com.mobica.womi.pushnots.storage.StorageManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String ARGUMENT_ID = "passedArgument";
    public static final String ACTION_ID = "com.mobica.womi.pushnots.NOTIFICATION";
    private static final String TIME_PATTERN = "HH:mm";

    private AlarmScheduler scheduler;
    private PendingIntent pendingAlarmIntent;
    private Calendar calendar;
    private TextView tvDate;
    private TextView tvTime;
    private RadioButton rbLater;
    private RadioButton rbNow;
    private Button btnCancel;
    private Button btnSubmit;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        scheduler = new AlarmScheduler(this);

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        rbLater = (RadioButton) findViewById(R.id.rbLater);
        rbNow = (RadioButton) findViewById(R.id.rbNow);

        fillBuildInfo();
        setEvents();
        update();
        setAlarmAfterReboot();
    }

    private void setEvents() {
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
    }

    private void setAlarmAfterReboot() {
        ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
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
        TextView sender = (TextView) v;
        switch (sender.getId()) {
            case R.id.btn_submit:
                scheduleAlarm();
                break;
            case R.id.btn_cancel:
                cancelAlarm();
                break;
            case R.id.tvDate:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.tvTime:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
        }
    }

    private void cancelAlarm() {
        if (pendingAlarmIntent != null) {
            scheduler.cancelAlarm(pendingAlarmIntent);
            StorageManager.cancelAll(this);
        }


    }

    private void fillBuildInfo() {
        Date buildDate = new Date(BuildConfig.TIMESTAMP);
        String buildInfo = "App built: " + buildDate;
        TextView tvBuildInfo = (TextView) findViewById(R.id.tvBuildDate);
        tvBuildInfo.setText(buildInfo);
    }

    private NotificationModel getNotificationInfo() {
        NotificationModel notificationModel = new NotificationModel();

        EditText editText = (EditText) findViewById(R.id.etTitle);
        notificationModel.setTitle(editText.getText().toString());

        CheckBox cbIsLong = (CheckBox) findViewById(R.id.cbIsLongText);
        notificationModel.setIsLongContent(cbIsLong.isChecked());

        CheckBox cbAutoCancelling = (CheckBox) findViewById(R.id.cbAutoCancelling);
        notificationModel.setIsAutoCancel(cbAutoCancelling.isChecked());

        if (((RadioButton) findViewById(R.id.rbLargeImagePanda)).isChecked()) {
            notificationModel.setLargeImageId(R.drawable.panda);
        }
        if (((RadioButton) findViewById(R.id.rbLargeImageMonkey)).isChecked()) {
            notificationModel.setLargeImageId(R.drawable.monkey);
        }

        if (((RadioButton) findViewById(R.id.rbSmallImagePanda)).isChecked()) {
            notificationModel.setSmallImageId(R.drawable.panda);
        }
        if (((RadioButton) findViewById(R.id.rbSmallImageMonkey)).isChecked()) {
            notificationModel.setSmallImageId(R.drawable.monkey);
        }
        if (((RadioButton) findViewById(R.id.rbSmallImageStar)).isChecked()) {
            notificationModel.setSmallImageId(R.drawable.star);
        }

        return notificationModel;
    }

    private DelayModel getDelayInfo() {
        DelayModel delayModel = new DelayModel();

        Calendar requestedStart;
        if (rbNow.isChecked())
            requestedStart = Calendar.getInstance();
        else
            requestedStart = calendar;
        delayModel.setStartTime(requestedStart);

        CheckBox cbIsRepeatable = (CheckBox) findViewById(R.id.cbRepeat);
        delayModel.setIsRepeatable(cbIsRepeatable.isChecked());

        EditText etMinutes = (EditText) findViewById(R.id.etMinutes);
        int minutes = Integer.parseInt(etMinutes.getText().toString());
        delayModel.setRepetitionIntervalInMinutes(minutes);

        return delayModel;
    }

    private void scheduleAlarm() {
        NotificationModel notificationDetails = getNotificationInfo();
        DelayModel delayDetails = getDelayInfo();
        CompleteNotificationInfo cni = new CompleteNotificationInfo();
        cni.setNotificationModel(notificationDetails);
        cni.setDelayModel(delayDetails);
        StorageManager.add(this, cni);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra(MainActivity.ARGUMENT_ID, notificationDetails);
        alarmIntent.setAction(MainActivity.ACTION_ID);
        pendingAlarmIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        scheduler.scheduleAlarm(pendingAlarmIntent, delayDetails);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    private void update() {
        tvDate.setText(dateFormat.format(calendar.getTime()));
        tvTime.setText(timeFormat.format(calendar.getTime()));
        rbLater.setChecked(true);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }
}
