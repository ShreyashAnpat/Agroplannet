package com.e.agroplannet.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.e.agroplannet.R;
import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {

    CountryCodePicker ccp ;
    EditText phonenumber ;
    Button getotp ;
    TextView sign_up ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_up = findViewById(R.id.sign_up);
        ccp = findViewById(R.id.ccp);
        phonenumber = findViewById(R.id.number);
        ccp.registerCarrierNumberEditText(phonenumber);
        getotp = findViewById(R.id.getOtp);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this , Register_user.class));
                finish();
            }
        });
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , get_OTP.class);
                intent.putExtra("number",ccp.getFullNumberWithPlus().replace(" ",""));

                startActivity(intent);
                finish();
            }
        });


    }
}