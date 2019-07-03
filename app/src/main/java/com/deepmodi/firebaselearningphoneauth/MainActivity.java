package com.deepmodi.firebaselearningphoneauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class MainActivity extends AppCompatActivity {

    EditText user_number_edit,user_password_edit;
    Button btn_register_user,btn_sign_in_user;
    CheckBox checkBox;
    String userNumber;
    String userPassword;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        user_number_edit = (EditText)findViewById(R.id.user_number_edit);
        user_password_edit = (EditText)findViewById(R.id.user_password_edit);

        btn_register_user = (Button)findViewById(R.id.user_btn_sign_up);
        btn_sign_in_user = (Button)findViewById(R.id.user_btn_sign_in);

        checkBox = (CheckBox)findViewById(R.id.ic_checkBox);

        //Automatic check for Login
        //check remember
        String user = Paper.book().read(Common.USER_PHONE_NUMBER);
        String pwd = Paper.book().read(Common.USER_PASSWORD);
        if (user != null && pwd != null) {
            if (!user.isEmpty() && !pwd.isEmpty()) {
                login(user, pwd);
                user_number_edit.setText(user);
                user_password_edit.setText(pwd);
            }
        }

        btn_register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ActivityUserSignUp.class));
                Toast.makeText(MainActivity.this, "Register new user", Toast.LENGTH_SHORT).show();
            }
        });

        btn_sign_in_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(checkBox.isChecked())
                        {
                            Paper.book().write(Common.USER_PHONE_NUMBER,user_number_edit.getText().toString());
                            Paper.book().write(Common.USER_PASSWORD,user_password_edit.getText().toString());
                        }
                        login(user_number_edit.getText().toString(),user_password_edit.getText().toString());
            }
        });
    }


    private void login(final String phone, final String pwd) {
        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        if (Common.InternetCheckSystem(getBaseContext())) {

            //Init Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = database.getReference("Users");


            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(phone).exists()) {
                        //Check whether user Exist or not
                        mDialog.dismiss();
                        //Get user information
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        if (user.getUserPassword().equals(pwd)) {
                            Toast.makeText(MainActivity.this, "Sign in Sucessfully!!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(MainActivity.this, "Please enter the valid password or number.", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Please Register First.", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
