package com.example.alisveris_sepetim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView shoppingList;
    private ShoppingItemAdapter adapter;
    private List<ShoppingItem> items;
    private FirestoreHelper firestoreHelper;
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
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        shoppingList = findViewById(R.id.shoppingList);
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
        Button addItemButton = findViewById(R.id.addButton);
        addItemButton.setOnClickListener(new View.  OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                addItemActivityLauncher.launch(intent);
            }
        });
    }
}