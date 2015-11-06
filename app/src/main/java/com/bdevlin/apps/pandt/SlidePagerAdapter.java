package com.bdevlin.apps.pandt;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bdevlin.apps.ui.fragments.BlankFragment;
import com.bdevlin.apps.ui.fragments.PagerFragment;

/**
 * Created by brian on 7/26/2014.
 */
public class SlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = Items.getCount();

  //  private static final String ARG_INDEX = " com.bdevlin.apps.pandt.arg_position";

    public SlidePagerAdapter(Resources res, FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

       // return new PagerFragment();
        BlankFragment frag = BlankFragment.newInstance("item1","itetm2");
        /*Bundle args = new Bundle();
        args.putInt(PagerFragment.ARG_INDEX, position);
        frag.setArguments(args);*/
       boolean visible =  frag.isVisible();
        return frag;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }


}