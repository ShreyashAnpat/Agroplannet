package com.cctpl.agroplannet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
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
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_order);
        done = findViewById(R.id.done);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.tone);
        mp.start();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot delete : queryDocumentSnapshots.getDocuments())
                            delete.getReference().delete();
                    }
                });
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
        db.collection("user").document(auth.getCurrentUser().getUid()).collection("UserCard").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot delete : queryDocumentSnapshots.getDocuments())
                    delete.getReference().delete();
            }
        });
        Intent intent = new Intent(successfull_order.this, NevigationActivity.class);
        intent.putExtra("flag" ,"0");
        startActivity(intent);
        finish();
    }
}