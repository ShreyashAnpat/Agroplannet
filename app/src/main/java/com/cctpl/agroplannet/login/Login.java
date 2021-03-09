package com.cctpl.agroplannet.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.cctpl.agroplannet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    CountryCodePicker ccp ;
    EditText phonenumber ;
    Button getotp ;
    TextView sign_up ;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        sign_up = findViewById(R.id.sign_up);
        ccp = findViewById(R.id.ccp);
        phonenumber = findViewById(R.id.number);
        ccp.registerCarrierNumberEditText(phonenumber);
        getotp = findViewById(R.id.getOtp);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this , com.cctpl.agroplannet.login.Register_user.class));
                finish();
            }
        });

        List<String> no = new ArrayList<>();
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCanceledOnTouchOutside(false);

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                db.collection("user").whereEqualTo("phone_no",ccp.getFullNumberWithPlus().replace(" ","")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()){
                            no.add(doc.get("phone_no").toString());
                        }
                        if (phonenumber.getText().toString().isEmpty()){
                            phonenumber.setError("Enter Phone No");
                        }
                        if (no.size() == 0){
                            phonenumber.setError("Mobile number not register");
                        }
                        else {
                            Intent intent = new Intent(Login.this , get_OTP.class);
                            intent.putExtra("number",ccp.getFullNumberWithPlus().replace(" ",""));
                            startActivity(intent);
                            finish();
                        }
                        pd.cancel();

                    }
                });


            }
        });


    }
}