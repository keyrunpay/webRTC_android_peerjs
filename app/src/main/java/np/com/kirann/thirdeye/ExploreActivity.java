package np.com.kirann.thirdeye;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ExploreActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView myWeb;
    BottomNavigationView bottomNavigationView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.btmNavView);
        setSupportActionBar(toolbar);
        myWeb = findViewById(R.id.wb1);
        setTitle("Home");
        myWeb.getSettings().setJavaScriptEnabled(true);
        myWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("android_asset")) return false;
                return true;
            }
        });

        myWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title.contains("Home")) {
                    bottomNavigationView.setSelectedItemId(R.id.btnMenuNavHome);
                } else if (title.contains("Explore")) {
                    bottomNavigationView.setSelectedItemId(R.id.btnMenuNavExplore);
                } else if (title.contains("Leaderboard")) {
                    bottomNavigationView.setSelectedItemId(R.id.btnMenuNavLeaderboard);
                } else if (title.contains("Favourites")) {
                    bottomNavigationView.setSelectedItemId(R.id.btnMenuNavFav);
                }
                toolbar.setTitle(title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (consoleMessage.message().contains("explore_clicked")) {
                    Intent i = new Intent(ExploreActivity.this, ExpMoreActivity.class);
                    i.putExtra("url", "morexp");
                    startActivity(i);
                }
                if (consoleMessage.message().contains("home_clicked_done")) {
                    Intent i = new Intent(ExploreActivity.this, ExpMoreActivity.class);
                    i.putExtra("url", "exploremore");
                    startActivity(i);
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        myWeb.loadUrl("file:///android_asset/internal/home.html");


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btnMenuBeGuide) {
                    Toast.makeText(ExploreActivity.this, "Open Register Page", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.btnMenuBeVolunteer) {
                    Toast.makeText(ExploreActivity.this, "Open Register Page", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.btnMenuConnectVolunteer || item.getItemId() == R.id.btnMenuConnectGuide) {
                    Intent i = new Intent(ExploreActivity.this, WebActivity.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.menu_help) {
                    showAlert();
                }
                return false;
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                toolbar.setTitle(item.getTitle());
                if (item.getItemId() == R.id.btnMenuNavHome) {
                    myWeb.loadUrl("file:///android_asset/internal/home.html");
                }
                if (item.getItemId() == R.id.btnMenuNavExplore) {
                    myWeb.loadUrl("file:///android_asset/internal/explore.html");
                }
                if (item.getItemId() == R.id.btnMenuNavLeaderboard) {
                    myWeb.loadUrl("file:///android_asset/internal/leaderboard.html");
                }
                if (item.getItemId() == R.id.btnMenuNavFav) {
                    myWeb.loadUrl("file:///android_asset/internal/fav.html");
                }
                return true;
            }
        });

    }

    public void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("");
        alertDialog.setMessage("Info \n \n This application sole purpose is the help disabled people travel with ease. \n We also provide volunteer support for the people who got stuck while solo travelling \n \n We help guides get their target customer..");


        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
//        if(myWeb.canGoBack()){
//            myWeb.goBack();
//        }else{
        super.onBackPressed();
//        }
    }
}
