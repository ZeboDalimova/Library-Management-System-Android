package com.example.kutubxona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class ActivityReadPdf extends AppCompatActivity {


    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page curPage;
    private ParcelFileDescriptor descriptor;
    private int currentPage = 0;
    private ImageView imgView;
    private float currentZoomLevel = 5;
    private Button btnPrevious, btnNext;
    private SQLiteDatabase db;
    List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pdf);

        imgView = findViewById(R.id.imgView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0) {
                    currentPage--;
                    displayPage(currentPage);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < pdfRenderer.getPageCount() - 1) {
                    currentPage++;
                    displayPage(currentPage);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            openPdfRenderer();  // Метод для открытия PDF
            displayPage(currentPage);  // Отображаем первую страницу
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при открытии PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openPdfRenderer() {
        File directory = new File(this.getFilesDir(), "myFiles");
        if (!directory.exists()) {
            Toast.makeText(this, "Папка не найдена", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();
        String userId = intent.getStringExtra("kitobIdRead");
//        String userId = getArguments().getString("kitobIdRead"); // Извлечение данных

        File file = new File(directory, userId);
        if (!file.exists()) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(descriptor);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при открытии PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void displayPage(int index) {
        if (pdfRenderer.getPageCount() <= index) return;

        if (curPage != null) {
            curPage.close();
        }

        // Открыть новую страницу
        curPage = pdfRenderer.openPage(index);

        // Получаем ширину экрана
        int width = getResources().getDisplayMetrics().widthPixels;

        // Рассчитываем пропорциональную высоту с учетом масштабирования
        float pageWidth = curPage.getWidth();
        float pageHeight = curPage.getHeight();

        // Пропорции для правильного отображения
        float aspectRatio = pageHeight / pageWidth;

        int height = (int) (width * aspectRatio);


        // определяем размеры Bitmap
        int newWidth = (int) (getResources().getDisplayMetrics().widthPixels * curPage.getWidth() / 72
                * currentZoomLevel / 40);//45

        int newHeight = (int) (getResources().getDisplayMetrics().heightPixels * curPage.getHeight() / 72
                * currentZoomLevel / 65);//90

        Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);


        Matrix matrix = new Matrix();
        float dpiAdjustedZoomLevel = currentZoomLevel * DisplayMetrics.DENSITY_MEDIUM
                / getResources().getDisplayMetrics().densityDpi;
        matrix.setScale(dpiAdjustedZoomLevel, dpiAdjustedZoomLevel);
        curPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//        curPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_COLOR);
        // Отображаем картинку
        imgView.setImageBitmap(bitmap);

        // Настроим кнопки для навигации
        btnPrevious.setEnabled(index > 0);
        btnNext.setEnabled(index < pdfRenderer.getPageCount() - 1);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (curPage != null) {
            curPage.close();
        }
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
        if (descriptor != null) {
            try {
                descriptor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}