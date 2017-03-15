package com.example.manan.enhancedurdureader.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.ApplicationEntities.Magazine;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by manan on 3/10/17.
 */

public class ArticlePublishAdapter extends BaseAdapter {
    private ArrayList<Magazine> mData = new ArrayList<Magazine>(0);
    private Context mContext;
    ArrayList <Article> articles = new ArrayList<Article>();
    boolean hasLoaded = false;
    Hashtable<Integer,Bitmap> imageCache = new Hashtable<Integer, Bitmap>();
    public ArticlePublishAdapter(Context context) {

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
            rowView = inflater.inflate(R.layout.publish_magazine_layout, null);

            ImageView magazineTitlePage = (ImageView) rowView.findViewById(R.id.publish_magazine_title_page);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;

            Bitmap image;
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

            }
        }


        Integer articleCount = 0;
        for (Article article:articles) {
            if(article.getMagzineId() == mData.get(position).get_id())
                articleCount++;
        }
        if (mData.get(position).get_id() == 2) {
            articleCount = articles.size();
        } else if(mData.get(position).get_id() == 1){
            articleCount = 0;
            for (Article article:articles) {
                if(article.getMagzineId() == 0)
                    articleCount++;
            }
        }


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
