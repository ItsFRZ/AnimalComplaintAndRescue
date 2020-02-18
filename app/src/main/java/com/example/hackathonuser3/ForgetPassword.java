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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText femail1;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        femail1 = (EditText) findViewById(R.id.forgetemail1);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void Login(View view) {

        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);

    }

    public void Submit(View view) {

        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Sending Mail....");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(femail1.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                    Toast.makeText(ForgetPassword.this, "Check Your Mail Or Spam Box", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ForgetPassword.this, "Failed To Send Mail", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}
