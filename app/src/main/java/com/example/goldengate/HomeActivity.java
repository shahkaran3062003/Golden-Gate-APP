package com.example.goldengate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView profileImg, messageBtn, nav_img, nav_close_img;
    NavigationView mNavigationView;

    TextView tt, nav_name;
    BottomNavigationView mBottomNavigationView;

    Fragment selectedFragment = null;
    AppSharedPreferences appSharedPreferences;

    UserModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appSharedPreferences = new AppSharedPreferences(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        profileImg = findViewById(R.id.img);

        mNavigationView = findViewById(R.id.nav_view);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_home){
                    selectedFragment = new HomeFragment();
                }
                else if(id == R.id.nav_uplod){
                    selectedFragment = null;
                    startActivity(new Intent(HomeActivity.this, SharePostActivity.class));
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
                else if(id==R.id.nav_network){
                    selectedFragment = new NetworkFragment();
                }


                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
                }
                return true;
            }
        });


        //UniversalImageLoaderClass
        UniversalImageLoderClass universalImageLoderClass = new UniversalImageLoderClass(this);
        ImageLoader.getInstance().init(universalImageLoderClass.getConfig());


        // Header
        View header = mNavigationView.getHeaderView(0);
        nav_name = header.findViewById(R.id.user_name);
        nav_img = header.findViewById(R.id.img);
        nav_close_img = header.findViewById(R.id.close_img);
        tt = header.findViewById(R.id.tt);


        //Open Profile Activity
        tt.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        // Set Header Data
//        Glide.with(this).load(appSharedPreferences.getImgUrl()).into(profileImg);
//        Glide.with(this).load(appSharedPreferences.getImgUrl()).into(nav_img);
        nav_name.setText(appSharedPreferences.getUserName());


        //NavBar Close
        nav_close_img.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Open Drawer Layout
        profileImg.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.openDrawer(Gravity.LEFT);
            else drawerLayout.closeDrawer(Gravity.RIGHT);
        });


    }

    @Override
    public void onBackPressed() {
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        if (mBottomNavigationView.getSelectedItemId() == R.id.nav_home) {
            super.onBackPressed();
            finish();
        } else {
            mBottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }



    private final BottomNavigationView.OnNavigationItemSelectedListener navigationSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int id = item.getItemId();

            if(id == R.id.nav_home){
                selectedFragment = new HomeFragment();
            }
            else if(id == R.id.nav_uplod){
                selectedFragment = null;
                startActivity(new Intent(HomeActivity.this, SharePostActivity.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
            else if(id==R.id.nav_network){
                selectedFragment = new NetworkFragment();
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
            }
            return true;
        }
    };


}