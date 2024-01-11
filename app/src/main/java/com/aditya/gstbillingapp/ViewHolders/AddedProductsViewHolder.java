package com.aditya.gstbillingapp.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.databinding.AddedItemsBinding;
import com.aditya.gstbillingapp.databinding.LayoutAddedItemsBinding;

public class AddedProductsViewHolder extends RecyclerView.ViewHolder {
    public AddedItemsBinding binding;
    public AddedProductsViewHolder(@NonNull View itemView, AddedItemsBinding binding) {
        super(itemView);
        this.binding = binding;
    }
}
