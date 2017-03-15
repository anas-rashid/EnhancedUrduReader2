package com.example.manan.enhancedurdureader.ApplicationEntities;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;


import java.io.FileInputStream;

import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by manan on 3/7/17.
 */

public class EpubBook extends Book {
    EpubReader epubReader = new EpubReader();

    public EpubBook(String path)
    {
        this.BookPath = path;
        try
        {
            nl.siegmann.epublib.domain.Book book = epubReader.readEpub(new FileInputStream(BookPath));

            this.BookTittle = book.getTitle();
            this.BookTittlePage = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
        }
        catch (Exception ex)
        {

        }

    }
}
