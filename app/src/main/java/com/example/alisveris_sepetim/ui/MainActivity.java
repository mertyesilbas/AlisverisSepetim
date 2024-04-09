package com.example.alisveris_sepetim.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.alisveris_sepetim.R;
import com.example.alisveris_sepetim.ui.fragments.NotTakenFragment;
import com.example.alisveris_sepetim.ui.fragments.TakenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private NotTakenFragment notTakenFragment;
    private TakenFragment takenFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

}