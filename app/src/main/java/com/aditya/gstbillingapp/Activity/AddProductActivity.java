package com.aditya.gstbillingapp.Activity;

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

import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Helper.PermissionHelper;
import com.aditya.gstbillingapp.MainActivity;
import com.aditya.gstbillingapp.R;
import com.aditya.gstbillingapp.databinding.ActivityAddProductBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AddProductActivity extends AppCompatActivity {
    private ActivityAddProductBinding binding;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        permissionHelper = new PermissionHelper();

        binding.buttonSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = binding.editProductName.getText().toString();
                String price = binding.editPrice.getText().toString();
                String HSNSACno= binding.editGovtNo.getText().toString();
                if(!permissionHelper.checkPermissions(AddProductActivity.this))
                {
                    permissionHelper.requestPermission(AddProductActivity.this);
                }else if(productName.isEmpty() && price.isEmpty() && HSNSACno.isEmpty())
                {
                    Toast.makeText(AddProductActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    createProduct();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionHelper.PERMISSION_REQUEST_CODE)
        {
            if(permissionHelper.checkPermissions(AddProductActivity.this))
            {
                Toast.makeText(AddProductActivity.this, "Permission  Granted", Toast.LENGTH_LONG).show();
               // binding.btnGeneratePdf.setVisibility(View.GONE);
            }
            else{
                Toast.makeText(AddProductActivity.this, "Permission Not Granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void createProduct()
    {
        JSONObject newBranch = new JSONObject();
        try {
            newBranch.put("productName", binding.editProductName.getText().toString());
            newBranch.put("price", binding.editPrice.getText().toString());
            newBranch.put("HSNSACno", binding.editGovtNo.getText().toString());


            JSONArray existingBranches = loadExistingProducts();
            if (existingBranches == null) {
                existingBranches = new JSONArray();
            }
            existingBranches.put(newBranch);

            // Save JSON array of branches to internal storage
            saveBranchesToFile(existingBranches.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray loadExistingProducts() {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file,AppConfig.products);
        File filePath = new File(branch,"products.json");
        try {
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            Toast.makeText(AddProductActivity.this, "readed", Toast.LENGTH_SHORT).show();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveBranchesToFile(String jsonString) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
            File branch = new File(file,AppConfig.products);
            File filePath = new File(branch,"products.json");
            if(!branch.exists())
            {
                branch.mkdir();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
            Toast.makeText(AddProductActivity.this, "Product created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}