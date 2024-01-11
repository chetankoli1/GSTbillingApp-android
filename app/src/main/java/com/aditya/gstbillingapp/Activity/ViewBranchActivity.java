package com.aditya.gstbillingapp.Activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.gstbillingapp.Adapters.UserAdapter;
import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Model.User;
import com.aditya.gstbillingapp.R;

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

public class ViewBranchActivity extends AppCompatActivity {
    private com.aditya.gstbillingapp.databinding.ActivityViewBranchBinding binding;
    private ArrayList<User> customerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = com.aditya.gstbillingapp.databinding.ActivityViewBranchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        customerList = new ArrayList<>();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadAndDisplayCustomers();

//        binding.fabAddBranch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ViewBranchActivity.this, CreateBranchActivity.class));
//            }
//        });
    }

    private JSONArray loadExistingProducts() {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file, AppConfig.customer);
        File filePath = new File(branch, "customer.json");
        try {
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            Toast.makeText(ViewBranchActivity.this, "readed", Toast.LENGTH_SHORT).show();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void loadAndDisplayCustomers() {
        binding.custemerRv.setHasFixedSize(true);
        JSONArray existingCustomers = loadExistingProducts();
        if (existingCustomers != null) {
            for (int i = 0; i < existingCustomers.length(); i++) {
                try {
                    JSONObject customerObject = existingCustomers.getJSONObject(i);
                    String name = customerObject.getString("custemerName");
                    String address = customerObject.getString("custemerAddress");
                    String phone = customerObject.getString("custemerPhone");
                    String gst = customerObject.getString("custemerGstNo");
                    String email = customerObject.getString("custemerEmail");

                    // Create a Customer object and add it to the list
                    User customer = new User(name, address, phone, gst, email);
                    customerList.add(customer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // Set the loaded customer list to the RecyclerView adapter
            UserAdapter adapter = new UserAdapter(customerList);
            binding.custemerRv.setAdapter(adapter);
        }
    }

}