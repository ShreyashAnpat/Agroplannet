package com.e.agroplannet.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.e.agroplannet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.ViewHolder> {
    List<String> Image1,MRP, ProductDetails , ProductPrice , ProductCount ,ProductMeasurement , ProductSellingPrice;
    Context context ;
    LayoutInflater  inflater ;
    FirebaseAuth auth;
    FirebaseFirestore db ;

    public cardAdapter(Context context, List<String> image1, List<String> productDetails, List<String> productMeasurement, List<String> productPrice, List<String> productCount, List<String> MRP, List<String> productSellingPrice) {
        this.context = context ;
        this.inflater = LayoutInflater.from(context);
        this.Image1 = image1 ;
        this.ProductCount = productCount ;
        this.ProductDetails = productDetails ;
        this.ProductMeasurement = productMeasurement ;
        this.ProductPrice = productPrice ;
        this.MRP = MRP ;
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        ProgressDialog db = new ProgressDialog(context);
        this.ProductSellingPrice = productSellingPrice ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_product,parent,false);
        return new cardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(Image1.get(position)).into(holder.productImage);
        holder.productDetails.setText(ProductDetails.get(position));
        holder.price.setText( "₹ "+ProductPrice.get(position));
        holder.Amount.setText( ProductCount.get(position));
        holder.MRP.setText("M.R.P. - ₹ "+ MRP.get(position));
        holder.Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer add = Integer.valueOf(holder.Amount.getText().toString()) +1;
                holder.Amount.setText(add.toString());
                Double price = Double.parseDouble(ProductSellingPrice.get(position))*Double.parseDouble(add.toString()) ;
                holder.price.setText("₹ "+price.toString());
                Map<String , Object> count = new HashMap<>();
                count.put("ProductCount" , add.toString());
                count.put("TotalPrice" ,price.toString());
                ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("Updating");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").whereEqualTo("ProductDetails", ProductDetails.get(position)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                            db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").document(doc.getDocument().getId()).update(count).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.cancel();
                                }
                            });
                        }
                    }
                });

            }
        });

        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Integer.valueOf(holder.Amount.getText().toString())>1){
                    Integer add = Integer.valueOf(holder.Amount.getText().toString()) - 1;
                    holder.Amount.setText(add.toString());
                    Double price = Double.parseDouble(ProductSellingPrice.get(position))*Double.parseDouble(add.toString()) ;
                    holder.price.setText("₹ "+price.toString());
                    ProgressDialog pd = new ProgressDialog(context);
                    Map<String , Object> count1 = new HashMap<>();
                    count1.put("ProductCount" , add.toString());
                    count1.put("TotalPrice" ,price.toString());
                    pd.setMessage("Updating");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").whereEqualTo("ProductDetails", ProductDetails.get(position)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").document(doc.getDocument().getId()).update(count1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.cancel();
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("Product Removed ");
                pd.show();

                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").whereEqualTo("ProductDetails", ProductDetails.get(position)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                            if (doc.getType()== DocumentChange.Type.ADDED ){
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, Image1.size());
                                Image1.remove(position);
                                MRP.remove(position);
                                ProductCount.remove(position);
                                ProductMeasurement.remove(position);
                                ProductPrice.remove(position);
                                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").document(doc.getDocument().getId()).delete() ;
                                pd.cancel();
                            }

                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return Image1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage  , delete ,Add , subtract;
        TextView productDetails , price , MRP , Amount ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            delete = itemView.findViewById(R.id.Delete);
            productDetails = itemView.findViewById(R.id.productDetails);
            price = itemView.findViewById(R.id.productPrice);
            Amount = itemView.findViewById(R.id.Amount);
            MRP = itemView.findViewById(R.id.MRP);
            Add = itemView.findViewById(R.id.AddCount);
            subtract = itemView.findViewById(R.id.subtract);
        }
    }
}
