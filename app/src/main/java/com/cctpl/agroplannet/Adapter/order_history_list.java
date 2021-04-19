package com.cctpl.agroplannet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.agroplannet.History_order_details;
import com.cctpl.agroplannet.R;

import java.util.List;

public class order_history_list extends RecyclerView.Adapter<order_history_list.ViewHolder> {
    List<String> Order_Date ,Order_Time ,Total ,Total_Products , OrderID , status  , Order_activate ;
    Context context , mcontext ;
    LayoutInflater inflater ;

    public order_history_list(Context context, List<String> order_date, List<String> order_time, List<String> total, List<String> total_products, List<String> orderID, List<String> status, List<String> order_Activate) {
        this.context = context ;
        this.Order_Date = order_date ;
        this.Order_Time  = order_time ;
        this.Total = total ;
        this.Total_Products = total_products ;
        this.OrderID = orderID ;
        this.inflater = LayoutInflater.from(context);
        this.status = status ;
        this.Order_activate = order_Activate ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_list_card , parent ,false);
        mcontext = parent.getContext();
        return new order_history_list.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderID.setText("Order Total - "+ Total.get(position) + "");
        holder.Order_details.setText(Order_Date.get(position) +" , " + Total_Products.get(position) +" Products .");

        if (status.get(position).equals("2")){
            holder.status.setText("Delivered Order");
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.correct));
            holder.status.setTextColor(Color.parseColor("#03A561"));
        }
        if (Order_activate.get(position).equals("cancel")){
            holder.status.setText("Canceled Order");
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.cancel));
            holder.status.setTextColor(Color.parseColor("#FF2A00"));
        }


        holder.card_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context , History_order_details.class);
                intent.putExtra("DocID" , OrderID.get(position));
                intent.putExtra("status" , status.get(position));
                intent.putExtra("Order_activate" , Order_activate.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Order_Date.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderID , Order_details , status;
        ImageView icon ;
        ConstraintLayout card_Details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderId);
            Order_details = itemView.findViewById(R.id.details);
            status = itemView.findViewById(R.id.status);
            icon = itemView.findViewById(R.id.icon);
            card_Details = itemView.findViewById(R.id.card_Details);
        }
    }
}
