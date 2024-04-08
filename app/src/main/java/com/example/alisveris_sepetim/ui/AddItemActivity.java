package com.example.alisveris_sepetim.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alisveris_sepetim.utils.FirestoreHelper;
import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.ShoppingItem;

import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {
    private Button addButton;
    private EditText itemNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize views
        addButton = findViewById(R.id.addButton);
        itemNameEditText = findViewById(R.id.itemNameEditText);

        // Set click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get item name from edit text
                String itemName = itemNameEditText.getText().toString();

                // Create new shopping item
                ShoppingItem item = new ShoppingItem();
                String randomId = UUID.randomUUID().toString();
                item.setId(randomId);
                item.setName(itemName);
                item.setChecked(false);

                // Add new shopping item to Firestore
                FirestoreHelper firestoreHelper = new FirestoreHelper();
                firestoreHelper.addItem(item, new FirestoreHelper.AddItemCallback() {

                    @Override
                    public void onAddSuccess(String id) {
                        // Finish the activity
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onAddFailure(String message) {
                        // Show error message
                        //...
                    }
                });
            }
        });
    }
}