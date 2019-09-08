package com.thetripod.synq;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.text.format.DateFormat;
import android.view.View;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.Calendar;

import java.util.Locale;

public class BookingActivity extends AppCompatActivity /*implements
        AdapterView.OnItemSelectedListener */{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference nDatabase;
    private Spinner newBookingServiceId,newBookingSlot;
    private String city,branch;

    private TextView cancelNewBooking, confirmNewBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_modified);

        branch=getIntent().getStringExtra("BRANCH_NAME");
        city="Bangalore";

        cancelNewBooking = findViewById(R.id.cancel_booking);
        confirmNewBooking = findViewById(R.id.confirm_booking);

        cancelNewBooking.setClickable(true);
        confirmNewBooking.setClickable(true);

        mAuth = FirebaseAuth.getInstance();

        confirmNewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEntry();
                finish();
            }
        });

        cancelNewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void dataEntry(){
        newBookingSlot = findViewById(R.id.new_booking_slot);
        newBookingServiceId = findViewById(R.id.new_booking_service_id);
        String bookingSlot = String.valueOf(newBookingSlot.getSelectedItem());
        String bookingServiceId = String.valueOf(newBookingServiceId.getSelectedItem());
        long timestamp = System.currentTimeMillis();

        String userId = mAuth.getCurrentUser().getUid();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String bookingDate = DateFormat.format("dd-MM-yyyy", cal).toString();

        String bookingStatus = "waiting in queue";
        String bookingId ="B" + DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString().replaceAll("-","").replaceAll(" ","").replaceAll(":","");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Current");
        BookingCurrent bookingCurrent = new BookingCurrent(userId, bookingServiceId,bookingSlot, branch , timestamp+"",bookingStatus,"20 mins","10", bookingId,null);
        mDatabase.child(userId).setValue(bookingCurrent);

        nDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Queue").child(bookingDate).child(bookingSlot);
        BookingCurrent bookingQueueItem = new BookingCurrent(userId, bookingServiceId, bookingSlot, branch, timestamp+"","ongoing","20 mins","10", bookingId,null);
        nDatabase.child(""+timestamp).setValue(bookingQueueItem);


    }


}
