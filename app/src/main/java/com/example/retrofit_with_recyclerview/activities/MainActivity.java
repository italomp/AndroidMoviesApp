package com.example.retrofit_with_recyclerview.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.fragments.SearchFragment;
import com.example.retrofit_with_recyclerview.fragments.StatisticsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private SearchFragment searchFragment;
    private StatisticsFragment statisticsFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentFragment = searchFragment = new SearchFragment();
        statisticsFragment = new StatisticsFragment();
        replaceFragment(currentFragment);

        this.setBottomNavigationView();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_home_icon:
                        item.setChecked(true);
                        currentFragment = searchFragment;
                        replaceFragment(currentFragment);
                        break;

                    case R.id.bottom_nav_chart_icon:
                        item.setChecked(true);
                        currentFragment = statisticsFragment;
                        replaceFragment(currentFragment);
                        break;
                }
                return false;
            }
        });
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putSerializable("currentFragment", (Serializable) currentFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            currentFragment = (Fragment) savedInstanceState.getSerializable("currentFragment");
            replaceFragment(currentFragment);
        }
    }
}