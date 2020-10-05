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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    EditText remail12,rpass12,rmobile12,rname12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

            remail12 = (EditText) findViewById(R.id.remail1);
            rpass12 = (EditText) findViewById(R.id.rpassword1);
            rname12 = (EditText) findViewById(R.id.rname);
            rmobile12 = (EditText) findViewById(R.id.rmobile);
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo");
            progressDialog = new ProgressDialog(this);
    }

    public void Register(View view) {
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Trying To Register");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(remail12.getText().toString().trim(),
                rpass12.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    HashMap<String,String> map = new HashMap<>();
                    map.put("name",rname12.getText().toString().trim());
                    map.put("mobile",rmobile12.getText().toString().trim());
                    databaseReference.push().setValue(map);
                    Toast.makeText(Register.this, "Registration Successull", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Register.this, "Failed To Register", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}
