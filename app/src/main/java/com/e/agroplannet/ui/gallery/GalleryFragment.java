package com.e.agroplannet.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.agroplannet.Adapter.cardAdapter;
import com.e.agroplannet.R;
import com.e.agroplannet.checkOut;
import com.e.agroplannet.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    RecyclerView cardList ;
    cardAdapter  cardAdapter ;
    FirebaseFirestore db ;
    FirebaseAuth auth;
    TextView SubTotal , VAT ,DeliveryCharge;
    CardView Secure_Checkout ;
    SwipeRefreshLayout refreshLayout ;
    List<String> Image1, ProductDetails , ProductPrice , ProductCount ,ProductMeasurement ,MRP,ProductSellingPrice;
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
        MRP = new ArrayList<>();
        Secure_Checkout = root.findViewById(R.id.Secure_Checkout);
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

                }
                cardList.setLayoutManager(new LinearLayoutManager(root.getContext()));
                cardAdapter = new cardAdapter(root.getContext(),Image1,ProductDetails,ProductMeasurement,ProductPrice,ProductCount ,MRP,ProductSellingPrice);
                cardList.setAdapter(cardAdapter);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Image1.clear();
                ProductCount.clear();
                ProductPrice.clear();
                ProductDetails.clear();
                ProductMeasurement.clear();
                MRP.clear();
                ProductSellingPrice.clear();

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
                        cardList.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        cardAdapter = new cardAdapter(root.getContext(),Image1,ProductDetails,ProductMeasurement,ProductPrice,ProductCount ,MRP,ProductSellingPrice);
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