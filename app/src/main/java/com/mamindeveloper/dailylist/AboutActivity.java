package com.mamindeveloper.dailylist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
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
}
