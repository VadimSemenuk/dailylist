package com.mamindeveloper.dailylist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView t2 = (TextView) findViewById(R.id.credits);
        t2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView feedback = (TextView) findViewById(R.id.developer_email);
        Spanned _content = Html.fromHtml("<a href=\"mailto:" + getResources().getString(R.string.developer_email) + "\">" + getResources().getString(R.string.developer_email) + "</a>");
        SpannableString content = new SpannableString(_content);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        feedback.setText(content);
        feedback.setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.rate_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppInGooglePlay();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAppInGooglePlay() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
}
