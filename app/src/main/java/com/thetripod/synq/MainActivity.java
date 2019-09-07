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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter<BookingCompleted, HomeHolder> mRecyclerViewAdapter;
    private List<BookingCompleted> homeList = new ArrayList<BookingCompleted>();
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView logout, bankerName, slot, serviceId, waitQueue, eta,branchNameMain;
    TextView currentBookingStatus;
    LinearLayout currentBookingInfo;
    FloatingActionButton fab,fab_edit;
    private String selectedItem;
    private Spinner spin_main;
    private String city = "Bangalore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bankerName = findViewById(R.id.bankerName_current);
        serviceId = findViewById(R.id.serviceID_current);
        slot = findViewById(R.id.slot_current);
        waitQueue = findViewById(R.id.waitQueue_current);
        eta = findViewById(R.id.eta_current);
        spin_main=findViewById(R.id.branch_name_main);
        selectedItem = String.valueOf(spin_main.getSelectedItem());
        spin_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                viewPreviousBookings();
                viewCurrentBookings();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        currentBookingInfo = findViewById(R.id.current_booking_info);
        currentBookingStatus = findViewById(R.id.current_booking_status);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fab = findViewById(R.id.new_booking);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startBookingActivity=new Intent(MainActivity.this,BookingActivity.class);
                startBookingActivity.putExtra("BRANCH_NAME" , String.valueOf(spin_main.getSelectedItem()));
                startActivity(startBookingActivity);
            }
        });
        fab_edit = findViewById(R.id.edit_booking);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startBookingActivity=new Intent(MainActivity.this,BookingEditActivity.class);
                startBookingActivity.putExtra("BRANCH_NAME" , String.valueOf(spin_main.getSelectedItem()));
                startActivity(startBookingActivity);
            }
        });

        fab_edit.hide();
        fab.hide();

        viewPreviousBookings();
        viewCurrentBookings();
        //String spin=String.valueOf(sp.getSelectedItem());
        //branchNameMain.setText(spin);
        //Log.i("string",spin);
        //Toast.makeText(MainActivity.this,spin, Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView)findViewById(R.id.booking_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mRecyclerViewAdapter);
        //checksas
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



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

        String branch = selectedItem;
        Toast.makeText(MainActivity.this, city+branch,Toast.LENGTH_SHORT).show();
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
                        .inflate(R.layout.item_booking, parent, false);

                return new HomeHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeHolder homeHolder, int i, @NonNull BookingCompleted bookingCompleted) {
                homeHolder.setBankerId(bookingCompleted.getBankerId());
                homeHolder.setBookingId(bookingCompleted.getBookingId());
                homeHolder.setBranch(bookingCompleted.getBranch());
                homeHolder.setServiceId(bookingCompleted.getServiceId());
                homeHolder.setBookingTimestamp(bookingCompleted.getBookingTimestamp());

            }


        }

        ;

    }

    void viewCurrentBookings(){

        String branch = selectedItem;
        Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
        final DatabaseReference mRef1 = mDatabase.child("bookings").child(city).child(branch).child("Booking_Current").child(mAuth.getCurrentUser().getUid());
        Log.i("REERENCE",mRef1.toString());
        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookingCurrent bookingCurrent = dataSnapshot.getValue(BookingCurrent.class);
                currentBookingStatus.setVisibility(View.VISIBLE);
                currentBookingStatus.setText("There are no current bookings");
                currentBookingInfo.setVisibility(View.GONE);
                fab_edit.hide();
                fab.show();
                //Log.i("TAG", bookingCurrent.toString());
                if(null!= bookingCurrent) {
                    currentBookingStatus.setVisibility(View.GONE);
                    currentBookingInfo.setVisibility(View.VISIBLE);
                    serviceId.setText(bookingCurrent.getServiceId());
                    eta.setText(bookingCurrent.getEta());
                    waitQueue.setText(bookingCurrent.getQueuePosition());
                    slot.setText(bookingCurrent.getSlot());
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
                        fab.hide();
                        fab_edit.hide();
                    }
                    bankerName.setText("Not Yet Assigned");
                    fab.hide();
                    fab_edit.show();
                }


                //mRef1.re
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
