package com.lewisen.goodnight.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.lewisen.goodnight.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "ArticlePage")
public class ArticlePage extends Model implements IPage {

    @Column(name = "articlePageID")
    private int articlePageID;

    @Column(name = "date")
    private String date;

    @Column(name = "title")
    private String title;

    @Column(name = "comment")
    private String comment;

    @Column(name = "author")
    private String author;

    @Column(name = "text")
    private String text;

    @Column(name = "readCount")
    private int readCount;

    @Column(name = "authorIntro")
    private String authorIntro;

    public ArticlePage() {
        super();
    }

    public int getArticlePageID() {
        return articlePageID;
    }

    public void setArticlePageID(int articlePageID) {
        this.articlePageID = articlePageID;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
            this.articlePageID = jsonObject.getInt("articlePageID");
            this.date = jsonObject.getString("date");
            this.title = jsonObject.getString("title");
            this.author = jsonObject.getString("author");
            this.text = jsonObject.getString("text");
            this.readCount = jsonObject.getInt("readCount");
            this.authorIntro = jsonObject.getString("authorIntro");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //新加入的 用来显示在列表里面的段落
        try {
            this.comment = jsonObject.getString("comment");
        } catch (JSONException e) {
            e.printStackTrace();
            this.comment = " ";
//            Util.printInfo("comment 没有");
        }

    }
}
