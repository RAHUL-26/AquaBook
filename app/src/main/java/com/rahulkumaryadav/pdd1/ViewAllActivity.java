package com.rahulkumaryadav.pdd1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;
    public static List<WishlistModel> wishlistModelList;
    public static List<HorizontalProductScrollModel> horizontalProductScrollModelList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.view_all_recycler_view);
        gridView =findViewById(R.id.view_all_grid_view);

        int layout_code= getIntent().getIntExtra("layout_code",-1);

        if(layout_code==0) {
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);


//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_jar_image, "Bisleri 20L Jar", 1, "4.7", 270, "₹ 69", "₹ 80", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_ten_l, "Bisleri 10L Jar", 0, "4.5", 27, "₹ 49", "₹ 60", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_five_l, "Bisleri 5L Jar", 4, "4.6", 70, "₹ 34", "₹ 40", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_two_l, "Bisleri 2L Bottle", 2, "4.5", 2170, "₹ 27", "₹ 30", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_one_l, "Bisleri 1L Bottle", 0, "4.5", 20, "₹ 20", "₹ 25", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_half_ml, "Bisleri 500mL Bottle", 4, "4.7", 70, "₹ 15", "₹ 20", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_three_hun_ml, "Bisleri 300mL Bottle", 2, "4.5", 2170, "₹ 10", "₹ 14", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.normal_water_one_l, "Mineral Water 1L Bottle", 0, "4.5", 20, "₹ 15", "₹ 25", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.normal_jar, "Mineral Water 20L Jar", 2, "4.5", 2170, "₹ 44", "₹ 60", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.aquafina_one_l, "Aquafina 1L Bottle", 0, "4.5", 20, "₹ 22", "₹ 25", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.bisleri_five_l, "Bisleri 5L Bottle", 4, "4.7", 70, "₹ 34", "₹ 40", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.normal_water_one_l, "Mineral Water 1L Bottle", 2, "4.5", 2170, "₹ 15", "₹ 25", "Cash On Delivery available"));
//            wishlistModelList.add(new WishlistModel(R.drawable.aquafina_one_l, "Aquafina 1L Bottle", 0, "4.5", 20, "₹ 22", "₹ 25", "Cash On Delivery available"));

            WishlistAdapter adapter = new WishlistAdapter(wishlistModelList, false);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }



        else if(layout_code==1) {
            gridView.setVisibility(View.VISIBLE);
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));
//            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.waterlogosec, "20L Bisleri Jar", "₹ 70", "Available"));

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
            gridView.setAdapter(gridProductLayoutAdapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}