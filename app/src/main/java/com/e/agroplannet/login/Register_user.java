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

public class Register_user extends AppCompatActivity {

    EditText name ,village , taluka,dist, near_by ;
    CountryCodePicker ccp ;
    EditText phonenumber ;
    Button getotp ;
    TextView login ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        name = findViewById(R.id.name);
        ccp = findViewById(R.id.ccp);
        phonenumber = findViewById(R.id.phone_number);
        ccp.registerCarrierNumberEditText(phonenumber);
        getotp = findViewById(R.id.verify);
        village = findViewById(R.id.velage);
        taluka = findViewById(R.id.taluka);
        dist = findViewById(R.id.district);
        near_by = findViewById(R.id.nearBy);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register_user.this , Login.class));
                finish();
            }
        });

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_user.this , Register_Otp.class);
                intent.putExtra("name" , name.getText().toString());
                intent.putExtra("add", village.getText().toString()+" "+taluka.getText().toString()+" "+dist.getText().toString()+" "+near_by.getText().toString());
                intent.putExtra("number",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
            }
        });

    }
}