package com.example.manan.enhancedurdureader.ApplicationEntities;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by manan on 3/10/17.
 */

public class PageInfo {
    public ArrayList<ImageView> line = new ArrayList<ImageView>();
    public ImageView lastRenderedLigature = null;
    public ImageView previousLineFirstRenderedLigature = null;
    public int totalRenderedPages = 0;
    public int pageHeight =0;
    public int pageWidth = 0;
    public PageOffset pageOffset = new PageOffset();
    public int oldDistance = 0;


}
