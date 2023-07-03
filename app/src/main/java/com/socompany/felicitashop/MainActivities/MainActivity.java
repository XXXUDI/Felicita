package com.socompany.felicitashop.MainActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socompany.felicitashop.Auth.LoginActivity;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView userNameProfile;
    private TextView userEmailProfile;
    private DrawerLayout drawerLayout;

    CircleImageView userImageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors

        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);

        userNameProfile = header.findViewById(R.id.userNameProfile);
        userEmailProfile = header.findViewById(R.id.userEmailProfile);
        userImageProfile = header.findViewById(R.id.userImageProfile);


        displayUserData();

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if(item.getItemId() == R.id.nav_basket){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BasketFragment()).commit();
        } else if(item.getItemId() == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
        } else if(item.getItemId() == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        } else if(item.getItemId() == R.id.nav_logout) {
            Paper.book().destroy();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // do nothing
        }
    }

    private void displayUserData() {
        String userEmail = Paper.book().read(Prevalent.userEmailKey);
        if(userEmail != null) {
            String userName = Paper.book().read(Prevalent.userNameKey);
            if(userName != null) {
                userNameProfile.setText(userName);
                userEmailProfile.setText(userEmail);
                try {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(Paper.book().read(Prevalent.userPhoneKey));
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                if(snapshot.child("Image").exists()) {
                                    String image = snapshot.child("Image").getValue().toString();
                                    Picasso.get().load(image).into(userImageProfile);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }catch (Exception e) {
                    // do nothing
                }
            }
        }
    }
}