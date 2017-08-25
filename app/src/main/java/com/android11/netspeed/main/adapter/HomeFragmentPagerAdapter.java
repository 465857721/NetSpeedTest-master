package com.android11.netspeed.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list_fragment; // fragment列表
//	private List<String> list_Title;

	public HomeFragmentPagerAdapter(FragmentManager fm,
                                    List<Fragment> list_fragment) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.list_fragment = list_fragment;
//		this.list_Title = list_Title;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return list_fragment.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_fragment.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return "";
	}

}
