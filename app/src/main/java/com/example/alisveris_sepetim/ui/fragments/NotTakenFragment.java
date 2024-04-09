package com.example.alisveris_sepetim.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.adapters.NotTakenRecyclerAdapter;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.utils.FirestoreHelper;

import java.util.List;

public class NotTakenFragment extends Fragment {
    private static final String TAG = "NotTakenFragment";
    private RecyclerView notTakenRecyclerView;
    private List<ShoppingItem> notTakenList;
    private FirestoreHelper firestoreHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get item list
        firestoreHelper = new FirestoreHelper();
        firestoreHelper.getItems(new FirestoreHelper.GetItemsCallback() {
            @Override
            public void onGetSuccess(List<ShoppingItem> items) {
                notTakenList = items;
            }

            @Override
            public void onGetFailure(String error) {
                Log.w(TAG ,"Error getting items: " + error);
                // Handle error
            }
        });

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_taken, container, false);
        notTakenRecyclerView = view.findViewById(R.id.notTakenRecyclerView);
        notTakenRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notTakenRecyclerView.setAdapter(new NotTakenRecyclerAdapter( notTakenList));
        return view;
    }
}