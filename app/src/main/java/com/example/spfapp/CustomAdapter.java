package com.example.spfapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {


    private Context context;
    private ArrayList ProductBatch, Details;
    CustomAdapter(Context context, ArrayList ProductBatch, ArrayList Details, OnNoteListener onNoteListener){

        this.context = context;
        this.ProductBatch = ProductBatch;
        this.Details = Details;

        this.mOnNoteListener = onNoteListener;
//        this.colour = colour;
//        this.align = align;

    }

    private OnNoteListener mOnNoteListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ProductBatch.setText(String.valueOf(ProductBatch.get(position)));
        holder.Details.setText(String.valueOf(Details.get(position)));
   }

    @Override
    public int getItemCount() {
        return ProductBatch.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView ProductBatch,Details;
        CardView cardView;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            ProductBatch = itemView.findViewById(R.id.ProductBatch);
            Details = itemView.findViewById(R.id.Details);
            cardView = itemView.findViewById(R.id.cv);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}