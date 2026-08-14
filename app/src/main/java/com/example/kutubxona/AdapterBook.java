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

public class AdapterBook extends BaseAdapter
{
    List<Book> bookList;
    LayoutInflater layoutInflater;
    Context context;
    DbHelperBook dbHelper;


    List<Kategoriya> kategoriyaList;

    public AdapterBook(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DbHelperBook(this.context);
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
            convertView = layoutInflater.inflate(R.layout.for_kitob_list, null);
        }



        TextView textFirstname = (TextView) convertView.findViewById(R.id.k_a_name);

        TextView textKat = (TextView) convertView.findViewById(R.id.k_a_kat);
        TextView texеAvtor = (TextView) convertView.findViewById(R.id.k_a_avtor);

        ImageView imageUser = (ImageView) convertView.findViewById(R.id.k_a_rasm);

        final Book user = bookList.get(position);
        textFirstname.setText(user.kitobname);
        texеAvtor.setText(user.komment);

        DbHelperKategoriya dbHelperKategoriya = new DbHelperKategoriya(context);
        kategoriyaList = dbHelperKategoriya.readAllKat(user.kateg_id);

        textKat.setText(String.valueOf(kategoriyaList.get(0).kat_name));

        try {
            // Papka yo'lini aniqlaymiz
            File directory = new File(context.getFilesDir(), "myPhotos");
            File file = new File(directory, user.rasm_id);

            // Rasmni yuklaymiz
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageUser.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}