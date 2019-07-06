package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvUserTweet;
    TextView time;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_adapter);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(getIntent().getStringExtra("name")+" @"+getIntent().getStringExtra("username"));
        tvUserTweet = (TextView) findViewById(R.id.tvUserTweet);
        tvUserTweet.setText(getIntent().getStringExtra("body"));
        time = (TextView) findViewById(R.id.time);
        time.setText(getIntent().getStringExtra("time"));
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        Glide.with(this)
                .load(getIntent().getStringExtra("imageUrl"))
                .into(ivProfileImage);
    }


}
