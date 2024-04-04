package com.example.alisveris_sepetim;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {
    private static final String TAG = "FirestoreHelper";
    private final FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void addItem(ShoppingItem item, final AddItemCallback callback) {
        db.collection("items")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        callback.onAddSuccess(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        callback.onAddFailure(e.getMessage());
                    }
                });
    }

    public void updateItem(ShoppingItem item) {
        DocumentReference itemRef = db.collection("items").document(item.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("checked", item.isChecked());

        itemRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void deleteItem(ShoppingItem item, final DeleteItemCallback callback) {
        db.collection("items").document(item.getId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        callback.onDeleteSuccess(item.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        callback.onDeleteFailure(e.getMessage());
                    }
                });
    }

    public void getItems(final GetItemsCallback callback) {
        db.collection("items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ShoppingItem> items = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShoppingItem item = document.toObject(ShoppingItem.class);
                                item.setId(document.getId());
                                items.add(item);
                            }
                            callback.onGetSuccess(items);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            callback.onGetFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    public interface AddItemCallback {
        void onAddSuccess(String id);

        void onAddFailure(String message);
    }

    public interface DeleteItemCallback {
        void onDeleteSuccess(String id);

        void onDeleteFailure(String message);
    }

    public interface GetItemsCallback {
        void onGetSuccess(List<ShoppingItem> items);

        void onGetFailure(String message);
    }
}