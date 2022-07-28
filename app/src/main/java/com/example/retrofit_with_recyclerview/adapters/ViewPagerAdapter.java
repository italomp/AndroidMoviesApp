package com.example.retrofit_with_recyclerview.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.retrofit_with_recyclerview.fragments.SearchFragment;
import com.example.retrofit_with_recyclerview.fragments.StatisticsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * @param position é a posição do fragmento (aba do TabLayout)
     * @return o conteúdo de cada fragmento.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SearchFragment();
            case 1:
                return new StatisticsFragment();
            default:
                return new SearchFragment();
        }
    }

    /**
     * @return a quantidade de itens/abas que serão exibidas no TabLayout.
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}
