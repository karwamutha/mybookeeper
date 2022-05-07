package com.example.mybookkeeper.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mybookkeeper.R;

public class HomeActivity extends AppCompatActivity{

    private String dataReceived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.nav_host_fragment_container, new HomeFragment());
        fragmentTransaction.commit();
    }

}