package com.e.agroplannet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.e.agroplannet.R;
import com.e.agroplannet.productDetails;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class productImageSlider extends SliderViewAdapter<productImageSlider.SliderViewHolder> {

    List<String> images ;
    Context context ;
    public productImageSlider(productDetails productDetails, List<String> images) {
        this.context = productDetails ;
        this.images = images;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_slider, null);
        return new  productImageSlider.SliderViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        Picasso.get().load(images.get(position)).into(viewHolder.imageViewBackground);
        viewHolder.imageViewBackground.setMaxHeight(150);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    static class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;
        public SliderViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
