package com.thetripod.synq;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference nDatabase;
    private Spinner spin_city,spin_branch,spin_slot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mAuth = FirebaseAuth.getInstance();
        spin_city = (Spinner)findViewById(R.id.spin_city);
        spin_branch= (Spinner)findViewById(R.id.spin_branch);
        spin_city.setOnItemSelectedListener(this);
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
        spin_slot = findViewById(R.id.spin_slot);
        String availableslot=String.valueOf(spin_slot.getSelectedItem());
        EditText serviceId = findViewById(R.id.enter_activity);
        long timestamp = System.currentTimeMillis();
        String sp2= String.valueOf(spin_branch.getSelectedItem());
        String userId = mAuth.getCurrentUser().getUid();
        String branch = sp2;
        String date = "01-09-2019";
        //String availableSlot = "1";
        String city =  String.valueOf(spin_city.getSelectedItem());

        Toast.makeText(this, sp2, Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Current");
        BookingCurrent bookingCurrent = new BookingCurrent(userId, serviceId.getText().toString(),availableslot, branch , timestamp+"","ongoing","20 mins","10", "1");
        mDatabase.child(userId).setValue(bookingCurrent);

        nDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Queue").child(date).child(availableslot);
        BookingCurrent bookingQueueItem = new BookingCurrent(userId, serviceId.getText().toString(),availableslot, branch, timestamp+"","ongoing","20 mins","10", "1");
        nDatabase.child(""+timestamp).setValue(bookingQueueItem);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String sp1= String.valueOf(spin_city.getSelectedItem());
        Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
        if(sp1.contentEquals("Kolkata")) {
            List<String> list = new ArrayList<String>();
            list.add("FGMO, KOLKATA");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            spin_branch.setAdapter(dataAdapter);
        }
        if(sp1.contentEquals("Delhi")) {
            List<String> list = new ArrayList<String>();
            list.add("LAVI PUB SCHOOL GHEVRA");
            list.add("AF SCHOOL DELHI");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            spin_branch.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("Bangalore")) {
            List<String> list = new ArrayList<String>();
            list.add("CARD CENTRE");
            list.add("CO GAD BANGALORE");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            spin_branch.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("Mumbai")) {
            List<String> list = new ArrayList<String>();
            list.add("MUMBAI FORT");
            list.add("MUMBAI KHAR");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            spin_branch.setAdapter(dataAdapter2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
