package com.lewisen.goodnight.bean;

import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

//数据库表
@Table(name = "HomePage")
public class HomePage extends Model implements IPage{
    @Column(name = "homePageID")
    private int homePageID;

    @Column(name = "date")
    private String date;

    @Column(name = "title")
    private String title;

    @Column(name = "imageSrc")
    private String imageSrc;

    @Column(name = "author")
    private String author;

    @Column(name = "text")
    private String text;

    @Column(name = "readCount")
    private int readCount;

    @Column(name = "authorIntro")
    private String authorIntro;

    @Column(name = "musicAuthor")
    private String musicAuthor;

    @Column(name = "musicTitle")
    private String musicTitle;

    @Column(name = "musicURL")
    private String musicURL;

    @Column(name = "musicImage")
    private String musicImage;

    //默认的构造方法
    public HomePage() {
        super();
    }

    public int getHomePageID() {
        return homePageID;
    }

    public void setHomePageID(int homePageID) {
        this.homePageID = homePageID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
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

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }

    public String getMusicImage() {
        return musicImage;
    }

    public void setMusicImage(String musicImage) {
        this.musicImage = musicImage;
    }

    @Override
    public void setPage(JSONObject jsonObject) {
        try {
            this.homePageID = jsonObject.getInt("homePageID");
            this.date = jsonObject.getString("date");
            this.title = jsonObject.getString("title");
            this.author = jsonObject.getString("author");
            this.text = jsonObject.getString("text");
            this.readCount = jsonObject.getInt("readCount");
            this.authorIntro = jsonObject.getString("authorIntro");
            this.imageSrc = jsonObject.getString("image");
            this.musicURL = jsonObject.getString("musicURL");
            if (!TextUtils.isEmpty(this.musicURL)) {
                this.musicTitle = jsonObject.getString("musicTitle");
                this.musicAuthor = jsonObject.getString("musicAuthor");
                this.musicImage = jsonObject.getString("musicImage");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
