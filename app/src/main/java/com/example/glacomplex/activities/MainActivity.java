package com.example.glacomplex.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initCategories();
        initProducts();
        initSlider();
    }

    private void initSlider() {
        binding.carousel.addData(new CarouselItem("https://feeds.abplive.com/onecms/images/uploaded-images/2022/09/22/555838c62ba9be4994a23797004986541663833308234402_original.jpg", "Amazon Caption Here"));
        binding.carousel.addData(new CarouselItem("https://images.hindustantimes.com/tech/img/2022/09/05/960x540/Flipkart_sale_1662379551619_1662379562805_1662379562805.PNG", "Flipkart Caption Here"));
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

    void initProducts(){
        products = new ArrayList<>();
        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));
        products.add(new Product("Mens Hoodie", "https://m.media-amazon.com/images/I/61FueJJCbLL._UX679_.jpg", "", 3500, 10, 5, 1));

        productAdapter = new ProductAdapter(this, products);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

}