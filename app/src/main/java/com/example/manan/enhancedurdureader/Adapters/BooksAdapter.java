package com.example.manan.enhancedurdureader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manan.enhancedurdureader.ApplicationEntities.Book;
import com.example.manan.enhancedurdureader.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BooksAdapter extends BaseAdapter {

    private ArrayList<Book> mData = new ArrayList<>(0);
    private Context mContext;

    public BooksAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<Book> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.book_layout, null);

            ImageView bookTitlePage = (ImageView) rowView.findViewById(R.id.book_title_page);
            bookTitlePage.setImageBitmap(mData.get(position).BookTittlePage);

            return rowView;
        }

        return rowView;
    }

}