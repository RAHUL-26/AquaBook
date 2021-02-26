package com.rahulkumaryadav.pdd1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationFragment extends Fragment {


    public ProductSpecificationFragment() {
        // Required empty public constructor
    }
private RecyclerView productSpecificationRecyclerView;
    public List<ProductSpecificationModel> productSpecificationModelList;

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_product_specification, container, false);
        productSpecificationRecyclerView =view.findViewById(R.id.product_specification_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        productSpecificationRecyclerView.setLayoutManager(linearLayoutManager);

//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Dolphin Water Suppliers"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Seller Name -","Rahul Kumar Yadav"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Contact -","9123939906"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Address -","My Address"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Supplies-","Can, Jar, Water Bottle, Dispenser, Container "));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Seller ID -","UID"));
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Other Branches:"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Seller Name -","Other Provider"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Contact -","9100000000"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Address -","Persons Address"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Supplies-","Can, Jar, Water Bottle, Dispenser, Container "));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Seller ID -","UID"));
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Other Branches:"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Seller Name -","Another Provider"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Contact -","9100000001"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Address -","Persons Address"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Supplies-","Can, Jar, Water Bottle, Dispenser, Container "));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Seller ID -","UID"));

       ProductSpecificationAdapter productSpecificationAdapter = new ProductSpecificationAdapter(productSpecificationModelList);
       productSpecificationRecyclerView.setAdapter(productSpecificationAdapter);
       productSpecificationAdapter.notifyDataSetChanged();


        return view;
       }
}