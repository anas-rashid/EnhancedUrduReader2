package com.example.manan.enhancedurdureader.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.CustomViews.NastaleeqEditText;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;

public class NewArticleActivity extends AppCompatActivity {

    LocalStorage db;
    NastaleeqEditText _articleTitle;
    NastaleeqEditText _articleBody;
    String _articleCoverURI;
    Integer articleId;
    Article article;
    SharedPreferences userProfileStorage;
    public static final String USER_PROFILE_STORAGE = "UserProfileStorage" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);

        Intent articleIntent = getIntent();
        articleId = articleIntent.getIntExtra("articleId", -1);

        db = new LocalStorage(this, null, null, 1);
        if(articleId==-1) {
            _articleTitle = (NastaleeqEditText) findViewById(R.id.new_article_title);
            _articleBody = (NastaleeqEditText) findViewById(R.id.new_article_body);
        } else {
            article = db.getArticle(articleId);

            _articleTitle = (NastaleeqEditText) findViewById(R.id.new_article_title);
            _articleTitle.setText(article.getArticleTitle());

            _articleBody = (NastaleeqEditText) findViewById(R.id.new_article_body);
            _articleBody.setText(article.getArticleBody());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save_article);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveArticle();
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_article_menu, menu);
        return true;
    }

    //save article to database of articles
    public void saveArticle(){

        userProfileStorage = getSharedPreferences(USER_PROFILE_STORAGE, Context.MODE_PRIVATE);
        if(articleId  == -1) {
            article = new Article();
            article.setArticleTitle(_articleTitle.getText().toString());
            article.setArticleBody(_articleBody.getText().toString());
            article.setArticleCover(_articleCoverURI);
            article.setAuthorId(userProfileStorage.getInt("userId", -1));

            try {

                db.insertArticle(article);
                Toast.makeText(this, "Article Saved Successfully", Toast.LENGTH_SHORT).show();
            } catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {

            article.setArticleTitle(_articleTitle.getText().toString());
            article.setArticleBody(_articleBody.getText().toString());
            article.setArticleCover(_articleCoverURI);
            article.setAuthorId(userProfileStorage.getInt("userId", -1));

            try {
                db.updateArticle(article);
                Toast.makeText(this, "Article Updated!"+String.valueOf(article.getAuthorId()), Toast.LENGTH_SHORT).show();
            } catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_article_cover) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
            return true;
        }
        if (id == R.id.action_create_New_Article) {

            Intent newArticle = new Intent(this, NewArticleActivity.class);
            startActivity(newArticle);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                _articleCoverURI = cursor.getString(columnIndex);
                cursor.close();
            }
        }
    }

}
