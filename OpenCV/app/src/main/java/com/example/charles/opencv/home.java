package com.example.charles.opencv;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;
import android.view.View;




public class home extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // Switch to camera activity upon click of button
        /*Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v){
                startActivity(new Intent(home.this, MainActivity.class));
            }
        });*/ // currently removed

        // load the home fragment
        loadFragment(new HomeFragment());
    }

    // Loads the desired fragment into the fragment container
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Declare the fragment
        Fragment fragment = null;

        // Find out which button was pressed and load the desired fragment
        switch(item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new SearchFragment();
                break;

            case R.id.navigation_birdBank:
                fragment = new BirdBankFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
