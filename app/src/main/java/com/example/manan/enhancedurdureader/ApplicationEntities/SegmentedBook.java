package com.example.manan.enhancedurdureader.ApplicationEntities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.example.manan.enhancedurdureader.R;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by manan on 3/7/17.
 */

public class SegmentedBook extends Book {

    public SegmentedBook(String path)
    {
        this.BookPath = path;

        File bookFile = new File(BookPath);

        this.BookTittle = bookFile.getName();

    }

    Bitmap ConvertStringToBitmap(String Tittle)
    {
        byte[] encodeByte = Tittle.getBytes(Charset.forName("UTF-8"));

        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
        return bitmap;

    }
}
