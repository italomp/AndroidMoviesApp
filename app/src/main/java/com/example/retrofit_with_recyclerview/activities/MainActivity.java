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

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new SearchFragment());

        this.setBottomNavigationView();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println("entoru no onNavigationItemSelected");
                switch (item.getItemId()){
                    case R.id.menu_item_home:
                        System.out.println("entrou no R.id.fragment_search");
                        replaceFragment(new SearchFragment());
                        break;
                    case R.id.menu_item_statistics:
                        System.out.println("entrou no R.id.fragment_statistics");
                        replaceFragment(new StatisticsFragment());
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
}