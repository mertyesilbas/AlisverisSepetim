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
import com.example.alisveris_sepetim.utils.FirestoreHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotTakenRecyclerAdapter extends RecyclerView.Adapter<NotTakenRecyclerAdapter.ViewHolder> {
    private static final String TAG = "NotTakenRecyclerAdapter";
    private List<ShoppingItem> notTakenList;
    private final FirestoreHelper firestoreHelper; // FirestoreHelper instance
    private final Context context;

    public NotTakenRecyclerAdapter(List<ShoppingItem> notTakenList, FirestoreHelper firestoreHelper, Context context) {

        this.notTakenList = notTakenList;// Filter items by checked state
        filterItems();
        this.firestoreHelper = firestoreHelper; // Inject FirestoreHelper
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

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingItem.setChecked(isChecked);
            shoppingItem.setUpdatedAt(new Date());
            firestoreHelper.updateShoppingItem(shoppingItem.getId(), shoppingItem)
                    .addOnSuccessListener(
                            v -> {
                                Log.d(TAG, "Shopping item updated: " + shoppingItem.getName());
                                filterItems();
                                notifyDataSetChanged();
                            }
                    );
            Toast.makeText(context , "Alındı olarak işaretlendi.", Toast.LENGTH_SHORT).show();
        });

        holder.deleteButton.setOnClickListener(v -> {
            firestoreHelper.deleteShoppingItem(shoppingItem.getId())
                    .addOnSuccessListener(aVoid -> {
                        notTakenList.remove(shoppingItem);
                        notifyDataSetChanged();
                    });
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

    // Add a method to filter notTakenList by checked state
    private void filterItems() {
        if (notTakenList == null) {
            Log.d(TAG, "Items list is empty");
            notTakenList = new ArrayList<>();
        } else {
            // Filter items by checked state
            notTakenList.removeIf(ShoppingItem::isChecked);
            Log.d(TAG, "Filtered items: " + notTakenList);
        }
    }
}
