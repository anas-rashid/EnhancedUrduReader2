package com.example.manan.enhancedurdureader.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.manan.enhancedurdureader.Activities.ArticleReadingActivity;
import com.example.manan.enhancedurdureader.Adapters.ArticleAdapter;
import com.example.manan.enhancedurdureader.Adapters.MagazineAdapter;
import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.ApplicationEntities.Magazine;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;
import com.salesforce.androidsdk.rest.RestClient;

import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by manan on 3/7/17.
 */

public class MagazinesFragment extends Fragment {
    View magazinesAndArticlesView;

    MagazineAdapter magazineAdapter;
    ArticleAdapter articleAdapter;

    FeatureCoverFlow magazinesCoverFlow;
    FeatureCoverFlow articlesCoverFlow;

    ArrayList<Magazine> magazines = new ArrayList<Magazine>();
    ArrayList<Article> articles = new ArrayList<Article>();

    TextSwitcher magazineTextSwitcher;
    TextSwitcher articleTextSwitcher;

    LocalStorage db = null;

    RestClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        magazinesAndArticlesView = inflater.inflate(R.layout.magazines_fragment, container, false);

        db = new LocalStorage(getContext(), null, null, 1);


        magazineAdapter = new MagazineAdapter(getContext());
        articleAdapter = new ArticleAdapter(getContext());
        LoadMagazines();
        LoadArticles();
        magazineAdapter.setData(magazines);
        articleAdapter.setData(articles);

        magazinesCoverFlow = (FeatureCoverFlow) magazinesAndArticlesView.findViewById(R.id.magazines_coverflow);
        magazinesCoverFlow.setAdapter(magazineAdapter);

        articlesCoverFlow = (FeatureCoverFlow) magazinesAndArticlesView.findViewById(R.id.articles_coverflow);
        articlesCoverFlow.setAdapter(articleAdapter);

        magazineTextSwitcher = (TextSwitcher) magazinesAndArticlesView.findViewById(R.id.magazine_title_switcher);
        magazineTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);

                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);
        magazineTextSwitcher.setInAnimation(in);
        magazineTextSwitcher.setOutAnimation(out);

        articleTextSwitcher = (TextSwitcher) magazinesAndArticlesView.findViewById(R.id.article_title_switcher);
        articleTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);

                return textView;
            }
        });
        articleTextSwitcher.setInAnimation(in);
        articleTextSwitcher.setOutAnimation(out);

        magazinesCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                magazineTextSwitcher.setText(magazines.get(position % magazines.size()).getTitleResId());
            }

            @Override
            public void onScrolling() {
                magazineTextSwitcher.setText("");
            }
        });
        articlesCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                articleTextSwitcher.setText(articles.get(position % articles.size()).getArticleTitle());
            }

            @Override
            public void onScrolling() {
                articleTextSwitcher.setText("");
            }
        });
        magazinesCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*ArrayList<Article> articles = db.getMagazineArticles(magazines.get(position%magazines.size()).get_id());
                articleAdapter = new ArticleAdapter(getContext());
                articleAdapter.setData(articles);
                articlesCoverFlow.setAdapter(articleAdapter);*/
                new SetMagazineArticlesTask().execute(position % magazines.size());

            }
        });
        articlesCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent readActivity = new Intent(view.getContext(), ArticleReadingActivity.class);
                Article a = (Article) articleAdapter.getItem(position % articleAdapter.getCount());
                readActivity.putExtra("articleId", a.get_id());
                startActivity(readActivity);

            }
        });

        return magazinesAndArticlesView;
    }


    private class SetMagazineArticlesTask extends AsyncTask<Integer, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(getContext());

        Integer vtCount;
        ArrayList<Article> articles;

        @Override
        protected String doInBackground(Integer... params) {
            int position = params[0];
            int magID = magazines.get(position % magazines.size()).get_id();

            if (magID == 1)
                articles = db.getMagazineArticles(0);
            else if (magID == 2)
                articles = db.getAllArticles();
            else {
                articles = db.getMagazineArticles(magazines.get(position % magazines.size()).get_id());
            }
            if (articles.size() > 0) {
                articleAdapter = new ArticleAdapter(getContext());
                articleAdapter.setData(articles);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            if (dialog.isShowing()) {
                if (articles.size() > 0)
                    articlesCoverFlow.setAdapter(articleAdapter);
                dialog.dismiss();
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

    public void UpdateMagazine() {
        LoadMagazines();
        magazineAdapter = new MagazineAdapter(getContext());
        magazineAdapter.setData(magazines);
        magazinesCoverFlow.setAdapter(magazineAdapter);

        //magazineAdapter.setData(magazines);
        //magazineAdapter.notifyDataSetChanged();
        //magazinesCoverFlow.setAdapter(magazineAdapter);
    }

    void LoadMagazines() {
        magazines = db.getAllMagzines();
    }

    void LoadArticles() {
        articles = db.getAllArticles();
        if (articles.size() <= 0) {
            Article art = new Article();
            art.set_id(100);
            art.setArticleBody("This is our fyp");
            art.setArticleTitle("MyFYP");

            articles.add(art);
        }


    }


}