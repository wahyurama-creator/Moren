package com.wahyurama.moren;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivCarDetail, icBack;
    private TextView tvMerkDetail, tvTypeDetail,
            tvDescriptionDetail, tvPriceDetail, tvTransmission, tvSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        icBack = findViewById(R.id.ic_back);
        ivCarDetail = findViewById(R.id.iv_car_detail);
        tvMerkDetail = findViewById(R.id.tv_merk_detail);
        tvTypeDetail = findViewById(R.id.tv_type_detail);
        tvDescriptionDetail = findViewById(R.id.tv_desc_detail);
        tvPriceDetail = findViewById(R.id.tv_price_detail);
        tvTransmission = findViewById(R.id.tv_transmission_detail);
        tvSeats = findViewById(R.id.tv_seats_detail);


        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        MainActivity.class));
            }
        });

        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra(
                "ivCar"))
                .into(ivCarDetail);
        tvMerkDetail.setText(getIntent().getStringExtra("tvMerk"));
        tvTypeDetail.setText(getIntent().getStringExtra("tvType"));
        tvDescriptionDetail.setText(getIntent().getStringExtra("tvDesc"));
        tvTransmission.setText(getIntent().getStringExtra("tvTransmission"));
        tvSeats.setText(getIntent().getStringExtra("tvSeat"));
        tvPriceDetail.setText(getIntent().getStringExtra("tvPrice"));
    }
}