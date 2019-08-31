package com.thetripod.synq;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

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
        EditText slot = (EditText)findViewById(R.id.enter_slot);
        EditText activity = (EditText)findViewById(R.id.enter_activity);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingUser");
        Booking booking = new Booking("15",slot.getText().toString(),activity.getText().toString(),"20","15");
        mDatabase.child("13").setValue(booking);


    }

}
