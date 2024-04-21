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

public class TakenChildRecyclerAdapter extends RecyclerView.Adapter<TakenChildRecyclerAdapter.TakenChildViewHolder> {

    private static final String TAG = "TakenChildRecyclerAdapter";
    private List<ShoppingItem> shoppingItems;
    private final Context context;
    private final FirestoreActions firestoreActions = new FirestoreActions();
    public TakenChildRecyclerAdapter(List<ShoppingItem> shoppingItems, Context context) {
        this.shoppingItems = shoppingItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TakenChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_item, parent, false);
        return new TakenChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TakenChildViewHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingItems.get(position);
        Log.d(TAG, "Shopping item: " + shoppingItem.getName());
        holder.itemName.setText(shoppingItem.getName());
        holder.checkBox.setChecked(shoppingItem.isChecked()); // Set initial checked state

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingItem.setChecked(isChecked);
            shoppingItem.setUpdatedAt(new Date());
            // update the item in Firestore
            firestoreActions.updateItem(shoppingItem);

            Toast.makeText(context , "Alacak olarak işaretlendi.", Toast.LENGTH_SHORT).show();
            removeItem(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            firestoreActions.deleteItem(shoppingItem);

            Toast.makeText(context, "Ürün silindi", Toast.LENGTH_SHORT).show();

            removeItem(position);
        });

    }

    @Override
    public int getItemCount() {
        if (shoppingItems == null) {
            return 0;
        } else {
            return shoppingItems.size();
        }
    }

    public static class TakenChildViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;
        public TextView itemName;
        public ImageButton deleteButton;
        public TakenChildViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemName = itemView.findViewById(R.id.itemName);
            deleteButton = itemView.findViewById(R.id.deleteItemButton);
        }
    }

    public void removeItem(int position) {
        if (shoppingItems.size() > position) {
            shoppingItems.remove(position);
            notifyItemRemoved(position); // Notify adapter about the removal
        } else {
            shoppingItems.clear();
            notifyDataSetChanged();
        }
    }


}
