package np.com.kirann.thirdeye;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {
WebView myWeb;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        checkForAndAskForPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createWebView();
                } else {
                }

                return;
            }
        }
    }


    private void createWebView() {

        myWeb=findViewById(R.id.wb);
        setUpWebViewDefaults(myWeb);
        myWeb.loadUrl("file:///android_asset/index2.html");
        myWeb.setWebChromeClient(new WebChromeClient() {

            public boolean onConsoleMessage(ConsoleMessage m) {
                Toast.makeText(WebActivity.this, m.message(), Toast.LENGTH_SHORT).show();
                if(m.message().contains("connection_closed_client") || m.message().contains("call_aborted_client")){
                    finish();
                }
                return true;
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                WebActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        if(request.getOrigin().toString().equals("file:///") ) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }
        });
    }
    private void checkForAndAskForPermissions() {
        if (ContextCompat.checkSelfPermission(WebActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WebActivity.this,
                    Manifest.permission.CAMERA)) {
            } else ActivityCompat.requestPermissions(WebActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            createWebView();
        }
    }

    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.clearCache(true);
        webView.clearHistory();
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
