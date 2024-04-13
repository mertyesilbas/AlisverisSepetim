package com.example.alisveris_sepetim.utils;

import android.util.Log;

import com.example.alisveris_sepetim.models.ShoppingItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import java.util.ArrayList;
import java.util.List;

public class FirestoreHelper {
    private static final String TAG = "FirestoreHelper";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference shoppingItemsRef = db.collection("shoppingItems");

    // Create a shopping item
    public Task<DocumentReference> createShoppingItem(ShoppingItem item) {
        return shoppingItemsRef.add(item);
    }

    // Update a shopping item
    public Task<Void> updateShoppingItem(String id, ShoppingItem item) {
        return shoppingItemsRef.document(id).set(item, SetOptions.merge());
    }

    // Delete a shopping item
    public Task<Void> deleteShoppingItem(String id) {
        return shoppingItemsRef.document(id).delete();
    }

    // Get a shopping item by id
    public Task<DocumentSnapshot> getShoppingItem(String id) {
        return shoppingItemsRef.document(id).get();
    }

    public interface OnGetShoppingItemsListener {
        void onSuccess(List<ShoppingItem> items);
        void onError(Exception e);
    }

    public void getShoppingItems(OnGetShoppingItemsListener listener) {
        shoppingItemsRef.orderBy("createdAt").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ShoppingItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ShoppingItem item = doc.toObject(ShoppingItem.class);
                        item.setId(doc.getId());
                        items.add(item);
                        Log.d(TAG, "Item: " + item.getName());
                    }
                    listener.onSuccess(items);
                })
                .addOnFailureListener(listener::onError);
    }

}