package com.example.manan.enhancedurdureader.DataStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.ApplicationEntities.ArticleVote;
import com.example.manan.enhancedurdureader.ApplicationEntities.Magazine;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Anas - Plumlogix on 2/5/2017.
 */

public class LocalStorage extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "risala.db";
    public static final String DATABASE_TABLE_ARTICLES = "Articles";
    public static final String ARTICLE_COLUMN_ID = "id";
    public static final String ARTICLE_COLUMN_TITLE = "title";
    public static final String ARTICLE_COLUMN_BODY = "body";
    public static final String ARTICLE_COLUMN_COVER_PATH = "coverPath";
    public static final String ARTICLE_COLUMN_MAGZINE_ID = "magzineId";
    public static final String ARTICLE_COLUMN_AUTHOR_ID = "authorId";

    public static final String DATABASE_TABLE_MAGZINES = "Magzines";
    public static final String MAGZINE_COLUMN_ID = "id";
    public static final String MAGZINE_COLUMN_NAME = "name";
    public static final String MAGZINE_COLUMN_COVER_PATH = "coverPath";
    public static final String MAGZINE_COLUMN_EDITOR_ID = "editorId";

    public static final String DATABASE_TABLE_VOTES="ArticleVotes";
    public static final String VOTES_ID = "id";
    public static final String VOTES_VALUE = "value";
    public static final String VOTES_ARTICLE = "articleId";
    public static final String VOTES_MAGZINE = "magzineId";
    public static final String VOTES_USER = "userId";


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "Create Table "+ DATABASE_TABLE_MAGZINES +" ("+
                MAGZINE_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MAGZINE_COLUMN_NAME+" TEXT, "+
                MAGZINE_COLUMN_COVER_PATH+" TEXT, "+
                MAGZINE_COLUMN_EDITOR_ID+ " INTEGER );";

        db.execSQL(query);

        query = "Create Table "+ DATABASE_TABLE_ARTICLES +" ("+
                ARTICLE_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ARTICLE_COLUMN_TITLE+" TEXT, "+
                ARTICLE_COLUMN_BODY+" TEXT, "+
                ARTICLE_COLUMN_COVER_PATH+" TEXT, "+
                ARTICLE_COLUMN_AUTHOR_ID+ " INTEGER, "+
                ARTICLE_COLUMN_MAGZINE_ID+" INTEGER );";

        db.execSQL(query);

        query = "Create Table "+ DATABASE_TABLE_VOTES +" ("+
                VOTES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                VOTES_VALUE+" TEXT, "+
                VOTES_MAGZINE+" TEXT, "+
                VOTES_ARTICLE+ " TEXT, "+
                VOTES_USER+" TEXT );";
        db.execSQL(query);

        //Insert Default Magzines
        ContentValues c = new ContentValues();
        c.put(MAGZINE_COLUMN_NAME, "Saved Articles");

        try{

            Log.w("name", MAGZINE_COLUMN_NAME);
            Log.w("cover", MAGZINE_COLUMN_COVER_PATH);
            db.insert(DATABASE_TABLE_MAGZINES, null,c);

        } catch (SQLiteException e){
            Log.w("Exception", e.getMessage());
        }

        c = new ContentValues();
        c.put(MAGZINE_COLUMN_NAME, "All Articles");

        try{

            Log.w("name", MAGZINE_COLUMN_NAME);
            Log.w("cover", MAGZINE_COLUMN_COVER_PATH);
            db.insert(DATABASE_TABLE_MAGZINES, null,c);

        } catch (SQLiteException e){
            Log.w("Exception", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS Magzines");

        // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS Articles");

        // Recreates the database with a new version
        // Recreates the database with a new version

        db.execSQL("DROP TABLE IF EXISTS ArticleVotes");
        onCreate(db);
    }

    public void insertVote(ArticleVote vote){

        ContentValues c = new ContentValues();
        c.put(VOTES_VALUE, String.valueOf(vote.getValue()));
        c.put(VOTES_ARTICLE, String.valueOf(vote.getArticle()));
        c.put(VOTES_MAGZINE, String.valueOf(vote.getMagzine()));
        c.put(VOTES_USER, String.valueOf(vote.getUser()));


        SQLiteDatabase db = getWritableDatabase();

        try{


            db.insert(DATABASE_TABLE_VOTES, null,c);

            String query = "Select id from " + DATABASE_TABLE_VOTES+" where "+VOTES_ARTICLE+" = 1 and "+VOTES_VALUE+" = 1 and "+VOTES_MAGZINE+" = 3";
            Cursor cr = db.rawQuery(query, null);
            cr.moveToFirst();


        } catch (SQLiteException e){
            Log.w("Exception", e.getMessage());
        }

    }
    public int getAllUpvotes(Integer articleId, Integer magzineId){

        int totalUpvotes = 0;
        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * from " + DATABASE_TABLE_VOTES+" where "+VOTES_ARTICLE+" = "+articleId.toString()+" and "+VOTES_VALUE+" = 1 and "+VOTES_MAGZINE+" = "+magzineId.toString();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        totalUpvotes += c.getCount();

        c.close();
        db.close();

        return totalUpvotes;

    }

    public boolean deleteVote(Integer userId, Integer articleId, Integer magzineId, Integer voteValue) {

        SQLiteDatabase db = getWritableDatabase();
        String query = "Delete from " + DATABASE_TABLE_VOTES+" where "+VOTES_ARTICLE+" = "+articleId.toString()+" and "+VOTES_VALUE+" = "+voteValue.toString()+" and "+VOTES_MAGZINE+" = "+magzineId.toString()+" and "+VOTES_USER+" = "+userId.toString();
        int rows = 0;
        try {


            rows = db.delete(DATABASE_TABLE_VOTES, VOTES_ARTICLE+" =?"+" and "+VOTES_VALUE+" =?"+" and "+VOTES_MAGZINE+" =?"+" and "+VOTES_USER+" =?", new String[]{articleId.toString(), voteValue.toString(), magzineId.toString(), userId.toString()} );
            // Cursor cr =  db.rawQuery(query, null);

        } catch (SQLException e) {
            Log.w("Deletion Vote Error ", e.getMessage());

        }
        if(rows > 0)
            return true;
        else
            return false;
    }
    public boolean alreadyCastedVote(Integer userId, Integer articleId, Integer magzineId, Integer voteValue) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from " + DATABASE_TABLE_VOTES+" where "+VOTES_ARTICLE+" = "+articleId.toString()+" and "+VOTES_VALUE+" = "+voteValue.toString()+" and "+VOTES_MAGZINE+" = "+magzineId.toString()+" and "+VOTES_USER+" = "+userId.toString();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() == 0)
            return false;
        else if (c.getCount() > 0)
            return true;
        else
            return true;
    }

    public int getAllDownvotes(Integer articleId, Integer magzineId){

        int totalDownvotes = 0;
        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * from " + DATABASE_TABLE_VOTES+" where "+VOTES_ARTICLE+" = "+articleId.toString()+" and "+VOTES_VALUE+" = -1 and "+VOTES_MAGZINE+" = "+magzineId.toString();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        totalDownvotes += c.getCount();

        c.close();
        db.close();

        return totalDownvotes;

    }

    public void insertMagzine(Magazine magzine){

        ContentValues c = new ContentValues();
        c.put(MAGZINE_COLUMN_NAME, magzine.getTitleResId());
        c.put(MAGZINE_COLUMN_COVER_PATH, magzine.getImageResId());
        //c.put(MAGZINE_COLUMN_EDITOR_ID, magzine.getEditorId());



        SQLiteDatabase db = getWritableDatabase();

       // int nRowsEffected = db.update(DATABASE_NAME, c, "name = '"+magzine.getTitleResId()+"'", null);

//        if(nRowsEffected <= 0) {
            try{

                Log.w("name", MAGZINE_COLUMN_NAME);
                Log.w("cover", MAGZINE_COLUMN_COVER_PATH);
                db.insert(DATABASE_TABLE_MAGZINES, null,c);

            } catch (SQLiteException e){
                Log.w("Exception", e.getMessage());
            }
      //  }



    }
    public void deleteMagzine(Integer id){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+DATABASE_TABLE_MAGZINES+" where id ="+id.toString());

    }
    public Magazine getMagzine(Integer magzineId) {

        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * from " + DATABASE_TABLE_MAGZINES+" where id = "+magzineId.toString()+"";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        Magazine magzine = null;
        if (!c.isAfterLast()){
            magzine= new Magazine();
            magzine.set_id(c.getInt(c.getColumnIndex("id")));
            magzine.setTitleResId(c.getString(c.getColumnIndex("name")));
            magzine.setImageResId(c.getString(c.getColumnIndex("coverPath")));
            //magzine.setEditorId(c.getInt(c.getColumnIndex("editorId")));

        }
        c.close();
        db.close();

        return magzine;
    }
    public ArrayList<Magazine> getAllMagzines() {

        ArrayList<Magazine> magzines = new ArrayList<Magazine>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+DATABASE_TABLE_MAGZINES;
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("name"))!=null) {
                Magazine magzine = new Magazine();
                magzine.set_id(c.getInt(c.getColumnIndex("id")));
                magzine.setTitleResId(c.getString(c.getColumnIndex("name")));
                magzine.setImageResId(c.getString(c.getColumnIndex("coverPath")));
                // magzine.setEditorId(c.getInt(c.getColumnIndex("editorId")));


                magzines.add(magzine);

            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return magzines;
    }

    public LocalStorage(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    public void insertArticle(Article article){

        ContentValues c = new ContentValues();
        c.put(ARTICLE_COLUMN_TITLE, article.getArticleTitle());
        c.put(ARTICLE_COLUMN_BODY, article.getArticleBody());
        c.put(ARTICLE_COLUMN_COVER_PATH, article.getArticleCover());
        c.put(ARTICLE_COLUMN_MAGZINE_ID, article.getMagzineId());
        //c.put(ARTICLE_COLUMN_AUTHOR_ID, 1);

        SQLiteDatabase db = getWritableDatabase();
      //  int nRowsEffected = db.update(DATABASE_NAME, c, "name = '"+article.getArticleTitle()+"'", null);

        //if(nRowsEffected <= 0) {
            try{

                db.insert(DATABASE_TABLE_ARTICLES, null,c);
                // Log.w("Inserting...", String.valueOf( db.insert(DATABASE_TABLE_ARTICLES, null,c)));

            } catch (SQLiteException e){
                Log.w("Exception", e.getMessage());
            }
//        }



    }
    public void updateArticle(Article article){

        ContentValues c = new ContentValues();
        if(article.get_id()!=0)
            c.put(ARTICLE_COLUMN_ID, article.get_id());

        if(article.getArticleTitle()!= null && !article.getArticleTitle().isEmpty())
            c.put(ARTICLE_COLUMN_TITLE, article.getArticleTitle());

        if(article.getArticleTitle()!= null && !article.getArticleTitle().isEmpty())
            c.put(ARTICLE_COLUMN_BODY, article.getArticleBody());

        if(article.getArticleBody()!= null && !article.getArticleBody().isEmpty())
            c.put(ARTICLE_COLUMN_COVER_PATH, article.getArticleCover());
        if(article.getMagzineId()!= 0)
            c.put(ARTICLE_COLUMN_MAGZINE_ID, article.getMagzineId());

        if(article.getAuthorId()!=0)
            c.put(ARTICLE_COLUMN_AUTHOR_ID, 1);

        SQLiteDatabase db = getWritableDatabase();

        try{

            //db.update(DATABASE_TABLE_ARTICLES, c, )
            int affectedRows  = db.update(DATABASE_TABLE_ARTICLES, c, " id = "+article.get_id(), null);

            Log.w("Affected Rows!", String.valueOf(affectedRows));

        } catch (SQLiteException e){
            Log.w("Exception", e.getMessage());
        }

    }

    public void deleteArticle(Integer id){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+DATABASE_TABLE_ARTICLES+" where id ="+id.toString());

    }
    public Article getArticle(Integer articleId) {
        Article article= new Article();
        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * from " + DATABASE_TABLE_ARTICLES+" where id = "+articleId.toString()+"";
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        if (!c.isAfterLast()){
            article.set_id(c.getInt(c.getColumnIndex("id")));
            article.setArticleTitle(c.getString(c.getColumnIndex("title")));
            article.setArticleBody(c.getString(c.getColumnIndex("body")));
            article.setArticleCover(c.getString(c.getColumnIndex("coverPath")));
            article.setMagzineId(c.getInt(c.getColumnIndex("magzineId")));
            article.setAuthorId(c.getInt(c.getColumnIndex("authorId")));

        }
        c.close();
        db.close();

        return article;
    }

    public boolean checkArticle(String name) {

        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * from " + DATABASE_TABLE_ARTICLES+" where title = \""+name+"\"";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(c.getCount() > 0){

            return true;
        }
        else return false;



    }
    public ArrayList<Article> getMagazineArticles(Integer magazineID)
    {
        ArrayList<Article> articles = new ArrayList<Article>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+DATABASE_TABLE_ARTICLES+" where magzineId = "+magazineID.toString()+"";
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();


        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("title"))!=null) {
                Article article = new Article();
                article.set_id(c.getInt(c.getColumnIndex("id")));
                article.setArticleTitle(c.getString(c.getColumnIndex("title")));
                article.setArticleBody(c.getString(c.getColumnIndex("body")));
                article.setArticleCover(c.getString(c.getColumnIndex("coverPath")));
                article.setMagzineId(c.getInt(c.getColumnIndex("magzineId")));
                //article.setAuthorId(c.getInt(c.getColumnIndex("authorId")));

                articles.add(article);

            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return articles;
    }
    public ArrayList<Article> getAllArticles() {

        ArrayList<Article> articles = new ArrayList<Article>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+DATABASE_TABLE_ARTICLES;
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();


        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("title"))!=null) {
                Article article = new Article();
                article.set_id(c.getInt(c.getColumnIndex("id")));
                article.setArticleTitle(c.getString(c.getColumnIndex("title")));
                article.setArticleBody(c.getString(c.getColumnIndex("body")));
                article.setArticleCover(c.getString(c.getColumnIndex("coverPath")));
                article.setMagzineId(c.getInt(c.getColumnIndex("magzineId")));
                //article.setAuthorId(c.getInt(c.getColumnIndex("authorId")));

                articles.add(article);

            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return articles;
    }
}
