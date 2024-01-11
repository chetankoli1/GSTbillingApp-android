package com.aditya.gstbillingapp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.gstbillingapp.Adapters.AddedProductsAdpater;
import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Helper.PermissionHelper;
import com.aditya.gstbillingapp.Model.MyProducts;
import com.aditya.gstbillingapp.Model.PdfView;
import com.aditya.gstbillingapp.Model.Products;
import com.aditya.gstbillingapp.Model.User;
import com.aditya.gstbillingapp.R;
import com.aditya.gstbillingapp.databinding.ActivityCreateNewBillBinding;
import com.aditya.gstbillingapp.databinding.LayoutAddedItemsBinding;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateNewBillActivity extends AppCompatActivity {
    private ActivityCreateNewBillBinding binding;
    List< Products> productsList;

    List<MyProducts> addedProductList;
    List<User> userList;
    User user;
    private PermissionHelper helper;

    private View progressBarView;

    private String billType = "INVOICE";

    private String selectedItem,selectedUser = "Select Customer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateNewBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productsList = new ArrayList<>();
        userList = new ArrayList<>();
        addedProductList = new ArrayList<>();
        user = new User();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createProgressScreen();
        inisiateProduct();
        inisiateUser();
        initHooksForClick();


    }

    private void initHooksForClick() {
        Activity activity = CreateNewBillActivity.this;
        binding.productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                // Toast.makeText(CreateNewBillActivity.this, "text"+selectedItem, Toast.LENGTH_SHORT).show();
                if(selectedItem.equals("Select Product")){
                    Toast.makeText(CreateNewBillActivity.this,"Invalid Product Selected",Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreateNewBillActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();

            }
        });
        binding.savedCustomerSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = parent.getItemAtPosition(position).toString();
                if(selectedUser.equals("Select Customer"))
                {
                    binding.editCustemerEmail.setEnabled(true);
                    binding.editCustemerEmail.setBackground(ContextCompat.getDrawable(CreateNewBillActivity.this,R.drawable.edittext_border));
                    binding.editCustemerName.setEnabled(true);
                    binding.editCustemerName.setBackground(ContextCompat.getDrawable(CreateNewBillActivity.this,R.drawable.edittext_border));
                    binding.editCustemerPhone.setEnabled(true);
                    binding.editCustemerPhone.setBackground(ContextCompat.getDrawable(CreateNewBillActivity.this,R.drawable.edittext_border));
                    binding.editCustemerAddr.setEnabled(true);
                    binding.editCustemerAddr.setBackground(ContextCompat.getDrawable(CreateNewBillActivity.this,R.drawable.edittext_border));
                    binding.editCustemerGSTno.setEnabled(true);
                    binding.editCustemerGSTno.setBackground(ContextCompat.getDrawable(CreateNewBillActivity.this,R.drawable.edittext_border));
                    Toast.makeText(CreateNewBillActivity.this,"Invalid User Selected",Toast.LENGTH_SHORT)
                            .show();
                }else{
                    binding.editCustemerEmail.setEnabled(false);
                    binding.editCustemerEmail.setBackgroundColor(ContextCompat.getColor(CreateNewBillActivity.this,R.color.gray));
                    binding.editCustemerName.setEnabled(false);
                    binding.editCustemerName.setBackgroundColor(ContextCompat.getColor(CreateNewBillActivity.this,R.color.gray));
                    binding.editCustemerPhone.setEnabled(false);
                    binding.editCustemerPhone.setBackgroundColor(ContextCompat.getColor(CreateNewBillActivity.this,R.color.gray));
                    binding.editCustemerAddr.setEnabled(false);
                    binding.editCustemerAddr.setBackgroundColor(ContextCompat.getColor(CreateNewBillActivity.this,R.color.gray));
                    binding.editCustemerGSTno.setEnabled(false);
                    binding.editCustemerGSTno.setBackgroundColor(ContextCompat.getColor(CreateNewBillActivity.this,R.color.gray));
                }
                for(int i = 0; i < userList.size(); i++)
                {
                    if(selectedUser.equals(userList.get(i).getCustemerName()))
                    {
                        user = userList.get(i);
                    }
                }

                //Toast.makeText(CreateNewBillActivity.this, "user: "+selectedUser, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.billTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(binding.invoiceRadioButton.isChecked())
                {
                    billType = "INVOICE";
                    Toast.makeText(CreateNewBillActivity.this, "invice", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    billType = "QUOTATION";
                    Toast.makeText(CreateNewBillActivity.this, "quatation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnGenerateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userList.size() != 0 && addedProductList.size() != 0)
                {
                    showProgressDialog();
                    String pdf = new PdfView().generatePdf(CreateNewBillActivity.this,
                            user.getCustemerName() + "_" + System.currentTimeMillis() + ".pdf",
                            addedProductList, user, billType);
                    File file = new File(pdf);
                    if (file.exists()) {
                        dismissProgressDialog(); // Dismiss the progress dialog

                        Uri uri = FileProvider.getUriForFile(CreateNewBillActivity.this, getApplicationContext().getPackageName() + ".provider", file.getAbsoluteFile());

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/pdf");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        if (intent.resolveActivity(getPackageManager()) == null) {
                            Toast.makeText(CreateNewBillActivity.this, "No PDF viewer found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dismissProgressDialog(); // Dismiss the progress dialog
                        Toast.makeText(CreateNewBillActivity.this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(CreateNewBillActivity.this, "tes"+user.getCustemerName(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateNewBillActivity.this, "Product or User are not selected!", Toast.LENGTH_SHORT).show();
                }



            }
        });
        binding.btnSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editCustemerName.getText().toString();
                String address = binding.editCustemerAddr.getText().toString();
                String phone = binding.editCustemerPhone.getText().toString();
                String gst = binding.editCustemerGSTno.getText().toString();
                String email = binding.editCustemerEmail.getText().toString();
                if(email.isEmpty() && address.isEmpty() && name.isEmpty() && phone.isEmpty() && gst.isEmpty())
                {
                    Toast.makeText(CreateNewBillActivity.this, "Some Fields are empty!", Toast.LENGTH_SHORT).show();
                }else {
                    inisiateUser();
                    loadExistingProducts();
                    saveCustemerInformation(name,address,phone,gst,email);
                    binding.editCustemerName.setText("");
                    binding.editCustemerAddr.setText("");
                    binding.editCustemerPhone.setText("");
                    binding.editCustemerGSTno.setText("");
                    binding.editCustemerEmail.setText("");
                    activity.recreate();
                }

            }
        });
        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quant = binding.edtProductQunt.getText().toString();


                if(quant.isEmpty())
                {
                    Toast.makeText(CreateNewBillActivity.this, "Enter Quantity", Toast.LENGTH_SHORT).show();
                }else {
                    for (int i = 0; i < productsList.size(); i++) {
                        Products p = productsList.get(i);
                        if (selectedItem.equals(p.getProductName())) {
                            MyProducts myproduct = new MyProducts(
                                    p.getProductName(), p.getPrice(), quant, p.getHSNSACno()
                            );
                            addedProductList.add(myproduct);
                            Toast.makeText(CreateNewBillActivity.this, "product added"+p.getProductName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        binding.btnViewAddedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddedProductsAdpater adapter = new AddedProductsAdpater((ArrayList<MyProducts>) addedProductList);
                LayoutAddedItemsBinding binding1 = LayoutAddedItemsBinding.inflate(getLayoutInflater());

                binding1.addedItemsRv.setAdapter(adapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewBillActivity.this)
                        .setView(binding1.getRoot());
                AlertDialog dialog = builder.create();
                dialog.show();

                builder.setCancelable(true);

                binding1.btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void saveCustemerInformation(String name, String address, String phone, String gst, String email) {

        JSONObject newBranch = new JSONObject();
        try {
            newBranch.put("custemerName", name);
            newBranch.put("custemerAddress", address);
            newBranch.put("custemerPhone", phone);
            newBranch.put("custemerGstNo", gst);
            newBranch.put("custemerEmail", email);


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
    void inisiateProduct(){
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file, AppConfig.products);
        File filePath = new File(branch, "products.json");


        List<String> productNamesList = new ArrayList<>();
        //List< Products> productsList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            String json = stringBuilder.toString();

            JSONArray jsonArray = new JSONArray(json);
            productNamesList.add("Select Product");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String productName = jsonObject.getString("productName");
                String productPrice = jsonObject.getString("price");
                String HSNSACno = jsonObject.getString("HSNSACno");

                Products pd = new Products();
                pd.setProductName(productName);
                pd.setPrice(productPrice);
                pd.setHSNSACno(HSNSACno);

                productNamesList.add(productName);
                productsList.add(pd);
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(CreateNewBillActivity.this, android.R.layout.simple_spinner_item, productNamesList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            binding.productSpinner.setAdapter(adapter);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    void inisiateUser()
    {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file, AppConfig.customer);
        File filePath = new File(branch, "customer.json");


        List<String> userNamesList = new ArrayList<>();
        //List< Products> productsList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            String json = stringBuilder.toString();

            JSONArray jsonArray = new JSONArray(json);

            userNamesList.add("Select Customer");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String custName = jsonObject.getString("custemerName");
                String custAddrs = jsonObject.getString("custemerAddress");
                String custPhone = jsonObject.getString("custemerPhone");
                String gstno = jsonObject.getString("custemerGstNo");
                String email = jsonObject.getString("custemerEmail");

                User u = new User();
                u.setCustemerName(custName);
                u.setCustemerAddress(custAddrs);
                u.setCustemerPhone(custPhone);
                u.setCustemerGstNo(gstno);
                u.setCustemerEmail(email);

                userNamesList.add(custName);
                userList.add(u);
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(CreateNewBillActivity.this, android.R.layout.simple_spinner_item, userNamesList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            binding.savedCustomerSpiner.setAdapter(adapter);
            binding.savedCustomerSpiner.setSelection(0);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
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
            Toast.makeText(CreateNewBillActivity.this, "readed", Toast.LENGTH_SHORT).show();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveBranchesToFile(String jsonString) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
            File branch = new File(file, AppConfig.customer);
            File filePath = new File(branch, "customer.json");
            if(!branch.exists())
            {
                branch.mkdir();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
            Toast.makeText(CreateNewBillActivity.this, "Custemer Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createProgressScreen()
    {
        LayoutInflater inflater = getLayoutInflater();
        progressBarView = inflater.inflate(R.layout.progress_dialog, null);
    }
    private void showProgressDialog() {
        addContentView(progressBarView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    // Method to dismiss the custom progress dialog
    private void dismissProgressDialog() {
        ((ViewGroup) progressBarView.getParent()).removeView(progressBarView);
    }
}