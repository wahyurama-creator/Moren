package com.wahyurama.moren.App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wahyurama.moren.Adapter.TransactionAdapter;
import com.wahyurama.moren.Model.Transaction;
import com.wahyurama.moren.R;

import java.util.ArrayList;
import java.util.Objects;

public class TransactionActivity extends AppCompatActivity {

    private String UID;
    private RecyclerView transactionView;
    private ArrayList<Transaction> transactionsList;
    private TransactionAdapter adapter;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        transactionsList = new ArrayList<>();
        UID = mAuth.getCurrentUser().getUid();

        ivBack = findViewById(R.id.iv_back_transaction);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransactionActivity.this,
                        MainActivity.class));
                finish();
            }
        });

        transactionView = findViewById(R.id.rv_transaction_history);
        transactionView.setLayoutManager(new LinearLayoutManager(this));

        CleanArray();
        GetData();

    }

    private void GetData() {
        Query query = myRef.child("transaction").child(UID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CleanArray();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Transaction transaction = new Transaction();
                    transaction.setImage(dataSnapshot.child("image").getValue().toString());
                    transaction.setCarMerk(dataSnapshot.child("carMerk").getValue().toString());
                    transaction.setCarType(dataSnapshot.child("carType").getValue().toString());
                    transaction.setDate(dataSnapshot.child("date").getValue().toString());
                    transaction.setEmail(dataSnapshot.child("email").getValue().toString());
                    transaction.setTransactionID(Objects.requireNonNull(dataSnapshot.child(
                            "transactionID").getValue()).toString());
                    transaction.setUsername(dataSnapshot.child(
                            "username").getValue().toString());
                    transaction.setPrice(dataSnapshot.child(
                            "price").getValue().toString());

                    transactionsList.add(transaction);
                }

                adapter = new TransactionAdapter(getApplicationContext(),
                        transactionsList);
                transactionView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CleanArray() {
        if (transactionsList != null) {
            transactionsList.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } else {
            transactionsList = new ArrayList<>();
        }
    }
}

