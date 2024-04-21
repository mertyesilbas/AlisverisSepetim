package com.example.alisveris_sepetim.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.utils.FirestoreActions;

import java.util.Date;

public class AddItemDialogFragment extends DialogFragment {
    public static String TAG = "AddItemDialogFragment";

    private final FirestoreActions firestoreActions = new FirestoreActions();
    private AddItemListener addItemListener;

    public static AddItemDialogFragment newInstance(AddItemListener listener) {
        AddItemDialogFragment fragment = new AddItemDialogFragment();
        fragment.setAddItemListener(listener);
        return fragment;
    }

    public void setAddItemListener(AddItemListener listener) {
        this.addItemListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Alacak Ekle")
                .setView(R.layout.fragment_add_item_dialog)
                .setPositiveButton("Ekle", (dialog, which) -> {
                    // Add item
                    EditText itemNameEditText = ((AlertDialog) dialog).findViewById(R.id.addItemEditText);
                    if (itemNameEditText!= null) {
                        String itemName = itemNameEditText.getText().toString();
                        if (!itemName.isEmpty()) {
                            ShoppingItem item = new ShoppingItem();
                            item.setName(itemName);
                            item.setChecked(false);
                            item.setCreatedAt(new Date());
                            item.setUpdatedAt(new Date());

                            firestoreActions.addItem(item, isAdded -> {
                                addItemListener.onItemAdded(isAdded);
                            });
                        }
                    }
                })
                .setNegativeButton("İptal Et", (dialog, which) -> {
                    // Cancel
                    addItemListener.onItemAdded(false);
                })
                .create();
    }

    public interface AddItemListener {
        void onItemAdded(boolean isSuccess);
    }
}