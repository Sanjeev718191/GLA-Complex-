package com.example.glacomplex.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.glacomplex.adapters.CartAdapter;
import com.example.glacomplex.databinding.ActivityCartBinding;
import com.example.glacomplex.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    static CartAdapter adapter;
    static ArrayList<Product> products;
    static Cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        cart = TinyCartHelper.getCart();

        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);
            products.add(product);
        }

//        products.add(new Product("Product 1", "https://m.media-amazon.com/images/I/61L5QgPvgqL._SX679_.jpg", "12345", 23, 12, 223, 8));
//        products.add(new Product("Product 1", "https://m.media-amazon.com/images/I/61L5QgPvgqL._SX679_.jpg", "12345", 23, 12, 223, 8));
//        products.add(new Product("Product 1", "https://m.media-amazon.com/images/I/61L5QgPvgqL._SX679_.jpg", "12345", 23, 12, 223, 8));
//        products.add(new Product("Product 1", "https://m.media-amazon.com/images/I/61L5QgPvgqL._SX679_.jpg", "12345", 23, 12, 223, 8));

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.totalPrice.setText(String.format("Rs. %.2f", cart.getTotalPrice()));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        binding.totalPrice.setText(String.format("Rs. %.2f", cart.getTotalPrice()));

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
//    public void emptyCart(){
//        cart.clearCart();
//        updateCart();
//    }
//    public void updateCart(){
//
//    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}