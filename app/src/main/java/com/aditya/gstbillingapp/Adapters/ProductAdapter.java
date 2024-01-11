package com.aditya.gstbillingapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.Activity.ViewProductActivity;
import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Model.Products;
import com.aditya.gstbillingapp.Model.User;
import com.aditya.gstbillingapp.R;
import com.aditya.gstbillingapp.ViewHolders.ProductViewHolder;
import com.aditya.gstbillingapp.ViewHolders.UserViewHolder;
import com.aditya.gstbillingapp.databinding.CustomerItemBinding;
import com.aditya.gstbillingapp.databinding.ItemEditProductBinding;
import com.aditya.gstbillingapp.databinding.ProductItemBinding;

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


public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private ArrayList<Products> productsList;
    private Activity activity;

    ProductAdapter(){

    }

    public ProductAdapter(ArrayList<Products> pList,Activity activity) {
        this.productsList = pList;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemBinding binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Products products = productsList.get(position);
        holder.bind(products);
        holder.binding.mProductDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDeleted(products.getProductName()))
                {
                    productsList.remove(position);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(v.getContext(), "product not found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.binding.mProductEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduct(products);
            }
        });
    }

    private void editProduct(Products products) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);

        ItemEditProductBinding binding = ItemEditProductBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();



        binding.editUpdatedProductName.setText(products.getProductName());
        binding.editUpdatedPrice.setText(products.getPrice());
        binding.editGovtNo.setText(products.getHSNSACno());

        binding.buttonUpdatedSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = binding.editUpdatedProductName.getText().toString().trim();
                String productPrice = binding.editUpdatedPrice.getText().toString().trim();
                String productSanNo = binding.editGovtNo.getText().toString().trim();
                if(productPrice.isEmpty() || productSanNo.isEmpty() || productName.isEmpty())
                {
                    Toast.makeText(v.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    Products updatedProduct = new Products(productName,productPrice,productSanNo);
                    updateProductInJson(products.getProductName(), updatedProduct);
                    notifyDataSetChanged();
//                    Intent intent = new Intent("com.aditya.gstbillingapp.REFRESH_ACTION");
//                    activity.sendBroadcast(intent);
                    activity.recreate();
                    dialog.dismiss();
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateProductInJson(String name, Products updatedProduct) {
        JSONArray existingProducts = loadExistingProducts();
        if (existingProducts != null) {
            for (int i = 0; i < existingProducts.length(); i++) {
                try {
                    JSONObject productObject = existingProducts.getJSONObject(i);
                    String productName = productObject.getString("productName");
                    if (productName.equals(name)) {
                        // Update the JSON object with new details
                        productObject.put("productName", updatedProduct.getProductName());
                        productObject.put("price", updatedProduct.getPrice());
                        productObject.put("HSNSACno", updatedProduct.getHSNSACno());

                        existingProducts.put(i, productObject);
                        saveProducts(existingProducts);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isUpdated(String name){
        JSONArray existingCustomers = loadExistingProducts();
        if (existingCustomers != null) {
            for (int i = 0; i < existingCustomers.length(); i++) {
                try {
                    JSONObject customerObject = existingCustomers.getJSONObject(i);
                    String productName1 = customerObject.getString("productName");
                    if(productName1.equals(name)){
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
    private boolean isDeleted(String productName) {
        JSONArray existingCustomers = loadExistingProducts();
        if (existingCustomers != null) {
            for (int i = 0; i < existingCustomers.length(); i++) {
                try {
                    JSONObject customerObject = existingCustomers.getJSONObject(i);
                    String productName1 = customerObject.getString("productName");
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
            //Toast.makeText(ViewProductActivity.this, "readed", Toast.LENGTH_SHORT).show();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveProducts(JSONArray products) {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file, AppConfig.products);
        File filePath = new File(branch, "products.json");
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(products.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
