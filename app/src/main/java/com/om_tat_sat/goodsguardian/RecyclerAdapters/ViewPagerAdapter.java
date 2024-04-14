package com.om_tat_sat.goodsguardian.RecyclerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.om_tat_sat.goodsguardian.Category_frag;
import com.om_tat_sat.goodsguardian.Expiring_frag;
import com.om_tat_sat.goodsguardian.MainActivity;
import com.om_tat_sat.goodsguardian.Expired_frag;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull MainActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:return new Category_frag();
            case 1:return new Expiring_frag();
            case 2:return new Expired_frag();
            default:return new Category_frag();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
