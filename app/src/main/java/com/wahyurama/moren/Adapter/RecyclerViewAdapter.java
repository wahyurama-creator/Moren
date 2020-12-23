package com.wahyurama.moren.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wahyurama.moren.App.DetailActivity;
import com.wahyurama.moren.Model.Car;
import com.wahyurama.moren.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String Tag = "RecyclerView";
    private final ArrayList<Car> carList;
    public Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Car> carList) {
        this.context = context;
        this.carList = carList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_car_card, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        // Set data Image
        Glide.with(context)
                .load(carList.get(position).getImage())
                .into(holder.ivCar);

        holder.tvMerk.setText(carList.get(position).getMerk());
        holder.tvType.setText(carList.get(position).getType());
        holder.tvPrice.setText("$ " + carList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    // Method View Holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivCar;
        TextView tvMerk, tvType, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCar = itemView.findViewById(R.id.iv_card);
            tvMerk = itemView.findViewById(R.id.tv_merk);
            tvType = itemView.findViewById(R.id.tv_type);
            tvPrice = itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            context = view.getContext();

            // Start activity with Data to detail page
            Intent intent = new Intent(view.getContext(), DetailActivity.class);

            intent.putExtra("ivCar", carList.get(position).getImage());
            intent.putExtra("tvMerk", carList.get(position).getMerk());
            intent.putExtra("tvType", carList.get(position).getType());
            intent.putExtra("tvPrice", carList.get(position).getPrice());
            intent.putExtra("tvDesc", carList.get(position).getDesc());
            intent.putExtra("tvSeat", carList.get(position).getSeat());
            intent.putExtra("tvTransmission", carList.get(position).getTransmission());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
    }
}
