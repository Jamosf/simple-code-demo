package com.lewisen.goodnight.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.bean.ArticlePage;
import com.lewisen.goodnight.bean.DispalyPage;
import com.lewisen.goodnight.bean.HomePage;
import com.lewisen.goodnight.bean.PicturePage;
import com.lewisen.goodnight.cache.SaveImage;
import com.lewisen.goodnight.collection.Favourite;
import com.lewisen.goodnight.controller.DisplayImage;
import com.lewisen.goodnight.controller.LoadPageData;
import com.lewisen.goodnight.dataSrc.MyServer;
import com.lewisen.goodnight.dataSrc.PageData;
import com.lewisen.goodnight.player.Player;
import com.lewisen.goodnight.social.Like;
import com.lewisen.goodnight.social.Social;

public class DisplayDetailActivity extends AppCompatActivity implements
        LoadPageData.LoadPageDataListener, View.OnClickListener, Like.LikeListener {
    private int type;
    private int pageID;
    private LoadPageData loadPageData;
    private DispalyPage dispalyPage;
    private DisplayImage displayImage;
    private Player player;
    private Social social;
    private ImageView likeView;
    private TextView likeText;
    private Like like;
    private int likeCount;
    private boolean likeState;
    private Favourite favourite;//收藏相关
    private boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 显示夜间 日间模式
        if (MyApplication.appConfig.isNightModeSwitch()) {
            this.setTheme(R.style.NightTheme);
        } else {
            this.setTheme(R.style.DayTheme);
        }
        setContentView(R.layout.activity_display_detail);
        type = getIntent().getIntExtra("type", 0);
        pageID = getIntent().getIntExtra("pageID", 0);
        isFav = getIntent().getBooleanExtra("isFav", false);
        dispalyPage = new DispalyPage();
        displayImage = new DisplayImage();
        social = new Social(this);
        loadPageData = new LoadPageData(type, this);
        favourite = new Favourite(this);
        like = new Like(this, type, pageID, this);
        //加载网络数据
        loadPageData.startLoadPageData(pageID, true);
        initView();


    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            String title = " ";
            if (type == PageData.TYPE_HOME) {
                title = "主页";
            } else if (type == PageData.TYPE_ARTICLE) {
                title = "文章";
            } else if (type == PageData.TYPE_PICTURE) {
                title = "图片";
            }
            actionBar.setTitle(title);
        }

        likeView = (ImageView) findViewById(R.id.like_image);
        likeText = (TextView) findViewById(R.id.like_count);

        likeText.setOnClickListener(this);
        likeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //如果当前是喜欢状态
        if (likeState) {
            likeState = false;
            likeText.setText((--likeCount) + "");
            likeView.setImageResource(R.mipmap.unlike);
            like.changeLikeState(-1);
        } else {
            likeState = true;
            likeText.setText((++likeCount) + "");
            likeView.setImageResource(R.mipmap.like);
            like.changeLikeState(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_set).setTitle(MyApplication.appConfig.isNightModeSwitch() ? "日间" : "夜间");
        if (isFav) {
            menu.findItem(R.id.action_collected).setTitle("移除");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_comment:
                social.openComment();
                break;

            case R.id.action_share:
                social.openShare();
                break;

            case R.id.action_collected:
                if (isFav) {
                    favourite.deleteFavourite(type, pageID);
                    Toast.makeText(this, "移除收藏成功~", Toast.LENGTH_LONG).show();
                } else {
                    favourite.addFavourite(type, pageID);
                    Toast.makeText(this, "添加收藏成功~", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.action_set:
                if (MyApplication.appConfig.isNightModeSwitch()) {
                    MyApplication.appConfig.setNightModeSwitch(false);
                } else {
                    MyApplication.appConfig.setNightModeSwitch(true);
                }
                recreate();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.destory();
        }
    }

    @Override
    public void onLoadPage(Object object) {
        if (type == PageData.TYPE_HOME) {
            HomePage homePage = (HomePage) object;
            dispalyPage.setPageID(homePage.getHomePageID());
            dispalyPage.setReadCount(homePage.getReadCount());
            dispalyPage.setDate(homePage.getDate());
            dispalyPage.setTitle(homePage.getTitle());
            dispalyPage.setAuthor(homePage.getAuthor());
            dispalyPage.setText(homePage.getText());
            dispalyPage.setAuthorIntro(homePage.getAuthorIntro());
            dispalyPage.setImageSrc(homePage.getImageSrc());
            dispalyPage.setMusicAuthor(homePage.getMusicAuthor());
            dispalyPage.setMusicURL(homePage.getMusicURL());
            dispalyPage.setMusicImage(homePage.getMusicImage());
            dispalyPage.setMusicTitle(homePage.getMusicTitle());
        } else if (type == PageData.TYPE_ARTICLE) {
            ArticlePage articlePage = (ArticlePage) object;
            dispalyPage.setPageID(articlePage.getArticlePageID());
            dispalyPage.setReadCount(articlePage.getReadCount());
            dispalyPage.setDate(articlePage.getDate());
            dispalyPage.setTitle(articlePage.getTitle());
            dispalyPage.setAuthor(articlePage.getAuthor());
            dispalyPage.setText(articlePage.getText());
            dispalyPage.setAuthorIntro(articlePage.getAuthorIntro());
        } else if (type == PageData.TYPE_PICTURE) {
            PicturePage picturePage = (PicturePage) object;
            dispalyPage.setPageID(picturePage.getPicturePageID());
            dispalyPage.setReadCount(picturePage.getReadCount());
            dispalyPage.setDate(picturePage.getDate());
            dispalyPage.setImageSrc(picturePage.getImageSrc());
            dispalyPage.setText(picturePage.getText());
            dispalyPage.setAuthorIntro(picturePage.getAuthorIntro());
        }

        //显示界面
        display();

        //初始化社会化组件
        social.init(type, pageID, dispalyPage);

        //获取like状态
        like.getLikeCount();
    }

    @Override
    public void onLoadError() {
        Toast.makeText(this, "获取更新失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadMaxID(boolean state) {

    }

    /**
     * Like回调函数
     */
    @Override
    public void onLikeEvent(int count, boolean state) {
        likeCount = count;
        likeState = state;

        likeText.setVisibility(View.VISIBLE);
        likeView.setVisibility(View.VISIBLE);
        likeText.setText(count + "");
        likeView.setImageResource((state ? R.mipmap.like : R.mipmap.unlike));
    }

    private void display() {
        TextView date = (TextView) findViewById(R.id.date_dis);
        TextView title = (TextView) findViewById(R.id.title_dis);
        TextView author = (TextView) findViewById(R.id.author_dis);
        TextView readCount = (TextView) findViewById(R.id.read_count_dis);

        TextView authorIntro = (TextView) findViewById(R.id.author_intro_dis);
        ImageView eye = (ImageView) findViewById(R.id.eye_dis);

        View topLine = findViewById(R.id.line_dis);
        View bottomLine = findViewById(R.id.line_bottom_dis);

        ProgressBar loading = (ProgressBar) findViewById(R.id.loading_bar_dis);
        loading.setVisibility(View.GONE);

        topLine.setVisibility(View.VISIBLE);
        bottomLine.setVisibility(View.VISIBLE);
        eye.setVisibility(View.VISIBLE);

        date.setText(dispalyPage.getDate());
        authorIntro.setText(dispalyPage.getAuthorIntro());
        readCount.setText(dispalyPage.getReadCount() + "");

        if ((type == PageData.TYPE_HOME) || (type == PageData.TYPE_ARTICLE)) {
            title.setText(dispalyPage.getTitle());
            author.setText(dispalyPage.getAuthor());
        }

        //显示图文的线性布局
        LinearLayout imageTextLayout = (LinearLayout) findViewById(R.id.layout_image_text_dis);
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout imageText = (RelativeLayout) inflater.inflate(R.layout.image_text, imageTextLayout, false);
        if (type == PageData.TYPE_ARTICLE) {
            TextView textView = (TextView) imageText.findViewById(R.id.text_image_text);
            textView.setVisibility(View.VISIBLE);
            textView.setText(dispalyPage.getText());
            // 加载视图
            imageTextLayout.addView(imageText);
        } else if (type == PageData.TYPE_PICTURE) {
            title.setVisibility(View.GONE);
            author.setVisibility(View.GONE);
            TextView textView = (TextView) imageText.findViewById(R.id.text_image_text);
            textView.setVisibility(View.VISIBLE);
            textView.setText(dispalyPage.getText());
            ImageView imageView = (ImageView) imageText.findViewById(R.id.picture_image_text);
            imageView.setVisibility(View.VISIBLE);
            displayImage.display(imageView, MyServer.PICTURE_URL + dispalyPage.getImageSrc());

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                final String url = MyServer.PICTURE_URL + dispalyPage.getImageSrc();

                @Override
                public boolean onLongClick(View v) {
                    SaveImage saveImage = new SaveImage();
                    String path = saveImage.saveImage(displayImage.getImageCachePath(url));
                    if (path != null) {
                        Toast.makeText(DisplayDetailActivity.this, "图片已保存到" + path, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DisplayDetailActivity.this, "图片保存失败，请重试", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
            // 加载视图
            imageTextLayout.addView(imageText);
        } else if (type == PageData.TYPE_HOME) {
            String[] imageSrcPart = dispalyPage.getImageSrc().split("###");
            String[] textPart = dispalyPage.getText().split("###");
            int textLength = textPart.length;// 文本分割为几个部分，从1开始
            int imageLength = imageSrcPart.length;

            for (int i = 0; i < textLength; i++) {
                // 加载一个布局
                imageText = (RelativeLayout) inflater.inflate(R.layout.image_text, imageTextLayout, false);
                ImageView imageView = (ImageView) imageText.findViewById(R.id.picture_image_text);
                TextView textView = (TextView) imageText.findViewById(R.id.text_image_text);
                textView.setVisibility(View.VISIBLE);
                String imageSrc = " ";
                if (i < imageLength) {//检查角标
                    imageSrc = imageSrcPart[i];
                }
                //对图片进行判断
                if (imageSrc.length() > 10) { //长度大于10 有效地址
                    imageView.setVisibility(View.VISIBLE);
                    displayImage.display(imageView, MyServer.PICTURE_URL + imageSrc);
                    final String u = imageSrc;
                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        final String url = MyServer.PICTURE_URL + u;

                        @Override
                        public boolean onLongClick(View v) {
                            SaveImage saveImage = new SaveImage();
                            String path = saveImage.saveImage(displayImage.getImageCachePath(url));
                            if (path != null) {
                                Toast.makeText(DisplayDetailActivity.this, "图片已保存到" + path, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DisplayDetailActivity.this, "图片保存失败，请重试", Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }
                    });
                }
                // 如果有@@@ 代表这里需要播放器 插入在图文显示的前面
                if (textPart[i].contains("@@@")) {
                    String musicTitle = dispalyPage.getMusicTitle();
                    String musicAuthor = dispalyPage.getMusicAuthor();
                    String musicURL = dispalyPage.getMusicURL();
                    String musicImage = dispalyPage.getMusicImage();
                    if ((musicTitle == null) || (musicAuthor == null) || (musicURL == null)) {
                        textView.setText(textPart[i].replace("@@@", ""));
                        imageTextLayout.addView(imageText);
                        continue;
                    }
                    RelativeLayout musicView = (RelativeLayout) inflater.inflate(R.layout.music_view, imageTextLayout,
                            false);
                    ProgressBar seekBar = (ProgressBar) musicView.findViewById(R.id.music_progress);
                    TextView musicTimeText = (TextView) musicView.findViewById(R.id.music_time);
                    ImageButton playButton = (ImageButton) musicView.findViewById(R.id.music_button);
                    TextView musicTitleView = (TextView) musicView.findViewById(R.id.music_title);
                    TextView musicAuthorView = (TextView) musicView.findViewById(R.id.music_author);
                    if (!TextUtils.isEmpty(musicImage)) {
                        ImageView musicIcon = (ImageView) musicView.findViewById(R.id.music_icon);
                        displayImage.display(musicIcon, MyServer.PICTURE_URL + musicImage, 20);
                    }
                    musicTitleView.setText(musicTitle);
                    musicAuthorView.setText(musicAuthor);
                    // 播放音乐初始化
                    player = new Player(this, musicURL, playButton, musicTimeText, seekBar,
                            musicTitle, musicAuthor, MyServer.PICTURE_URL + musicImage);
                    player.initPlayer();

                    // 添加音乐播放界面到主视图
                    imageTextLayout.addView(musicView);

                    textView.setText(textPart[i].replace("@@@", ""));
                } else {
                    textView.setText(textPart[i]);
                }
                imageTextLayout.addView(imageText);
            }
        }
    }

}
