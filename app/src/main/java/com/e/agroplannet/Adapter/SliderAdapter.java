package com.e.agroplannet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.e.agroplannet.R;
import com.e.agroplannet.productDetails;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    List<Integer> image ;
    Context context ;
    public SliderAdapter(Context context, List<Integer> images) {
        this.context = context ;
        this.image = images ;
    }



    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        viewHolder.imageViewBackground.setImageResource(image.get(position));

    }

    @Override
    public int getCount() {
        return image.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}