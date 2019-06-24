package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.view.ijkplayer.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by zy on 2017/4/18.
 */

public class IjkplayerActivity extends BaseActivity{

    private IjkVideoView mVideoView;


    private String liveUrl = null;
    private String id;
    private String title;
    private String image;
    private String author;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ijkplayer_layout);

        Intent intent = getIntent();
        liveUrl = intent.getStringExtra("liveUrl");
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");
        author = intent.getStringExtra("teacher");

        initView();

        Log.e("IJKId", id);

//        liveUrl = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    }

    private void initView() {
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView.setVideoPath(liveUrl);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {

                Log.e("IJK", "Prepared");
            }
        });

        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.e("IJK", "Complete");
            }
        });

        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.e("IJK", "Error");
                Intent intent = new Intent(IjkplayerActivity.this, OnlineRatingActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("image", image);
                intent.putExtra("author", author);
                startActivity(intent);
                finish();
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent = new Intent(IjkplayerActivity.this, OnlineRatingActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("image", image);
            intent.putExtra("author", author);
            startActivity(intent);
            finish();
        }

        return false;
    }
}
