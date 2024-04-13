package com.example.alisveris_sepetim.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.utils.FirestoreHelper;

import java.util.Date;

public class AddItemDialogFragment extends DialogFragment {
    public static String TAG = "AddItemDialogFragment";
    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Alacak Ekle")
                .setView(R.layout.fragment_add_item_dialog)
                .setPositiveButton("Ekle", (dialog, which) -> {
                    // Add item
                    EditText itemNameEditText = ((AlertDialog) dialog).findViewById(R.id.addItemEditText);
                    if (itemNameEditText != null) {
                        String itemName = itemNameEditText.getText().toString();
                        if (!itemName.isEmpty()) {
                            addItem(itemName);
                        }
                    }
                })
                .setNegativeButton("İptal Et", (dialog, which) -> {
                    // Cancel
                    AddItemDialogFragment.this.onCancel(dialog);
                })
                .create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    private void addItem(String name) {
        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setChecked(false);
        item.setCreatedAt(new Date());
        item.setUpdatedAt(new Date());
        // Add item
        FirestoreHelper firestoreHelper = new FirestoreHelper();
        firestoreHelper.createShoppingItem(item)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Item added with ID: " + documentReference.getId());
                    listener.isSuccessful(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding item: " + e.getMessage());
                    listener.isSuccessful(false);
                });

    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }
    public interface DialogListener {
        void isSuccessful(boolean isSuccess);
    }

}