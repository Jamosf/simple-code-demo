package com.lewisen.goodnight.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

/**
 * 播放音乐的服务
 */
public class PlayerService extends Service implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {
    public static final String MUSIC_ACTION = "com.lewisen.player.action.MUSIC";
    private MediaPlayer mediaPlayer;
    private PlayerBinder playerBinder;
    private String url;
    private boolean isPlaying;
    private int percent;//网络加载进度

    @Override
    public void onCreate() {
        super.onCreate();
//        Util.printInfo("播放器  Service onCreate");

        playerBinder = new PlayerBinder();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        isPlaying = false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Util.printInfo("播放器   Service onStartCommand  " + "  - flags =" + flags + "; startid = " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        Util.printInfo(" 播放器  Service onDestroy");

        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        Util.printInfo(" 播放器  Service onBind");
        return playerBinder;
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.percent = percent;

    }

    /**
     * 数据准备完毕
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    class PlayerBinder extends Binder {

        /**
         * 这里开启播放器，耗时略多，故开启新线程启动吗、播放器
         */
        public void playMusic(final String u) {
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(u)) return;
                            isPlaying = true;

                            if (!u.equals(url)) {
                                url = u;
                                mediaPlayer.reset();
                                try {
                                    mediaPlayer.setDataSource(url);
                                    mediaPlayer.prepare();
                                    mediaPlayer.setLooping(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mediaPlayer.start();
                            }

                        }
                    }
            ).start();
        }

        public void stopMusic() {
            isPlaying = false;
            mediaPlayer.pause();
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public String getMusicPath() {
            return url;
        }

        public int getDuration() {
            if (mediaPlayer == null) return 0;
            try {
                if (!mediaPlayer.isPlaying()) return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mediaPlayer.getDuration();
        }

        public int getPosition() {
            if (mediaPlayer == null) return 0;
            return mediaPlayer.getCurrentPosition();
        }

        public int getPercent() {
            return percent;
        }

    }

}
