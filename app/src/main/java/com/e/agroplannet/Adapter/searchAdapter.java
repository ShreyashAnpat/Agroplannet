package com.e.agroplannet.Adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.agroplannet.NevigationActivity;
import com.e.agroplannet.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {
    Context context ;
    List<String> Image1, details, sellingPrice , productMRP ;
    LayoutInflater inflater ;
    public searchAdapter(NevigationActivity nevigationActivity, List<String> image1, List<String> details, List<String> sellingPrice, List<String> productMRP) {
        this.context = nevigationActivity ;
        this.inflater = LayoutInflater.from(nevigationActivity);
        this.Image1 = image1 ;
        this.details = details ;
        this.sellingPrice = sellingPrice ;
        this.productMRP = productMRP ;
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
    }

    @Override
    public int getItemCount() {
        return Image1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ProductImage ;
        TextView Details, MRP , productSellingProduct ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductImage = itemView.findViewById(R.id.productImage);
            Details = itemView.findViewById(R.id.productDetails);
            MRP = itemView.findViewById(R.id.MRP);
            productSellingProduct = itemView.findViewById(R.id.sellingPrice);
        }
    }
}
