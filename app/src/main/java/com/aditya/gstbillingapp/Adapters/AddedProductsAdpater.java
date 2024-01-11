package com.aditya.gstbillingapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.Model.MyProducts;
import com.aditya.gstbillingapp.Model.Products;
import com.aditya.gstbillingapp.ViewHolders.AddedProductsViewHolder;
import com.aditya.gstbillingapp.databinding.AddedItemsBinding;
import com.aditya.gstbillingapp.databinding.LayoutAddedItemsBinding;

import java.util.ArrayList;

public class AddedProductsAdpater extends RecyclerView.Adapter<AddedProductsViewHolder> {
    public AddedProductsAdpater() {
    }

    private ArrayList<MyProducts> productsList;

    public AddedProductsAdpater(ArrayList<MyProducts> productsList) {
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public AddedProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddedItemsBinding binding = AddedItemsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AddedProductsViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedProductsViewHolder holder, int position) {
        MyProducts products = productsList.get(position);
        holder.binding.miProductName.setText(products.getProductName());
        holder.binding.miProductPrice.setText(products.getProductPrice());
        holder.binding.miProductSanNo.setText(products.getProductSANno());

        holder.binding.miDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
