package com.example.alisveris_sepetim.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.utils.FirestoreActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotTakenRecyclerAdapter extends RecyclerView.Adapter<NotTakenRecyclerAdapter.ViewHolder> {
    private static final String TAG = "NotTakenRecyclerAdapter";
    private List<ShoppingItem> notTakenList;
    private final Context context;

    private final FirestoreActions firestoreActions = new FirestoreActions();

    public NotTakenRecyclerAdapter(List<ShoppingItem> notTakenList, Context context) {
this.notTakenList = notTakenList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotTakenRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotTakenRecyclerAdapter.ViewHolder holder, int position) {
        ShoppingItem shoppingItem = notTakenList.get(position);
        Log.d(TAG, "Shopping item: " + shoppingItem.getName());
        holder.itemName.setText(shoppingItem.getName());
        holder.checkBox.setChecked(shoppingItem.isChecked()); // Set initial checked state

        // Set the checked state of the item
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingItem.setChecked(isChecked);
            shoppingItem.setUpdatedAt(new Date());
            // Update the item in Firestore
            firestoreActions.updateItem(shoppingItem);

            Toast.makeText(context, "Alacak olarak işaretlendi.", Toast.LENGTH_SHORT).show();
            removeItem(position);
        });

        // Delete the item
        holder.deleteButton.setOnClickListener(v -> {
            firestoreActions.deleteItem(shoppingItem);

            Toast.makeText(context, "Ürün silindi", Toast.LENGTH_SHORT).show();

            removeItem(position);
        });
    }

    @Override
    public int getItemCount() {
        if (notTakenList == null) {
            return 0;
        } else {
            return notTakenList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView itemName;
        public ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemName = itemView.findViewById(R.id.itemName);
            deleteButton = itemView.findViewById(R.id.deleteItemButton);
        }
    }

    public void removeItem(int position) {
        if (notTakenList.size() > position) {
            notTakenList.remove(position);
            notifyItemRemoved(position); // Notify adapter about the removal
        } else {
            notTakenList.clear();
            notifyDataSetChanged();
        }
    }
}
