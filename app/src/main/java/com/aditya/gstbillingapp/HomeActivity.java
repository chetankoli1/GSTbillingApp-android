package com.aditya.gstbillingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.gstbillingapp.Activity.AddProductActivity;
import com.aditya.gstbillingapp.Activity.CreateNewBillActivity;
import com.aditya.gstbillingapp.Activity.ViewAllBillsActivity;
import com.aditya.gstbillingapp.Activity.ViewBranchActivity;
import com.aditya.gstbillingapp.Activity.ViewProductActivity;
import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Helper.PermissionHelper;
import com.aditya.gstbillingapp.databinding.ActivityHomeBinding;

import java.io.File;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private PermissionHelper permissionHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        permissionHelper = new PermissionHelper();

        if(!permissionHelper.checkPermissions(HomeActivity.this)) {
            permissionHelper.requestPermission(HomeActivity.this);
        }
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        if (!file.exists())
        {
            file.mkdir();
        }
        File products = new File(file, AppConfig.products);
        if (!products.exists())
        {
            products.mkdir();
        }

        File customer = new File(file, AppConfig.customer);
        if (!customer.exists())
        {
            customer.mkdir();
        }

        File bill = new File(file, AppConfig.Bills);
        if (!bill.exists())
        {
            bill.mkdir();
        }
        clickInit();
    }

    private void clickInit()
    {
       binding.cardCreateBill.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(HomeActivity.this, CreateNewBillActivity.class);
               startActivity(intent);
           }
       });

        binding.cardViewBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ViewAllBillsActivity.class);
                startActivity(intent);
            }
        });

        binding.cardCreateBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ViewBranchActivity.class);
                startActivity(intent);
            }
        });

        binding.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ViewProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionHelper.PERMISSION_REQUEST_CODE)
        {
            if(permissionHelper.checkPermissions(HomeActivity.this))
            {
                //Toast.makeText(HomeActivity.this, "Permission  Granted", Toast.LENGTH_LONG).show();
                // binding.btnGeneratePdf.setVisibility(View.GONE);
                File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
                if (!file.exists())
                {
                    file.mkdir();
                }
                File products = new File(file, AppConfig.products);
                if (!products.exists())
                {
                    products.mkdir();
                }

                File customer = new File(file, AppConfig.customer);
                if (!customer.exists())
                {
                    customer.mkdir();
                }

                File bill = new File(file, AppConfig.Bills);
                if (!bill.exists())
                {
                    bill.mkdir();
                }
            }
            else{
                finish();
                //Toast.makeText(HomeActivity.this, "Permission Not Granted", Toast.LENGTH_LONG).show();
            }
        }
    }
}