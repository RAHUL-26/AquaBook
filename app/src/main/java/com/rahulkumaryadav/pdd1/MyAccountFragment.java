package com.rahulkumaryadav.pdd1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton settingsBtn;
    private Button viewAllAddressBtn,signOutBtn;
    public static final int MANAGE_ADDRESS=1;
    private CircleImageView profileView,currentOrderImage;
    private TextView name,email,tvCurrentOrderStatus;
    private LinearLayout layoutContainer,recentOrdersContainer;
    private Dialog loadingDialog;
    private ImageView orderIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView yourRecentOrdersTitle;
    private TextView addressName,address,pincode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_account, container, false);

        ////////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.button_round));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////////loading dialog

        layoutContainer=view.findViewById(R.id.layout_container);
        profileView=view.findViewById(R.id.profile_image);
        name=view.findViewById(R.id.username_tv);
        email=view.findViewById(R.id.user_email_tv);
        currentOrderImage=view.findViewById(R.id.current_order_image);
        tvCurrentOrderStatus=view.findViewById(R.id.current_status_order_tv);
        orderIndicator=view.findViewById(R.id.ordered_indicator);
        packedIndicator=view.findViewById(R.id.packed_indicator);
        shippedIndicator=view.findViewById(R.id.shipped_indicator);
        deliveredIndicator=view.findViewById(R.id.delivered_indicator);
        O_P_progress=view.findViewById(R.id.order_packed_progress);
        P_S_progress=view.findViewById(R.id.packed_shipped_progress);
        S_D_progress=view.findViewById(R.id.shipped_delivered_progress);
        yourRecentOrdersTitle=view.findViewById(R.id.your_recent_orders_title);
        recentOrdersContainer=view.findViewById(R.id.recent_oders_container);
        addressName=view.findViewById(R.id.address_fullname);
        address=view.findViewById(R.id.address);
        pincode=view.findViewById(R.id.address_pincode);
        signOutBtn=view.findViewById(R.id.sign_out_btn);
        settingsBtn=view.findViewById(R.id.settings_btn);


        layoutContainer.getChildAt(1).setVisibility(View.GONE);

        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                for (MyOrderItemModel orderItemModel:DBQueries.myOrderItemModelList){
                    if (!orderItemModel.isCancellationRequested()){
                        if (!orderItemModel.getOrderStatus().equals("Delivered")&&!orderItemModel.getOrderStatus().equals("Cancelled")){
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.color.white)).into(currentOrderImage);
                            tvCurrentOrderStatus.setText(orderItemModel.getOrderStatus());
                            switch (orderItemModel.getOrderStatus()){
                                case "Ordered":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    O_P_progress.setProgress(20);
                                    break;
                                case "Packed":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(20);
                                    break;
                                case "Shipped":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);
                                    S_D_progress.setProgress(20);
                                    break;
                                case "Out for Delivery":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);
                                    S_D_progress.setProgress(70);
                                    break;

                            }

                        }
                    }
                }
                int i=0;
                for (MyOrderItemModel myOrderItemModel:DBQueries.myOrderItemModelList) {
                    if (i<4) {
                        if (myOrderItemModel.getOrderStatus().equals("Delivered")) {
                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.color.white)).into((CircleImageView) recentOrdersContainer.getChildAt(i));
                            i++;
                        }
                    }
                    else{
                        break;
                    }
                }
                if (i==0){
                    yourRecentOrdersTitle.setText("No recent Orders");
                }
                if (i<3){
                    for (int x=i;x<4;x++){
                        recentOrdersContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialog.setOnDismissListener(null);
                        if (DBQueries.addressesModelList.size()==0){
                            addressName.setText("No Address added yet");
                            address.setText("-");
                            pincode.setText("-");
                        }else{
                            setAddress();
                           }
                    }
                });
                DBQueries.loadAddresses(getContext(),loadingDialog,false);

            }
        });
        DBQueries.loadOrders(getContext(),null,loadingDialog);
        viewAllAddressBtn=view.findViewById(R.id.view_all_addresses_btn);
        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressesIntent = new Intent(getContext(),MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE",MANAGE_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                DBQueries.clearData();
                Intent registerIntent = new Intent (getContext(),RegisterActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateUserInfo = new Intent(getContext(),UpdateUserInfoActivity.class);
                updateUserInfo.putExtra("Name",name.getText());
                updateUserInfo.putExtra("Email",email.getText());
                updateUserInfo.putExtra("Photo",DBQueries.profile);
                startActivity(updateUserInfo);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(DBQueries.firstName+" "+DBQueries.lastName);
        email.setText(DBQueries.email);
        if (!(DBQueries.profile.equals(""))){
            Glide.with(getContext()).load(DBQueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile_pic)).into(profileView);
        }else {
            profileView.setImageResource(R.drawable.profile_pic);
        }

        if (!loadingDialog.isShowing()){
            if (DBQueries.addressesModelList.size()==0){
                addressName.setText("No Address added yet");
                address.setText("-");
                pincode.setText("-");
            }else{
                setAddress();
            }
        }
    }

    private void setAddress(){
        String nameText,mobileNo;
        nameText = DBQueries.addressesModelList.get(DBQueries.selectedAddress).getName();
        mobileNo=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getMobileNo();
        if (DBQueries.addressesModelList.get(DBQueries.selectedAddress).getAlternateMobileNo().equals("")) {
            addressName.setText(nameText + " - " + mobileNo);
        }else{
            addressName.setText(nameText + " - " + mobileNo+" | "+DBQueries.addressesModelList.get(DBQueries.selectedAddress).getAlternateMobileNo());

        }
        String flatNo=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getFlatNo();
        String locality=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getLocality();
        String landmark=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getLandmark();
        String city=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getCity();
        String state=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getState();

        if (landmark.equals("")){
            address.setText(flatNo + ", " + locality +" | CITY - " + city + " | " + state);

        }else {
            address.setText(flatNo + ", " + locality + " | LANDMARK - " + landmark + " | CITY - " + city + " | " + state);
        }
        pincode.setText(DBQueries.addressesModelList.get(DBQueries.selectedAddress).getPincode());

    }
}