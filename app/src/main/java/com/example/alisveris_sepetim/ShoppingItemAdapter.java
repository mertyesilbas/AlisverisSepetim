package com.example.alisveris_sepetim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.itemName.setText(item.getName());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemCheckChange(item, holder.getAdapterPosition());
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

        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemName = itemView.findViewById(R.id.itemName);
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