package com.thetripod.synq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class BookingEditActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase , nDatabase , pDatabase;
    private Spinner editBookingServiceId, editBookingSlot;
    private EditText serviceId ;
    private String city , branch , mUserId , date;

    private TextView cancelEditBooking, confirmEditBooking, deleteEditBooking ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_edit_modified);

        city = "Bangalore";
        branch = getIntent().getStringExtra("BRANCH_NAME");

        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();

        editBookingServiceId = findViewById(R.id.edit_booking_service_id);
        editBookingSlot = findViewById(R.id.edit_booking_slot);

        date = convertTimestampToDate(System.currentTimeMillis());

        populateBooking();

        cancelEditBooking = findViewById(R.id.cancel_edit_booking);
        confirmEditBooking = findViewById(R.id.reschedule_edit_booking);
        deleteEditBooking = findViewById(R.id.delete_edit_booking);
        cancelEditBooking.setClickable(true);
        confirmEditBooking.setClickable(true);
        deleteEditBooking.setClickable(true);



        cancelEditBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmEditBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataReschedule();
                finish();
            }
        });

        deleteEditBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataDelete();
                finish();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void populateBooking(){
        pDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Current")
                .child(mUserId);
        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookingCurrent bookingCurrent = dataSnapshot.getValue(BookingCurrent.class);
                //serviceId.setText(bookingCurrent.getServiceId());
                String service = bookingCurrent.getServiceId();
                ArrayAdapter<CharSequence> service_adapter = ArrayAdapter.createFromResource(BookingEditActivity.this, R.array.service_selector, android.R.layout.simple_spinner_item);
                service_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editBookingServiceId.setAdapter(service_adapter);
                if (service != null) {
                    int spinnerPosition = service_adapter.getPosition(service);
                    editBookingServiceId.setSelection(spinnerPosition);
                }

                String compareValue = bookingCurrent.getSlot();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(BookingEditActivity.this, R.array.slot_selector, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editBookingSlot.setAdapter(adapter);
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    editBookingSlot.setSelection(spinnerPosition);
                }
                pDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void dataReschedule(){

        final String availableslot = String.valueOf(editBookingSlot.getSelectedItem());
        final String serviceId = String.valueOf(editBookingServiceId.getSelectedItem());
        pDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Current")
                .child(mAuth.getCurrentUser().getUid());
        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookingCurrent bookingCurrent = dataSnapshot.getValue(BookingCurrent.class);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Queue").child(date)
                        .child(bookingCurrent.getSlot()).child(bookingCurrent.getBookingTimestamp());
                mDatabase.removeValue();

                BookingCurrent book = new BookingCurrent(mUserId ,serviceId , availableslot , bookingCurrent.getBranch() ,
                        bookingCurrent.getBookingTimestamp() , bookingCurrent.getStatus() , bookingCurrent.getEta() , bookingCurrent.getQueuePosition() ,
                        bookingCurrent.getBookingId() , null);
                pDatabase.setValue(book);



                nDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Queue")
                        .child(date)
                        .child(availableslot).child(bookingCurrent.getBookingTimestamp());
                nDatabase.setValue(book);

                pDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public String convertTimestampToDate(Long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public void dataDelete(){

        pDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Current")
                .child(mAuth.getCurrentUser().getUid());
        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookingCurrent bookingCurrent = dataSnapshot.getValue(BookingCurrent.class);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(city).child(branch).child("Booking_Queue")
                        .child(date).child(bookingCurrent.getSlot()).child(bookingCurrent.getBookingTimestamp());

                mDatabase.removeValue();
                pDatabase.removeValue();
                pDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}
