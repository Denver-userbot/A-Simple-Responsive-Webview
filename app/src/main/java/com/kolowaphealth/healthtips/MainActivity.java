package com.kolowaphealth.healthtips;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.kobakei.ratethisapp.RateThisApp;


import static android.R.attr.id;
import static android.R.attr.theme;

public class MainActivity extends AppCompatActivity {
    WebView webview;
    AdView mAdView;
    FrameLayout frameLayout;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        RateThisApp.Config config = new RateThisApp.Config(2, 3);
        config.setUrl("market://details?id=com.kolowaphealth.healthtips");
        RateThisApp.init(config);

        frameLayout = (FrameLayout) findViewById(R.id.webframe);

        TextView textView = (TextView) findViewById(R.id.retry);
        webview = (WebView) findViewById(R.id.wv_test);


        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-3517746418699749~8732130915");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl("https://www.yourhealthz.com");
                frameLayout.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);

            }
        });


        webview.setWebViewClient
                (new WebViewClient());
        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setEnableSmoothTransition(true);
        webview.getSettings().setJavaScriptEnabled(true);


        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://www.yourhealthz.com");
    }

    public class WebViewClient extends
            android.webkit.WebViewClient {

        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Please wait....");
            pd.setMessage("Page is Loading....");
            pd.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pd.dismiss();
            super.onPageFinished(view, url);

        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("yourhealthz.com")) {
                view.loadUrl(url);

            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
            }
            return true;
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            frameLayout.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
        }

            {
                if (

                        isNetworkStatusAvailable(getApplicationContext()))

                {
                    Toast.makeText(getApplicationContext(), "Internet available", Toast.LENGTH_SHORT).show();
                } else

                {
                    Toast.makeText(getApplicationContext(), "Connection is not available, Please Turn On Your Data Network And Launch App Again", Toast.LENGTH_LONG).show();
                }

            }



        public boolean isNetworkStatusAvailable
                (Context context)

        {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
                if (netInfos != null)
                    if (netInfos.isConnected())
                        return true;
            }
            return false;
        }
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do You Want To Exit Application?");
            alertDialogBuilder
                    .setMessage("Click Yes To Exist!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);

                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        if (id == R.id.rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.kolowaphealth.healthtips"));
            startActivity(intent);

        }
        if (id == R.id.action) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Allow Notifications For Daily Health Tips?");
            alertDialogBuilder
                    .setMessage("")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getApplicationContext(), "Settings Saved", Toast.LENGTH_LONG).show();


                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), "Settings Saved", Toast.LENGTH_LONG).show();
                            dialog.cancel();

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        if (id == R.id.more) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.kolowaphealth.fitnesshealth"));
        startActivity(intent);

    }
        if (id == R.id.share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Download this Health application, it's very useful,i Love it ! Am sure you will also Check it out using this link here....https://play.google.com/store/apps/details?id=com.kolowaphealth.healthtips");
            startActivity(shareIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}


