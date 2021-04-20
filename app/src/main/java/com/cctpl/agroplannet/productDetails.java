package com.cctpl.agroplannet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.agroplannet.Adapter.SliderAdapter;
import com.cctpl.agroplannet.Adapter.productImageSlider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class productDetails extends AppCompatActivity {
    SliderView sliderView ;
    productImageSlider  slider ;
    List<String>  Images , product ;
    ImageView  Add , subtract;
    ImageView back;
    Integer count =1;
    TextView Product_details , Product_Price ,Product_MRP ,Available_Stock , Count , Off,OutOffStock;
    Button addToCard ;
    FirebaseAuth auth ;
    FirebaseFirestore db ;
    String Price ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        sliderView = findViewById(R.id.imageSlider);
        Product_details = findViewById(R.id.textView);
        Product_MRP = findViewById(R.id.textView3);
        Product_Price = findViewById(R.id.textView2);
        Available_Stock = findViewById(R.id.textView6);
        addToCard = findViewById(R.id.add_to_card);
        Add = findViewById(R.id.add);
        subtract = findViewById(R.id.subtract);
        Count = findViewById(R.id.count);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        back = findViewById(R.id.back);
        Off = findViewById(R.id.Off) ;
        OutOffStock = findViewById(R.id.OutOffStock);
        Images = new ArrayList<>();
        Images.add(getIntent().getStringExtra("image1"));
        Images.add(getIntent().getStringExtra("image2"));
        Images.add(getIntent().getStringExtra("image3"));

        slider = new productImageSlider(productDetails.this ,Images);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setSliderAdapter(slider);
        sliderView.setScrollTimeInSec(5);
        sliderView.setAutoCycle(true);

        sliderView.isAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.BLACK);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        Product_details.setText(getIntent().getStringExtra("details"));
        Product_MRP.setText("M.R.P. :- ₹ " + getIntent().getStringExtra("MRP"));
        Off.setText("(" + String.format("%.1f",((( Double.parseDouble( getIntent().getStringExtra("MRP"))  - Double.parseDouble( getIntent().getStringExtra("sellingPrice")))/ Double.parseDouble( getIntent().getStringExtra("MRP")))*100.0) )  +"%) ");

        Product_Price.setText("₹ "+ getIntent().getStringExtra("sellingPrice") );
        Available_Stock.setText("Available Stock :- " +getIntent().getStringExtra("available")+ " " +getIntent().getStringExtra("measurement"));

        Spannable spannable = (Spannable) Product_MRP.getText() ;
        spannable.setSpan(new StrikethroughSpan(), 0,Product_MRP.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Price = getIntent().getStringExtra("sellingPrice");


        if (Integer.parseInt(getIntent().getStringExtra("available"))==0){
            OutOffStock.setVisibility(View.VISIBLE);
        }

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             Integer add = Integer.valueOf(Count.getText().toString()) +1;

                if (Integer.parseInt(getIntent().getStringExtra("available"))>=add){
                    Count.setText(add.toString());
                    Double price = Double.parseDouble(getIntent().getStringExtra("sellingPrice"))*Double.parseDouble(add.toString()) ;
                    Product_Price.setText("₹ "+price.toString());
                    Price = price.toString();
                    OutOffStock.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(productDetails.this, "Stock is not Available", Toast.LENGTH_SHORT).show();
                    OutOffStock.setVisibility(View.VISIBLE);
                }

            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(Count.getText().toString()) >1){
                    Integer add = Integer.valueOf(Count.getText().toString()) -1;
                    Count.setText(add.toString());
                    Double price =( Double.parseDouble(getIntent().getStringExtra("sellingPrice")) * Double.parseDouble(add.toString()));
                    Product_Price.setText("₹ "+price.toString());
                    Price = price.toString();
                }
            }
        });

        product = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetails.super.onBackPressed();
            }
        });

        addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt( Count.getText().toString()) <= Integer.parseInt(getIntent().getStringExtra("available"))){
                    OutOffStock.setVisibility(View.GONE);
                    ProgressDialog pd = new ProgressDialog(productDetails.this);
                    pd.setMessage("Adding Product");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    String total = Product_Price.getText().toString() ;
                    total.replace("₹","");
                    Map<String,Object> addProduct = new HashMap<>();
                    addProduct.put("ProductDetails",Product_details.getText().toString() );
                    addProduct.put("TotalPrice",Price);
                    addProduct.put("ProductCount" , Count.getText().toString());
                    addProduct.put("ProductImage1" , getIntent().getStringExtra("image1"));
                    addProduct.put("ProductImage2",getIntent().getStringExtra("image2"));
                    addProduct.put("ProductImage3",getIntent().getStringExtra("image3"));
                    addProduct.put("ProductMeasurement", getIntent().getStringExtra("measurement") );
                    addProduct.put("ProductOriginalPrice",getIntent().getStringExtra("sellingPrice"));
                    addProduct.put("MRP", getIntent().getStringExtra("MRP"));
                    addProduct.put("Flag","1");

                    db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").whereEqualTo("ProductDetails",Product_details.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()){
                                product.add(doc.get("ProductDetails").toString());
                            }
                            if (product.size() == 0){
                                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").document().set(addProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.cancel();
                                        Toast to = Toast.makeText(productDetails.this, "Product Add Successfully", Toast.LENGTH_SHORT);
                                        to.setGravity(Gravity.CENTER,0,0);
                                        to.show();
                                        Intent intent = new Intent(productDetails.this,NevigationActivity.class);
                                        intent.putExtra("flag" ,"0");
                                        startActivity(intent);
                                    }
                                });
                            }
                            else {
                                pd.cancel();
                                Toast.makeText(productDetails.this, "Product Is available in cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    OutOffStock.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}