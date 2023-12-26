package com.example.capstone;


import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViolationAndPricingAdapter extends RecyclerView.Adapter<ViolationAndPricingAdapter.ViewHolder> {

    private StringBuilder combinedViolation = new StringBuilder();
    private StringBuilder combinedPrice = new StringBuilder();

    private OnItemClickListener mListener;
    private Set<Integer> highlightedPositions = new HashSet<>();

    private static Context context;
    private static List<ViolationAndPricing> violationAndPricing;

    public ViolationAndPricingAdapter(Context context, List<ViolationAndPricing> violationAndPricing, OnItemClickListener listener) {
        this.context = context;
        this.violationAndPricing = violationAndPricing;
        this.mListener = listener;
    }
    @NonNull
    @Override
    public ViolationAndPricingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_violation_and_pricing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationAndPricingAdapter.ViewHolder holder, int position) {
        ViolationAndPricing item = violationAndPricing.get(position);

        holder.name.setText(""+item.getName());

        if (item.getName().length() > 29 && item.getName().length() <= 58) {
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        }
        else if (item.getName().length() > 59) {
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);
        }
        else {
            holder.name.setGravity(Gravity.CENTER);
        }
        holder.price.setText(""+item.getPrice());


        holder.violation = item.getName();
        holder.violation_price = item.getPrice();

        Picasso.get()
                .load(item.getIconForViolationUrl())
                .placeholder(R.drawable.upload)
                .into(holder.icon);

        toggleBackground(holder.cardView, isItemHighlighted(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && mListener != null) {
                    toggleBackground(holder.cardView, !isItemHighlighted(adapterPosition));

                    if (isItemHighlighted(adapterPosition)) {
                        highlightedPositions.remove(adapterPosition);
                    } else {
                        highlightedPositions.add(adapterPosition);
                    }
                    updateCombinedStrings();
                    mListener.onItemClick(adapterPosition, combinedViolation.toString(), combinedPrice.toString());
                }
            }
        });

    }

    private boolean isItemHighlighted(int position) {
        return highlightedPositions.contains(position);
    }

    @Override
    public int getItemCount() {
        return violationAndPricing.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String violation;
        String violation_price;
        TextView name, price;
        ImageView icon;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.violation_name);
            price = itemView.findViewById(R.id.violation_price);
            icon = itemView.findViewById(R.id.violation_icon);
            cardView = itemView.findViewById(R.id.cardViewViolations);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String name, String price);
    }

    private void toggleBackground(View view, boolean isHighlighted) {
        if (isHighlighted) {
            view.setBackgroundResource(R.drawable.highlight_cardview);
        } else {
            view.setBackgroundResource(android.R.color.white);
        }
    }

    private void updateCombinedStrings() {
        combinedViolation.setLength(0);
        combinedPrice.setLength(0);

        for (int position : highlightedPositions) {
            ViolationAndPricing item = violationAndPricing.get(position);

            if (!item.getName().equals("Please Specify")) {
                combinedViolation.append(item.getName()).append("\n");
                combinedPrice.append(item.getPrice()).append("\n");

            }
        }
        trimTrailingNewline(combinedViolation);
        trimTrailingNewline(combinedPrice);
    }

    private void trimTrailingNewline(StringBuilder stringBuilder) {
        int length = stringBuilder.length();
        while (length > 0 && stringBuilder.charAt(length - 1) == '\n') {
            length--;
        }
        stringBuilder.setLength(length);
    }
}