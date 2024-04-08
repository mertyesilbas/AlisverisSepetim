package com.example.alisveris_sepetim.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alisveris_sepetim.ui.fragments.NotTakenFragment;
import com.example.alisveris_sepetim.ui.fragments.TakenFragment;
import com.example.alisveris_sepetim.utils.FirestoreHelper;
import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.adapters.ShoppingItemAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView shoppingList;
    private ShoppingItemAdapter adapter;
    private List<ShoppingItem> items;
    private FirestoreHelper firestoreHelper;
    private BottomNavigationView bottomNavigationView;
    private NotTakenFragment notTakenFragment;
    private TakenFragment takenFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ;
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainer, NotTakenFragment.class, null)
                    .commit();

            takenFragment = new TakenFragment();
            notTakenFragment = new NotTakenFragment();

            bottomNavigationView = findViewById(R.id.btmNavBar);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.not_taken:
                            selectedFragment = notTakenFragment;
                            break;
                        case R.id.taken:
                            selectedFragment = takenFragment;
                            break;
                    }
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, selectedFragment);
                    transaction.commit();
                    return true;
                }
            });
        }







        /*shoppingList = findViewById(R.id.notBoughtRecyclerView);
        shoppingList.setLayoutManager(new LinearLayoutManager(this));
        items = new ArrayList<>();
        adapter = new ShoppingItemAdapter(items, new ShoppingItemAdapter.OnItemClickListener() {

            @Override
            public void onItemCheckChange(ShoppingItem item, int position) {
                Log.d("Item", "Item checked: " + item.getName()+ " " + item.isChecked());
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                firestoreHelper.updateItem(item);

            }

        });

        shoppingList.setAdapter(adapter);


        firestoreHelper = new FirestoreHelper();
        firestoreHelper.getItems(new FirestoreHelper.GetItemsCallback() {

            @Override

            public void onGetSuccess(List<ShoppingItem> items) {

                adapter.setItems(items);

            }


            @Override

            public void onGetFailure(String message) {

                Log.e("Error", "Error getting items: " + message);

            }


        });
        Button addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.  OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                addItemActivityLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher addItemActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        firestoreHelper.getItems(new FirestoreHelper.GetItemsCallback() {

                            @Override

                            public void onGetSuccess(List<ShoppingItem> items) {

                                adapter.setItems(items);

                            }


                            @Override

                            public void onGetFailure(String message) {

                                Log.e("Error", "Error getting items: " + message);

                            }


                        });
                    }
                }
            });*/
}

}