package com.example.manan.enhancedurdureader.Adapters;

/**
 * Created by manan on 3/7/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.manan.enhancedurdureader.Fragments.BooksFragment;
import com.example.manan.enhancedurdureader.Fragments.MagazinesFragment;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    static ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(fragments.size()>0 &&fragments.get(0) != null)
                    return fragments.get(0);
                BooksFragment tab1 = new BooksFragment();
                fragments.add(tab1);
                return tab1;
            case 1:
                if(fragments.size()>1 &&fragments.get(1) != null)
                    return fragments.get(1);
                MagazinesFragment tab2 = new MagazinesFragment();
                fragments.add(tab2);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    public Fragment getMagazineFragment()
    {
        if(fragments.size()>1)
            return fragments.get(1);
        return null;
    }
}
