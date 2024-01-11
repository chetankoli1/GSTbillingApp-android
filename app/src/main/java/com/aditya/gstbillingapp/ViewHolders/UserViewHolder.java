package com.aditya.gstbillingapp.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.gstbillingapp.Model.User;
import com.aditya.gstbillingapp.databinding.CustomerItemBinding;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public final CustomerItemBinding binding;
    public UserViewHolder(@NonNull View itemView, CustomerItemBinding binding) {
        super(itemView);
        this.binding = binding;
    }
    public void bind(User customer) {


        binding.mUserName.setText(customer.getCustemerName());
        binding.mUserPhone.setText(customer.getCustemerPhone());
        binding.mUserAddress.setText(customer.getCustemerAddress());
        binding.mUserEmail.setText(customer.getCustemerEmail());
        binding.mUserGstno.setText(customer.getCustemerGstNo());
    }
}
