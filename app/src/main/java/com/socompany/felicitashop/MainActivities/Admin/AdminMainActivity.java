package com.socompany.felicitashop.MainActivities.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.socompany.felicitashop.Auth.LoginActivity;
import com.socompany.felicitashop.MainActivities.AboutUsFragment;
import com.socompany.felicitashop.MainActivities.Admin.Fragments.AdminAddFragment;
import com.socompany.felicitashop.MainActivities.Admin.Fragments.AdminDeleteFragment;
import com.socompany.felicitashop.MainActivities.Admin.Fragments.AdminHomeFragment;
import com.socompany.felicitashop.MainActivities.Admin.Fragments.AdminUsersFragment;
import com.socompany.felicitashop.MainActivities.BasketFragment;
import com.socompany.felicitashop.MainActivities.HomeFragment;
import com.socompany.felicitashop.MainActivities.MainActivity;
import com.socompany.felicitashop.MainActivities.SettingsFragment;
import com.socompany.felicitashop.R;

public class AdminMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        replaceFragment(new AdminAddFragment());
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.bot_home) {
                replaceFragment(new AdminHomeFragment());
            } else if(item.getItemId() == R.id.bot_add){
                replaceFragment(new AdminAddFragment());
            } else if(item.getItemId() == R.id.bot_delete) {
                replaceFragment(new AdminDeleteFragment());
            } else if(item.getItemId() == R.id.bot_users) {
                replaceFragment(new AdminUsersFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}