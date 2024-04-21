package com.example.alisveris_sepetim.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.adapters.TakenMainRecyclerAdapter;
import com.example.alisveris_sepetim.models.SectionItem;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.utils.FirestoreActions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TakenFragment extends Fragment {
    private static final String TAG = "TakenFragment";
    private RecyclerView takenMainRecyclerView;
    private TakenMainRecyclerAdapter takenMainRecyclerAdapter;

    private List<ShoppingItem> takenList;

    private List<SectionItem> sectionItems;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taken, container, false);
        takenList = new ArrayList<>();
        sectionItems = new ArrayList<SectionItem>();

        List<ShoppingItem> todayItems = new ArrayList<>();
        List<ShoppingItem> yesterdayItems = new ArrayList<>();
        List<ShoppingItem> otherItems = new ArrayList<>();
        List<SectionItem> sectionList = new ArrayList<>();
        FirestoreActions firestoreActions = new FirestoreActions();
        Log.d(TAG, "Fetching items from Firestore.");
        firestoreActions.fetchItems(shoppingItems -> {
            takenList = shoppingItems.stream()
                    .filter(ShoppingItem::isChecked)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            for (ShoppingItem item : takenList) {
                Date createdAt = item.getCreatedAt();
                Date today = new Date();
                if (isSameDay(createdAt, today)) {
                    todayItems.add(item);
                } else if (createdAt.after(subtractDays(today))) {
                    yesterdayItems.add(item);
                } else {
                    otherItems.add(item);
                }
            }
            sectionList.add(new SectionItem("Bugün", todayItems));
            sectionList.add(new SectionItem("Dün", yesterdayItems));
            sectionList.add(new SectionItem("Daha Eski", otherItems));
            Log.d(TAG, "Section list: " + sectionList);
            takenMainRecyclerView = view.findViewById(R.id.mainSectionRecyclerView);

            takenMainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            takenMainRecyclerAdapter = new TakenMainRecyclerAdapter(sectionList, getContext());
            takenMainRecyclerView.setAdapter(takenMainRecyclerAdapter);
//            takenMainRecyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    takenMainRecyclerAdapter.notifyDataSetChanged();
//                }
//            });
        });
        return view;
    }


    private Date subtractDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

}