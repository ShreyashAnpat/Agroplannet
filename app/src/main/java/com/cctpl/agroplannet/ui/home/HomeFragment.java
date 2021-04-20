package com.cctpl.agroplannet.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cctpl.agroplannet.Adapter.ProductAdapter;
import com.cctpl.agroplannet.Adapter.SliderAdapter;

import com.cctpl.agroplannet.R;
import com.cctpl.agroplannet.Welcome;
import com.cctpl.agroplannet.ui.cart.CartFragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    SliderView sliderView ;
    List<Integer> images ;
    RecyclerView categoryList;
    ProductAdapter productAdapter;
    String[] category ;
    List<String> Image1,Image2,Image3,productDetails,productMRP,productSellingPrice,productMeasurement,productType,AvailableProduct;
    FirebaseFirestore db ;
    FirebaseAuth firebaseAuth;
    SwipeRefreshLayout refresh;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = root.findViewById(R.id.imageSlider);
        images = new ArrayList<>();
        images.add(R.drawable.farm);
        images.add(R.drawable.banner);
        images.add(R.drawable.pump);
        refresh = root.findViewById(R.id.refresh);
        categoryList = root.findViewById(R.id.categoryAdapter);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        SliderAdapter adapter = new SliderAdapter(root.getContext(),images);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);

        category= new String[]{"SPRAYER PUMPS" ,"FILMS" , "BAGS" ,"PLASTIC PRODUCTS" ,"PIPES" ,"ACCESSORIES"};

        FloatingActionButton cart = root.findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavController navController = Navigation.findNavController(root);;
                navController.navigate(R.id.nav_gallery);
            }
        });

        // Cart item count
        TextView itemCount = root.findViewById(R.id.Item_count);
        db.collection("user").document(firebaseAuth.getCurrentUser().getUid())
                .collection("UserCard").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    String  count = String.valueOf(value.size());
                    itemCount.setText(count);
                }
            }
        });

        Image1 = new ArrayList<>();
        Image2 = new ArrayList<>();
        Image3 = new ArrayList<>();
        productDetails = new ArrayList<>();
        productMeasurement = new ArrayList<>();
        productMRP = new ArrayList<>();
        productSellingPrice = new ArrayList<>();
        productType = new ArrayList<>();
        AvailableProduct = new ArrayList<>();

        db.collection("Product").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                Image1.clear();
//                Image2.clear();
//                Image3.clear();
//                productMRP.clear();
//                productType.clear();
//                productSellingPrice.clear();
//                AvailableProduct.clear();
//                productMeasurement.clear();
//                productDetails.clear();
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (DocumentChange.Type.ADDED == doc.getType() ){
                        Image1.add(doc.getDocument().get("Image1").toString());
                        Image2.add(doc.getDocument().get("Image2").toString());
                        Image3.add(doc.getDocument().get("Image3").toString());
                        productDetails.add(doc.getDocument().get("Product_Details").toString());
                        productMeasurement.add(doc.getDocument().get("Measurement").toString());
                        productMRP.add(doc.getDocument().get("MRP").toString());
                        productSellingPrice.add(doc.getDocument().get("Product_Selling_Price").toString());
                        productType.add(doc.getDocument().get("Product_Type").toString());
                        AvailableProduct.add(doc.getDocument().get("Available").toString());
                    }

                }

                categoryList.setLayoutManager(new GridLayoutManager(root.getContext(),2));
                productAdapter = new ProductAdapter(root.getContext(),Image1,Image2,Image3,productDetails,productMRP,productSellingPrice,productMeasurement,productType,AvailableProduct);

                categoryList.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
            }
        });


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                db.collection("Product").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        Image1.clear();
                        Image2.clear();
                        Image3.clear();
                        productMRP.clear();
                        productType.clear();
                        productSellingPrice.clear();
                        AvailableProduct.clear();
                        productMeasurement.clear();
                        productDetails.clear();
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (DocumentChange.Type.ADDED == doc.getType()){
                                Image1.add(doc.getDocument().get("Image1").toString());
                                Image2.add(doc.getDocument().get("Image2").toString());
                                Image3.add(doc.getDocument().get("Image3").toString());
                                productDetails.add(doc.getDocument().get("Product_Details").toString());
                                productMeasurement.add(doc.getDocument().get("Measurement").toString());
                                productMRP.add(doc.getDocument().get("MRP").toString());
                                productSellingPrice.add(doc.getDocument().get("Product_Selling_Price").toString());
                                productType.add(doc.getDocument().get("Product_Type").toString());
                                AvailableProduct.add(doc.getDocument().get("Available").toString());

                            }

                        }

                        categoryList.setLayoutManager(new GridLayoutManager(root.getContext(),2));
                        productAdapter = new ProductAdapter(root.getContext(),Image1,Image2,Image3,productDetails,productMRP,productSellingPrice,productMeasurement,productType,AvailableProduct);

                        categoryList.setAdapter(productAdapter);
                        refresh.setRefreshing(false);
                    }
                });

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
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.launcher)
                        .setTitle("Alert")
                        .setMessage("Are you want to close this app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               getActivity().finish();
                                getActivity().moveTaskToBack(true);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
