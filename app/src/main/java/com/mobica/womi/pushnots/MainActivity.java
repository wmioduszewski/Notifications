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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String ARGUMENT_ID = "passedArgument";
    private static final String TIME_PATTERN = "HH:mm";

    private AlarmManager alarmManager;
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
    }

    private void setEvents() {
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
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
        alarmManager.cancel(pendingAlarmIntent);
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

    private void scheduleAlarm() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra(ARGUMENT_ID, getNotificationInfo());

        pendingAlarmIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Long time = new GregorianCalendar().getTimeInMillis() + 1000 * 2;

        long intervalInMinutes = 1;
        long interval = intervalInMinutes * 1000 * 60;
        //alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingAlarmIntent);

        Toast.makeText(this, "Notification send to appearance", Toast.LENGTH_LONG).show();
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
