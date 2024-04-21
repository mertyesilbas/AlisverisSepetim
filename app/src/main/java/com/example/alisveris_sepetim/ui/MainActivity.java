package com.example.alisveris_sepetim.ui;

import static com.example.alisveris_sepetim.R.id.not_taken;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.models.ShoppingItem;
import com.example.alisveris_sepetim.ui.fragments.NotTakenFragment;
import com.example.alisveris_sepetim.ui.fragments.TakenFragment;
import com.example.alisveris_sepetim.utils.FirestoreActions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private NotTakenFragment notTakenFragment;
    private TakenFragment takenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            takenFragment = new TakenFragment();
            notTakenFragment = new NotTakenFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainer, NotTakenFragment.class, null)
                    .commit();



            BottomNavigationView bottomNavigationView = findViewById(R.id.btmNavBar);
            bottomNavigationView.setSelectedItemId(not_taken);
            bottomNavigationView.setOnItemSelectedListener(menuItem -> {
                Fragment selectedFragment = notTakenFragment;
                switch (menuItem.getItemId()) {
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
            });
        }
    }

}