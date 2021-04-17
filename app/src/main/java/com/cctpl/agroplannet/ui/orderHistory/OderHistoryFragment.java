package com.cctpl.agroplannet.ui.orderHistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.agroplannet.Adapter.order_history_list;
import com.cctpl.agroplannet.Model.OrderData;
import com.cctpl.agroplannet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OderHistoryFragment extends Fragment {
    RecyclerView orderHistory  ;
    List<String> Order_Date ,Order_Time ,Total ,Total_Products , OrderID  , status , Order_Activate;
    order_history_list adapter ;
    FirebaseAuth auth ;
    FirebaseFirestore db ;
    LinearLayout first_Order ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view   =  inflater.inflate(R.layout.fragment_oder_history, container, false);
        orderHistory = view.findViewById(R.id.order_list);
        auth = FirebaseAuth.getInstance() ;
        db = FirebaseFirestore.getInstance() ;

        Order_Date = new ArrayList<>();
        Order_Time = new ArrayList<>();
        Total = new ArrayList<>();
        Total_Products = new ArrayList<>();
        OrderID = new ArrayList<>();
        status = new ArrayList<>();
        first_Order = view.findViewById(R.id.firstOrder);
        Order_Activate = new ArrayList<>();




        db.collection("Pending Order").whereEqualTo("UserId" , auth.getCurrentUser().getUid()).orderBy("TimeStamp" , Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()){
                    for (DocumentChange doc : task.getResult().getDocumentChanges()){
                        Order_Date.add(doc.getDocument().get("Order_Date").toString());
                        Order_Time.add(doc.getDocument().getString("Order_Time"));
                        Total.add(doc.getDocument().getString("Total"));
                        OrderID.add(doc.getDocument().getId());
                        Total_Products.add(doc.getDocument().get("Total_Product").toString());
                        status.add(doc.getDocument().get("Flag").toString());
                        Order_Activate.add(doc.getDocument().get("Status").toString());
                    }
                    orderHistory.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new order_history_list(getContext() , Order_Date ,Order_Time , Total , Total_Products ,OrderID , status , Order_Activate);
                    orderHistory.setAdapter(adapter);

                    if (Order_Date.size() == 0){
                        first_Order.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        return view ;
    }
}