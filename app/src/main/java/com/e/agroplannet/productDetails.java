package com.e.agroplannet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.agroplannet.Adapter.SliderAdapter;
import com.e.agroplannet.Adapter.productImageSlider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
    List<String>  Images ;
    ImageView  Add , subtract;
    TextView back;
    Integer count =1;
    TextView Product_details , Product_Price ,Product_MRP ,Available_Stock , Count ;
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

        Product_details.setText(getIntent().getStringExtra("details"));
        Product_MRP.setText("M.R.P. :- ₹ " + getIntent().getStringExtra("MRP"));
        Product_Price.setText("₹ "+ getIntent().getStringExtra("sellingPrice") );
        Available_Stock.setText("Available Stock :- " +getIntent().getStringExtra("available")+ " " +getIntent().getStringExtra("measurement"));

        Spannable spannable = (Spannable) Product_MRP.getText() ;
        spannable.setSpan(new StrikethroughSpan(), 0,Product_MRP.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Price = getIntent().getStringExtra("sellingPrice");

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Integer add = Integer.valueOf(Count.getText().toString()) +1;
             Count.setText(add.toString());
             Double price = Double.parseDouble(getIntent().getStringExtra("sellingPrice"))*Double.parseDouble(add.toString()) ;
             Product_Price.setText("₹ "+price.toString());
             Price = price.toString();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(productDetails.this , NevigationActivity.class));
                finish();
            }
        });

        addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").document().set(addProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.cancel();
                        startActivity(new Intent(productDetails.this,NevigationActivity.class));
                    }
                });

            }
        });
    }
}