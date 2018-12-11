package cf.awidiyadew.drawerexpandablelistview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cf.awidiyadew.drawerexpandablelistview.dto.StaticVariable;

public class MapActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "daummaps://open?page=routeSearch";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        // Handle the camera action

        startActivity(intent);
    }
}