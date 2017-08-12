package com.lewisen.goodnight.dataSrc;


import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.lewisen.goodnight.bean.ArticlePage;
import com.lewisen.goodnight.bean.HomePage;
import com.lewisen.goodnight.bean.PicturePage;

/**
 * 操作数据库
 * Created by Lewisen on 2015/9/9.
 */
public class OperateDb {

    /**
     * @param t 需传递一个实例化的对象
     * @param pageID 获取的对象ID号
     * @param <T> 对象类型
     * @return 对象
     */
    public static <T> Object getPage(T t, int pageID) {
        if (t instanceof HomePage) {
            return new Select().from(HomePage.class).where("homePageID = ?", pageID).<HomePage>executeSingle();
        } else if (t instanceof ArticlePage) {
            return new Select().from(ArticlePage.class).where("articlePageID = ?", pageID).<ArticlePage>executeSingle();
        } else if (t instanceof PicturePage) {
            return new Select().from(PicturePage.class).where("picturePageID = ?", pageID).<PicturePage>executeSingle();
        } else return null;
    }

    /**
     * @param t 实例化的一个对象，保存有需要保存到数据库的内容
     * @param <T> 类型
     */
    public static <T> void addPage(T t) {
        if (t instanceof HomePage) {
            int homePageID = ((HomePage) t).getHomePageID();
            From from = new Select().from(HomePage.class).where("homePageID = ?", homePageID);
            if (from.exists()) {
                new Delete().from(HomePage.class).where("homePageID = ?", homePageID).execute();
            }
            ((HomePage) t).save();
        } else if (t instanceof ArticlePage) {
            int articlePageID = ((ArticlePage) t).getArticlePageID();
            From from = new Select().from(ArticlePage.class).where("articlePageID = ?", articlePageID);
            if (from.exists()) {
                new Delete().from(ArticlePage.class).where("articlePageID = ?", articlePageID).execute();
            }
            ((ArticlePage) t).save();
        } else if (t instanceof PicturePage) {
            int picturePageID = ((PicturePage) t).getPicturePageID();
            From from = new Select().from(PicturePage.class).where("picturePageID = ?", picturePageID);
            if (from.exists()) {
                new Delete().from(PicturePage.class).where("picturePageID = ?", picturePageID).execute();
            }
            ((PicturePage) t).save();
        }
    }

}
