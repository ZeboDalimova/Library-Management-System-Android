package com.example.kutubxona;

import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.util.List;

public class AdapterKategoriya extends BaseAdapter {
    List<Kategoriya> bookList;
    LayoutInflater layoutInflater;
    Context context;
    DbHelperKategoriya dbHelper;

    public AdapterKategoriya(Context context, List<Kategoriya> bookList) {
        this.context = context;
        this.bookList = bookList;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DbHelperKategoriya(this.context);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.for_kategoriya_list, null);
        }
        TextView textFirstname = (TextView) convertView.findViewById(R.id.kategoriya_nomi_for_list);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.kategoriya_image_for_list);


        final Kategoriya user = bookList.get(position);
        textFirstname.setText(user.kat_name);

        try {
            // Papka yo'lini aniqlaymiz
            File directory = new File(context.getFilesDir(), "categoryPhotos");
            File file = new File(directory, user.image);

            // Rasmni yuklaymiz
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}