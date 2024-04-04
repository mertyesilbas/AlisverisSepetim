package com.example.alisveris_sepetim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addItemButton = findViewById(R.id.addButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        shoppingList = findViewById(R.id.shoppingList);

        shoppingList.setLayoutManager(new LinearLayoutManager(this));

        items = new ArrayList<>();

        adapter = new ShoppingItemAdapter(items, new ShoppingItemAdapter.OnItemClickListener() {

            @Override

            public void onItemCheckChange(ShoppingItem item, int position) {

                firestoreHelper.updateItem(item, !item.isChecked());

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
    }
}