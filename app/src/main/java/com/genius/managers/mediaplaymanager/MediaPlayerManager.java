package com.genius.managers.mediaplaymanager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangly on 2018/8/28.
 */

public class MediaPlayerManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private Context mContext;
    /**
     * 播放回调
     */
    private PlayCallback mPlayCallback;
    /**
     * 播放状态
     */
    private PlayStatus mPlayStatus;
    /**
     * 是否打印log
     */
    private boolean isShowlog = true;
    /**
     * 多个音频当前播放的位置
     */
    private int index;
    /**
     * 多个音频地址
     */
    private ArrayList<String> paths;
    /**
     * 音频缓存路径
     */
    private String FilePath;
    /**
     * 多个音频中间的停顿时间
     */
    private long delayTime = 1000;
    /**
     * 定时器
     */
    private Timer timer;

    public static MediaPlayerManager inStance;

    public static MediaPlayerManager getInStance(Context context) {
        if (inStance == null) {
            inStance = new MediaPlayerManager(context);
        }
        return inStance;
    }

    private MediaPlayerManager(Context context) {
        mContext = context;

        init();
    }

    /**
     * 初始化
     */
    private void init() {

//        FilePath = Environment.getExternalStorageDirectory().getPath();
        //app内部缓存目录
        FilePath = mContext.getCacheDir().getPath();
        timer= new Timer();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }


    /**
     * 播放多个音频
     *
     * @param paths：多个音频地址
     */
    public void play(ArrayList<String> paths) {
        if (paths != null && paths.size() > 0) {
            this.paths = paths;
            startDownload();
        } else {
            if (mPlayCallback != null) {
                mPlayCallback.onError("地址列表异常");
            }
        }
    }

    /**
     * 播放单个音频
     *
     * @param path：音频地址
     */
    public void play(String path) {
        paths = new ArrayList<>();
        paths.add(path);
        startDownload();
    }

    /**
     * 循环播放多个音频
     */
    private void play() {
        mPlayStatus = PlayStatus.PREPARED;
        index++;
        if (index < paths.size()) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    startDownload();
                }
            };
            timer.schedule(timerTask, delayTime);
        } else {
            stop();
        }
    }

    /**
     * 开始下载音频文件
     */
    private void startDownload() {
        mPlayStatus = PlayStatus.PREPARED;
        //如果是网络链接
        if(paths.get(index).startsWith("http"))
        {
            downloadFile(paths.get(index), new DownloadCallback() {
                @Override
                public void onSuccess(final File file) {
                    if (mPlayStatus == PlayStatus.PREPARED) {
                        try {
                            mMediaPlayer.reset();
                            mMediaPlayer.setDataSource(file.getAbsolutePath());
                            mMediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            onResError(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailed(final String errorMsg) {
                    if (mPlayCallback != null) {
                        mPlayCallback.onError(errorMsg);
                    }
                }
            });
        }
        //如果是其他地址
        else{
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mContext,Uri.parse(paths.get(index)));
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                onResError(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 资源出错
     */
    private void onResError(String msg)
    {
        if (mPlayCallback != null) {
            mPlayCallback.onError(" 资源出错：" + msg);
        }
        log(index + " 资源出错：" + msg);
        mPlayStatus = PlayStatus.ERROR;
    }

    /**
     * 暂停播放音频
     */
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayStatus = PlayStatus.PAUSED;
            log("暂停播放！");
        }
    }

    /**
     * 停止播放音频
     */
    public void stop() {
        if (mMediaPlayer.isPlaying() || mPlayStatus == PlayStatus.PREPARED) {
            index = 0;
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mPlayStatus = PlayStatus.STOPED;
            log("停止播放！");
        }
    }

    /**
     * 打印日志
     *
     * @param content
     */
    private void log(String content) {
        if (isShowlog) {
            Log.e(MediaPlayerManager.class.getSimpleName(), content);
        }
    }

    /**
     * 下载缓存音频文件
     *
     * @param url
     * @param downloadCallback
     */
    private void downloadFile(final String url, final DownloadCallback downloadCallback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(FilePath + File.separator + url.substring(url.lastIndexOf('/') + 1));
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                        URL url1 = new URL(url);
                        HttpURLConnection Connection = (HttpURLConnection) url1.openConnection();
                        Connection.setRequestMethod("GET");
                        Connection.setRequestProperty("Connection", "Keep-Alive");
                        Connection.setRequestProperty("Charset", "UTF-8");
                        Connection.setDoOutput(true);
                        Connection.setDoInput(true);
                        Connection.connect();
                        int totalLength = Connection.getContentLength();
                        InputStream inputStream = Connection.getInputStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int tempLength;
                        int totalRead = 0;
                        while ((tempLength = inputStream.read(buffer)) != -1) {
                            totalRead += tempLength;
                            fileOutputStream.write(buffer, 0, tempLength);
                            log("正在下载……" + totalRead * 100 / totalLength);
                        }
                        fileOutputStream.close();
                    }
                    downloadCallback.onSuccess(file);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    file.delete();
                    downloadCallback.onFailed(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    file.delete();
                    downloadCallback.onFailed(e.getMessage());
                }
            }
        });
        thread.start();
    }

    public void setShowlog(boolean isShowLog) {
        this.isShowlog = isShowLog;
    }

    public void setPlayCallback(final PlayCallback playCallback) {
        mPlayCallback = playCallback;
    }

    public PlayStatus getPlayStatus() {
        return mPlayStatus;
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        //音频准备完毕，开始播放
        mp.start();
        log("音频准备完毕，开始播放");
        mPlayStatus = PlayStatus.PLAYIND;
    }

    @Override
    public boolean onError(final MediaPlayer mp, final int what, final int extra) {
        if (mPlayCallback != null) {
            mPlayCallback.onError("播放出错！");
        }
        log("播放出错！");
        mPlayStatus = PlayStatus.ERROR;
        return false;
    }

    @Override
    public void onCompletion(final MediaPlayer mp) {
        mPlayStatus = PlayStatus.STOPED;
        if (mPlayCallback != null) {
            mPlayCallback.onCompletion();
        }
        log(index + " 播放结束！");
        play();
    }

    /**
     * 播放状态
     */
    enum PlayStatus {
        PREPARED, PLAYIND, PAUSED, STOPED, ERROR,
    }

    interface DownloadCallback {
        void onSuccess(File file);

        void onFailed(String errorMsg);
    }

    /**
     * 播放回调
     */
    interface PlayCallback {
        /**
         * 播放出错
         *
         * @param error
         */
        void onError(String error);

        /**
         * 播放完毕
         */
        void onCompletion();
    }
}
