package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_page extends AppCompatActivity {

    EditText email , pass , repass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        email = findViewById(R.id.text1);
        pass = findViewById(R.id.text2);
        repass = findViewById(R.id.text3);
        auth = FirebaseAuth.getInstance();
    }
    public void open(View view){
        String email1 = email.getText().toString();
        String pass1 = pass.getText().toString();
        String repass1 = repass.getText().toString();
        if(pass1.equals(repass1)){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1) || TextUtils.isEmpty(repass1)){
            Toast.makeText(this, "Please fill all the Section", Toast.LENGTH_SHORT).show();
        }
        else {
            regi(email1,pass1);
        }
    }
    private void regi(String email1, String pass1) {
        auth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(Register_page.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Register_page.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Register_page.this, "Register Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void open2(View view){
        Intent intent = new Intent(Register_page.this, Login_page.class);
        startActivity(intent);
    }
}