package com.example.glacomplex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.glacomplex.R;
import com.example.glacomplex.databinding.ActivityMyOrdersBinding;
import com.example.glacomplex.utils.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

public class MyOrders extends AppCompatActivity {

    ActivityMyOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.app31_s7sharpreferences", Context.MODE_PRIVATE);
        ArrayList<String> orderList = new ArrayList<>();
        orderList.add("Your Orders");
        try {
            ArrayList<String> curr = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("orderList", ""));
            if(curr != null)
                orderList = curr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, orderList);
        binding.myOrdersList.setAdapter(arrayAdapter);
    }
}