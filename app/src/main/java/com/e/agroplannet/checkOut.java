package com.e.agroplannet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.agroplannet.Adapter.Billing_cart;
import com.e.agroplannet.Model.OrderData;
import com.e.agroplannet.Notification.ApiService;
import com.e.agroplannet.Notification.Client;
import com.e.agroplannet.Notification.Data;
import com.e.agroplannet.Notification.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;
import com.squareup.okhttp.ResponseBody;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class checkOut extends AppCompatActivity {
    RecyclerView recyclerView ;
    FirebaseFirestore db ;
    FirebaseAuth auth ;
    Billing_cart cart ;
    List<String> MRP,ProductCount , ProductDetails , Image1,Image2,Image3,ProductMeasurement,ProductOriginalPrice,TotalPrice ;
    TextView subtotal , Est_tax, Total , delivery_charge ,ItemCount ,edit_Cart ;
    ImageView back ;
    CardView place_Order ;
    SwipeRefreshLayout refreshLayout ;
    String fcmUrl = "https://fcm.googleapis.com/",Token;
    String OwnerId = "dY6zP55i2MT0Pzy4QEa55tplERv1";
    String CurrentUserId,UserName;
    
    List<OrderData> orderData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        recyclerView = findViewById(R.id.card_list);
        edit_Cart = findViewById(R.id.edit_Order);
        back = findViewById(R.id.back);
        db = FirebaseFirestore.getInstance() ;
        auth = FirebaseAuth.getInstance();
        MRP = new ArrayList<>();
        ProductCount = new ArrayList<>();
        ProductDetails = new ArrayList<>();
        Image1 = new ArrayList<>();
        Image2 = new ArrayList<>();
        Image3 = new ArrayList<>();
        ProductMeasurement = new ArrayList<>();
        ProductOriginalPrice = new ArrayList<>();
        TotalPrice = new ArrayList<>();
        subtotal = findViewById(R.id.textView12);
        Est_tax = findViewById(R.id.textView14);
        Total = findViewById(R.id.total);
        delivery_charge = findViewById(R.id.textView16);
        ItemCount = findViewById(R.id.Item_count);
        refreshLayout = findViewById(R.id.refresh);
        place_Order = findViewById(R.id.cardView);

        orderData = new ArrayList<>();
        
        CurrentUserId = auth.getCurrentUser().getUid();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut.super.onBackPressed();
            }
        });

        edit_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut.super.onBackPressed();
            }
        });


        LoadData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Double total = 0.0;
                MRP.clear();
                ProductCount.clear();
                ProductDetails.clear();
                Image1.clear();
                Image2.clear();
                Image3.clear();
                LoadData();
                refreshLayout.setRefreshing(false);
            }
        });

        place_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String , Object> PlaceOrder  = new HashMap<>();
                PlaceOrder.put("SubTotal" ,subtotal.getText().toString() );
                PlaceOrder.put("Total" ,Total.getText().toString());
                PlaceOrder.put("UserId" , auth.getCurrentUser().getUid());

                db.collection("Pending Order").document().set(PlaceOrder) ;
                db.collection("Pending Order").whereEqualTo("UserId" , auth.getCurrentUser().getUid()).whereEqualTo("Total" ,Total.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()){

                            db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Double total = 0.0;
                                    Map<String,Object>  PlaceOrderProduct = new HashMap<>();

                                    for (DocumentSnapshot doc : task.getResult().getDocuments()){
                                        PlaceOrderProduct.put("MRP" ,doc.get("MRP").toString()) ;
                                        PlaceOrderProduct.put("ProductCount" ,doc.get("ProductCount").toString());
                                        PlaceOrderProduct.put("ProductDetails" , doc.get("ProductDetails").toString());
                                        PlaceOrderProduct.put("Image1" ,doc.get("ProductImage1").toString());
                                        PlaceOrderProduct.put("Image2" , doc.get("ProductImage2").toString());
                                        PlaceOrderProduct.put("Image3" , doc.get("ProductImage3").toString());
                                        PlaceOrderProduct.put("ProductMeasurement", doc.get("ProductMeasurement").toString()) ;
                                        PlaceOrderProduct.put("ProductOriginalPrice" , doc.get("ProductOriginalPrice").toString());
                                        PlaceOrderProduct.put("TotalPrice" ,doc.get("TotalPrice").toString() );
                                        db.collection("Pending Order").document(  documentChange.getDocument().getId()).collection("Order Product").document().set(PlaceOrderProduct);
                                        PlaceOrderProduct.clear();
                                    }


                                }
                            });
                        }
                    }
                });

                db.collection("user").document(CurrentUserId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                 UserName = task.getResult().getString("name");
                            }
                        });

                db.collection("Tokens").document(OwnerId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    Token = task.getResult().getString("token");
                                    String extra = " has requested for product.";
                                    String data = "Tab see information";
                                    sendNotification(Token,UserName + extra ,data);
                                }
                            }

                        });
            }
        });

    }

    private void LoadData() {
        db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Double total = 0.0;

                for (DocumentChange doc : value.getDocumentChanges()){
                    MRP.add(doc.getDocument().get("MRP").toString());
                    ProductCount.add(doc.getDocument().get("ProductCount").toString());
                    ProductDetails.add(doc.getDocument().get("ProductDetails").toString());
                    Image1.add(doc.getDocument().get("ProductImage1").toString());
                    Image2.add(doc.getDocument().get("ProductImage2").toString());
                    Image3.add(doc.getDocument().get("ProductImage3").toString());
                    total = total + Double.valueOf(doc.getDocument().get("TotalPrice").toString());
                    ProductMeasurement.add(doc.getDocument().get("ProductMeasurement").toString());
                    ProductOriginalPrice.add(doc.getDocument().get("ProductOriginalPrice").toString());
                    TotalPrice.add(doc.getDocument().get("TotalPrice").toString());

                    recyclerView.setLayoutManager(new LinearLayoutManager(checkOut.this));
                    cart = new Billing_cart(checkOut.this , MRP,ProductCount , ProductDetails , Image1,Image2,Image3,ProductMeasurement,ProductOriginalPrice,TotalPrice );
                    recyclerView.setAdapter(cart);

                }
                subtotal.setText("₹ " + total.toString());
                Est_tax.setText("₹ 10.0");
                delivery_charge.setText("₹ 40.0");
                Double finalTotal = 0.0;
                finalTotal = total + 10.0 + 40.0 ;
                Total.setText("₹ " + finalTotal.toString());
                ItemCount.setText(ProductDetails.size() + " Items in cart");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendNotification(String token, String title, String msg) {
        Data data = new Data(title,msg);
        NotificationSender notificationSender = new NotificationSender(data,token);

        ApiService apiService = Client.getRetrofit(fcmUrl).create(ApiService.class);

        apiService.sendNotification(notificationSender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}