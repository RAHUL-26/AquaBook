package com.rahulkumaryadav.pdd1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.rahulkumaryadav.pdd1.DBQueries.lists;
import static com.rahulkumaryadav.pdd1.DBQueries.loadFragmentData;
import static com.rahulkumaryadav.pdd1.DBQueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///home page fake list
        List <SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#FFFFFF"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#FFFFFF", horizontalProductScrollModelFakeList, new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#FFFFFF",horizontalProductScrollModelFakeList));
        // /home page fake list

        //***********************Banner Slider******************************//


//        List<SliderModel> sliderModelList=new ArrayList<SliderModel>();

//        sliderModelList.add(new SliderModel(R.drawable.waterlogosec,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.profile_pic,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.waterlogosec,"#00CCCB"));
//
//        sliderModelList.add(new SliderModel(R.drawable.waterlogo,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.profile_pic,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.bisleri_jar_image,"#FFFFFF"));
//        sliderModelList.add(new SliderModel(R.drawable.waterlogosec,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.waterlogosec,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.waterlogosec,"#00CCCB"));
//
//        sliderModelList.add(new SliderModel(R.drawable.profile_pic,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.waterlogosec,"#00CCCB"));
//        sliderModelList.add(new SliderModel(R.drawable.waterlogo,"#00CCCB"));


        //***********************Banner Slider******************************//



        //************************Horizontal product Layout*****************//

//
//        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 70","Available"));
//
//        //************************Horizontal product Layout*****************//



        // ************************Test*****************//

        categoryRecyclerView=findViewById(R.id.category_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       categoryRecyclerView.setLayoutManager(testingLayoutManager);


        adapter = new HomePageAdapter(homePageModelFakeList);

       int listPosition =0;
        for (int x=0;x< loadedCategoriesNames.size();x++){
            if(loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition=x;
            }
        }
        if(listPosition==0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size()-1,title);
        }else{
            adapter = new HomePageAdapter(lists.get(listPosition));
        }
//        homePageModelList.add(new HomePageModel(0,sliderModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.waterlogo,"#00CCCB"));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"#Top Selling",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"#Top Selling",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.bisleri_jar_image,"#00CCCB"));
//
        categoryRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.main_search_icon)
        {
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }
        else if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}