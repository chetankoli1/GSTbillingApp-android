package com.aditya.gstbillingapp.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InvoiceGenerator {
    private int currentYear;
    private int currentSerial;

    public InvoiceGenerator(int startSerial) {
        Calendar calendar = Calendar.getInstance();
        this.currentYear = calendar.get(Calendar.YEAR) % 100;
        this.currentSerial = startSerial;
    }

    public String generateInvoiceNumber() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        String dayMonthYearPrefix = dateFormat.format(calendar.getTime());
        String monthPrefix = String.format("%02d", currentMonth);

        String invoiceNumber = dayMonthYearPrefix + String.format("%04d", this.currentSerial);

        this.currentSerial++;

        if (currentMonth > this.currentYear) {
            this.currentYear = currentMonth;
        }

        return invoiceNumber;
    }
}
