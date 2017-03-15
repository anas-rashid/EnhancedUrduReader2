package com.example.manan.enhancedurdureader.ApplicationEntities;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by manan on 3/10/17.
 */

public class BookInfo {
    public ArrayList<Bitmap> ligatures = new ArrayList<Bitmap>();
    public int zoomSize = 1;
    public ArrayList<PageOffset> pagedOffsets = new ArrayList<PageOffset>();
    public int maxLigaturesInLine =0;
    public int maxLinesInPage = 0;
    public int margin = 5;


}

