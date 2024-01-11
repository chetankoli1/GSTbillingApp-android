package com.aditya.gstbillingapp.Helper;

public class NumberToWorld {
    private String[] units = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private String[] teens = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private String[] tens = {"", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    public String asWords(int number) {
        if (number == 0) {
            return "zero";
        }
        String words = "";
        if ((number / 1000) > 0) {
            words += asWords(number / 1000) + " thousand ";
            number %= 1000;
        }
        if ((number / 100) > 0) {
            words += asWords(number / 100) + " hundred ";
            number %= 100;
        }
        if (number > 0) {
            if (number < 10) {
                words += units[number];
            } else if (number < 20) {
                words += teens[number - 10];
            } else {
                words += tens[number / 10];
                if ((number % 10) > 0) {
                    words += " " + units[number % 10];
                }
            }
        }
        return words;
    }
}
