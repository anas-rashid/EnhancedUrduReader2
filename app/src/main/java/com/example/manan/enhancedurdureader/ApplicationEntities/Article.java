package com.example.manan.enhancedurdureader.ApplicationEntities;

import com.bumptech.glide.Glide;

/**
 * Created by manan on 3/9/17.
 */

public class Article {

    private int _id;
    private int magzineId;
    private String articleTitle;
    private String articleBody;
    public String articleCover;
    private int authorId;


    public int getAuthorId() {
        return authorId;
    }

    public Article() {
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Article(String articleTitle, String articleBody, String articleCover, int magzine, int author){
        this.articleTitle = articleTitle;
        this.articleBody = articleBody;
        this.articleCover = articleCover;
        this.magzineId = magzine;
        this.authorId = author;
    }

    public void setMagzineId(int magzineId) {
        this.magzineId = magzineId;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public int getMagzineId() {
        return magzineId;
    }

    public int get_id() {
        return _id;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public String getArticleCover() {
        return articleCover;
    }
}

