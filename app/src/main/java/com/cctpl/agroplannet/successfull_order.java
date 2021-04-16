package com.cctpl.agroplannet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cctpl.agroplannet.login.getUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Timer;
import java.util.TimerTask;

public class successfull_order extends AppCompatActivity {
    FirebaseAuth auth ;
    CardView done ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_order);
        done = findViewById(R.id.done);
        auth = FirebaseAuth.getInstance();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(successfull_order.this, NevigationActivity.class);
                intent.putExtra("flag" ,"0");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(successfull_order.this, NevigationActivity.class);
        intent.putExtra("flag" ,"0");
        startActivity(intent);
        finish();
    }
}