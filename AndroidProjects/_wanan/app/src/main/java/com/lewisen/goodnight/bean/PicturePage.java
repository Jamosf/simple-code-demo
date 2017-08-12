package com.lewisen.goodnight.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "PicturePage")
public class PicturePage extends Model implements IPage{
    @Column(name = "picturePageID")
    private int picturePageID;

    @Column(name = "date")
    private String date;

    @Column(name = "imageSrc")
    private String imageSrc;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "text")
    private String text;

    @Column(name = "readCount")
    private int readCount;

    @Column(name = "authorIntro")
    private String authorIntro;

    public PicturePage() {
        super();
    }

    public int getPicturePageID() {
        return picturePageID;
    }

    public void setPicturePageID(int picturePageID) {
        this.picturePageID = picturePageID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getAuthorIntro() {
        return authorIntro;
    }

    public void setAuthorIntro(String authorIntro) {
        this.authorIntro = authorIntro;
    }

    @Override
    public void setPage(JSONObject jsonObject) {
        try {
            this.picturePageID = jsonObject.getInt("picturePageID");
            this.date = jsonObject.getString("date");
//            this.title = jsonObject.getString("title");
//            this.author = jsonObject.getString("author");
            this.text = jsonObject.getString("text");
            this.readCount = jsonObject.getInt("readCount");
            this.authorIntro = jsonObject.getString("authorIntro");
            this.imageSrc = jsonObject.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
