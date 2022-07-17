package com.main.easy2learnproject.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.main.easy2learnproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET, locationET;
    private TextView eventDateTV, eventTimeTV,eventTitle;
    private TextInputLayout timeTextView;
    private LocalTime time;
    private String timeTxt = "-1";
    private TextView textViewShowDate;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        getSupportActionBar().hide();
        initWidgets();
        initViews();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
    }

    private void initViews() {
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        eventTitle = findViewById(R.id.eventTitle);
        locationET = findViewById(R.id.event_EDT_location);
        timeTextView =(TextInputLayout) findViewById(R.id.event_edit_timeTextView);
        textViewShowDate = findViewById(R.id.photoActivity_textViewShowDate);


    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
//        boolean is24HourFormat = DateFormat.is24HourFormat(this);
        boolean is24HourFormat = true;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
//                String dateText = DateFormat.format("h:mm a", calendar1).toString();

//                timeTextView.setText(hour + ":" + minute);
                timeTxt = hour + ":" + minute;
//                textViewShowDate.setText(getDateString());
                eventTimeTV.setText("Time: " + timeTxt);

            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }

    public String getDateString() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        todayAsString = CalendarUtils.formattedDate(CalendarUtils.selectedDate)+"\t"+timeTxt;
        return todayAsString;
    }


    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        String loc = locationET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, timeTxt,loc,locationET.getText().toString(),eventTitle.getText().toString());
        Event.eventsList.add(newEvent);
        finish();
    }
}