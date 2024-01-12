package com.aditya.gstbillingapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

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
    private Activity context;

    public BillAdapter(ArrayList<File> pdfFiles, Activity context) {
        this.pdfFiles = pdfFiles;
        this.context = context;
    }

    public void setSearch(ArrayList<File> searchList){
        this.pdfFiles = searchList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PdfItemBinding binding = PdfItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BillViewHolder(binding.getRoot(), binding);
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
                        file.getAbsoluteFile());
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
}