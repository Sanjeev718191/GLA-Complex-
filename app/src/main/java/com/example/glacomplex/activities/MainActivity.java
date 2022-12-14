package com.example.glacomplex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.glacomplex.R;
import com.example.glacomplex.adapters.CategoryAdapter;
import com.example.glacomplex.adapters.ProductAdapter;
import com.example.glacomplex.databinding.ActivityMainBinding;
import com.example.glacomplex.model.Category;
import com.example.glacomplex.model.Product;
import com.example.glacomplex.utils.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    int registeredUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = this.getSharedPreferences("com.example.app31_s7sharpreferences", Context.MODE_PRIVATE);
        registeredUser = sharedPreferences.getInt("registeredUser", 0);

        //goto Login page
        if(registeredUser == 0) {

            Intent intent = new Intent(this, SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        //start components of main activity
        else {
            initSearchBar();
            initCategories();
            initProducts();
            initSlider();
            initDrawer();
        }
    }

    private void initDrawer(){
        navigationView = findViewById(R.id.navigation_menu);
        drawerLayout = findViewById(R.id.drawerLayout);
        View headerView = navigationView.getHeaderView(0);
        TextView userName = (TextView) headerView.findViewById(R.id.userNameTextView);
        userName.setText(sharedPreferences.getString("userName", "GuestUser"));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_home:
                        drawerLayout.closeDrawers();
                        break;
                    case  R.id.cartBtn: {
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                    }
                    break;
                    case  R.id.myOrders: {
                        startActivity(new Intent(MainActivity.this, MyOrders.class));
                    }
                    break;
                    case  R.id.signIn: {
                        if(registeredUser == 0)
                            startActivity(new Intent(MainActivity.this, SignIn.class));
                        else
                            Toast.makeText(MainActivity.this, sharedPreferences.getString("userName", "GuestUser") + " is Registered", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case  R.id.nav_share:{

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody =  "http://play.google.com/store/apps/detail?id=" + getPackageName();
                        String shareSub = "Try now";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));

                    }
                    break;
                }
                return false;
            }
        });
    }

    private void initSearchBar(){
        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void initSlider() {
//        binding.carousel.addData(new CarouselItem("https://feeds.abplive.com/onecms/images/uploaded-images/2022/09/22/555838c62ba9be4994a23797004986541663833308234402_original.jpg", "Amazon Caption Here"));
//        binding.carousel.addData(new CarouselItem("https://images.hindustantimes.com/tech/img/2022/09/05/960x540/Flipkart_sale_1662379551619_1662379562805_1662379562805.PNG", "Flipkart Caption Here"));

        getRecentOffers();
    }

    void initCategories(){
        categories = new ArrayList<>();
//        categories.add(new Category("Food and Fun", "https://cdn-icons-png.flaticon.com/512/851/851554.png", "#18ab4e", "Some Description", 1));
//        categories.add(new Category("Food and Fun", "https://cdn-icons-png.flaticon.com/512/851/851554.png", "#18ab4e", "Some Description", 1));
//        categories.add(new Category("Food and Fun", "https://cdn-icons-png.flaticon.com/512/851/851554.png", "#18ab4e", "Some Description", 1));
//        categories.add(new Category("Food and Fun", "https://cdn-icons-png.flaticon.com/512/851/851554.png", "#18ab4e", "Some Description", 1));
//        categories.add(new Category("Food and Fun", "https://cdn-icons-png.flaticon.com/512/851/851554.png", "#18ab4e", "Some Description", 1));
//        categories.add(new Category("Food and Fun", "https://cdn-icons-png.flaticon.com/512/851/851554.png", "#18ab4e", "Some Description", 1));
        categoryAdapter = new CategoryAdapter(this, categories);

        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void getCategories(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        for(int i = 0; i<categoriesArray.length(); i++){
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                        System.out.println(categories.size());
                        for(int i = 0; i<categories.size(); i++){
                            System.out.println(categories.get(i).getName()+" "+categories.get(i).getIcon()+" "+categories.get(i).getBrief());
                        }
                    } else{
                        //do nothing
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    void getRecentProducts(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL + "?count=8";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("products");
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        Product product = new Product(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")
                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        queue.add(request);
    }

    void getRecentOffers(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for (int i = 0; i < offerArray.length(); i++) {
                        JSONObject childObj = offerArray.getJSONObject(i);
                        binding.carousel.addData(
                                new CarouselItem(
                                        Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                        childObj.getString("title")
                                )
                        );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        queue.add(request);
    }

    void initProducts(){
        products = new ArrayList<>();
//        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
//        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
//        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
//        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
//        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
//        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
//        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));

        productAdapter = new ProductAdapter(this, products);

        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }


}