package com.anas.firebaseotp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPverify extends AppCompatActivity {

    TextInputEditText eOTP;
    AppCompatButton btnVerifyOTP;
    String phoneNumber,OTPid;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        eOTP=findViewById(R.id.eOTP);
        btnVerifyOTP=findViewById(R.id.btnVerifyOTP);

        phoneNumber= getIntent().getStringExtra("phone");

        mAuth = FirebaseAuth.getInstance();

        send_otp();

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eOTP.getText().toString().isEmpty()){
                    Toast.makeText(OTPverify.this, "Blank Otp", Toast.LENGTH_SHORT).show();
                } else if (eOTP.getText().toString().length() != 6) {
                    Toast.makeText(OTPverify.this, "Invalid Otp Count", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTPid,eOTP.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });


    }

    private void send_otp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                                        eOTP.setText(credential.getSmsCode());
                                        signInWithPhoneAuthCredential(credential);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(OTPverify.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {

                                        OTPid = verificationId;
                                    }
                                })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //method for verifying otp
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(OTPverify.this,HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(OTPverify.this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}