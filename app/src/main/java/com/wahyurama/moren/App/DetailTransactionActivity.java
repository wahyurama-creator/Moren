package com.wahyurama.moren.App;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wahyurama.moren.R;

public class DetailTransactionActivity extends AppCompatActivity {

    private TextView transactionID, bookingDate, carMerk, carType, username,
            email, price;
    private ImageView ivBack;
    private MaterialButton btnCancel;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        transactionID = findViewById(R.id.tv_transaction_id_detail_transaction);
        bookingDate = findViewById(R.id.tv_date_detail_transaction);
        carMerk = findViewById(R.id.tv_merk_confirm_detail_transaction);
        carType = findViewById(R.id.tv_type_confirm_detail_transaction);
        username = findViewById(R.id.tv_username_detail_transaction);
        email = findViewById(R.id.tv_email_detail_transaction);
        price = findViewById(R.id.tv_price_confirm_detail_transaction);
        btnCancel = findViewById(R.id.btn_cancel);
        ivBack = findViewById(R.id.iv_back_detail_transaction);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        transactionID.setText(getIntent().getStringExtra("transactionID"));
        bookingDate.setText(getIntent().getStringExtra("date"));
        carMerk.setText(getIntent().getStringExtra("tvMerk"));
        carType.setText(getIntent().getStringExtra("tvType"));
        username.setText(getIntent().getStringExtra("username"));
        email.setText(getIntent().getStringExtra("email"));
        price.setText("$ " + getIntent().getStringExtra("price"));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteData();
            }
        });
    }

    private void DeleteData() {
        String transactionID = getIntent().getStringExtra("transactionID");

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Cancel book ?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myRef = firebaseDatabase.getReference("transaction")
                        .child(mAuth.getCurrentUser().getUid())
                        .child(transactionID);
                myRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailTransactionActivity.this,
                                "Booking cancelled", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DetailTransactionActivity.this, TransactionActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailTransactionActivity.this,
                                "Failed to cancel: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.create().show();

    }
}