package com.example.hackathonuser3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class Rescue extends AppCompatActivity implements LocationListener {


    StorageReference storageReference;
    DatabaseReference databaseReference;
    Uri uri = null;
    ImageView imageView;
    EditText title1,description1;
    protected LocationManager locationManager;
    String currentlocation = null;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        imageView = (ImageView) findViewById(R.id.image123);
        title1 = (EditText) findViewById(R.id.title123);
        description1 = (EditText) findViewById(R.id.description123);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AnimalEData");
        storageReference = FirebaseStorage.getInstance().getReference().child("Images");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
        {

            return;
        }

        locationManager.requestLocationUpdates
                (locationManager.GPS_PROVIDER,0,0,this);


    }

    public void Gallery(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,10);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10 && resultCode == RESULT_OK &&
        data != null && data.getData() != null)
        {

            try {

                uri = data.getData();


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    public void Submit(View view) {

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Uploading Data");
        progressDialog.show();
        final StorageReference filepath = storageReference.child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Uri download = uri;
                        HashMap<String,String> map = new HashMap<>();
                        map.put("title",title1.getText().toString().trim());
                        map.put("description",description1.getText().toString().trim());
                        map.put("image",download.toString().trim());
                        map.put("location",currentlocation);
                        databaseReference.push().setValue(map);
                        Toast.makeText(Rescue.this, "Success", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(Rescue.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        })
        ;

    }

    @Override
    public void onLocationChanged(Location location) {


        currentlocation = ""+location.getLatitude()+","+location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
