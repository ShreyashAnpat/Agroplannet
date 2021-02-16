package com.e.agroplannet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.agroplannet.R;
import com.e.agroplannet.productDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<String> Image1,Image2,Image3,productDetails,productMRP,productSellingPrice,productMeasurement,productType,AvailableProduct;
    Context Context ;
    LayoutInflater layoutInflater ;
    public ProductAdapter(Context context, List<String> image1, List<String> image2, List<String> image3, List<String> productDetails, List<String> productMRP, List<String> productSellingPrice, List<String> productMeasurement, List<String> productType, List<String> availableProduct) {
        this.Context = context ;
        this.layoutInflater = LayoutInflater.from(context);
        this.Image1 = image1 ;
        this.Image2 = image2 ;
        this.Image3 = image3 ;
        this.productDetails = productDetails ;
        this.productMeasurement = productMeasurement ;
        this.productMRP = productMRP ;
        this.productSellingPrice = productSellingPrice ;
        this.productType = productType ;
        this.AvailableProduct = availableProduct ;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(Image1.get(position)).into(holder.productImage);
        holder.productSellingPrice.setText("₹"+productSellingPrice.get(position));
        holder.Available.setText("Available :- " +AvailableProduct.get(position) +" " +productMeasurement.get(position) );
        holder.productMRP.setText("M.R.P. : ₹"+productMRP.get(position));
        holder.productDetails.setText(productDetails.get(position));
        Spannable spannable = (Spannable) holder.productMRP.getText() ;
        spannable.setSpan(new StrikethroughSpan(), 0,holder.productMRP.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Context ,productDetails.class);
                intent.putExtra("image1",Image1.get(position));
                intent.putExtra("image2",Image2.get(position));
                intent.putExtra("image3", Image3.get(position));
                intent.putExtra("details",productDetails.get(position));
                intent.putExtra("MRP",productMRP.get(position));
                intent.putExtra("sellingPrice" , productSellingPrice.get(position));
                intent.putExtra("available",AvailableProduct.get(position));
                intent.putExtra("measurement" , productMeasurement.get(position));
                Context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Image1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout ;
        ImageView productImage;
        TextView productDetails ,productSellingPrice,productMRP,Available;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image);
            productDetails = itemView.findViewById(R.id.productDetails);
            productMRP = itemView.findViewById(R.id.MRP);
            Available = itemView.findViewById(R.id.available);
            productSellingPrice = itemView.findViewById(R.id.productSellingPrice);
            linearLayout =itemView.findViewById(R.id.linearLayout);
        }
    }
}
