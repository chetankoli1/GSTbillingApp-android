package com.aditya.gstbillingapp.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.databinding.PdfItemBinding;

public class BillViewHolder extends RecyclerView.ViewHolder {
    public final PdfItemBinding binding;
    public BillViewHolder(@NonNull View itemView, PdfItemBinding binding) {
        super(itemView);
        this.binding = binding;
    }
}
