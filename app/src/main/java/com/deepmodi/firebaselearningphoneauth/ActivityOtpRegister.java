package com.deepmodi.firebaselearningphoneauth;

import android.arch.core.executor.TaskExecutor;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityOtpRegister extends AppCompatActivity {

    EditText editText_otp;
    Button btn_register_final;
    private String mVerificationId;
    private String mobile;

    //Firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);

        mAuth = FirebaseAuth.getInstance();

        editText_otp = (EditText)findViewById(R.id.enit_text_otp);
        btn_register_final = (Button)findViewById(R.id.btn_register_final);

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        btn_register_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText_otp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editText_otp.setError("Enter valid code");
                    editText_otp.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verificationCode(code);
            }
        });
    }

    private void sendVerificationCode(String mobile) {
     PhoneAuthProvider.getInstance().verifyPhoneNumber(
             "+"+91+mobile,
             60,
             TimeUnit.SECONDS,
             TaskExecutors.MAIN_THREAD,
             mCallbacks
     );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code by sms
            String code = phoneAuthCredential.getSmsCode();
            if (code != null)
            {
                editText_otp.setText(code);
                verificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ActivityOtpRegister.this, "Verification failed.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };

    private void verificationCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        SignInWithPhoneAuthCredential(credential);
    }

    private  void SignInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ActivityOtpRegister.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ActivityOtpRegister.this,HomeActivity.class));
                        }
                        else
                        {
                            String message  = "Somthing is wrong,you entered wrong otp.";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(ActivityOtpRegister.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
