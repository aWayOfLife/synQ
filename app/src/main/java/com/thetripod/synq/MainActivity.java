package com.thetripod.synq;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter<BookingCompleted, HomeHolder> mRecyclerViewAdapter;
    private List<BookingCompleted> homeList = new ArrayList<BookingCompleted>();
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView logout, bankerName, slot, serviceId, waitQueue, eta, bookingId, bookingDate;

    //LinearLayout currentBookingInfo;

    private String selectedBranch;

    private String city = "Bangalore";



    private FloatingActionButton fab_add,fab_edit;
    private LinearLayout emptystate, filledstate;
    private TextView currentBookingDate, currentBookingId, currentBookingSlot, currentBookingEta, currentBookingStatus, currentBookingServiceId;
    private Spinner spinBranch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_modified);

        fab_add = findViewById(R.id.new_booking);
        fab_edit = findViewById(R.id.edit_booking);
        emptystate = findViewById(R.id.empty_booking_state);
        filledstate = findViewById(R.id.filled_booking_state);

        currentBookingDate = findViewById(R.id.current_booking_date);
        currentBookingId = findViewById(R.id.current_booking_id);
        currentBookingSlot = findViewById(R.id.current_booking_slot);
        currentBookingEta = findViewById(R.id.current_booking_eta);
        currentBookingStatus = findViewById(R.id.current_booking_status);
        currentBookingServiceId = findViewById(R.id.current_booking_service_id);

        spinBranch = findViewById(R.id.branch_name);
        selectedBranch = String.valueOf(spinBranch.getSelectedItem());
        emptystate.setVisibility(View.VISIBLE);
        filledstate.setVisibility(View.GONE);
        spinBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedBranch = parent.getItemAtPosition(position).toString(); //this is your selected item
                //viewPreviousBookings();
                viewCurrentBookings();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fab_add = findViewById(R.id.new_booking);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startBookingActivity=new Intent(MainActivity.this,BookingActivity.class);
                startBookingActivity.putExtra("BRANCH_NAME" , String.valueOf(spinBranch.getSelectedItem()));
                startActivity(startBookingActivity);
            }
        });
        fab_edit = findViewById(R.id.edit_booking);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startBookingActivity=new Intent(MainActivity.this,BookingEditActivity.class);
                startBookingActivity.putExtra("BRANCH_NAME" , String.valueOf(spinBranch.getSelectedItem()));
                startActivity(startBookingActivity);
            }
        });


        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        viewCurrentBookings();
        viewPreviousBookings();

        recyclerView = findViewById(R.id.booking_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mRecyclerViewAdapter);


    }




    @Override
    protected void onStart() {
        super.onStart();
        mRecyclerViewAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRecyclerViewAdapter.stopListening();
    }

    void viewPreviousBookings() {

        String branch = selectedBranch;
        String userId = mAuth.getCurrentUser().getUid();
        /*Query query = FirebaseDatabase.getInstance()
                .getReference().child("bookings").child(city).child(branch).child("Booking_Completed").child(userId);*/
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("bookings").child("Booking_Completed").child(userId);
        Log.i("QUERY",query.toString());
        FirebaseRecyclerOptions<BookingCompleted> options =
                new FirebaseRecyclerOptions.Builder<BookingCompleted>()
                        .setQuery(query, BookingCompleted.class)
                        .build();

        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<BookingCompleted, HomeHolder>(options){
            @NonNull
            @Override
            public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_booking_modified, parent, false);

                return new HomeHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeHolder homeHolder, int i, @NonNull BookingCompleted bookingCompleted) {
                homeHolder.setBankerId(bookingCompleted.getBankerId());
                homeHolder.setBookingId(bookingCompleted.getBookingId());
                homeHolder.setServiceId(bookingCompleted.getServiceId());
                homeHolder.setBookingTimestamp(bookingCompleted.getBookingTimestamp());

            }


        }

        ;

    }

    void viewCurrentBookings(){

        String branch = selectedBranch;
        final DatabaseReference mRef1 = mDatabase.child("bookings").child(city).child(branch).child("Booking_Current").child(mAuth.getCurrentUser().getUid());
        Log.i("REERENCE",mRef1.toString());
        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookingCurrent bookingCurrent = dataSnapshot.getValue(BookingCurrent.class);
                fab_edit.hide();
                fab_add.show();
                if(null!= bookingCurrent) {
                    emptystate.setVisibility(View.GONE);
                    filledstate.setVisibility(View.VISIBLE);
                    currentBookingServiceId.setText(bookingCurrent.getServiceId());
                    currentBookingEta.setText(bookingCurrent.getEta());
                    currentBookingStatus.setText(bookingCurrent.getStatus());
                    currentBookingSlot.setText(bookingCurrent.getSlot());
                    currentBookingId.setText(bookingCurrent.getBookingId());
                    currentBookingDate.setText("Today - "+convertTimestampToDate(Long.valueOf(bookingCurrent.getBookingTimestamp())));
                    if (null != bookingCurrent.getBankerId()) {
                        final DatabaseReference mRef2 = mDatabase.child("Banker").child(bookingCurrent.getBankerId());
                        mRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                BankerDetails bankerDetails = dataSnapshot.getValue(BankerDetails.class);
                                bankerName.setText(bankerDetails.getName());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        fab_add.hide();
                        fab_edit.hide();
                    }
                    //bankerName.setText("Not Yet Assigned");
                    fab_add.hide();
                    fab_edit.show();
                }
                else
                {
                    emptystate.setVisibility(View.VISIBLE);
                    filledstate.setVisibility(View.GONE);

                }



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

}
