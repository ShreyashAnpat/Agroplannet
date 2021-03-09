package com.cctpl.agroplannet.Adapter;

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

import com.cctpl.agroplannet.NevigationActivity;
import com.cctpl.agroplannet.R;
import com.cctpl.agroplannet.productDetails;

import com.squareup.picasso.Picasso;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {
    Context context ;
    List<String> Image1, details, sellingPrice , productMRP ,productType,productMeasurement,AvailableProduct,Image2,Image3;
    LayoutInflater inflater ;
    public searchAdapter(NevigationActivity nevigationActivity, List<String> image1, List<String> details, List<String> sellingPrice, List<String> productMRP, List<String> productType, List<String> productMeasurement, List<String> availableProduct, List<String> image2, List<String> image3) {
        this.context = nevigationActivity ;
        this.inflater = LayoutInflater.from(nevigationActivity);
        this.Image1 = image1 ;
        this.details = details ;
        this.sellingPrice = sellingPrice ;
        this.productMRP = productMRP ;
        this.productMeasurement =productMeasurement ;
        this.productType = productType ;
        this.AvailableProduct = availableProduct ;
        this.Image2 = image2 ;
        this.Image3 = image3 ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_list,parent,false);
        return new searchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(Image1.get(position)).into(holder.ProductImage);
        holder.productSellingProduct.setText("₹" +sellingPrice.get(position));
        holder.Details.setText(details.get(position));
        holder.MRP.setText("M.R.P. :-₹" +productMRP.get(position));
        Spannable spannable = (Spannable) holder.MRP.getText() ;
        spannable.setSpan(new StrikethroughSpan(), 0,holder.MRP.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , productDetails.class);
                intent.putExtra("image1",Image1.get(position));
                intent.putExtra("image2",Image2.get(position));
                intent.putExtra("image3", Image3.get(position));
                intent.putExtra("details",details.get(position));
                intent.putExtra("MRP",productMRP.get(position));
                intent.putExtra("sellingPrice" , sellingPrice.get(position));
                intent.putExtra("available",AvailableProduct.get(position));
                intent.putExtra("measurement" , productMeasurement.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Image1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ProductImage ;
        LinearLayout linearLayout ;
        TextView Details, MRP , productSellingProduct ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            ProductImage = itemView.findViewById(R.id.productImage);
            Details = itemView.findViewById(R.id.productDetails);
            MRP = itemView.findViewById(R.id.MRP);
            productSellingProduct = itemView.findViewById(R.id.sellingPrice);
        }
    }
}
