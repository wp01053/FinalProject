package cf.awidiyadew.drawerexpandablelistview;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

public class Main2Activity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
        String academy_name = setting.getString("ACADEMY_NAME", "");

        // 학생 조회를 위한 데이터
        Log.d("123123", academy_name+"학원이름");


        WebView mWebView = (WebView) findViewById(R.id.webViewmain2);
        mWebView.setInitialScale(1);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            File dbDir = new File(getCacheDir(), "webDb");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            webSettings.setDatabasePath(dbDir.getAbsolutePath());
        }
        webSettings.setAppCacheEnabled(true);
        File appCacheDir = new File(getCacheDir(), "appCache");
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        webSettings.setAppCachePath(appCacheDir.getAbsolutePath());
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadsImagesAutomatically(true);

        mWebView.loadUrl("http://192.168.0.112:8081/spring/AcademyBoard?name="+academy_name);
        mWebView.setWebViewClient(new WebViewClientClass());    }

    private class WebViewClientClass extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}