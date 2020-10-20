package com.wahyurama.moren;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wahyurama.moren.Adapter.RecyclerViewAdapter;
import com.wahyurama.moren.Model.Car;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    private ArrayList<Car> carList;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Firebase
        myRef = FirebaseDatabase.getInstance().getReference();
        carList = new ArrayList<>();

        // Clear Array
        ClearArray();

        // GET Data from Firebase
        GetDataFromFirebase();

    }

    private void GetDataFromFirebase() {
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
                    car.setPrice(dataSnapshot.child("price").getValue().toString());

                    carList.add(car);
                }

                recyclerViewAdapter =
                        new RecyclerViewAdapter(getApplicationContext(),
                                carList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}