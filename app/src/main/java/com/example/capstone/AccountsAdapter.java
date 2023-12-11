package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    private static Context context;
    private OnItemClickListener mListener;

    private static List<Accounts> accounts;


    public AccountsAdapter(Context context, List<Accounts> accounts, FirebaseAuth auth,OnItemClickListener listener) {
        this.context = context;
        this.accounts = accounts;
        this.auth = auth;
        this.mListener = listener;
    }
    @NonNull
    @Override
    public AccountsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_different_users, parent, false);
        return new AccountsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsAdapter.ViewHolder holder, int position) {
        Accounts item = accounts.get(position);

        holder.email.setText(""+item.getEmail());
        holder.nicknames.setText(""+item.getName());

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView email;
        TextView nicknames;
        ImageView button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.violation_name_recycle);
            nicknames = itemView.findViewById(R.id.violation_nickname_recycle);
            button = itemView.findViewById(R.id.button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String clickedEmail = email.getText().toString();

                    currentUser = auth.getCurrentUser();
                    if (currentUser != null){
                        Toast.makeText(view.getContext(), "You are already Logged in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view.getContext(), Dashboard.class);
                        view.getContext().startActivity(intent);
                    }else{
                        Intent intent = new Intent(view.getContext(), Login.class);
                        intent.putExtra("emailFromRecycle", clickedEmail);
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String email);
    }

}
