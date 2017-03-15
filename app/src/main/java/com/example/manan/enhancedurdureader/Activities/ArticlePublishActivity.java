package com.example.manan.enhancedurdureader.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.manan.enhancedurdureader.Adapters.ArticlePublishAdapter;
import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.ApplicationEntities.Magazine;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;


import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class ArticlePublishActivity extends AppCompatActivity {

    private FeatureCoverFlow mCoverFlow;
    private ArticlePublishAdapter mAdapter;
    private ArrayList<Magazine> mData = new ArrayList<>(0);
    private TextSwitcher mTitle;
    LocalStorage db;
    Intent magzineSelectorIntent;
    Integer articleId;

    private void intentInformation() {

        magzineSelectorIntent = getIntent();
        articleId = magzineSelectorIntent.getIntExtra("articleId", -1);


    }
    private void updateDataList(){

        db = new LocalStorage(this, null, null, 1);
        mData = new ArrayList<>();
        for(Magazine o:db.getAllMagzines()) {
            if(o != null) {
                if(o.get_id() != 1 && o.get_id() != 2)
                    mData.add(o) ;

            }

        }
    }

    private void setMagzineAdapter(){

        mAdapter = new ArticlePublishAdapter(this);
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.publish_magazine_coverflow);
        mCoverFlow.setAdapter(mAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_publish);

        intentInformation();
        updateDataList();

        mTitle = (TextSwitcher) findViewById(R.id.publish_magazine_title_switcher);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getApplicationContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(getApplicationContext(),android.R.style.TextAppearance_Large);

                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        setMagzineAdapter();

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Article article = new Article();
                article.set_id(articleId);
                int actualPosition = position%mData.size();
                article.setMagzineId(mData.get(actualPosition).get_id());

                db.updateArticle(article);

                finish();
            }
        });



        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(mData.get(position%mData.size()).getTitleResId());
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateDataList();
        setMagzineAdapter();
    }

}
