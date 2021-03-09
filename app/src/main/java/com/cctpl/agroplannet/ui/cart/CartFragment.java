package com.cctpl.agroplannet.ui.cart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cctpl.agroplannet.Adapter.cardAdapter;

import com.cctpl.agroplannet.R;
import com.cctpl.agroplannet.checkOut;
import com.cctpl.agroplannet.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    RecyclerView cardList ;
    cardAdapter  cardAdapter ;
    FirebaseFirestore db ;
    FirebaseAuth auth;
    TextView SubTotal , VAT ,DeliveryCharge ,textOrder;
    CardView Secure_Checkout ;
    SwipeRefreshLayout refreshLayout ;
    LinearLayout  noProduct;
    List<String> Image1, ProductDetails , ProductPrice , ProductCount ,ProductMeasurement ,MRP,ProductSellingPrice , AvailableProduct;
    ProgressDialog pd ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        cardList = root.findViewById(R.id.card_list);
        SubTotal = root.findViewById(R.id.SubCount);
        VAT = root.findViewById(R.id.textView8);
        refreshLayout = root.findViewById(R.id.refresh);
        DeliveryCharge = root.findViewById(R.id.textView10);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance() ;
        Image1 = new ArrayList<>();
        ProductCount = new ArrayList<>();
        ProductDetails = new ArrayList<>();
        ProductPrice = new ArrayList<>();
        ProductSellingPrice = new ArrayList<>();
        ProductMeasurement = new ArrayList<>();
        AvailableProduct = new ArrayList<>();
        MRP = new ArrayList<>();
        Secure_Checkout = root.findViewById(R.id.Secure_Checkout);
        noProduct = root.findViewById(R.id.noProduct);
        pd = new ProgressDialog(root.getContext());
        pd.setMessage("Loading Product");
        pd.setCanceledOnTouchOutside(false);
        pd.show();



        db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    Image1.add(doc.getDocument().get("ProductImage1").toString());
                    ProductCount.add(doc.getDocument().get("ProductCount").toString());
                    ProductPrice.add(doc.getDocument().get("TotalPrice").toString());
                    ProductDetails.add(doc.getDocument().get("ProductDetails").toString());
                    ProductMeasurement.add(doc.getDocument().get("ProductMeasurement").toString());
                    MRP.add(doc.getDocument().get("MRP").toString());
                    ProductSellingPrice.add(doc.getDocument().get("ProductOriginalPrice").toString());

                    db.collection("Product").whereEqualTo("Product_Details" ,doc.getDocument().get("ProductDetails").toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()){
                                db.collection("Product").document(doc.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            AvailableProduct.add(task.getResult().get("Available").toString());
                                    }
                                });
                            }
                        }
                    });

                }
                if (Image1.size()!=0){
                    noProduct.setVisibility(View.GONE);
                    Secure_Checkout.setVisibility(View.VISIBLE);

                }else {
                    noProduct.setVisibility(View.VISIBLE);
                    Secure_Checkout.setVisibility(View.INVISIBLE);

                }

                cardList.setLayoutManager(new LinearLayoutManager(root.getContext()));
                cardAdapter = new cardAdapter(root.getContext(),Image1,ProductDetails,ProductMeasurement,ProductPrice,ProductCount ,MRP,ProductSellingPrice,AvailableProduct);
                cardList.setAdapter(cardAdapter);
                pd.cancel();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                            if (doc.getDocument().get("Flag") =="1" ){
                                Image1.add(doc.getDocument().get("ProductImage1").toString());
                                ProductCount.add(doc.getDocument().get("ProductCount").toString());
                                ProductPrice.add(doc.getDocument().get("TotalPrice").toString());
                                ProductDetails.add(doc.getDocument().get("ProductDetails").toString());
                                ProductMeasurement.add(doc.getDocument().get("ProductMeasurement").toString());
                                MRP.add(doc.getDocument().get("MRP").toString());
                                ProductSellingPrice.add(doc.getDocument().get("ProductOriginalPrice").toString());
                            }

                        }
                        if (Image1.size()!=0){
                            noProduct.setVisibility(View.GONE);
                            Secure_Checkout.setVisibility(View.INVISIBLE);

                        }else {
                            noProduct.setVisibility(View.VISIBLE);
                            Secure_Checkout.setVisibility(View.GONE);

                        }
                        cardList.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        cardAdapter = new cardAdapter(root.getContext(),Image1,ProductDetails,ProductMeasurement,ProductPrice,ProductCount ,MRP,ProductSellingPrice, AvailableProduct);
                        cardList.setAdapter(cardAdapter);
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        Secure_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(), checkOut.class));
            }
        });

        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = new HomeFragment();
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }
}