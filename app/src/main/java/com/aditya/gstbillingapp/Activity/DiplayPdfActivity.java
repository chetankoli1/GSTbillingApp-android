package com.aditya.gstbillingapp.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.gstbillingapp.R;
import com.aditya.gstbillingapp.databinding.ActivityDiplayPdfBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DiplayPdfActivity extends AppCompatActivity  implements SurfaceHolder.Callback {
    private ActivityDiplayPdfBinding binding;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    String pdfPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiplayPdfBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if (getIntent().hasExtra("path")) {
            pdfPath = getIntent().getStringExtra("path");
        } else {
            finish();
        }

        binding.surfaceView.getHolder().addCallback( this);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            openRenderer(this);
            showPage(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        closeRenderer();
    }


    private void openRenderer(Context context) throws IOException {
        File file = new File(getCacheDir(), pdfPath);
        if (!file.exists()) {
            InputStream asset = context.getAssets().open("name.pdf");
            FileOutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(parcelFileDescriptor);
    }

    private void closeRenderer() {
        if (currentPage != null) {
            currentPage.close();
        }
        pdfRenderer.close();
    }

    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        if (currentPage != null) {
            currentPage.close();
        }
        currentPage = pdfRenderer.openPage(index);
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        binding.surfaceView.getHolder().lockCanvas().drawBitmap(bitmap, 0, 0, null);
        binding.surfaceView.getHolder().unlockCanvasAndPost(binding.surfaceView.getHolder().lockCanvas());
    }
}

