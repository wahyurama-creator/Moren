package com.wahyurama.moren.App;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wahyurama.moren.Model.Transaction;
import com.wahyurama.moren.R;

import java.util.Random;

public class ConfirmActivity extends AppCompatActivity {

    private static final String ALLOWED_CHARACTERS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String fullName, emailData;
    private TextView transactionID, bookingDate, carMerk, carType, username,
            email, price;
    private MaterialButton btnCheckout;
    private ImageView ivBack;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        ProgressBar progressBar = findViewById(R.id.progressBarOrder);
        transactionID = findViewById(R.id.tv_transaction_id);
        bookingDate = findViewById(R.id.tv_date);
        carMerk = findViewById(R.id.tv_merk_confirm);
        carType = findViewById(R.id.tv_type_confirm);
        username = findViewById(R.id.tv_username);
        email = findViewById(R.id.tv_email);
        price = findViewById(R.id.tv_price_confirm);
        btnCheckout = findViewById(R.id.btn_checkout_confirm);
        ivBack = findViewById(R.id.iv_back_confim);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(
                "transaction");
        final String UID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference =
                mStore.collection("users").document(UID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();

                    fullName = snapshot.getString("fullName");
                    emailData = snapshot.getString("email");

                    username.setText(fullName);
                    email.setText(emailData);

                    btnCheckout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.d("TAG", "Mo such document :" + task.getException());
                }
            }
        });

        String random = getRandomString();
        final String image = getIntent().getStringExtra("iv_car");
        final String date = getIntent().getStringExtra("date");
        final String merk = getIntent().getStringExtra("tv_merk");
        final String type = getIntent().getStringExtra("tv_type");
        final String prc = getIntent().getStringExtra(
                "tv_price");

        transactionID.setText(random);
        bookingDate.setText(date);
        carMerk.setText(merk);
        carType.setText(type);
        price.setText("$ " + prc);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = databaseReference.push().getKey();
                Transaction transaction = new Transaction(merk, type,
                        date, emailData, image, random, fullName, prc);

                databaseReference.child(UID).child(random).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ConfirmActivity.this, "Booking " +
                                    "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ConfirmActivity.this, "Transaction" +
                                            " Failed! " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                startActivity(new Intent(ConfirmActivity.this,
                        SuccessActivity.class));
                finish();
            }
        });
    }

}