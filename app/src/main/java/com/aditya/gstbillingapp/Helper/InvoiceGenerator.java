package com.aditya.gstbillingapp.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class InvoiceGenerator {
    private int currentYear;

    public InvoiceGenerator() {
        Calendar calendar = Calendar.getInstance();
        this.currentYear = calendar.get(Calendar.YEAR) % 100;
    }

    public String generateInvoiceNumber() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

        int randomNumber;
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        String dayMonthYearPrefix = dateFormat.format(calendar.getTime());
        String monthPrefix = String.format("%02d", currentMonth);
        Random random = new Random();
        randomNumber = random.nextInt(900000) + 100000;


        String invoiceNumber = dayMonthYearPrefix + String.format("%06d", randomNumber);

        if (currentMonth > this.currentYear) {
            this.currentYear = currentMonth;
        }

        return invoiceNumber;
    }
}
