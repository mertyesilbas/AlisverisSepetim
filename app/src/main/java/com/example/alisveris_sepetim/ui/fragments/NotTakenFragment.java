package com.example.alisveris_sepetim.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.adapters.NotTakenRecyclerAdapter;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.utils.FirestoreHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotTakenFragment extends Fragment implements AddItemDialogFragment.DialogListener {
    private static final String TAG = "NotTakenFragment";
    private final FirestoreHelper firestoreHelper = new FirestoreHelper();
    private NotTakenRecyclerAdapter notTakenRecyclerAdapter;
    private List<ShoppingItem> notTakenList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_taken, container, false);
        notTakenList = new ArrayList<>();
        // Get items from Firestore
        fetchItems(view);
        // Add item button
        view.findViewById(R.id.addItemButton).setOnClickListener(v -> {
            AddItemDialogFragment addItemDialogFragment = new AddItemDialogFragment();
            addItemDialogFragment.setDialogListener(this);
            addItemDialogFragment.show(getParentFragmentManager(), AddItemDialogFragment.TAG);
        });

        return view;
    }

    @Override
    public void isSuccessful(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getActivity(), "Alacak başarıyla eklendi!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Alacak eklendi.");
            fetchItems(getView());
        } else {
            Log.e(TAG, "Alacak eklenemedi.");
            Toast.makeText(getActivity(), "Alacak eklenemedi.", Toast.LENGTH_SHORT).show();
        }
    }

    /*private List<ShoppingItem> filterItems(List<ShoppingItem> items) {
        if (items == null) {
            List<ShoppingItem> emptyList = new ArrayList<>();
            // Log
            Log.d(TAG, "Items list is empty");
            return emptyList;
        } else {
            // Filter items by checked state
            return items.stream()
                    .filter(item -> !item.isChecked())
                    .collect(Collectors.toList());
        }
    }*/

    // Create a reusable method for fetching items from Firestore
    private void fetchItems(View view) {
        firestoreHelper.getShoppingItems(new FirestoreHelper.OnGetShoppingItemsListener() {
            @Override
            public void onSuccess(List<ShoppingItem> items) {
                notTakenList = items;
                Log.d(TAG, "Items: " + items);
                for (ShoppingItem item : items) {
                    Log.d(TAG, "Item: " + item.getName());
                }
                /*// Filter items after data has been fetched
                notTakenList = filterItems(notTakenList);
                Log.d(TAG, "Filtered items: " + notTakenList);*/
                // Initialize adapter
                notTakenRecyclerAdapter = new NotTakenRecyclerAdapter(notTakenList, firestoreHelper,getActivity());
                RecyclerView notTakenRecyclerView = view.findViewById(R.id.notTakenRecyclerView);
                notTakenRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                notTakenRecyclerView.setAdapter(notTakenRecyclerAdapter);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error getting items: " + e.getMessage());
            }
        });
    }
}