package com.thetripod.synq;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter<BookingCompleted, HomeHolder> mRecyclerViewAdapter;
    private List<BookingCompleted> homeList = new ArrayList<BookingCompleted>();
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.new_booking);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startBookingActivity=new Intent(MainActivity.this,BookingActivity.class);
                startActivity(startBookingActivity);
            }
        });
        prepareEntryData();
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

    void prepareEntryData() {

        String userId = mAuth.getCurrentUser().getUid();
        long  timestamp = System.currentTimeMillis();
        String branch = "Behala";
        Query query = FirebaseDatabase.getInstance()
                .getReference().child(branch).child("Booking_Completed").child(userId);
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
}
