package com.wahyurama.moren.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wahyurama.moren.Model.Book;
import com.wahyurama.moren.R;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final ArrayList<Book> bookedList;
    private final Context context;

    public BookAdapter(Context context, ArrayList<Book> bookedList) {
        this.bookedList = bookedList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_car_horizontal, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Glide.with(context)
                .load(bookedList.get(position).getImage())
                .into(holder.ivCar);
        holder.carMerk.setText(bookedList.get(position).getCarMerk());
        holder.carType.setText(bookedList.get(position).getCarType());
    }

    @Override
    public int getItemCount() {
        return bookedList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCar;
        TextView carMerk, carType, date;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCar = itemView.findViewById(R.id.iv_car_book);
            carMerk = itemView.findViewById(R.id.tv_merk_book);
            carType = itemView.findViewById(R.id.tv_type_book);
        }
    }
}
