package com.cctpl.agroplannet.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.cctpl.agroplannet.R;
import com.hbb20.CountryCodePicker;

import java.util.List;
import java.util.Locale;

public class Register_user extends AppCompatActivity implements LocationListener {

    EditText name ,address ;
    CountryCodePicker ccp ;
    EditText phonenumber ;
    Button getotp ;
    TextView login ;
    LocationManager locationManager ;
    ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        name = findViewById(R.id.name);
        ccp = findViewById(R.id.ccp);
        phonenumber = findViewById(R.id.phone_number);
        ccp.registerCarrierNumberEditText(phonenumber);
        getotp = findViewById(R.id.verify);
        address = findViewById(R.id.location);

//        pd = new ProgressDialog(this);
//        pd.setCanceledOnTouchOutside(false);
//        pd.setMessage("Adding Your Location");
//        pd.show();
        if (ContextCompat.checkSelfPermission(Register_user.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Register_user.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        getLocation();
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
                if (address.getText().toString().isEmpty()){
                    address.setError("Enter address");
                }else if (phonenumber.getText().toString().isEmpty()){
                    phonenumber.setError("Enter Phone Number");
                }
                else if (name.getText().toString().isEmpty()){
                    name.setError("Enter name");
                }
                else {
                    Intent intent = new Intent(Register_user.this , Register_Otp.class);
                    intent.putExtra("add" , address.getText().toString());
                    intent.putExtra("name" , name.getText().toString());
                    intent.putExtra("number",ccp.getFullNumberWithPlus().replace(" ",""));
                    startActivity(intent);
                }

            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {


        try {


            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,Register_user.this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(Register_user.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude() ,location.getLongitude(),1);
            String address_txt = addresses.get(0).getAddressLine(0);
            address.setText(address_txt);
            pd.cancel();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}