package com.aditya.gstbillingapp.Helper;

import android.os.Environment;

import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Model.MyProducts;

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
import java.util.List;

public class BillSavingHeler {
    private JSONArray loadExistingBills() {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file, AppConfig.Bills);
        File filePath = new File(branch, "bills.json");
        try {
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveBillsToFile(String jsonString) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
            File branch = new File(file, AppConfig.Bills);
            File filePath = new File(branch, "bills.json");
            if (!branch.exists()) {
                branch.mkdir();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
          //  Toast.makeText(CreateNewBillActivity.this, "Bill Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCustemerInformation(String type, String inviceNo, String name, String address, String phone,
                                        String gst, String email, List<MyProducts> addedProductList,
                                        String totalAmt, String totalTax, String totalAmtWithTax) {
        JSONObject newBill = new JSONObject();
        try {
            newBill.put("invoiceNumber", inviceNo);
            newBill.put("billType", type);
            newBill.put("customerName", name);
            newBill.put("customerAddress", address);
            newBill.put("customerPhone", phone);
            newBill.put("customerGstNo", gst);
            newBill.put("customerEmail", email);

            JSONArray productsArray = new JSONArray();
            for (MyProducts product : addedProductList) {
                JSONObject productObj = new JSONObject();
                productObj.put("productName", product.getProductName());
                productObj.put("price", product.getProductPrice());
                productObj.put("quantity", product.getProductQuantity());
                productObj.put("hsnSacNo", product.getProductQuantity());
                productsArray.put(productObj);
            }
            newBill.put("products", productsArray);
            newBill.put("totalAmount", totalAmt);
            newBill.put("totalTax", totalTax);
            newBill.put("totalAmountWithTax", totalAmtWithTax);


            JSONArray existingBills = loadExistingBills();
            if (existingBills == null) {
                existingBills = new JSONArray();
            }
            existingBills.put(newBill);

            // Save JSON array of bills to internal storage
            saveBillsToFile(existingBills.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
