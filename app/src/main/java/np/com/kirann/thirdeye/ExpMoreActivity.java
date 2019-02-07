package np.com.kirann.thirdeye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ExpMoreActivity extends AppCompatActivity {
    WebView myWeb;
    Toolbar toolbar;
    String urlExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_more);
        toolbar = findViewById(R.id.toolbar);
        Intent iin = getIntent();
        urlExt = iin.getExtras().get("url").toString();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        myWeb = findViewById(R.id.wb2);
        myWeb.getSettings().setJavaScriptEnabled(true);
        myWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                toolbar.setTitle(view.getTitle());
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("android_asset")) return false;
                return true;
            }
        });
        myWeb.loadUrl("file:///android_asset/internal/" + urlExt + ".html");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
