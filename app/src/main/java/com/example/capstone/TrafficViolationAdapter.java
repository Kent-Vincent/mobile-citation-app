package com.example.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrafficViolationAdapter extends RecyclerView.Adapter<TrafficViolationAdapter.ViewHolder> {
    private static Context context;
    private static List<TrafficViolation> trafficViolations;

    public TrafficViolationAdapter(Context context, List<TrafficViolation> trafficViolations) {
        this.context = context;
        this.trafficViolations = trafficViolations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_traffic_violation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrafficViolation item = trafficViolations.get(position);

        holder.violation.setText("Violation: "+item.getViolation());
        holder.licenseNo.setText("License Number: "+item.getLicenseNumber());
        holder.name.setText("Name: "+item.getName());
        holder.totalPrice.setText("Total Price: "+item.getTotalPrice());

        /*Picasso.get()
                .load(item.getQrCodeImageUrl())
                .placeholder(R.drawable.upload)
                .into(holder.qr, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Image loaded successfully
                    }

                    @Override
                    public void onError(Exception e) {
                        //Handle errors here
                        e.printStackTrace();
                    }
               });
        */
    }

    @Override
    public int getItemCount() {
        return trafficViolations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, violation, totalPrice, licenseNo;

        public ViewHolder(View itemView) {
            super(itemView);
            violation = itemView.findViewById(R.id.violation_Recycle);
            licenseNo = itemView.findViewById(R.id.licenseNumberRecycle);
            name = itemView.findViewById(R.id.nameRecycle);
            totalPrice = itemView.findViewById(R.id.totalPrice_Recycle);
        }
    }
}