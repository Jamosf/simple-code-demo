package com.lewisen.goodnight.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.app.MyApplication;

import java.text.SimpleDateFormat;

/**
 * 音乐播放器
 * Created by Lewisen on 2015/9/13.
 */
public class Player implements View.OnClickListener {
    private PlayerServiceConnection serviceConn;
    private Context mContext;
    private ImageButton playButton;
    private TextView musicTime;
    private ProgressBar seekBar;
    private String url;
    private boolean isPlaying = false;//播放状态 停止
    private PlayerService.PlayerBinder playerBinder;
    private Handler mHandler;
    private Music music = MyApplication.music;
    private String author;
    private String title;
    private String imageUrl;
    private boolean isDrawer = false;//是否为drawer中的播放器


    /**
     *drawer中使用的构造函数
     */
    public Player(Context mContext, String url, ImageButton playButton, TextView musicTime,
                  ProgressBar seekBar) {
        this.url = url;
        this.playButton = playButton;
        this.mContext = mContext;
        this.musicTime = musicTime;
        this.seekBar = seekBar;
        isDrawer = true;
        if ((playerBinder != null) && (url.equals(playerBinder.getMusicPath()))) {
            isPlaying = playerBinder.isPlaying();
//            Util.printInfo("获取当前播放状态 = " + isPlaying);
        }
    }

    /**
     *非drawer中的构造函数
     */
    public Player(Context mContext, String url, ImageButton playButton, TextView musicTime,
                  ProgressBar seekBar, String title, String author, String imageUrl) {
        this(mContext, url, playButton, musicTime, seekBar);
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        isDrawer = false;
    }

    /**
     * 重设播放url
     */
    public void reSetUrl(String url) {
        this.url = url;
    }


    /**
     * 初始化播放器
     */
    public void initPlayer() {
        serviceConn = new PlayerServiceConnection();
        playButton.setOnClickListener(this);
        Intent startService = new Intent(mContext, PlayerService.class);
        mContext.startService(startService);
        mContext.bindService(startService, serviceConn, Context.BIND_AUTO_CREATE);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (isPlaying) {
                        int duration = playerBinder.getDuration();
                        int position = playerBinder.getPosition();
                        int percent = playerBinder.getPercent();

                        // Util.printInfo("duration=" + duration + ";positon=" + position + ";percent=" + percent);
                        if (duration > 0) {
                            seekBar.setProgress(100 * position / duration);
                        }
                        seekBar.setSecondaryProgress(percent);
                        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                        String ms = formatter.format(duration - position);
                        musicTime.setText("-" + ms);
                    }
                    if (mHandler == null) return;
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
    }

    /**
     * 取消绑定
     */
    public void destory() {
        if (mHandler != null) {
            mHandler.removeMessages(1);
            mHandler = null;
        }
        mContext.unbindService(serviceConn);
    }

    public void stopMusic() {
        if (mHandler != null) {
            mHandler.removeMessages(1);
        }
        isPlaying = false;
        playButton.setImageResource(R.mipmap.music_play);
        playerBinder.stopMusic();
        music.setIsPlaying(isPlaying);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(playButton)) {
            if (isPlaying) {
                mHandler.removeMessages(1);
                isPlaying = false;
                playButton.setImageResource(R.mipmap.music_play);
                playerBinder.stopMusic();
                Toast.makeText(mContext, "停止播放", Toast.LENGTH_SHORT).show();
            } else {
                isPlaying = true;
                playButton.setImageResource(R.mipmap.music_stop);
                Toast.makeText(mContext, "正在加载...", Toast.LENGTH_LONG).show();
                playerBinder.playMusic(url);
                mHandler.sendEmptyMessageDelayed(1, 900);

                if (!isDrawer){
                    music.setAuthor(author);
                    music.setTitle(title);
                    music.setUrl(url);
                    music.setUrlImage(imageUrl);
                }
            }
            music.setIsPlaying(isPlaying);
        }
    }


    class PlayerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            playerBinder = (PlayerService.PlayerBinder) service;
            if (TextUtils.isEmpty(url)) return;

            if (url.equals(playerBinder.getMusicPath())) {
                isPlaying = playerBinder.isPlaying();
            }
            if (isPlaying) {
                playButton.setImageResource(R.mipmap.music_stop);
                mHandler.sendEmptyMessageDelayed(1, 900);
            } else {
                playButton.setImageResource(R.mipmap.music_play);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Util.printInfo("onServiceDisconnected");

        }
    }
}
