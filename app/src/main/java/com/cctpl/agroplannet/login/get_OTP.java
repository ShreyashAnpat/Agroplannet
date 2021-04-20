package com.cctpl.agroplannet.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.cctpl.agroplannet.NevigationActivity;

import com.cctpl.agroplannet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;


import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class get_OTP extends AppCompatActivity {

    Button mBtnVerifyOTP;
    EditText mOtp1, mOtp2, mOtp3,mOtp4 , mOtp5,mOtp6  , OTP;
    String OtpId ,mPhoneNumber;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__o_t_p);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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

                        Toast.makeText(get_OTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallback
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(get_OTP.this, NevigationActivity.class);
                            intent.putExtra("flag" , "0");
                            startActivity(intent);
                            finish();
                        } else {
//                            Toast.makeText(get_OTP.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}