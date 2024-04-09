package com.example.alisveris_sepetim.adapters;

import android.util.Log;
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

public class NotTakenRecyclerAdapter extends RecyclerView.Adapter<NotTakenRecyclerAdapter.ViewHolder> {
    private static final String TAG = "NotTakenRecyclerAdapter";
    private final List<ShoppingItem> notTakenList;
    private final FirestoreHelper firestoreHelper = new FirestoreHelper();

    public NotTakenRecyclerAdapter(List<ShoppingItem> notTakenList) {
        this.notTakenList = notTakenList;
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
        holder.itemName.setText(shoppingItem.getName());
        holder.checkBox.setChecked(shoppingItem.isChecked());
        holder.deleteButton.setOnClickListener(v -> firestoreHelper.deleteItem(shoppingItem,
                new FirestoreHelper.DeleteItemCallback() {
                    @Override
                    public void onDeleteSuccess(String id) {
                        notTakenList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }

                    @Override
                    public void onDeleteFailure(String error) {
// Handle error
                        Log.w(TAG, "Error deleting item: " + error);
                    }
                }));
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

}
