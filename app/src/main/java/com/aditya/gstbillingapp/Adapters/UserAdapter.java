package com.aditya.gstbillingapp.Adapters;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Model.User;
import com.aditya.gstbillingapp.ViewHolders.UserViewHolder;
import com.aditya.gstbillingapp.databinding.CustomerItemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private ArrayList<User> customerList;

    UserAdapter()
    {
    }

    public UserAdapter(ArrayList<User> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomerItemBinding binding = CustomerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User customer = customerList.get(position);
        holder.bind(customer);
        holder.binding.mUserDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDeleted(customer.getCustemerName()))
                {
                    customerList.remove(position);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(v.getContext(), "user not found", Toast.LENGTH_SHORT).show();
                }


                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    private boolean isDeleted(String productName) {
        JSONArray existingCustomers = loadExistingProducts();
        if (existingCustomers != null) {
            for (int i = 0; i < existingCustomers.length(); i++) {
                try {
                    JSONObject customerObject = existingCustomers.getJSONObject(i);
                    String productName1 = customerObject.getString("custemerName");
                    if(productName1.equals(productName)){
                        existingCustomers.remove(i);
                        saveProducts(existingCustomers);
                        return true;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private JSONArray loadExistingProducts() {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file,AppConfig.customer);
        File filePath = new File(branch,"customer.json");
        try {
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            //Toast.makeText(ViewProductActivity.this, "readed", Toast.LENGTH_SHORT).show();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void saveProducts(JSONArray products) {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file,AppConfig.customer);
        File filePath = new File(branch,"customer.json");
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(products.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
