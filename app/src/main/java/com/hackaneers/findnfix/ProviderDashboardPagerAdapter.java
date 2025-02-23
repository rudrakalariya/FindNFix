package com.hackaneers.findnfix;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProviderDashboardPagerAdapter extends FragmentPagerAdapter {

    public ProviderDashboardPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new UpcomingRequestsFragment();  // Tab 1: Upcoming Requests
        } else {
            return new CompletedRequestsFragment(); // Tab 2: Completed Requests
        }
    }

    @Override
    public int getCount() {
        return 2; // Two tabs: Upcoming & Completed Requests
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Upcoming Requests";
        } else {
            return "Completed Requests";
        }
    }
}
