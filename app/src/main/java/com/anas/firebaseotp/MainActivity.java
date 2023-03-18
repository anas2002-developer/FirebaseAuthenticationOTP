package com.anas.firebaseotp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btnSendOTP;
    TextInputEditText ePhone;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnSendOTP=findViewById(R.id.btnSendOTP);
        ePhone=findViewById(R.id.ePhone);
        ccp=findViewById(R.id.countyCodePicker);

        //associates number with +91 country code in ccp variable
        ccp.registerCarrierNumberEditText(ePhone);

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,OTPverify.class);
                i.putExtra("phone",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(i);
            }
        });

    }
}