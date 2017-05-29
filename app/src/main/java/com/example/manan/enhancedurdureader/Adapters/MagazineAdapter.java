package com.example.manan.enhancedurdureader.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.ApplicationEntities.Magazine;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by manan on 3/9/17.
 */

public class MagazineAdapter extends BaseAdapter {

    private ArrayList<Magazine> mData = new ArrayList<Magazine>(0);
    private Context mContext;
    ArrayList <Article> articles = new ArrayList<Article>();
    boolean hasLoaded = false;
    Hashtable<Integer,Bitmap> imageCache = new Hashtable<Integer, Bitmap>();
    public MagazineAdapter(Context context) {

        mContext = context;

    }

    public void setData(ArrayList<Magazine> data) {
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
        LoadArticles();

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.magazine_layout, null);

            ImageView magazineTitlePage = (ImageView) rowView.findViewById(R.id.magazine_title_page);

            /*Bitmap image;
            /*BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;

            if(!imageCache.containsKey(position)) {
                image = BitmapFactory.decodeFile(mData.get(position).getImageResId(), options);

            }
            else
                image = imageCache.get(position);

            int magazineID = mData.get(position).get_id();
            if(image == null && magazineID == 2)
                magazineTitlePage.setImageResource(R.drawable.all_articles_magazine);
            else if(image == null && magazineID ==1 )
                magazineTitlePage.setImageResource(R.drawable.saved_articles_magazine);
            else {
                magazineTitlePage.setImageBitmap(image);
                imageCache.put(position,image);

            }*/
            int magazineID = mData.get(position%mData.size()).get_id();
            if(magazineID == 2)
                Glide.with(mContext).load(R.drawable.all_articles_magazine).into(magazineTitlePage);
            else if(magazineID ==1 )
                Glide.with(mContext).load(R.drawable.saved_articles_magazine).into(magazineTitlePage);
            else if(mData.get(position%mData.size()).getImageResId() != null) {
                Glide.with(mContext).load(new File(mData.get(position%mData.size()).getImageResId())).skipMemoryCache(false).into(magazineTitlePage);
            } else {

                Glide.with(mContext).load(R.drawable.saved_articles_magazine).into(magazineTitlePage);

            }

        }


        Integer articleCount = 0;
        for (Article article:articles) {
            if(article.getMagzineId() == mData.get(position%mData.size()).get_id())
                articleCount++;
        }
        if (mData.get(position%mData.size()).get_id() == 2) {
            articleCount = articles.size();
        } else if(mData.get(position%mData.size()).get_id() == 1){
            articleCount = 0;
            for (Article article:articles) {
                if(article.getMagzineId() == 0)
                    articleCount++;
            }
        }
        TextView voteCount = (TextView)rowView.findViewById(R.id.vote_count);
        voteCount.setText(Integer.toString(articleCount));

        return rowView;
    }
    void LoadArticles()
    {
        if(!hasLoaded)
        {
            LocalStorage voteDb = new LocalStorage(mContext, null, null, 1);
            articles = voteDb.getAllArticles();
            hasLoaded = true;
        }
    }


}
