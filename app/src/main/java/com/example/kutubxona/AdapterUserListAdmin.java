package com.example.kutubxona;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class AdapterUserListAdmin extends BaseAdapter {
    List<Person> bookList;
    LayoutInflater layoutInflater;
    Context context;
    DbHelperPerson dbHelper;
    FragmentManager fragmentManager;

    public AdapterUserListAdmin(Context context, List<Person> bookList, FragmentManager fragmentManager) {
        this.context = context;
        this.bookList = bookList;
        this.fragmentManager = fragmentManager;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DbHelperPerson(this.context);
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
            convertView = layoutInflater.inflate(R.layout.for_user_list_admin, null);
        }
        Person book = bookList.get(position);
        TextView name=convertView.findViewById(R.id.user_name_for_listAdmin);
        TextView email=convertView.findViewById(R.id.user_email_for_listAdmin);
        TextView status=convertView.findViewById(R.id.user_status_for_listAdmin);
        Button deleteButton = convertView.findViewById(R.id.user_button2_for_listAdmin);


        name.setText(book.username);
        email.setText(book.email);
        status.setText(book.status);

        deleteButton.setOnClickListener(v -> {
            String stat;
            if(book.status.equals("admin"))
            {
                stat="user";
                status.setText("user");
            }
            else
            {
                stat="admin";
                status.setText("admin");
            }
            Person p=new Person(
                    book.id,
                    book.username,
                    book.email,
                    book.phone,
                    book.password,
                    stat,
                    book.image);
//            Person p=new Person( book.username, book.phone, book.password, "admin", book.image );
            if(dbHelper.updatePerson(p)!=-1)
            {
                FragmentManager fragmentManager = this.fragmentManager;
                fragmentManager.beginTransaction()
                        .detach(fragmentManager.findFragmentById(R.id.frame_layout)) // Отсоединяем текущий фрагмент
                        .attach(fragmentManager.findFragmentById(R.id.frame_layout)) // Заново прикрепляем его
                        .commit();
            }
            else
            {
                Toast.makeText(context, "Не получается изменить данные!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
