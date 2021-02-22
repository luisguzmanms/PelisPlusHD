package com.lamesa.netfilms.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.amnix.adblockwebview.util.AdBlocker;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.otros.TinyDB;
import com.lamesa.netfilms.video.FloatWindowActivity;
import com.lamesa.netfilms.webview.VideoEnabledWebChromeClient;

import java.util.HashMap;
import java.util.Map;

import static com.lamesa.netfilms.otros.metodos.CargarInterAd;
import static com.lamesa.netfilms.otros.statics.constantes.TBurlEpisodio;
import static com.lamesa.netfilms.otros.metodos.CheckReproductor;

public class act_webview extends AppCompatActivity {

    private WebView mWebView;
    private VideoEnabledWebChromeClient webChromeClient;
    private String mUrl;
    private TinyDB tinyDB;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        AdBlocker.init(this);
        tinyDB = new TinyDB(this);
        mWebView = findViewById(R.id.webView);


        mUrl = tinyDB.getString(TBurlEpisodio);


        //region CodeWeb

// Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments


        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, mWebView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100) {
                    WaitDialog.dismiss();
                } else {
                    WaitDialog.show(act_webview.this, "Cargando...").setCancelable(true);
                }


                if (view.getUrl() != null) {

                    if (!view.getUrl().toLowerCase().contains(mUrl.toLowerCase())) {


                        if(!view.getUrl().contains("feurl.com") || view.getUrl().contains("yandex") || view.getUrl().contains("femosteradiacomoe")) {

                            if (mWebView.canGoBack()) {
                                mWebView.goBack();
                                System.out.println("INFA: URL ERRONEA: -- " + view.getUrl());
                            } else {
                                mWebView.loadUrl(mUrl);
                            }

                        }


                        //no es la url princiapl

                      // WaitDialog.show(act_webview.this, "Cargando de nuevo...").setCancelable(true);
                    }
                }
            }
        };


        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });


        //endregion

        mWebView.setWebChromeClient(webChromeClient);
        // Call private class InsideWebViewClient
        mWebView.setWebViewClient(new InsideWebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSaveFormData(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setSupportMultipleWindows(true);


        // Navigate anywhere you want, but consider that this classes have only been tested on YouTube's mobile site
        mWebView.loadUrl(mUrl);


        CargarInterAd(act_webview.this, "ca-app-pub-3040756318290255/5485293339", 20);


    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(".mp4")) {
                /*
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
               // Toast.makeText(view.getContext(), url, Toast.LENGTH_LONG).show();

                 */
                return true;

            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }


        private Map<String, Boolean> loadedUrls = new HashMap<>();

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

            if (url.contains(".mp4") || url.contains("fvs.io") || url.contains(".m3u8")) {

                tinyDB.putString(TBurlEpisodio, url);


                if (CheckReproductor(act_webview.this, tinyDB).contains("web")) {

                } else if (CheckReproductor(act_webview.this, tinyDB).contains("externo")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                    finish();
                } else {
                    view.getContext().startActivity(new Intent(view.getContext(), FloatWindowActivity.class));
                    finish();
                }








                /*

                 */


                System.out.println("ENCONTRADO");


            }



            boolean ad;

            if (!loadedUrls.containsKey(url)) {
                ad = AdBlocker.isAd(url);
                loadedUrls.put(url, ad);
                System.out.println("NO EN HOST -- " + url);
                Log.e("TAG_NOHOST", url);


            } else {
                ad = loadedUrls.get(url);
                System.out.println("SI EN HOST -- " + url);
                Log.e("TAG_SIHOST", url);

            }

            return ad ? AdBlocker.createEmptyResource() :

            super.shouldInterceptRequest(view, url);






        }


    }


    @Override
    public void onBackPressed() {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed()) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                // Standard back button implementation (for example this could close the app)
                super.onBackPressed();
            }
        }
    }


}
