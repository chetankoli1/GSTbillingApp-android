package com.aditya.gstbillingapp.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.Model.Products;
import com.aditya.gstbillingapp.databinding.ProductItemBinding;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public final ProductItemBinding binding;
    public ProductViewHolder(@NonNull View itemView, ProductItemBinding binding) {
        super(itemView);
        this.binding = binding;
    }
    public void bind(Products products) {
        binding.mProductName.setText("Product Name: "+products.getProductName());
        binding.mProductPrice.setText("Product Price:- "+String.valueOf("\u20B9 "+products.getPrice()));
        binding.mProductHSNSACno.setText("HSN/SAC: "+String.valueOf(products.getHSNSACno()));
    }
}
