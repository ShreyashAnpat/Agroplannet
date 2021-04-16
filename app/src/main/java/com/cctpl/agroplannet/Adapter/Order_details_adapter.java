package com.cctpl.agroplannet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cctpl.agroplannet.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Order_details_adapter extends RecyclerView.Adapter<Order_details_adapter.ViewHolder> {
    List<String> Image , ProductDetails , ProductPrice , ProductCount , ProductMeasurement , ProductSellingPrice;
    LayoutInflater inflater ;
    Context context ;

    public Order_details_adapter(Context applicationContext, List<String> image, List<String> productCount, List<String> productDetails, List<String> productPrice, List<String> productMeasuriment, List<String> productSellingPrice) {
        this.context = applicationContext ;
        this.inflater = LayoutInflater.from(context);
        this.Image = image ;
        this.ProductDetails = productDetails;
        this.ProductPrice = productPrice ;
        this.ProductCount = productCount ;
        this.ProductMeasurement = productMeasuriment;
        this.ProductSellingPrice = productSellingPrice ;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(com.cctpl.agroplannet.R.layout.product_cart,parent, false);
        return new Order_details_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ProductDetails.setText(ProductDetails.get(position));
        Picasso.get().load(Image.get(position)).into(holder.ProductImage);
        holder.ProductPrice.setText("₹ " +ProductPrice.get(position));
        holder.count.setText(ProductCount.get(position));
        holder.Price.setText("₹ " + ProductSellingPrice.get(position) +" / " + ProductMeasurement.get(position));
    }

    @Override
    public int getItemCount() {
        return ProductDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ProductImage ;
        TextView ProductDetails ,ProductPrice ,count , Price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductImage = itemView.findViewById(R.id.Product_Image);
            ProductDetails = itemView.findViewById(R.id.Details);
            ProductPrice = itemView.findViewById(R.id.Price);
            count = itemView.findViewById(R.id.count);
            Price = itemView.findViewById(R.id.MRP);
        }
    }
}
