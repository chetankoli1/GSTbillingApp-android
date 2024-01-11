package com.aditya.gstbillingapp.Helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefreshReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }
}
