package com.e.agroplannet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.agroplannet.R;
import com.e.agroplannet.checkOut;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Properties;

public class Billing_cart extends RecyclerView.Adapter<Billing_cart.ViewHolder> {

    List<String>  MRP,ProductCount , ProductDetails , Image1,Image2,Image3,ProductMeasurement,ProductOriginalPrice,TotalPrice ;
    Context context ;
    LayoutInflater inflater ;


    public Billing_cart(checkOut checkOut, List<String> mrp, List<String> productCount, List<String> productDetails, List<String> image1, List<String> image2, List<String> image3, List<String> productMeasurement, List<String> productOriginalPrice, List<String> totalPrice) {
        this.context = checkOut ;
        this.inflater = LayoutInflater.from(context);
        this.MRP = mrp ;
        this.ProductCount =productCount ;
        this.ProductDetails = productDetails ;
        this.Image1 = image1;
        this.Image2 = image2 ;
        this.Image3 = image3 ;
        this.ProductMeasurement = productMeasurement ;
        this.ProductOriginalPrice = productOriginalPrice ;
        this.TotalPrice = totalPrice ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.billing_cart, parent, false) ;
        return new Billing_cart.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ProductDetails.setText(ProductDetails.get(position));
        Picasso.get().load(Image1.get(position)).into(holder.ProductImage);
        holder.ProductPrice.setText("₹ " +TotalPrice.get(position));
        holder.count.setText(ProductCount.get(position));
        holder.Price.setText("₹ " + ProductOriginalPrice.get(position) +" / " + ProductMeasurement.get(position));
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
