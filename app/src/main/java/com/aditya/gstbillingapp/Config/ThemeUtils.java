package com.aditya.gstbillingapp.Config;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aditya.gstbillingapp.R;

public class ThemeUtils {
    private static boolean isDarkModeEnabled(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }

    public static void applyNavigationIconColor(Context context, ActionBar actionBar) {
        if (actionBar != null) {
            if (isDarkModeEnabled(context)) {
                actionBar.setHomeAsUpIndicator(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
            } else {
                actionBar.setHomeAsUpIndicator(new ColorDrawable(ContextCompat.getColor(context, R.color.black)));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int statusBarColor;
                if (isDarkModeEnabled(context)) {
                    statusBarColor = ContextCompat.getColor(context, R.color.black);
                } else {
                    statusBarColor = ContextCompat.getColor(context, R.color.white);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((AppCompatActivity) context).getWindow().setStatusBarColor(statusBarColor);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((AppCompatActivity) context).getWindow().setStatusBarColor(statusBarColor);
                }
            }
        }
    }
}
