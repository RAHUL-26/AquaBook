package com.rahulkumaryadav.pdd1;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.rahulkumaryadav.pdd1.DBQueries.categoryModelList;
import static com.rahulkumaryadav.pdd1.DBQueries.firebaseFirestore;

import static com.rahulkumaryadav.pdd1.DBQueries.lists;
import static com.rahulkumaryadav.pdd1.DBQueries.loadCategories;
import static com.rahulkumaryadav.pdd1.DBQueries.loadFragmentData;
import static com.rahulkumaryadav.pdd1.DBQueries.loadedCategoriesNames;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;
    private Button retryBtn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection=view.findViewById(R.id.no_internet_connection);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        retryBtn = view.findViewById(R.id.retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.aqua),getContext().getResources().getColor(R.color.aqua),getContext().getResources().getColor(R.color.aqua));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);


//////categories fake list
        categoryModelFakeList.add(new CategoryModel("null",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
//////categories fake list

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

        categoryAdapter = new CategoryAdapter(categoryModelFakeList);


        adapter = new HomePageAdapter(homePageModelFakeList);


        connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()==true) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);


            if (categoryModelList.size()==0){
                loadCategories(categoryRecyclerView,getContext());
            }
            else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }

            categoryRecyclerView.setAdapter(categoryAdapter);


//        categoryModelList.add(new CategoryModel("link","Home"));
//        categoryModelList.add(new CategoryModel("link","Jar"));
//        categoryModelList.add(new CategoryModel("link","Bottle"));
//        categoryModelList.add(new CategoryModel("link","Occasional"));
//        categoryModelList.add(new CategoryModel("link","Mineral"));
//        categoryModelList.add(new CategoryModel("link","Trending"));
//        categoryModelList.add(new CategoryModel("link","Daily"));
//        categoryModelList.add(new CategoryModel("link","Offer"));
//        categoryModelList.add(new CategoryModel("link","Heavy"));


            //***********************Banner Slider******************************//


//       List<SliderModel> sliderModelList=new ArrayList<SliderModel>();
//
//        sliderModelList.add(new SliderModel(R.drawable.water_delivery_mask_banner,"#FFFFFF"));
//        sliderModelList.add(new SliderModel(R.drawable.water_bottle_only_banner,"#84cff0"));
//        sliderModelList.add(new SliderModel(R.drawable.water_image_banner,"#ccd5d8"));
//        sliderModelList.add(new SliderModel(R.drawable.water_bottle_banner,"#FFFFFF"));
//        sliderModelList.add(new SliderModel(R.drawable.water_delivery_image_banner,"#FFFFFF"));
//        sliderModelList.add(new SliderModel(R.drawable.dolphin_water_banner,"#FFFFFF"));
//        sliderModelList.add(new SliderModel(R.drawable.water_bottle_only_banner,"#84cff0"));
//        sliderModelList.add(new SliderModel(R.drawable.normal_water_banner,"#FFFFFF"));


            //***********************Banner Slider******************************//


            //************************Horizontal product Layout*****************//


//        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_jar_image,"20L Bisleri Jar","₹ 80","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_ten_l,"10L Bisleri Jar","₹ 50","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_five_l,"5L Bisleri Jar","₹ 35","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_two_l,"2L Bisleri Bottle","₹ 25","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_one_l,"1L Bisleri Bottle","₹ 20","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_half_ml,"500mL Bisleri Bottle","₹ 15","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.bisleri_three_hun_ml,"300mL Bisleri Bottle","₹ 10","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.normal_water_one_l,"1L Mineral Water Bottle","₹ 15","Available"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.normal_jar,"20L Mineral Water Jar","₹ 60","Available"));

            //************************Horizontal product Layout*****************//


            // ************************Test*****************//



//        homePageModelList.add(new HomePageModel(0,sliderModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.water_bottle_only_banner,"#84cff0"));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"#Top Selling",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"#Top Selling",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.bisleri_jar_image,"#FFFFFF"));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"#Top Selling",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"#Top Selling",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.waterlogo,"#00CCCB"));


            if (lists.size()==0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());

                loadFragmentData(homePageRecyclerView,getContext(),0,"Home");
            }
            else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }

            homePageRecyclerView.setAdapter(adapter);


            // ************************test*****************//
        }
        else{
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_internet_icon).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            noInternetConnection.setBackgroundColor(getResources().getColor(R.color.white));
            retryBtn.setVisibility(View.VISIBLE);

        }

/////// refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });
// ///// refresh layout

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });

        return view;
    }

    private void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();
//        categoryModelList.clear();
//        lists.clear();
//        loadedCategoriesNames.clear();
        DBQueries.clearData();

        if(networkInfo!=null && networkInfo.isConnected()==true) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            categoryAdapter = new CategoryAdapter(categoryModelFakeList);
            adapter = new HomePageAdapter(homePageModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);

            homePageRecyclerView.setAdapter(adapter);

            loadCategories(categoryRecyclerView,getContext());

            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView,getContext(),0,"Home");

        }
        else{
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_internet_icon).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            noInternetConnection.setBackgroundColor(getResources().getColor(R.color.white));
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}