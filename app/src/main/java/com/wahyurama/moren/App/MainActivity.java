package com.wahyurama.moren.App;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wahyurama.moren.Adapter.BookAdapter;
import com.wahyurama.moren.Adapter.RecyclerViewAdapter;
import com.wahyurama.moren.Model.Book;
import com.wahyurama.moren.Model.Car;
import com.wahyurama.moren.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    private RecyclerView rvCarBooked;
    private ArrayList<Car> carList;
    private ArrayList<Book> bookedList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private BookAdapter bookedAdapter;
    private ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayoutBooked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvItem);
        rvCarBooked = findViewById(R.id.rvCarBooked);
        rvCarBooked.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Firebase
        myRef = FirebaseDatabase.getInstance().getReference();
        carList = new ArrayList<>();
        bookedList = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ImageView ivAccount = findViewById(R.id.iv_account);
        ImageView ivHistory = findViewById(R.id.iv_history);
        ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
            }
        });
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,
                        TransactionActivity.class));
            }
        });

        // Clear Array
        ClearArray();

        shimmerFrameLayout = findViewById(R.id.rvShimmer);
        shimmerFrameLayoutBooked = findViewById(R.id.rvShimmerBooked);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayoutBooked.startShimmer();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayoutBooked.stopShimmer();
                GetHorizontalItem();
                GetVerticalItem();
            }
        }, 3000);
    }

    private void GetVerticalItem() {

        Query query =
                myRef.child("booked");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cleanArray();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Book book = new Book();
                    book.setImage(dataSnapshot.child("image").getValue().toString());
                    book.setCarMerk(dataSnapshot.child("carMerk").getValue().toString());
                    book.setCarType(dataSnapshot.child("carType").getValue().toString());

                    bookedList.add(book);
                }
                bookedAdapter = new BookAdapter(getApplicationContext(),
                        bookedList);
                rvCarBooked.setAdapter(bookedAdapter);
                bookedAdapter.notifyDataSetChanged();
                shimmerFrameLayoutBooked.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetHorizontalItem() {
        Query query = myRef.child("message");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearArray();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Car car = new Car();
                    car.setImage(dataSnapshot.child("image").getValue().toString());
                    car.setMerk(dataSnapshot.child("merk").getValue().toString());
                    car.setType(dataSnapshot.child("type").getValue().toString());
                    car.setDesc(dataSnapshot.child("desc").getValue().toString());
                    car.setTransmission(dataSnapshot.child("transmission").getValue().toString());
                    car.setSeat(dataSnapshot.child("seat").getValue().toString());
                    car.setPrice(dataSnapshot.child("price").getValue().toString());

                    carList.add(car);
                }

                recyclerViewAdapter =
                        new RecyclerViewAdapter(getApplicationContext(),
                                carList);

                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ClearArray() {
        if (carList != null) {
            carList.clear();
            if (recyclerViewAdapter != null) {
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
        carList = new ArrayList<>();
    }

    private void cleanArray() {
        if (bookedList != null) {
            bookedList.clear();
            if (bookedAdapter != null) {
                bookedAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}