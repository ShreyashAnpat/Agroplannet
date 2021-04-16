package com.cctpl.agroplannet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.cctpl.agroplannet.ui.cart.CartFragment;

public class error extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Fragment fragment = new CartFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
    }
}