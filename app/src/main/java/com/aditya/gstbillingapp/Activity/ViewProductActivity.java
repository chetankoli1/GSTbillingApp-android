package com.aditya.gstbillingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.gstbillingapp.Adapters.ProductAdapter;
import com.aditya.gstbillingapp.Adapters.UserAdapter;
import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Helper.PermissionHelper;
import com.aditya.gstbillingapp.HomeActivity;
import com.aditya.gstbillingapp.Model.Products;
import com.aditya.gstbillingapp.Model.User;
import com.aditya.gstbillingapp.R;
import com.aditya.gstbillingapp.databinding.ActivityViewProductBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity {
    private ActivityViewProductBinding binding;
    private ArrayList<Products> productsList;

    PermissionHelper permissionHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityViewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productsList = new ArrayList<>();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadAndDisplayProducts();

        binding.fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProductActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
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
            Toast.makeText(ViewProductActivity.this, "readed", Toast.LENGTH_SHORT).show();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void loadAndDisplayProducts() {
        binding.productRv.setHasFixedSize(true);
        JSONArray existingCustomers = loadExistingProducts();
        if (existingCustomers != null) {
            for (int i = 0; i < existingCustomers.length(); i++) {
                try {
                    JSONObject customerObject = existingCustomers.getJSONObject(i);
                    String productName = customerObject.getString("productName");
                    String price = customerObject.getString("price");
                    String HSNSACno = customerObject.getString("HSNSACno");

                    Toast.makeText(ViewProductActivity.this,productName,Toast.LENGTH_SHORT).show();

                    // Create a Customer object and add it to the list

                    Products products = new Products(productName, price, HSNSACno);
                    productsList.add(products);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // Set the loaded customer list to the RecyclerView adapter
            ProductAdapter adapter = new ProductAdapter(productsList,ViewProductActivity.this);
            binding.productRv.setAdapter(adapter);
        }
    }

}