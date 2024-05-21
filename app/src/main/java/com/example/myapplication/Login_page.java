package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_page extends AppCompatActivity {

    EditText email, pass;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email = findViewById(R.id.text1);
        pass = findViewById(R.id.text2);
        auth = FirebaseAuth.getInstance();
    }
    public  void  open1(View view){
        String email1 = email.getText().toString();
        String pass1 = pass.getText().toString();
        if(TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1)){
            Toast.makeText(this, "Please fill all the sections", Toast.LENGTH_SHORT).show();
        }
        else {
            regi(email1,pass1);
        }
    }
    private void regi(String email1, String pass1) {
        auth.signInWithEmailAndPassword(email1,pass1).addOnSuccessListener(Login_page.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(Login_page.this, Photo_page.class);
                startActivity(intent);
            }
        });
    }
}