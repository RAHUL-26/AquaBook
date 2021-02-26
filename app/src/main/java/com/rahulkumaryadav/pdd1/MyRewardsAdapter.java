package com.rahulkumaryadav.pdd1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {
    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView couponsRecyclerView;
    private  LinearLayout selectedCoupon;
    private String productOriginalPrice;
    private TextView selectedCouponTitle;
    private TextView selectedCouponExpiryDate;
    private TextView selectedCouponBody;
    private TextView yourCouponTv;
    private TextView changeCouponTv;
    private TextView discountedPrice;
    private int cartItemPosition=-1;
    private List <CartItemModel> cartItemModelList;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout ) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;

    }
    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView couponsRecyclerView, LinearLayout selectedCoupon , String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody,TextView yourCouponTv,TextView changeCouponTv,TextView discountedPrice ) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.couponsRecyclerView=couponsRecyclerView;
        this.selectedCoupon=selectedCoupon;
        this.productOriginalPrice=productOriginalPrice;
        this.selectedCouponTitle=couponTitle;
        this.selectedCouponExpiryDate=couponExpiryDate;
        this.selectedCouponBody=couponBody;
        this.yourCouponTv=yourCouponTv;
        this.changeCouponTv=changeCouponTv;
        this.discountedPrice=discountedPrice;
    }
    public MyRewardsAdapter(int cartItemPosition,List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView couponsRecyclerView, LinearLayout selectedCoupon , String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody,TextView yourCouponTv,TextView changeCouponTv,TextView discountedPrice ,List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.couponsRecyclerView=couponsRecyclerView;
        this.selectedCoupon=selectedCoupon;
        this.productOriginalPrice=productOriginalPrice;
        this.selectedCouponTitle=couponTitle;
        this.selectedCouponExpiryDate=couponExpiryDate;
        this.selectedCouponBody=couponBody;
        this.yourCouponTv=yourCouponTv;
        this.changeCouponTv=changeCouponTv;
        this.discountedPrice=discountedPrice;
        this.cartItemPosition=cartItemPosition;
        this.cartItemModelList=cartItemModelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
       View view;
        if(useMiniLayout){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);

        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
              }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {

        String couponID = rewardModelList.get(position).getCouponID();
        String type=rewardModelList.get(position).getType();
        Date validity=rewardModelList.get(position).getTimestamp();
        String body=rewardModelList.get(position).getCouponBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getDiscORamt();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();
        viewholder.setData(couponID,type,validity,body,upperLimit,lowerLimit,discORamt,alreadyUsed);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView couponTitle;
        private TextView couponExpiryDate;
        private TextView couponBody;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            couponTitle= itemView.findViewById(R.id.coupon_title);
            couponExpiryDate=itemView.findViewById(R.id.coupon_validity);
            couponBody= itemView.findViewById(R.id.coupon_body);
        }
        private void setData(String couponID,String type,Date validity,String body,String upperLimit,String lowerLimit,String discORamt,boolean alreadyUsed){
           if (type.equals("Discount")){
               couponTitle.setText(type);
           }else{
               couponTitle.setText("Get FLAT ₹ "+discORamt+" OFF");
           }

//           Date prevYear = validity;
//           prevYear.setYear(prevYear.getYear()-1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMMM");
            if (alreadyUsed){
                couponExpiryDate.setText("Already used");
                couponExpiryDate.setTextColor(Color.parseColor("#FF0000"));
                couponBody.setTextColor(Color.parseColor("#50ffffff"));
                couponTitle.setTextColor(Color.parseColor("#50ffffff"));

            }else {
                couponBody.setTextColor(Color.parseColor("#ffffff"));
                couponTitle.setTextColor(Color.parseColor("#ffffff"));
                couponExpiryDate.setTextColor(Color.parseColor("#03A9F4"));
                couponExpiryDate.setText("till " + simpleDateFormat.format(validity));
            }

           couponBody.setText(body);
            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!alreadyUsed) {

                            selectedCouponTitle.setText(type);
                            selectedCouponExpiryDate.setText(simpleDateFormat.format(validity));
                            selectedCouponBody.setText(body);

                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    discountedPrice.setTextColor(Color.parseColor("#000000"));
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discORamt) / 100;
                                    discountedPrice.setText("₹ " + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount));
                                } else {
                                    discountedPrice.setTextColor(Color.parseColor("#000000"));
                                    discountedPrice.setText("₹ " + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discORamt)));

                                }
                                if  (cartItemPosition!=-1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCouponId(couponID);
                                }
                            } else {
                                if (cartItemPosition!=-1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCouponId(null);
                                }
                                discountedPrice.setText("Oops :(");
                                discountedPrice.setTextColor(Color.parseColor("#FF0000"));
                                Toast.makeText(itemView.getContext(), "Sorry! Coupon is not applicable for this product.", Toast.LENGTH_LONG).show();
                            }

                            if (couponsRecyclerView.getVisibility() == View.GONE) {
                                couponsRecyclerView.setVisibility(View.VISIBLE);
                                selectedCoupon.setVisibility(View.GONE);
                                changeCouponTv.setVisibility(View.GONE);
                                yourCouponTv.setText("Available Coupons");
                            } else {
                                couponsRecyclerView.setVisibility(View.GONE);
                                selectedCoupon.setVisibility(View.VISIBLE);
                                changeCouponTv.setVisibility(View.VISIBLE);
                                yourCouponTv.setText("YOUR COUPON");
                            }
                        }
                    }
                });
            }
        }
    }
}
