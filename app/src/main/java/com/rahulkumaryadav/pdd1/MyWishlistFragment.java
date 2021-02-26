package com.rahulkumaryadav.pdd1;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MyWishlistFragment extends Fragment {

    public MyWishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishlistRecyclerView;
    private Dialog loadingDialog;
    public static WishlistAdapter wishlistAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        ////////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog .setContentView(R.layout.loading_progress_dialog);
        loadingDialog .setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.button_round));
        loadingDialog .getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////////loading dialog

        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

      //  List<WishlistModel> wishlistModelList=new ArrayList<>();
//        wishlistModelList.add(new WishlistModel(R.drawable.bisleri_jar_image,"Bisleri 20L Jar",1,"4.7",270,"₹ 69","₹ 80","Cash On Delivery available"));
//        wishlistModelList.add(new WishlistModel(R.drawable.bisleri_jar_image,"Bisleri 20L Jar",0,"4.0",27,"₹ 69","₹ 80","Cash On Delivery available"));
//        wishlistModelList.add(new WishlistModel(R.drawable.bisleri_jar_image,"Bisleri 20L Jar",4,"3.7",70,"₹ 69","₹ 80","Cash On Delivery available"));
//        wishlistModelList.add(new WishlistModel(R.drawable.bisleri_jar_image,"Bisleri 20L Jar",2,"3.5",2170,"₹ 69","₹ 80","Cash On Delivery available"));
//        wishlistModelList.add(new WishlistModel(R.drawable.bisleri_jar_image,"Bisleri 20L Jar",0,"4.5",20,"₹ 69","₹ 80","Cash On Delivery available"));

        if(DBQueries.wishlistModelList.size()==0){
            DBQueries.wishList.clear();
            DBQueries.loadWishList(getContext(),loadingDialog,true);
        }
        else {
            loadingDialog.dismiss();
        }
       wishlistAdapter = new WishlistAdapter(DBQueries.wishlistModelList,true);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();
        return view;
    }
}