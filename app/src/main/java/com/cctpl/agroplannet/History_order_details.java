package com.cctpl.agroplannet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctpl.agroplannet.Adapter.Order_details_adapter;
import com.cctpl.agroplannet.ui.cart.CartFragment;
import com.cctpl.agroplannet.ui.orderHistory.OderHistoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History_order_details extends AppCompatActivity {
    String orderID  , status ;
    RecyclerView recyclerView ;
    FirebaseAuth auth ;
    Order_details_adapter adapter ;
    List<String> Image , ProductDetails , ProductPrice , ProductCount , ProductMeasuriment , ProductSellingPrice;
    FirebaseFirestore db ;
    TextView subtotal , OrderDate ,OrderTime  ,ItemCount ,DeliveredOrder;
    CardView OrderInfo ;
    String Total , User ,Time , Date , PhoneNo;
    CardView Delete_order , cardView ;
    ImageView back ;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order_details);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        orderID = getIntent().getStringExtra("DocID");
        status = getIntent().getStringExtra("status");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance() ;

        recyclerView = findViewById(R.id.card_list);
        Image = new ArrayList<>();
        ProductDetails = new ArrayList<>();
        ProductPrice = new ArrayList<>();
        ProductCount = new ArrayList<>();
        ProductMeasuriment = new ArrayList<>();
        ProductSellingPrice =new ArrayList<>();
        subtotal = findViewById(R.id.textView12);
        OrderDate = findViewById(R.id.textView14);
        OrderTime = findViewById(R.id.textView16);
        back = findViewById(R.id.back);
        Delete_order = findViewById(R.id.Cancel_Order);
        DeliveredOrder = findViewById(R.id.place_order);
        cardView = findViewById(R.id.cardView);

        builder = new AlertDialog.Builder(getApplicationContext());

        db.collection("Pending Order").document(orderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    OrderTime.setText(task.getResult().getString("Order_Time"));
                    OrderDate.setText(task.getResult().getString("Order_Date"));
                    subtotal.setText(task.getResult().getString("Total"));

                }
            }
        });

        db.collection("Pending Order").document(orderID).collection("Order Product").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot doc : value.getDocuments()){
                    Image.add(doc.getString("Image1"));
                    ProductCount.add(doc.getString("ProductCount"));
                    ProductDetails.add(doc.getString("ProductDetails"));
                    ProductPrice.add(doc.getString("TotalPrice"));
                    ProductMeasuriment.add(doc.getString("ProductMeasurement"));
                    ProductSellingPrice.add(doc.getString("ProductOriginalPrice"));
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(History_order_details.this));
                adapter = new Order_details_adapter(History_order_details.this, Image , ProductCount , ProductDetails ,ProductPrice ,ProductMeasuriment,ProductSellingPrice );
                recyclerView.setAdapter(adapter);
            }
        });

        if (status.equals("2")){
                Delete_order.setVisibility(View.GONE);
                cardView.setMinimumWidth(200);
                DeliveredOrder.setText("Reorder");
        }

        Delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> cancelOrder = new HashMap<>();
                cancelOrder.put("Status" , "cancel");
                db.collection("Pending Order").document(orderID).update(cancelOrder);
                Fragment fragment = new OderHistoryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment ,fragment).addToBackStack(null).commit();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeliveredOrder.getText().toString().equals("Track Order")){
                    Snackbar.make(findViewById(android.R.id.content), "Coming Soon..!", Snackbar.LENGTH_LONG).show();
                }
                else {
                    db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                doc.getReference().delete();
                            }
                            db.collection("Pending Order").document(orderID).collection("Order Product").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for(DocumentSnapshot doc : value.getDocuments()){
                                        Image.add(doc.getString("Image1"));
                                        ProductCount.add(doc.getString("ProductCount"));
                                        ProductPrice.add(doc.getString("TotalPrice"));
                                        ProductMeasuriment.add(doc.getString("ProductMeasurement"));
                                        ProductSellingPrice.add(doc.getString("ProductOriginalPrice"));

                                        Map<String,Object> addProduct = new HashMap<>();
                                        addProduct.put("ProductDetails",doc.getString("ProductDetails") );
                                        addProduct.put("TotalPrice",doc.getString("TotalPrice"));
                                        addProduct.put("ProductCount" , doc.getString("ProductCount"));
                                        addProduct.put("ProductImage1" , doc.getString("Image1"));
                                        addProduct.put("ProductImage2", doc.getString("Image2"));
                                        addProduct.put("ProductImage3",doc.getString("Image3"));
                                        addProduct.put("ProductMeasurement", doc.getString("ProductMeasurement") );
                                        addProduct.put("ProductOriginalPrice",doc.getString("ProductOriginalPrice"));
                                        addProduct.put("MRP", doc.getString("MRP"));
                                        addProduct.put("Flag","1");
                                        db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").document().set(addProduct);

                                    }

                                    Intent intent = new Intent(History_order_details.this , NevigationActivity.class);
                                    intent.putExtra("flag" , "Open Cart");
                                    startActivity(intent);
                                }
                            });

                        }
                    });
                }
            }
        });
    }
}