package com.example.manan.enhancedurdureader.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.ApplicationEntities.ArticleVote;
import com.example.manan.enhancedurdureader.CustomViews.NastaleeqTextView;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;

public class ArticleReadingActivity extends AppCompatActivity {

    Integer articleId;
    NastaleeqTextView title;
    NastaleeqTextView body;
    LocalStorage db;
    Article article;
    ImageView upvotes;
    ImageView downvotes;
    TextView totalUpvotes;
    TextView totalDownvotes;
    Intent articleIntent;

    Integer currentUpvotes;
    Integer currentDownvotes;

    SharedPreferences userProfileStorage;
    public static final String USER_PROFILE_STORAGE = "UserProfileStorage" ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_reading);

        userProfileStorage = getSharedPreferences(USER_PROFILE_STORAGE, Context.MODE_PRIVATE);

        articleIntent = getIntent();
        articleId = articleIntent.getIntExtra("articleId", -1);

        updateValues();

        upvotes = (ImageView)findViewById(R.id.upvote);
        totalUpvotes = (TextView) findViewById(R.id.total_upvotes);
        currentUpvotes = db.getAllUpvotes(article.get_id(), article.getMagzineId());
        totalUpvotes.setText(currentUpvotes.toString());

        upvotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleVote vote = new ArticleVote();
                vote.setArticle(articleId);
                vote.setMagzine(article.getMagzineId());
                vote.setValue(1);
                vote.setUser(userProfileStorage.getInt("userId", -1));

                if(db.alreadyCastedVote(vote.getUser(), vote.getArticle(), vote.getMagzine(), 1)) {
                    Toast.makeText(getApplicationContext(), "Already Upvoted!", Toast.LENGTH_SHORT ).show();
                } else {
                    if(db.alreadyCastedVote(vote.getUser(), vote.getArticle(), vote.getMagzine(), -1)) {
                        db.deleteVote(vote.getUser(), vote.getArticle(), vote.getMagzine(), -1);
                        currentDownvotes --;
                        totalDownvotes.setText(currentDownvotes.toString());
                    }

                    db.insertVote(vote);
                    currentUpvotes = Integer.valueOf(totalUpvotes.getText().toString());
                    currentUpvotes ++;
                    totalUpvotes.setText(currentUpvotes.toString());
                }


            }
        });

        downvotes = (ImageView) findViewById(R.id.downvote);
        totalDownvotes = (TextView) findViewById(R.id.total_downvotes);

        currentDownvotes = db.getAllDownvotes(article.get_id(), article.getMagzineId());
        totalDownvotes.setText(currentDownvotes.toString());

        downvotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArticleVote vote = new ArticleVote();
                vote.setArticle(articleId);
                vote.setMagzine(article.getMagzineId());
                vote.setValue(-1);
                vote.setUser(userProfileStorage.getInt("userId", -1));

                if(db.alreadyCastedVote(vote.getUser(), vote.getArticle(), vote.getMagzine(), -1)) {
                    Toast.makeText(getApplicationContext(), "Already Downvoted!", Toast.LENGTH_SHORT ).show();
                }
                else {
                    if(db.alreadyCastedVote(vote.getUser(), vote.getArticle(), vote.getMagzine(), 1)) {
                        db.deleteVote(vote.getUser(), vote.getArticle(), vote.getMagzine(), 1);
                        currentUpvotes --;
                        totalUpvotes.setText(currentUpvotes.toString());
                    }
                    db.insertVote(vote);
                    currentDownvotes = Integer.valueOf(totalDownvotes.getText().toString());
                    currentDownvotes++;
                    totalDownvotes.setText(currentDownvotes.toString());
                }



            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.publish_article);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent publishIntent = new Intent(ArticleReadingActivity.this, ArticlePublishActivity.class);
                publishIntent.putExtra("articleId", articleId);
                startActivity(publishIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.publish_article_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void updateValues(){

        title = (NastaleeqTextView) findViewById(R.id.article_read_act_title);
        body = (NastaleeqTextView) findViewById(R.id.article_read_act_body);

        db = new LocalStorage(this, null, null, 1);
        article = db.getArticle(articleId);

        title.setText(article.getArticleTitle());
        body.setText(article.getArticleBody());

        body.setMovementMethod(new ScrollingMovementMethod());
    }
    protected void onResume() {
        super.onResume();
        updateValues();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_edit_article) {

            Intent editIntent = new Intent(this, NewArticleActivity.class);
            editIntent.putExtra("articleId", articleId);
            startActivity(editIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
