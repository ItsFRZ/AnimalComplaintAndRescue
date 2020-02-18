package com.example.hackathonuser3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    EditText email123,pass123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        email123 = (EditText) findViewById(R.id.loginemail1);
        pass123 = (EditText) findViewById(R.id.loginpassword1);

        progressDialog = new ProgressDialog(this);
    }

    public void Login(View view) {
        progressDialog.setTitle("Loading....");
        progressDialog.setMessage("Try To Login");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email123.getText().toString().trim(),
                pass123.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    public void Signup(View view) {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);

    }

    public void Forget(View view) {

        Intent intent = new Intent(getApplicationContext(),ForgetPassword.class);
        startActivity(intent);

    }
}
