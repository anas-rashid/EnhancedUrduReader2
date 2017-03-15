package com.example.manan.enhancedurdureader.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manan.enhancedurdureader.Activities.SegmentedBookReaderActivity;
import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by manan on 3/9/17.
 */

public class ArticleAdapter extends BaseAdapter {
    private ArrayList<Article> mData = new ArrayList<>(0);
    private Context mContext;

    View rowView;

    public ArticleAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<Article> data) {
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
    TextView voteCount;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.article_layout, null);

            ImageView articleTitlePage = (ImageView) rowView.findViewById(R.id.article_title_page);
            /*BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            articleTitlePage.setImageBitmap(BitmapFactory.decodeFile(mData.get(position).getArticleCover(),options));
            */
            if(mData.get(position).getArticleCover() != null)
                Glide.with(mContext).load(new File(mData.get(position%mData.size()).getArticleCover())).into(articleTitlePage);

        }

        voteCount = (TextView) rowView.findViewById(R.id.article_vote_count);

        //new SetVotesTask().execute(position);
        LocalStorage voteDb = new LocalStorage(mContext, null, null, 1);
        vtCount = (voteDb.getAllUpvotes(mData.get(position%mData.size()).get_id(), mData.get(position%mData.size()).getMagzineId())) - (voteDb.getAllDownvotes(mData.get(position%mData.size()).get_id(), mData.get(position%mData.size()).getMagzineId()));



        voteCount.setText(vtCount.toString());


        return rowView;
    }
    Integer vtCount = 0;

    private class SetVotesTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            int position = params[0];

            LocalStorage voteDb = new LocalStorage(mContext, null, null, 1);
            vtCount = (voteDb.getAllUpvotes(mData.get(position%mData.size()).get_id(), mData.get(position%mData.size()).getMagzineId())) - (voteDb.getAllDownvotes(mData.get(position%mData.size()).get_id(), mData.get(position%mData.size()).getMagzineId()));


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

                voteCount.setText(Integer.toString(vtCount));


        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }




}
