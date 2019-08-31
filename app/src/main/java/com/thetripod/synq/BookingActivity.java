package com.thetripod.synq;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;

public class BookingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.confirm_booking);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEntry();
                finish();
            }
        });
    }

    public void dataEntry(){
        EditText slot = findViewById(R.id.enter_slot);
        EditText serviceId = findViewById(R.id.enter_activity);
        long timestamp = System.currentTimeMillis();
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking_Current");
        BookingCurrent bookingCurrent = new BookingCurrent(userId, serviceId.getText().toString(),slot.getText().toString(), "behala", timestamp+"","ongoing","20 mins","10");
        mDatabase.child(userId).setValue(bookingCurrent);


    }

}
