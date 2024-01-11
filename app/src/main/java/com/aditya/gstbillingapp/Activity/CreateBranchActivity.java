package com.aditya.gstbillingapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.R;
import com.aditya.gstbillingapp.databinding.ActivityCreateBranchBinding;

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
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateBranchActivity extends AppCompatActivity {
    private ActivityCreateBranchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateBranchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       binding.buttonSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               createBranch();
           }
       });
    }

    private void createBranch()
    {
        JSONObject newBranch = new JSONObject();
        try {
            newBranch.put("BranchName", binding.editTextBranchName.getText().toString());
            newBranch.put("BranchAddress", binding.editTextBranchAddress.getText().toString());
            newBranch.put("GSTNo", binding.editTextGSTNo.getText().toString());
            newBranch.put("BranchManager", binding.editTextBranchManager.getText().toString());
            newBranch.put("BankAccountNo", binding.editTextBankAccountNo.getText().toString());
            newBranch.put("IFSCCode", binding.editTextIFSCCode.getText().toString());
            newBranch.put("BranchCity", binding.editTextBranchCity.getText().toString());

            JSONArray existingBranches = loadExistingBranches();
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

    private JSONArray loadExistingBranches() {
        String fileName = "branches.json";
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file,AppConfig.branches);
        File filePath = new File(branch,"branches.json");
        try {
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            Toast.makeText(CreateBranchActivity.this, "readed", Toast.LENGTH_SHORT).show();
            return new JSONArray(jsonString.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveBranchesToFile(String jsonString) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
            File branch = new File(file,AppConfig.branches);
            File filePath = new File(branch,"branches.json");
            if(!branch.exists())
            {
                branch.mkdir();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
            Toast.makeText(CreateBranchActivity.this, "branch created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}