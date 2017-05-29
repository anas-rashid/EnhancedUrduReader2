package com.example.manan.enhancedurdureader.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.manan.enhancedurdureader.Activities.EpubReaderActivity;
import com.example.manan.enhancedurdureader.Activities.SegmentedBookReaderActivity;
import com.example.manan.enhancedurdureader.Adapters.BooksAdapter;
import com.example.manan.enhancedurdureader.ApplicationEntities.Book;
import com.example.manan.enhancedurdureader.ApplicationEntities.EpubBook;
import com.example.manan.enhancedurdureader.ApplicationEntities.SegmentedBook;
import com.example.manan.enhancedurdureader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by manan on 3/7/17.
 */

public class BooksFragment extends Fragment {

    TextSwitcher mTitle;
    ArrayList<Book> books = new ArrayList<>(0);
    BooksAdapter booksAdapter;
    FeatureCoverFlow booksCoverFlow;
    View booksView;
    LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        booksView = inflater.inflate(R.layout.books_fragment,container,false);

        booksAdapter = new BooksAdapter(getContext());
        LoadBooks();
        booksAdapter.setData(books);

        booksCoverFlow = (FeatureCoverFlow) booksView.findViewById(R.id.books_coverflow);
        booksCoverFlow.setAdapter(booksAdapter);


        mTitle = (TextSwitcher) booksView.findViewById(R.id.book_title_switcher);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(getContext(),android.R.style.TextAppearance_Large);

                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        booksCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(books.get(position).BookTittle);
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });
        booksCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book)booksAdapter.getItem(position%booksAdapter.getCount());

                if(book instanceof SegmentedBook) {
                    Intent readActivity = new Intent(view.getContext(), SegmentedBookReaderActivity.class);
                    readActivity.putExtra("path", book.BookPath);
                    startActivity(readActivity);
                }
                else
                {
                    Intent readActivity = new Intent(view.getContext(), EpubReaderActivity.class);
                    readActivity.putExtra("path", book.BookPath);
                    startActivity(readActivity);

                }
            }
        });

        return booksView;
    }
    void LoadBooks()
    {
        ArrayList<File> bookFiles = new ArrayList<File>();

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                bookFiles = new ArrayList<File>(Arrays.asList(new File("/storage/398A-161B/EnhancedBooks/").listFiles()));
               // Log.v(TAG,"Permission is granted");
               // return true;
            } else {

            //    Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                //return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            bookFiles = new ArrayList<File>(Arrays.asList(new File("/storage/398A-161B/EnhancedBooks/").listFiles()));
           // Log.v(TAG,"Permission is granted");
            //return true;
        }

        bookFiles = new ArrayList<File>(Arrays.asList(new File("/storage/398A-161B/EnhancedBooks/").listFiles()));

        for(File bookFile:bookFiles)
        {
            Book book = new Book();
            if(getFileExtension(bookFile).equalsIgnoreCase("epub")) {
                book = new EpubBook(bookFile.getAbsolutePath());
            }
            else
            {
                book = new SegmentedBook(bookFile.getAbsolutePath());
                book.BookTittlePage = BitmapFactory.decodeResource(booksView.getResources(),R.drawable.book_icon);
            }
            books.add(book);
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

}

