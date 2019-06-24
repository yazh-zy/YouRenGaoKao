package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;

/**
 * Created by zy on 2017/4/5.
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener{

    private WebView mWebView;
    private TextView mBackView;
    private Handler mHandler = new Handler();

    public static final String URL_KEY = "Url_Key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_layout);

        Intent intent = getIntent();
        String url = intent.getStringExtra(URL_KEY);

        initView();
        initWebView(url);
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview_view);
        mBackView = (TextView) findViewById(R.id.web_close_view);
        mBackView.setOnClickListener(this);
    }

    private void initWebView(String url) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        addJs();
        openNewWeb();
        showProgress();

        mWebView.loadUrl(url);
    }

    private void addJs() {
        mWebView.addJavascriptInterface(new Object() {
            public void clickOnAndroid() {
                mHandler.post(new Runnable() {
                    public void run() {
                        mWebView.loadUrl("javascript:wave()");
                    }
                });
            }
        }, "android");
    }

    private void openNewWeb() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void showProgress() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                setTitle("页面加载中，请稍候..." + progress + "%");
                setProgress(progress * 100);
                if (progress == 100) {
                    setTitle(R.string.app_name);
                }
            }
        });
    }



    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            // 返回键退回
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.web_close_view:
                finish();
                break;
        }
    }
}

