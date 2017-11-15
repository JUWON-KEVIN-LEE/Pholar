package com.hooooong.pholar.view.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hooooong.pholar.R;
import com.hooooong.pholar.util.BottomNavigationViewHelper;
import com.hooooong.pholar.view.gallery.GalleryActivity;
import com.hooooong.pholar.view.list.ListFragment;
import com.hooooong.pholar.view.mypage.MypageFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
    }
    FragmentTransaction transaction;
    private void initView() {
        navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new ListFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new ListFragment())
                            .commit();
                    return true;
                case R.id.navigation_search:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new SearchFragment())
                            .commit();
                    return true;
                case R.id.navigation_write:
                    Intent intent = new Intent(HomeActivity.this, GalleryActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notification:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new NotiFragment())
                            .commit();
                    return true;
                case R.id.navigation_mypage:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, MypageFragment.newInstance(fUser.getDisplayName(),fUser.getPhotoUrl().toString()))
                            .commit();
                    return true;
            }
            return false;
        }
    };

}
