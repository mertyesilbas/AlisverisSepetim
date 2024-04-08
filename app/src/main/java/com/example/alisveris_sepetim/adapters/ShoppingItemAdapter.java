package com.example.alisveris_sepetim.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.utils.FirestoreHelper;

import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder> {

    private List<ShoppingItem> items;
    private OnItemClickListener listener;

    public ShoppingItemAdapter(List<ShoppingItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        return new ShoppingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position) {

        ShoppingItem item = items.get(position);
        holder.checkBox.setChecked(item.isChecked());
        if (item.isChecked()) {
            holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.itemName.setText(item.getName());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemCheckChange(item, holder.getAdapterPosition());
                if (item.isChecked()) {
                    holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemCheckChange(item, holder.getAdapterPosition());
                // Delete the item from Firestore
                FirestoreHelper firestoreHelper = new FirestoreHelper();
                firestoreHelper.deleteItem(item, new FirestoreHelper.DeleteItemCallback() {
                    @Override
                    public void onDeleteSuccess(String id) {
// Remove the item from the list
                        items.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        
                    }

                    @Override
                    public void onDeleteFailure(String message) {
                        // Show error message
                        //...
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ShoppingItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView itemName;
        ImageButton deleteButton;
        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemName = itemView.findViewById(R.id.itemName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnItemClickListener {
        void onItemCheckChange(ShoppingItem item, int position);
    }


    public void setItems(List<ShoppingItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }


}