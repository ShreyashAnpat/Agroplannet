package com.cctpl.agroplannet.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cctpl.agroplannet.NevigationActivity;

import com.cctpl.agroplannet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Register_Otp extends AppCompatActivity {

    Button mBtnVerifyOTP;
    EditText mOtp1, mOtp2, mOtp3,mOtp4 , mOtp5,mOtp6 ,OTP;
    String OtpId ,mPhoneNumber;
    FirebaseAuth mAuth;
    String add , name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__otp);
        mPhoneNumber = getIntent().getStringExtra("number").toString();
        mBtnVerifyOTP = findViewById(R.id.verify);
//        mOtp1 = findViewById(R.id.number1);
//        mOtp2 = findViewById(R.id.number2);
//        mOtp3 = findViewById(R.id.number3);
//        mOtp4 = findViewById(R.id.number4);
//        mOtp5 = findViewById(R.id.number5);
//        mOtp6 = findViewById(R.id.number6);

        OTP = findViewById(R.id.OTP);
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("fr");
        InitiateOtp(mPhoneNumber);

        add =  getIntent().getStringExtra("add");
        name = getIntent().getStringExtra("name");
        mPhoneNumber = getIntent().getStringExtra("number").toString();

        getSupportActionBar().hide();

        mBtnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amOtp = OTP.getText().toString();
                if (amOtp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                }else if (amOtp.length()!=6){
                    Toast.makeText(getApplicationContext(), "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpId,amOtp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }
    private void InitiateOtp(String mPhoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        OtpId = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(Register_Otp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth auth= FirebaseAuth.getInstance();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String , Object> info = new HashMap<>();
                            info.put("name" , name);
                            info.put("address " ,add);
                            info.put("phone_no", mPhoneNumber);
                            info.put("imgUrl", "https://firebasestorage.googleapis.com/v0/b/agrocart-f10b8.appspot.com/o/UserProfile%2Fprofile_agrocart.png?alt=media&token=6988be85-cf7a-4546-95e3-69cf385d5648");

                            db.collection("user").document(auth.getCurrentUser().getUid()).set(info) ;
                            Toast.makeText(getApplicationContext(),auth.getUid(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register_Otp.this, NevigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Register_Otp.this, "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }
}