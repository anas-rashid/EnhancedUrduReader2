package com.example.manan.enhancedurdureader.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.stream.StreamUriLoader;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.example.manan.enhancedurdureader.ApplicationEntities.BookInfo;
import com.example.manan.enhancedurdureader.ApplicationEntities.PageInfo;
import com.example.manan.enhancedurdureader.ApplicationEntities.PageOffset;
import com.example.manan.enhancedurdureader.ApplicationEntities.SegmentedBook;
import com.example.manan.enhancedurdureader.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SegmentedBookReaderActivity extends AppCompatActivity {

    BookInfo bookInfo = new BookInfo();
    PageInfo pageInfo = new PageInfo();
    PageOffset pageOffset = new PageOffset();
    File[] files = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segmented_book_reader);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*final ImageView iv =new ImageView(this);
        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                final int Height = iv.getMeasuredHeight();
                final int Width = iv.getMeasuredWidth();

                pageInfo.pageWidth = pageInfo.pageWidth/(bookInfo.ligatures.get(0).getWidth()/Width);

                pageInfo.pageHeight = pageInfo.pageHeight/(bookInfo.ligatures.get(0).getHeight()/Height);

                RenderPage(pageOffset.startingLigatureIndex);
                return true;
            }
        });*/
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("path");


        SetScreenSize();
        OpenLigatureFiles(filePath);
        LoadLigatures();
        RenderPage(pageOffset.endingLigatureIndex);

        /*iv.setImageBitmap(bookInfo.ligatures.get(0));
        RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.activity_segmented_book_reader);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bookInfo.ligatures.get(0).getWidth(), bookInfo.ligatures.get(0).getHeight());
        layout.addView(iv,params);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }






    //======================================================================================================
    private void SetScreenSize() {

        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE);

        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        pageInfo.pageHeight = Math.round(displayMetrics.heightPixels / displayMetrics.density);
        pageInfo.pageWidth = Math.round(displayMetrics.widthPixels / displayMetrics.density);

        pageInfo.pageHeight -= 50;
        pageInfo.pageWidth -= 50;

    }

    private void RenderPage(int startingLigatureIndex) {
        RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.activity_segmented_book_reader);


        layout.removeAllViews();
        pageInfo.previousLineFirstRenderedLigature = pageInfo.lastRenderedLigature = null;

        int totalLineWidth = 0;
        int totalPageHeight = 0;
        int i = 0;
        int accMargin = bookInfo.margin;

        pageOffset.startingLigatureIndex = startingLigatureIndex;
        totalLineWidth += bookInfo.ligatures.get(startingLigatureIndex).getHeight();
        for (int j = startingLigatureIndex; j < bookInfo.ligatures.size(); j++) {
            Bitmap image = bookInfo.ligatures.get(j);
            ImageView iView = new ImageView(SegmentedBookReaderActivity.this);
            iView.setId(View.generateViewId());
            iView.setBackground(new BitmapDrawable(getResources(), image));

            totalLineWidth += image.getWidth();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(image.getWidth(), image.getHeight());

            if (pageInfo.lastRenderedLigature == null) {
                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                params.setMargins(0, bookInfo.margin, 0, 0);
                pageInfo.previousLineFirstRenderedLigature = iView;
            } else if (totalLineWidth >= pageInfo.pageWidth) {
                params.addRule(RelativeLayout.BELOW, pageInfo.previousLineFirstRenderedLigature.getId());
                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                int maxHeight = getMaxHeightLigature(i);
                if (maxHeight > bookInfo.margin) {
                    //params.setMargins(0,bookInfo.margin+(maxHeight - bookInfo.margin - iView.getHeight()),0,0);
                    params.setMargins(0, bookInfo.margin + maxHeight, 0, 0);
                    totalPageHeight += bookInfo.margin + 2 * maxHeight;
                } else {
                    params.setMargins(0, bookInfo.margin, 0, 0);
                    totalPageHeight += bookInfo.margin;
                }
                pageInfo.previousLineFirstRenderedLigature = iView;
                totalLineWidth = 0;

                //accMargin += bookInfo.margin*2;
            } else {
                params.addRule(RelativeLayout.ALIGN_BOTTOM, pageInfo.lastRenderedLigature.getId());
                params.addRule(RelativeLayout.START_OF, pageInfo.lastRenderedLigature.getId());


            }

            //if((i+1) >= ((bookInfo.maxLinesInPage+1) * bookInfo.maxLigaturesInLine)) {
            if (totalPageHeight >= pageInfo.pageHeight) {
                pageOffset.endingLigatureIndex = j - 1;
                break;
            }

            layout.addView(iView, params);
            pageInfo.lastRenderedLigature = iView;
            i++;

        }


    }

    int getMaxHeightLigature(int startIndex) {
        int maxHeight = bookInfo.ligatures.get(0).getHeight();
        for (int i = startIndex; i < bookInfo.maxLigaturesInLine; i++) {
            if (maxHeight < bookInfo.ligatures.get(i).getHeight())
                maxHeight = bookInfo.ligatures.get(i).getHeight();
        }
        return maxHeight;
    }

    private void OpenLigatureFiles(String path) {
        files = new File(path).listFiles();
    }

    private void LoadLigatures() {
        int width = 0;
        int height = 0;
        bookInfo.ligatures.clear();


        for (int i = 0; i < files.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(files[i].getPath());


            width += bitmap.getWidth() * bookInfo.zoomSize;
            //height += bitmap.getHeight() * bookInfo.zoomSize;
            height = height < bitmap.getHeight() * bookInfo.zoomSize ? bitmap.getHeight() * bookInfo.zoomSize : height;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                    bitmap, bitmap.getWidth() * bookInfo.zoomSize, bitmap.getHeight() * bookInfo.zoomSize, false);

            bookInfo.ligatures.add(scaledBitmap);
        }
        bookInfo.maxLigaturesInLine = pageInfo.pageWidth / (width / bookInfo.ligatures.size());
        bookInfo.maxLinesInPage = pageInfo.pageHeight / (bookInfo.margin + 2 * height);//bookInfo.maxLigaturesInLine * (pageInfo.pageHeight/(height/bookInfo.ligatures.size()));
    }

    float oldDist = 1f;
    static final int NONE = 0;
    static final int ZOOM = 1;
    int mode = NONE;
    float tempZoomInCount = 0;
    float tempZoomOutCount = 1;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(mode != ZOOM) {
                    x2 = event.getX();
                    float deltaX = x2 - x1;
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (x2 > x1) {
                            //Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                            pageOffset.pageLigaturesCount.push(pageOffset.endingLigatureIndex - pageOffset.startingLigatureIndex + 1);
                            RenderPage(pageOffset.endingLigatureIndex + 1);
                        } else {
                            //Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                            if(pageOffset.pageLigaturesCount.size()>0) {
                                Integer lastPageLiagturesCount = pageOffset.pageLigaturesCount.pop();
                                RenderPage(pageOffset.startingLigatureIndex - lastPageLiagturesCount);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if(mode == ZOOM) {

                    float newDist = spacing(event);
                    if(newDist > 10f)
                    {
                        float scale = newDist/oldDist;
                        if(scale > 1)
                        {
                            if(bookInfo.zoomSize<3)
                            {
                                tempZoomInCount+=0.5;
                                if(tempZoomInCount == 1)
                                {
                                    mode = NONE;
                                    bookInfo.zoomSize++;
                                    tempZoomInCount =0;

                                    LoadAndRender zoomIn = new LoadAndRender();
                                    Toast.makeText(this, String.valueOf(bookInfo.zoomSize), Toast.LENGTH_SHORT).show ();
                                    zoomIn.execute();
                                }
                            }
                        }
                        else
                        {
                            scale = 0.95f;
                            if(bookInfo.zoomSize>1)
                            {
                                tempZoomOutCount-=0.5;
                                if(tempZoomOutCount == 0)
                                {
                                    mode = NONE;
                                    bookInfo.zoomSize--;
                                    tempZoomOutCount =1;

                                    LoadAndRender zoomOut = new LoadAndRender();
                                    Toast.makeText(this, String.valueOf(bookInfo.zoomSize), Toast.LENGTH_SHORT).show ();
                                    zoomOut.execute();
                                }
                            }
                        }
                    }
                }
        }
        return super.onTouchEvent(event);
    }
    private class LoadAndRender extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(SegmentedBookReaderActivity.this);
        private Boolean stop = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {

                LoadLigatures();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                RenderPage(pageOffset.startingLigatureIndex);
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait....");
            dialog.show();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }
}
