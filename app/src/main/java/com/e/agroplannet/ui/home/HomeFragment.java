package com.e.agroplannet.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Int2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.agroplannet.Adapter.ProductAdapter;
import com.e.agroplannet.Adapter.SliderAdapter;
import com.e.agroplannet.R;
import com.e.agroplannet.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    SwipeRefreshLayout refresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = root.findViewById(R.id.imageSlider);
        images = new ArrayList<>();
        images.add(R.drawable.farm);
        images.add(R.drawable.farm1);
        images.add(R.drawable.pump);
        refresh = root.findViewById(R.id.refresh);
        categoryList = root.findViewById(R.id.categoryAdapter);
        db = FirebaseFirestore.getInstance();

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
                Fragment fragment = new GalleryFragment();
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
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
                for (DocumentChange doc : value.getDocumentChanges()){
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
                categoryList.setLayoutManager(new GridLayoutManager(root.getContext(),2));
                productAdapter = new ProductAdapter(root.getContext(),Image1,Image2,Image3,productDetails,productMRP,productSellingPrice,productMeasurement,productType,AvailableProduct);
                categoryList.setAdapter(productAdapter);
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
}