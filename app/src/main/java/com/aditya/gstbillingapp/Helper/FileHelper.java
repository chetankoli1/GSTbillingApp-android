package com.aditya.gstbillingapp.Helper;

import android.content.Context;
import android.os.Environment;

import com.aditya.gstbillingapp.Config.AppConfig;

import java.io.File;

public class FileHelper {
    public boolean createFolder(Context context) {
        File folder = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);

        if (!folder.exists()) {
            folder.mkdir();
        } else {
            return false;
        }
        return true;
    }
}
