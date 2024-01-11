package com.aditya.gstbillingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aditya.gstbillingapp.Helper.FileHelper;
import com.aditya.gstbillingapp.Helper.PermissionHelper;
import com.aditya.gstbillingapp.Model.PdfView;
import com.aditya.gstbillingapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    PermissionHelper permissionHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        permissionHelper = new PermissionHelper();

        binding.btnGeneratePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!permissionHelper.checkPermissions(MainActivity.this))
                {
                    permissionHelper.requestPermission(MainActivity.this);
                }else{
                    FileHelper helper = new FileHelper();
                    boolean checkFolder = helper.createFolder(MainActivity.this);
                    if(!checkFolder)
                    {
                        Toast.makeText(MainActivity.this, "Folder Already Exists", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
               // new PdfView().generatePdf(MainActivity.this,"chetan.pdf");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionHelper.PERMISSION_REQUEST_CODE)
        {
            if(permissionHelper.checkPermissions(MainActivity.this))
            {
                Toast.makeText(MainActivity.this, "Permission  Granted", Toast.LENGTH_LONG).show();
                binding.btnGeneratePdf.setVisibility(View.GONE);
            }
            else{
                Toast.makeText(MainActivity.this, "Permission Not Granted", Toast.LENGTH_LONG).show();
            }
        }
    }
}