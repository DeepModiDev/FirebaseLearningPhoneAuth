package com.deepmodi.firebaselearningphoneauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deepmodi.firebaselearningphoneauth.Common.Common;
import com.deepmodi.firebaselearningphoneauth.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class ActivityUserSignUp extends AppCompatActivity {

    EditText user_register_name,user_register_number,user_register_password;
    Button btn_continue,btn_login;
    FirebaseDatabase database;
    DatabaseReference table_user;
    String mobileNumber,password,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        Paper.init(this);

        user_register_name = (EditText)findViewById(R.id.user_register_edit_name);
        user_register_number = (EditText)findViewById(R.id.user_register_edit_number);
        user_register_password = (EditText)findViewById(R.id.user_register_edit_password);

        database  = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");


        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityUserSignUp.this,MainActivity.class));
                finish();
            }
        });

        btn_continue = (Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.InternetCheckSystem(getBaseContext())) {
                    mobileNumber = user_register_number.getText().toString();
                    password = user_register_password.getText().toString();
                    userName = user_register_name.getText().toString();

                    Paper.book().write(Common.USER_PHONE_NUMBER, mobileNumber);
                    Paper.book().write(Common.USER_PASSWORD, password);


                    if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {
                        user_register_number.setError("Enter valid number");
                        user_register_number.requestFocus();
                    } else if (password.isEmpty() || password.length() < 6) {
                        user_register_password.setError("Password length must be greater than 6.");
                        user_register_password.requestFocus();
                    } else if (userName.isEmpty()) {
                        user_register_name.setError("Please enter name");
                        user_register_name.requestFocus();
                    } else {
                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = new User(userName, mobileNumber, password);
                                table_user.child(mobileNumber).setValue(user);
                                Intent intent = new Intent(ActivityUserSignUp.this,ActivityOtpRegister.class);
                                intent.putExtra("mobile",mobileNumber);//this is very important line
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(ActivityUserSignUp.this, "No internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
