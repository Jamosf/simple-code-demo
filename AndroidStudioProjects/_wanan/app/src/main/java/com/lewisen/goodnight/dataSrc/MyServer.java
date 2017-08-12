package com.lewisen.goodnight.dataSrc;

/**
 * 存放与服务器相关的数据
 *
 * @author Lewisen
 */
public class MyServer {
    public static final String URL = "http://42.96.149.128:8080/goodNightServer/";
    public static final String HOME_PAGE = URL + "HomePageServlet?id=";
    public static final String ARTICLE_PAGE = URL + "ArticlePageServlet?id=";
    public static final String PICTURE_PAGE = URL + "PicturePageServlet?id=";
    public static final String SECOND_IMAGE = URL + "SecondImageServlet";
    public static final String PICTURE_URL = URL + "PictureServlet?path=";
    public static final String SHARE_URL = URL + "ShareContent?";
    public static final String APP_ICON = PICTURE_URL + "C://pic//app.jpg";
    public static final String MAX_ID = URL + "MaxIdManageServlet?order=get";
    public static final String LIKE = URL + "LikeServlet";
}
