package com.example.glacomplex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.glacomplex.databinding.ActivityMyOrdersBinding;

public class MyOrders extends AppCompatActivity {

    ActivityMyOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}