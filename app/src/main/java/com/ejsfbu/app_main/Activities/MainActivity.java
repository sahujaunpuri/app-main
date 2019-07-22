package com.ejsfbu.app_main.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ejsfbu.app_main.Fragments.BadgesFragment;
import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.Fragments.NeedsParentDialogFragment;
import com.ejsfbu.app_main.Fragments.ProfileFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        setNavigationClick();

        User user = (User) ParseUser.getCurrentUser();
        if (user.getNeedsParent()) {
            showConnectParentDialog();
        }
    }

    private void setNavigationClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.miRewards:
                    fragment = new BadgesFragment();
                    break;
                case R.id.miGoals:
                    fragment = new GoalsListFragment();
                    break;
                case R.id.miProfile:
                default:
                    fragment = new ProfileFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.miGoals);
    }

    private void showConnectParentDialog() {
        NeedsParentDialogFragment needsParentDialogFragment = NeedsParentDialogFragment.newInstance("Needs Parent");
        needsParentDialogFragment.show(MainActivity.fragmentManager, "fragment_needs_parent");
    }

}
