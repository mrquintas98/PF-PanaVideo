package com.example.javappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.javappandroid.databinding.ActivityDashboardBinding;

public class Dashboard extends DrawerBaseActivity {

    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = activityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
    }
}