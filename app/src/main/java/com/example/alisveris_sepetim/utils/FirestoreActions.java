package com.example.alisveris_sepetim.utils;

import android.util.Log;

import com.example.alisveris_sepetim.models.ShoppingItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirestoreActions {
    private static final String TAG = "FirestoreActions";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference itemsRef = db.collection("shoppingItems");

    public void fetchItems(FirestoreCallback callback) {
        itemsRef.orderBy("updatedAt").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<ShoppingItem> items = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                ShoppingItem item = document.toObject(ShoppingItem.class);
                if (item != null) {
                    Log.d(TAG, "Item: " + item);
                }
                items.add(item);
            }
            Log.d(TAG, "Items: " + items);
            callback.onFirestoreDataFetched(items);
        }).addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching items: " + e.getMessage());
                    callback.onFirestoreDataFetched(new ArrayList<>());
                }
        );
    }

    // Update item
    public void updateItem(ShoppingItem item) {
        itemsRef.document(item.getId()).set(item)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Item updated: " + item))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating item: " + e.getMessage()));
    }

    // Add item
    public void addItem(ShoppingItem item, DataAddedCallback callback) {
        itemsRef.add(item)
                .addOnSuccessListener(documentReference -> {
                    item.setId(documentReference.getId());
                    updateItem(item);
                    Log.d(TAG, "Item added: " + item);
callback.onFirestoreDataAdded(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding item: " + e.getMessage());
callback.onFirestoreDataAdded(false);
               });
    }

    // Delete item
    public void deleteItem(ShoppingItem item) {
        itemsRef.document(item.getId()).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Item deleted: " + item))
                .addOnFailureListener(e -> Log.e(TAG, "Error deleting item: " + e.getMessage()));
    }
    public interface FirestoreCallback {
        void onFirestoreDataFetched(List<ShoppingItem> shoppingItems);
    }

    public interface DataAddedCallback {
        void onFirestoreDataAdded(boolean isAdded);
    }

}
