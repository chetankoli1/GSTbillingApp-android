package com.aditya.gstbillingapp.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.gstbillingapp.Adapters.BillAdapter;
import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.R;
import com.aditya.gstbillingapp.databinding.ActivityViewAllBillsBinding;

import java.io.File;
import java.util.ArrayList;

public class ViewAllBillsActivity extends AppCompatActivity {
    private ActivityViewAllBillsBinding binding;
    private ArrayList<File> pdfFiles;
    private BillAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityViewAllBillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pdfFiles = new ArrayList<>();
        binding.pdfRv.setHasFixedSize(true);
        adapter = new BillAdapter(pdfFiles, ViewAllBillsActivity.this);

        binding.edtSearchBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                adapter.filter(query);
            }
        });

        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File branch = new File(file, AppConfig.Bills);

        if (branch.exists() && branch.isDirectory()) {
            File[] files = branch.listFiles();

            if (files != null) {
                for (File file1 : files) {
                    if (file1.isFile() && file1.getName().toLowerCase().endsWith(".pdf")) {
                        pdfFiles.add(file1);
                    }
                }
            }
        }
        binding.pdfRv.setAdapter(adapter);
    }
}