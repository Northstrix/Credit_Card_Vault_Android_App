package com.example.unlock;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.unlock.fragments.Add;
import com.example.unlock.fragments.Delete;
import com.example.unlock.fragments.About;
import com.example.unlock.fragments.View_tab;


public class MyViewPagerAdapter  extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Add();
            case 1:
                return new Delete();
            case 3:
                return new About();
            default:
                return new View_tab();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
