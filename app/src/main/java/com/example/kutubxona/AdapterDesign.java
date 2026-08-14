package com.example.kutubxona;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

    public class AdapterDesign extends RecyclerView.Adapter<AdapterDesign.ViewHolder> {
        private final List<Design> bookList;
        private final OnDesignClickListener listener;

        public interface OnDesignClickListener {
            void onClick(Design design);
        }

        public AdapterDesign(List<Design> bookList, OnDesignClickListener listener) {
            this.bookList = bookList;
            this.listener = listener;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.design_image_for_list);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_design_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Design design = bookList.get(position);

            // Установите изображение
            try {
                File directory = new File(holder.itemView.getContext().getFilesDir(), "categoryPhotos");
                File file = new File(directory, design.image);
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                holder.imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Обработчик клика
            holder.itemView.setOnClickListener(v -> listener.onClick(design));
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }
    }
