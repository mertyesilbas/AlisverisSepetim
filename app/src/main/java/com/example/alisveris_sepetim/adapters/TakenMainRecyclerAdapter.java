package com.example.alisveris_sepetim.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.SectionItem;
import com.example.alisveris_sepetim.models.ShoppingItem;

import java.util.List;

public class TakenMainRecyclerAdapter extends RecyclerView.Adapter<TakenMainRecyclerAdapter.TakenMainViewHolder> {
    private static final String TAG = "TakenMainRecyclerAdapter";
    private final List<SectionItem> sectionList;
    private final Context context;

    public TakenMainRecyclerAdapter(List<SectionItem> sectionList, Context context) {
        this.sectionList = sectionList;
        this.context = context;
    }

    @NonNull
    @Override
    public TakenMainRecyclerAdapter.TakenMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.section_row, parent, false);
        return new TakenMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TakenMainRecyclerAdapter.TakenMainViewHolder holder, int position) {
        SectionItem sectionItem = sectionList.get(position);
        String sectionName = sectionItem.getSectionName();
        List<ShoppingItem> sectionItems = sectionItem.getSectionItems();
        Log.d(TAG, "Section name: " + sectionName);

        holder.sectionName.setText(sectionName);
        TakenChildRecyclerAdapter takenChildRecyclerAdapter = new TakenChildRecyclerAdapter(sectionItems, context);
        holder.takenChildRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.takenChildRecyclerView.setAdapter(takenChildRecyclerAdapter);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }


    public static class TakenMainViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        RecyclerView takenChildRecyclerView;

        public TakenMainViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionName);
            takenChildRecyclerView = itemView.findViewById(R.id.takenChildRecyclerView);
        }
    }
}
