 package com.example.cafateriaclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cafateriaclientapp.Fragments.AccountFragment;
import com.example.cafateriaclientapp.Fragments.HomeFragment;
import com.example.cafateriaclientapp.Fragments.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

 public class MainActivity extends AppCompatActivity implements AccountFragment.Callbacks {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.main_btm_navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        if(selectedFragment==null){
            selectedFragment=new HomeFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment,selectedFragment)
                .commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id=menuItem.getItemId();

            switch (id){
                case R.id.btm_nav_home:
                    selectedFragment=new HomeFragment();
                    break;
                case R.id.btm_nav_orders:
                    selectedFragment=new OrdersFragment();
                    break;
                case R.id.btm_nav_account:
                        selectedFragment=new AccountFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment,selectedFragment)
                    .commit();
            return true;
        }
    };

     @Override
     public void onButtonClicked() {
         getSupportFragmentManager().beginTransaction()
                 .replace(R.id.main_fragment, new HomeFragment())
                 .commit();
         bottomNavigationView.setSelectedItemId(R.id.btm_nav_home);
     }
 }
