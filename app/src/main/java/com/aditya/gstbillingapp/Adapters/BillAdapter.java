package com.aditya.gstbillingapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.Activity.CreateNewBillActivity;
import com.aditya.gstbillingapp.ViewHolders.BillViewHolder;
import com.aditya.gstbillingapp.databinding.PdfItemBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {
    private ArrayList<File> pdfFiles;

    private ArrayList<File> filteredFiles;
    private Activity context;

    public BillAdapter(ArrayList<File> pdfFiles, Activity context) {
        this.pdfFiles = pdfFiles;
        this.filteredFiles = new ArrayList<>(pdfFiles);
        this.context = context;
    }

    public BillAdapter() {

    }
    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PdfItemBinding binding = PdfItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BillViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, @SuppressLint("RecyclerView") int position) {
        File file = pdfFiles.get(position);
        holder.binding.pdfTitle.setText(file.getName());
        holder.binding.pdfDate.setText(getFormattedDate(file.lastModified()));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",
                        file.getAbsoluteFile());;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.binding.mPdfDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = pdfFiles.get(position);
                if (file.exists()) {
                    boolean isDeleted = file.delete();
                    if (isDeleted) {
                        pdfFiles.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, pdfFiles.size());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    private String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public void filter(String query) {
        query = query.toLowerCase().trim();
        filteredFiles.clear(); // Clear the existing filtered data

        if (query.isEmpty()) {
            filteredFiles.addAll(pdfFiles); // If query is empty, show all data
        } else {
            for (File file : pdfFiles) {
                // Filter logic based on title and date
                String fileName = file.getName().toLowerCase();
                long fileLastModified = file.lastModified(); // Get the last modified date of the file

                // Match based on title or part of the title
                if (fileName.contains(query)) {
                    filteredFiles.add(file);
                } else {
                    // You can add additional date filtering logic here if needed
                    // For instance, compare fileLastModified with a date range
                    // For simplicity, let's assume a date filter example based on the last 7 days
                    long lastSevenDaysInMillis = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000); // Calculate time in milliseconds for the last 7 days

                    if (fileLastModified >= lastSevenDaysInMillis) {
                        filteredFiles.add(file);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
