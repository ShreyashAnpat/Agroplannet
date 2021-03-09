package com.cctpl.agroplannet;

import android.content.Intent;
import android.os.Bundle;


import com.cctpl.agroplannet.login.getUser;
import com.cctpl.agroplannet.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {
    ImageView logo ;
    TextView text ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        logo = findViewById(R.id.logo);
        text = findViewById(R.id.text);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Welcome.this, getUser.class));
                finish();
            }
        },4000);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
