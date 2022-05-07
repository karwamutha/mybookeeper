package com.example.mybookkeeper;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    //private SqliteDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Add comment Example 1
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("HOME PAGE");
        toolbar.setSubtitle("My Book_keeper");
        toolbar.setLogo(android.R.drawable.ic_btn_speak_now);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }
}