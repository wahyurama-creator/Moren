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
import com.wahyurama.moren.App.DetailTransactionActivity;
import com.wahyurama.moren.Model.Transaction;
import com.wahyurama.moren.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final ArrayList<Transaction> transactionList;
    public Context context;

    public TransactionAdapter(Context context,
                              ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_history, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int position) {
        Glide.with(context)
                .load(transactionList.get(position).getImage())
                .into(holder.ivCar);
        holder.carMerk.setText(transactionList.get(position).getCarMerk());
        holder.carType.setText(transactionList.get(position).getCarType());
        holder.transactionID.setText(transactionList.get(position).getTransactionID());
        holder.date.setText(transactionList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivCar;
        TextView carMerk, carType, transactionID, date;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCar = itemView.findViewById(R.id.iv_car_transaction);
            carMerk = itemView.findViewById(R.id.tv_merk_transaction);
            carType = itemView.findViewById(R.id.tv_type_transaction);
            transactionID = itemView.findViewById(R.id.tv_transactionID);
            date = itemView.findViewById(R.id.tv_date_transaction);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            context = view.getContext();

            // Start activity with Data to detail page
            Intent intent = new Intent(view.getContext(),
                    DetailTransactionActivity.class);

            intent.putExtra("tvMerk", transactionList.get(position).getCarMerk());
            intent.putExtra("tvType",
                    transactionList.get(position).getCarType());
            intent.putExtra("price", transactionList.get(position).getPrice());
            intent.putExtra("date", transactionList.get(position).getDate());
            intent.putExtra("email", transactionList.get(position).getEmail());
            intent.putExtra("username",
                    transactionList.get(position).getUsername());
            intent.putExtra("transactionID",
                    transactionList.get(position).getTransactionID());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
    }
}
