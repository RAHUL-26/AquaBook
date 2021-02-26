package com.rahulkumaryadav.pdd1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.FontResourcesParserCompat;

import android.animation.StateListAnimator;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {
    private int position;
    private TextView title,price,quantity;
    private ImageView productImage,orderedIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView orderedTitle,packedTitle,shippedTitle,deliveredTitle;
    private TextView orderedDate,packedDate,shippedDate,deliveredDate;
    private TextView orderedBody,packedBody,shippedBody,deliveredBody;
    private LinearLayout rateNowContainer;
    private int rating;
    private TextView fullName,address,pincode;
    private TextView totalItems,totalItemsPrice,discountText,discountPrice,deliveryPrice,totalAmount,savedAmount;
    private Dialog loadingDialog,cancelDialog;
    private SimpleDateFormat simpleDateFormat;
    private Button cancelOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////////loading dialog
        loadingDialog = new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.button_round));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ////////loading dialog

 ////////cancel dialog
        cancelDialog = new Dialog(OrderDetailsActivity.this);
        cancelDialog.setContentView(R.layout.order_cancel_dialog);
        cancelDialog.setCancelable(true);
      //  cancelDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.button_round));
     //   cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ////////cancel dialog

        position=getIntent().getIntExtra("Position",-1);
        MyOrderItemModel model=DBQueries.myOrderItemModelList.get(position);

        title=findViewById(R.id.product_title);
        price=findViewById(R.id.product_price);
        quantity=findViewById(R.id.product_quantity);
        productImage=findViewById(R.id.product_image);
        cancelOrderBtn=findViewById(R.id.cancel_order_btn);

        orderedIndicator =findViewById(R.id.ordered_indicator);
        packedIndicator=findViewById(R.id.packed_indicator);
        shippedIndicator=findViewById(R.id.shipped_indicator);
        deliveredIndicator=findViewById(R.id.delivered_indicator);

        O_P_progress=findViewById(R.id.ordered_packed_progress);
        P_S_progress=findViewById(R.id.packed_shipped_progress);
        S_D_progress=findViewById(R.id.shipped_delivered_progress);

        orderedTitle=findViewById(R.id.ordered_title);
        packedTitle=findViewById(R.id.packed_title);
        shippedTitle=findViewById(R.id.shipped_title);
        deliveredTitle=findViewById(R.id.delivered_title);

        orderedDate=findViewById(R.id.ordered_date);
        packedDate=findViewById(R.id.packed_date);
        shippedDate=findViewById(R.id.shipped_date);
        deliveredDate=findViewById(R.id.delivered_date);

        orderedBody=findViewById(R.id.ordered_body);
        packedBody=findViewById(R.id.packed_body);
        shippedBody=findViewById(R.id.shipped_body);
        deliveredBody=findViewById(R.id.delivered_body);

        rateNowContainer=findViewById(R.id.rate_now_container);
        fullName=findViewById(R.id.fullname);
        address=findViewById(R.id.address);
        pincode=findViewById(R.id.pincode);

        totalItems=findViewById(R.id.total_items);
        totalItemsPrice = findViewById(R.id.total_items_price);
        discountPrice=findViewById(R.id.discount_price);
        discountText=findViewById(R.id.discount_tv);
        deliveryPrice=findViewById(R.id.delivery_price);
        totalAmount = findViewById(R.id.total_price);
        savedAmount=findViewById(R.id.saved_amount);

        title.setText(model.getProductTitle());
        if (!model.getDiscountedPrice().equals("")) {
            price.setText("₹ "+model.getDiscountedPrice());
        }else{
            price.setText("₹ "+model.getProductPrice());
        }
        quantity.setText("Qty: "+String.valueOf(model.getProductQuantity()));
        Glide.with(this).load(model.getProductImage()).into(productImage);

        simpleDateFormat=new SimpleDateFormat("hh:mm aa EEE, dd MMM yyyy");

        switch (model.getOrderStatus()){
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                O_P_progress.setProgress(20);
//                P_S_progress.setVisibility(View.GONE);
//                S_D_progress.setVisibility(View.GONE);
//                O_P_progress.setVisibility(View.GONE);
//
//                packedIndicator.setVisibility(View.GONE);
//                packedBody.setVisibility(View.GONE);
//                packedDate.setVisibility(View.GONE);
//                packedTitle.setVisibility(View.GONE);
//
//                shippedIndicator.setVisibility(View.GONE);
//                shippedBody.setVisibility(View.GONE);
//                shippedDate.setVisibility(View.GONE);
//                shippedTitle.setVisibility(View.GONE);
//
//                deliveredIndicator.setVisibility(View.GONE);
//                deliveredBody.setVisibility(View.GONE);
//                deliveredDate.setVisibility(View.GONE);
//                deliveredTitle.setVisibility(View.GONE);
                break;
            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(20);
               // P_S_progress.setVisibility(View.GONE);
               // S_D_progress.setVisibility(View.GONE);
               // shippedIndicator.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                //deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);

                break;
            case "Shipped":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(20);
               // S_D_progress.setVisibility(View.GONE);

               // deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                break;
            case "Out for Delivery":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(70);

                deliveredTitle.setText("Out for Delivery");
                deliveredBody.setText("Your product is out for delivery");
                break;
            case "Delivered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);

                break;
            case "Cancelled":
                if (model.getPackedDate().after(model.getOrderedDate())){
                    if (model.getShippedDate().after(model.getPackedDate())){

                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your order was cancelled.");

                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);
                        S_D_progress.setProgress(100);

                    }else{
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                        shippedTitle.setText("Cancelled");
                        shippedBody.setText("Your order was cancelled.");

                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);
                        //S_D_progress.setVisibility(View.GONE);

                       // deliveredIndicator.setVisibility(View.GONE);
                        deliveredBody.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);

                    }
                }else{
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.aqua)));
                    orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    packedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                    packedTitle.setText("Cancelled");
                    packedBody.setText("Your order was cancelled.");

                    O_P_progress.setProgress(100);
                    P_S_progress.setVisibility(View.GONE);
                    S_D_progress.setVisibility(View.GONE);
                    shippedIndicator.setVisibility(View.GONE);
                    shippedBody.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);
                    shippedTitle.setVisibility(View.GONE);

                    deliveredIndicator.setVisibility(View.GONE);
                    deliveredBody.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);

                }
                break;
        }
        ///////////////////rating layout

        rating = model.getRating();
        setRating(rating);
        for(int x=0;x<rateNowContainer.getChildCount();x++){
            final int starPosition=x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingDialog.show();
                    setRating(starPosition);
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductId());
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot documentSnapshot =transaction.get(documentReference);
                            if (rating!=0){
                                Long increase = documentSnapshot.getLong(starPosition+1+"_star")+1;
                                Long decrease = documentSnapshot.getLong(rating+1+"_star")-1;
                                transaction.update(documentReference,starPosition+1+"_star",increase);
                                transaction.update(documentReference,rating+1+"_star",decrease);
                            }else{
                                Long increase = documentSnapshot.getLong(starPosition+1+"_star")+1;
                                transaction.update(documentReference,starPosition+1+"_star",increase);

                            }
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            Map<String, Object> myRating = new HashMap<>();

                            if (DBQueries.myRatedIds.contains(model.getProductId())) {

                                myRating.put("rating_" + DBQueries.myRatedIds.indexOf(model.getProductId()), (long) starPosition + 1);
                            } else {
                                myRating.put("list_size", (long) DBQueries.myRatedIds.size() + 1);
                                myRating.put("product_ID_" + DBQueries.myRatedIds.size(), model.getProductId());
                                myRating.put("rating_" + DBQueries.myRatedIds.size(), (long) starPosition + 1);
                            }

                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DBQueries.myOrderItemModelList.get(position).setRating(starPosition);
                                        if (DBQueries.myRatedIds.contains(model.getProductId())){
                                            DBQueries.myRating.set(DBQueries.myRatedIds.indexOf(model.getProductId()),Long.parseLong(String.valueOf(starPosition+1)));

                                        }else{
                                            DBQueries.myRatedIds.add(model.getProductId());
                                            DBQueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1)));
                                        }

                                    }
                                    else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
        }
        ///////////////////rating layout

        if (model.isCancellationRequested()){
            cancelOrderBtn.setVisibility(View.VISIBLE);
            cancelOrderBtn.setEnabled(false);
            cancelOrderBtn.setTextSize(12f);
            cancelOrderBtn.setStateListAnimator(null);
            cancelOrderBtn.setText("Cancellation in process...");
           // cancelOrderBtn.setTextColor(Color.parseColor("#FF0000"));
          //  cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }else{
            if (model.getOrderStatus().equals("Ordered")||model.getOrderStatus().equals("Packed")){
                cancelOrderBtn.setVisibility(View.VISIBLE);
                cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancelDialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialog.dismiss();
                            }
                        });
                        cancelDialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialog.dismiss();
                                loadingDialog.show();
                                Map<String,Object> map= new HashMap<>();
                                map.put("Order Id",model.getOrderID());
                                map.put("Product Id",model.getProductId());
                                map.put("Order Cancelled",false);
                                FirebaseFirestore.getInstance().collection("CANCELLED ORDERS").document().set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    FirebaseFirestore.getInstance().collection("ORDERS").document(model.getOrderID()).collection("OrderItems").document(model.getProductId()).update("Cancellation requested",true)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        model.setCancellationRequested(true);
                                                                        cancelOrderBtn.setEnabled(false);
                                                                        cancelOrderBtn.setTextSize(12f);
                                                                        cancelOrderBtn.setStateListAnimator(null);
                                                                        cancelOrderBtn.setText("Cancellation in process...");
                                                                        // cancelOrderBtn.setTextColor(Color.parseColor("#FF0000"));
                                                                        //  cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

                                                                    }else{

                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    loadingDialog.dismiss();
                                                                }
                                                            });
                                                }else{
                                                    loadingDialog.dismiss();
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                        cancelDialog.show();
                    }
                });
            }
        }

        Long totalProductPrice =model.getProductQuantity()*Long.parseLong(model.getProductPrice());
        Long totalCuttedPrice;
        long savedAmountValue=0;
        if(!model.getCuttedPrice().equals("")) {
            totalCuttedPrice = model.getProductQuantity() * Long.parseLong(model.getCuttedPrice());
            savedAmountValue=savedAmountValue+ totalCuttedPrice-totalProductPrice;
        }else{
            totalCuttedPrice = model.getProductQuantity() * Long.parseLong(model.getProductPrice());
        }
        fullName.setText(model.getFullName());
        address.setText(model.getAddress());
        pincode.setText(model.getPincode());

        totalItems.setText("Price ("+model.getProductQuantity()+" items)");

        if(model.getDiscountedPrice().equals("")){
            totalItemsPrice.setText("₹ "+totalCuttedPrice);

        }else {
            totalItemsPrice.setText("₹ "+totalCuttedPrice);
            savedAmountValue =savedAmountValue+(Long.valueOf(model.getProductPrice())-Long.valueOf(model.getDiscountedPrice()))*model.getProductQuantity();
        }

        if (model.getDeliveryPrice().equals("FREE")){
            deliveryPrice.setText(model.getDeliveryPrice());
            totalAmount.setText("₹ "+totalProductPrice);

        }else {
            deliveryPrice.setText("₹ " + model.getDeliveryPrice());
            totalAmount.setText("₹ "+(totalProductPrice+Long.valueOf(model.getDeliveryPrice())));
        }

//        if (!model.getCuttedPrice().equals("")) {
//            discountPrice.setVisibility(View.VISIBLE);
//            discountText.setVisibility(View.VISIBLE);
//            discountPrice.setText("- ₹ " + savedAmountValue);
//        }
//        else{
//            if(!model.getDiscountedPrice().equals("")){
//                discountPrice.setVisibility(View.VISIBLE);
//                discountText.setVisibility(View.VISIBLE);
//                discountPrice.setText("- ₹ " + savedAmountValue);
//            }else {
//                discountPrice.setVisibility(View.GONE);
//                discountText.setVisibility(View.GONE);
//            }
//        }
        savedAmount.setText("You saved ₹ "+savedAmountValue+" on this order");
        discountPrice.setText("- ₹ "+savedAmountValue);
        if (savedAmountValue!=0){
            savedAmount.setVisibility(View.VISIBLE);
            discountPrice.setVisibility(View.VISIBLE);
            discountText.setVisibility(View.VISIBLE);
        }else{
            savedAmount.setVisibility(View.GONE);
            discountPrice.setVisibility(View.GONE);
            discountText.setVisibility(View.GONE);

        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setRating(int starPosition) {
        for(int x=0;x<rateNowContainer.getChildCount();x++){
            ImageView starBtn = (ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#80979797")));
            //  starBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#80979797")));

            if(x<=starPosition){
                switch (starPosition){
                    case 0:
                        starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E10404")));
                        // starBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E10404")));
                        break;
                    case 1:
                        starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                        //    starBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                        break;
                    case 2:
                    case 3:
                        // starBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2F9AED")));
                    case 4:
                        starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00BCD4")));
                        //   starBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#42D619")));
                        break;
                    //   starBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0053F8")));
                }
            }

        }
    }
}