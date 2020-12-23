package com.wahyurama.moren.App;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wahyurama.moren.R;

import java.text.DateFormat;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    String currentDate;
    private ImageView ivCarDetail, icBack;
    private TextView tvMerkDetail, tvTypeDetail,
            tvDescriptionDetail, tvPriceDetail, tvTransmission, tvSeats;
    private MaterialButton selectDate;
    private FloatingActionButton btnCheckout;

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
        selectDate = findViewById(R.id.btn_select_date);
        btnCheckout = findViewById(R.id.btn_checkout);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(DetailActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker,
                                                          int year, int month,
                                                          int dayOfMonth) {

                                        calendar.set(Calendar.YEAR, year);
                                        calendar.set(Calendar.MONTH, month);
                                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        currentDate =
                                                DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                                        selectDate.setVisibility(View.INVISIBLE);
                                        btnCheckout.setVisibility(View.VISIBLE);
                                    }
                                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this,
                        ConfirmActivity.class);

                intent.putExtra("iv_car", getIntent().getStringExtra(
                        "ivCar"));
                intent.putExtra("date", currentDate);
                intent.putExtra("tv_merk", getIntent().getStringExtra("tvMerk"));
                intent.putExtra("tv_type", getIntent().getStringExtra("tvType"));
                intent.putExtra("tv_price", getIntent().getStringExtra(
                        "tvPrice"));
                startActivity(intent);
            }
        });
    }

}