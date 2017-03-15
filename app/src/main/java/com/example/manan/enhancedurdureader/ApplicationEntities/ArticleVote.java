package com.example.manan.enhancedurdureader.ApplicationEntities;

/**
 * Created by manan on 3/9/17.
 */

public class ArticleVote {

    int _id,
            value,
            downvotes,
            magzine,
            article,
            user;

    public int totalVotes(){
        return value -downvotes;
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getMagzine() {
        return magzine;
    }

    public void setMagzine(int magzine) {
        this.magzine = magzine;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}

