package com.rahulkumaryadav.pdd1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
//import com.paytm.pgsdk.PaytmOrder;
//import com.paytm.pgsdk.PaytmPGService;
//import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecyclerView;
    public static CartAdapter cartAdapter;
    private Button changeOrAddNewAddressBtn;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullName;
    private String name,mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private Button continueBtn;
    public static Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private TextView codTitle;
    private View divider;
    private ImageButton paytm,cod;
    private String paymentMethod = "PAYTM";
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;
    private TextView orderId;

    private boolean successResponse = false;
    public static boolean fromCart ;
    public static boolean codOrderConfirmed=false;

    private  FirebaseFirestore firebaseFirestore;


    public static boolean getQtyIDs = true;

    private Integer ActivityRequestCode = 2;
    private String TAG = "DeliveryActivity";
    public static String midString = "JxkGAT12164880633079", txnAmountString = "", orderIdString = "", txnTokenString = "";
   // private static String amount, note, name, upivirtualid;

//    String upiId = "surajdeoliluah73@okicici", transactionNote = "pay test";
//    String status;
//    Uri uri;
//    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
  //  int GOOGLE_PAY_REQUEST_CODE = 123;

    //final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeOrAddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);

        fullName = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout = findViewById(R.id.order_conformation_layout);
        orderId = findViewById(R.id.order_id);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);


        ////////loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.button_round));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ////////loading dialog


        ////////payment dialog
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm = paymentMethodDialog.findViewById(R.id.paytm);
        cod=paymentMethodDialog.findViewById(R.id.cod_btn);
        codTitle = paymentMethodDialog.findViewById(R.id.cod_btn_title);
        divider=paymentMethodDialog.findViewById(R.id.divider);
        ////////payment dialog

        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs=true;

        orderIdString = UUID.randomUUID().toString().substring(0,28);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs=false;
                Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressesIntent);

            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Boolean allProductsAvailable = true;
              for (CartItemModel cartItemModel: cartItemModelList){
                  if (cartItemModel.isQtyError()){
                      allProductsAvailable =false;
                      break;
                  }
                  if (cartItemModel.getType()==CartItemModel.CART_ITEM) {
                      if (!cartItemModel.isCOD()) {
                          cod.setEnabled(false);
                          cod.setAlpha(0.5f);
                          codTitle.setText("COD Unavailable");
                          codTitle.setAlpha(0.5f);

                          break;
                      } else {
                          cod.setEnabled(true);
                          cod.setAlpha(1f);
                          codTitle.setText("Cash on Delivery");
                          codTitle.setAlpha(1f);

                      }
                  }
              }
              if (allProductsAvailable){
                  paymentMethodDialog.show();
              }

            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod="COD";
                placeOrderDetails();
            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod="PAYTM";
                placeOrderDetails();
            }
        });

    }

    public void startPaytmPayment (String token){

        txnTokenString = token;
        // for test mode use it
         String host = "https://securegw-stage.paytm.in/";
        // for production mode use it
       // String host = "https://securegw.paytm.in/";
        String orderDetails = "MID: " + midString + ", OrderId: " + orderIdString + ", TxnToken: " + txnTokenString
                + ", Amount: " + txnAmountString;
        Log.e(TAG, "order details "+ orderDetails);

        String callBackUrl = host + "theia/paytmCallback?ORDER_ID="+orderIdString;
        Log.e(TAG, " callback URL "+callBackUrl);
        PaytmOrder paytmOrder = new PaytmOrder(orderIdString, midString, txnTokenString, txnAmountString, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback(){
            @Override
            public void onTransactionResponse(Bundle bundle) {
                Log.e(TAG, "Response (onTransactionResponse) : "+bundle.toString());

            }

            @Override
            public void networkNotAvailable() {
                Log.e(TAG, "network not available ");
            }

            @Override
            public void onErrorProceed(String s) {
                Log.e(TAG, " onErrorProcess "+s.toString());
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.e(TAG, "Clientauth "+s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.e(TAG, " UI error "+s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.e(TAG, " error loading web "+s+"--"+s1);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.e(TAG, "backPress ");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.e(TAG, " transaction cancel "+s);
            }
        });

        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, ActivityRequestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG ," result code "+resultCode);
        // -1 means successful  // 0 means failed
        // one error is - nativeSdkForMerchantMessage : networkError
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.e(TAG, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }

          //  Log.e(TAG, "*#*#*#"+data.getStringExtra("response").contains("TXN_SUCCESS"));
            if (data.getStringExtra("response").contains("TXN_SUCCESS")) {

                    Map<String,Object> updateStatus = new HashMap<>();
                    updateStatus.put("Payment Status","Paid");
                    updateStatus.put("Order Status","Ordered");

                    firebaseFirestore.collection("ORDERS").document(orderIdString).update(updateStatus)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Map<String,Object> userOrder = new HashMap<>();
                                        userOrder.put("order_id",orderIdString);
                                        userOrder.put("time",FieldValue.serverTimestamp());
                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(orderIdString).set(userOrder)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            showConformationLayout();
                                                        }else{
                                                            Toast.makeText(DeliveryActivity.this, "Failed to update user's Order List.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }
                                    else{
                                        Toast.makeText(DeliveryActivity.this, "Order Cancelled!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



            }

                Log.e(TAG, " data " + data.getStringExtra("nativeSdkForMerchantMessage"));
                Log.e(TAG, " data response - " + data.getStringExtra("response"));

/*
 data response - {"BANKNAME":"WALLET","BANKTXNID":"1395841115",
 "CHECKSUMHASH":"7jRCFIk6eRmrep+IhnmQrlrL43KSCSXrmMP5pH0hekXaaxjt3MEgd1N9mLtWyu4VwpWexHOILCTAhybOo5EVDmAEV33rg2VAS/p0PXdk\u003d",
 "CURRENCY":"INR","GATEWAYNAME":"WALLET","MID":"EAcR4116","ORDERID":"100620202152",
 "PAYMENTMODE":"PPI","RESPCODE":"01","RESPMSG":"Txn Success","STATUS":"TXN_SUCCESS",
 "TXNAMOUNT":"2.00","TXNDATE":"2020-06-10 16:57:45.0","TXNID":"202006101112128001101683631290118"}
  */
      //   Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }else{
            Log.e(TAG, " payment failed");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
///accessing quantity
        if (getQtyIDs) {
            loadingDialog.show();
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                for(int y =0;y<cartItemModelList.get(x).getProductQuantity();y++){
                    String quantityDocumentName = UUID.randomUUID().toString().substring(0,20);
                    Map<String,Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());
                    int finalX = x;
                    int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timeStamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        cartItemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);

                                        if (finalY +1 == cartItemModelList.get(finalX).getProductQuantity()){

                                            firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuantity()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()){
                                                                List <String> serverQuantity = new ArrayList<>();

                                                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                                                    serverQuantity.add(queryDocumentSnapshot.getId());
                                                                }

                                                                long availableQty = 0;
                                                                boolean toastOnce=true;      ///to be checked
                                                                boolean noLongerAvailable = true;
                                                                for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()){
                                                                    cartItemModelList.get(finalX).setQtyError(false);

                                                                    if (!serverQuantity.contains(qtyId)){

                                                                        if (noLongerAvailable){
                                                                            cartItemModelList.get(finalX).setInStock(false);
                                                                        }else {
                                                                            cartItemModelList.get(finalX).setQtyError(true);
                                                                            cartItemModelList.get(finalX).setMaxQuantity(availableQty);
                                                                            if (toastOnce) {   ///to be checked
                                                                                Toast.makeText(DeliveryActivity.this, "Sorry! some products may not be available in the required quantity.", Toast.LENGTH_LONG).show();
                                                                                toastOnce=false;
                                                                            }                                                                    }

                                                                    }else{
                                                                        availableQty++;
                                                                        noLongerAvailable= false;
                                                                    }

                                                                }
                                                                cartAdapter.notifyDataSetChanged();
                                                            }else{
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });

                                        }
                                    }else{
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }

            }
        }else{
            getQtyIDs=true;
        }
///accessing quantity

        name = DBQueries.addressesModelList.get(DBQueries.selectedAddress).getName();
        mobileNo=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getMobileNo();
        if (DBQueries.addressesModelList.get(DBQueries.selectedAddress).getAlternateMobileNo().equals("")) {
            fullName.setText(name + " - " + mobileNo);
        }else{
            fullName.setText(name + " - " + mobileNo+" | "+DBQueries.addressesModelList.get(DBQueries.selectedAddress).getAlternateMobileNo());

        }
        String flatNo=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getFlatNo();
        String locality=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getLocality();
        String landmark=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getLandmark();
        String city=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getCity();
        String state=DBQueries.addressesModelList.get(DBQueries.selectedAddress).getState();

        if (landmark.equals("")){
            fullAddress.setText(flatNo + ", " + locality +" | CITY - " + city + " | " + state);

        }else {
            fullAddress.setText(flatNo + ", " + locality + " | LANDMARK - " + landmark + " | CITY - " + city + " | " + state);
        }
        pincode.setText(DBQueries.addressesModelList.get(DBQueries.selectedAddress).getPincode());

        if (codOrderConfirmed){
            showConformationLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
      if(id== android.R.id.home){
          finish();
          return true;
      }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();

        if (getQtyIDs) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size()-1))){
                                            cartItemModelList.get(finalX).getQtyIDs().clear();

                                        }
                                    }
                                });
                    }
                }
                else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void showConformationLayout(){
        successResponse=true;
        codOrderConfirmed=false;
        getQtyIDs=false;
        for (int x=0;x<cartItemModelList.size()-1;x++){
            for(String qtyID : cartItemModelList.get(x).getQtyIDs()){

                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID",FirebaseAuth.getInstance().getUid());

            }

        }


        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        }else{
            MainActivity.resetMainActivity=true;
        }
        if (ProductDetailsActivity.productDetailsActivity != null) {
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }

//////////sent confirmation message
/*        //  Toast.makeText(DeliveryActivity.this, "Success!", Toast.LENGTH_SHORT).show();
       Log.e(TAG, "*********RUNNING");
        String SMS_API = "https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ////nothing to do here
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ////nothing to do here
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map <String,String> headers = new HashMap<>();
                headers.put("authorization","OTgh4JICeydqANnzGbxrcoQpM5kKf7B3jlRZ9PVuHvs6w1t8aEMWazSpOTRANo5V6rBHdPie04X2sZhg");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> body = new HashMap<>();
                body.put("sender_id","FSTSMS");
                body.put("language","english");
                body.put("route","qt");
                body.put("numbers",mobileNo);
                body.put("message","41955");
                body.put("variables","{#FF#}");
                body.put("variables_values", orderIdString);

                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequest);
*/

//////////sent confirmation message



        if (fromCart){
            loadingDialog.show();
            Map<String,Object> updateCartlist = new HashMap<>();
            long cartListSize =0;
            List<Integer> indexList = new ArrayList<>();
            for (int x=0;x<DBQueries.cartList.size();x++){
                if (!cartItemModelList.get(x).isInStock()){
                    updateCartlist.put("product_ID_"+cartListSize,cartItemModelList.get(x).getProductID());
                    cartListSize++;
                }else{
                    indexList.add(x);
                }
            }
            updateCartlist.put("list_size",cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        for (int x=0;x<indexList.size();x++){
                            DBQueries.cartList.remove(indexList.get(x).intValue());
                            DBQueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DBQueries.cartItemModelList.remove(DBQueries.cartItemModelList.size()-1);
                        }
                    }else{

                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });
        }

        continueBtn.setEnabled(false);
        changeOrAddNewAddressBtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order ID " + orderIdString);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        Log.e(TAG, "*********RUNNING");
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void placeOrderDetails() {
        String userID = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (CartItemModel cartItemModel : cartItemModelList) {
            if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER ID",orderIdString);
                orderDetails.put("Product Id",cartItemModel.getProductID());
                orderDetails.put("Product Image",cartItemModel.getProductImage());
                orderDetails.put("Product Title",cartItemModel.getProductTitle());
                orderDetails.put("User Id",userID);
                orderDetails.put("Product Quantity",cartItemModel.getProductQuantity());
                if (cartItemModel.getCuttedPrice()!=null) {
                    orderDetails.put("Cutted Price", cartItemModel.getCuttedPrice());
                }else{
                    orderDetails.put("Cutted Price", "");

                }
                orderDetails.put("Product Price",cartItemModel.getProductPrice());
                if (cartItemModel.getSelectedCouponId()!=null) {
                    orderDetails.put("Coupon Id", cartItemModel.getSelectedCouponId());
                }else{
                    orderDetails.put("Coupon Id", "");

                }
                if (cartItemModel.getDiscountedPrice()!=null) {
                    orderDetails.put("Discounted Price", cartItemModel.getDiscountedPrice());
                }else{
                    orderDetails.put("Discounted Price", "");

                }
                orderDetails.put("Ordered date",FieldValue.serverTimestamp());
                orderDetails.put("Packed date",FieldValue.serverTimestamp());
                orderDetails.put("Shipped date",FieldValue.serverTimestamp());
                orderDetails.put("Delivered date",FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date",FieldValue.serverTimestamp());
                orderDetails.put("Order Status","Ordered");
                orderDetails.put("Payment Method",paymentMethod);
                orderDetails.put("Address",fullAddress.getText());
                orderDetails.put("FullName",fullName.getText());
                orderDetails.put("Pincode",pincode.getText());
                orderDetails.put("Free Coupons",cartItemModel.getFreeCoupons());
                orderDetails.put("Delivery Price",cartItemModelList.get(cartItemModelList.size()-1).getDeliveryPrice());
                orderDetails.put("Cancellation requested",false);
                    firebaseFirestore.collection("ORDERS").document(orderIdString).collection("OrderItems").document(cartItemModel.getProductID())
                    .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()){

                                String error = task.getException().getMessage();
                                Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
            else{
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items",cartItemModel.getTotalItems());
                orderDetails.put("Total Items Price",cartItemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price",cartItemModel.getDeliveryPrice());
                orderDetails.put("Total Amount",cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount",cartItemModel.getSavedAmount());
                orderDetails.put("Payment Status","not Paid");
                orderDetails.put("Order Status","Cancelled");

                firebaseFirestore.collection("ORDERS").document(orderIdString)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if(paymentMethod.equals("PAYTM")){
                                paytm();
                            }else{
                                cod();
                            }

                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }
    }
    private void paytm(){
        getQtyIDs= false;
        paymentMethodDialog.dismiss();
        loadingDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        // midString = "JxkGAT12164880633079";
        String cutomer_id = FirebaseAuth.getInstance().getUid();

        txnAmountString =totalAmount.getText().toString().substring(2);

        //   String url = "https://dolphin-water-service.000webhostapp.com/paytm/TxnTest.php";
        //  String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";


        ServiceWrapper serviceWrapper = new ServiceWrapper(null);
        Call<Token_Res> call = serviceWrapper.getTokenCall("12345", midString, orderIdString, txnAmountString);
        Log.e("DeliveryActivity", midString+" // "+orderIdString+" // "+txnAmountString);

        call.enqueue(new Callback<Token_Res>() {
            @Override
            public void onResponse(Call<Token_Res> call, Response<Token_Res> response) {
                Log.e("DeliveryActivity","respo "+response.isSuccessful());
                loadingDialog.dismiss();
                try {

                    if (response.isSuccessful() && response.body()!=null){
                        if (response.body().getBody().getTxnToken()!="") {
                            Log.e("DeliveryActivity", " transaction token : "+response.body().getBody().getTxnToken());
                            startPaytmPayment(response.body().getBody().getTxnToken());
                        }else {
                            Log.e("DeliveryActivity", " Token status false");
                        }
                    }
                }catch (Exception e){
                    Log.e("DeliveryActivity", " error in Token Res "+e.toString());
                }
            }

            @Override
            public void onFailure(Call<Token_Res> call, Throwable t) {

                loadingDialog.dismiss();
                Log.e("DeliveryActivity", " response error "+t.toString());
            }
        });
    }

    private void cod(){
        getQtyIDs= false;
        paymentMethodDialog.dismiss();
        Intent otpIntent = new Intent(DeliveryActivity.this,OTPverificationActivity.class);
        otpIntent.putExtra("mobileNo",mobileNo.substring(0,10));
        otpIntent.putExtra("OrderID",orderIdString);
        startActivity(otpIntent);
    }
}