package com.aditya.gstbillingapp.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        adapter.notifyDataSetChanged();
        binding.edtSearchBill.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    searchFile(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Not needed in this case
                }
            });

        binding.pdfRv.setAdapter(adapter);
    }

    private void searchFile(CharSequence charSequence) {
        ArrayList<File> searchList = new ArrayList<>();

        for (File file : pdfFiles) {
            if (file.getName().toLowerCase().contains(charSequence) ||
                    getFormattedDate(file.lastModified()).toLowerCase().contains(charSequence)) {
                searchList.add(file);
            }
        }

        if (searchList.isEmpty()) {
            binding.pdfRv.setVisibility(View.GONE);
            binding.txtNotFound.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            binding.pdfRv.setVisibility(View.VISIBLE);
            binding.txtNotFound.setVisibility(View.GONE);
            adapter.setSearch(searchList);
        }
    }

    private String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}