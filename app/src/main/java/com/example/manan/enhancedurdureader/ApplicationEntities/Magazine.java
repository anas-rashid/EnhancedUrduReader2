package com.example.manan.enhancedurdureader.ApplicationEntities;

/**
 * Created by manan on 3/9/17.
 */

public class Magazine {
    public int _id;
    public String imageResId;
    public String titleResId;
    public int editorId;

    public Magazine(){
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitleResId() {
        return titleResId;
    }

    public void setTitleResId(String titleResId) {
        this.titleResId = titleResId;
    }

    public int getEditorId() {
        return editorId;
    }

    public void setEditorId(int editorId) {
        this.editorId = editorId;
    }
}
