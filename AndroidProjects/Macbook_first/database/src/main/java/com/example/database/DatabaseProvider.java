package com.example.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.security.PublicKey;

/**
 * Created by fengshangren on 16/8/7.
 */
public class DatabaseProvider extends ContentProvider{

    public static final int BOOK_DIR = 0;

    public static final int BOOK_ITEM = 1;

    public static final int CATEFORY_DIR = 2;

    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.example.database.provder";

    private static UriMatcher uriMatcher;

    private Mydatabasehelper mydatabasehelper;

    static {
        uriMatcher = new UriMatcher(uriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEFORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }


    @Override
    public boolean onCreate() {

        mydatabasehelper = new Mydatabasehelper(getContext(),"BookStore",null,2)

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        //查询数据
        SQLiteDatabase db = mydatabasehelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("Book",strings,s,strings1,null,null,s1);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book",strings,"id = ?",new String[]{bookId},null,null,s1);
                break;
            case CATEFORY_DIR:
                cursor = db.query("Category",strings,s,strings1,null,null,s1);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category",strings,"id = ?",new String[]{categoryId},null,null,s1);
                break;
            default:
                break;


        }


        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mydatabasehelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book",null,contentValues);
                uriReturn = uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEFORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category",null,contentValues);
                uriReturn = uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                break;
            default:
                break;

        }


        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = mydatabasehelper.getWritableDatabase();
        int deleteRow = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRow = db.delete("Book",s,strings);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Book","id = ?",new String[]{bookId});
                break;
            case CATEFORY_DIR:
                deleteRow = db.delete("Category",s,strings);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Category","id = ?", new String[]{categoryId});
                break;
            default:
                break;
        }


        return deleteRow;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
