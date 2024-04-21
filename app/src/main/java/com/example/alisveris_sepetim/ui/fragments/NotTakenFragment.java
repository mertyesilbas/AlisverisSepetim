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
import com.example.alisveris_sepetim.utils.FirestoreActions;

import java.util.List;
import java.util.stream.Collectors;

public class NotTakenFragment extends Fragment {
    private static final String TAG = "NotTakenFragment";
    private final FirestoreActions firestoreActions = new FirestoreActions();
    private NotTakenRecyclerAdapter notTakenRecyclerAdapter;
    private List<ShoppingItem> notTakenList;
    private RecyclerView notTakenRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_taken, container, false);
        notTakenRecyclerView = view.findViewById(R.id.notTakenRecyclerView);
        notTakenRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        // Get items from Firestore
        firestoreActions.fetchItems(shoppingItems -> {
            notTakenList = shoppingItems.stream()
                    .filter(item -> !item.isChecked())
                    .collect(Collectors.toList());
            Log.d(TAG, "Not taken items: " + notTakenList);
            notTakenRecyclerAdapter = new NotTakenRecyclerAdapter(notTakenList, getActivity());
            notTakenRecyclerView.setAdapter(notTakenRecyclerAdapter);
        });
        // Add item button
        view.findViewById(R.id.addItemButton).setOnClickListener(v -> {
            AddItemDialogFragment dialogFragment = AddItemDialogFragment.newInstance(new AddItemDialogFragment.AddItemListener() {

                @Override
                public void onItemAdded(boolean isSuccess) {
                    if (isSuccess) {
                        // Refresh the fragment data
                        firestoreActions.fetchItems(shoppingItems -> {
                            notTakenList = shoppingItems.stream()
                                    .filter(item -> !item.isChecked())
                                    .collect(Collectors.toList());
                            notTakenRecyclerAdapter = new NotTakenRecyclerAdapter(notTakenList, getActivity());
                            notTakenRecyclerView.setAdapter(notTakenRecyclerAdapter);
                            Log.d(TAG, "Not taken items: " + notTakenList);
                        });
                        Toast.makeText(getActivity(), "Alışveriş listesine eklendi.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Alışveriş listesine eklenemedi.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialogFragment.show(getChildFragmentManager(), "AddItemDialogFragment");
        });

        return view;
    }
}

